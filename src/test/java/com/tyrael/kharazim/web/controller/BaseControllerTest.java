package com.tyrael.kharazim.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.base.auth.CurrentUser;
import com.tyrael.kharazim.application.base.auth.CurrentUserHolder;
import com.tyrael.kharazim.application.base.auth.CurrentUserMethodArgumentResolver;
import com.tyrael.kharazim.common.util.RandomStringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.mockito.Mockito;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.handler.MockHandlerImpl;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Tyrael Archangel
 * @since 2023/12/21
 */
@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTest<T> {

    /**
     * mock出来的controller，不能用于直接调用，只能用于获取方法签名，拼接请求
     */
    protected final T mockController;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    public BaseControllerTest(Class<T> controller) {
        MockCreationSettings<T> settings = Mockito.withSettings().build(controller);
        this.mockController = Plugins.getMockMaker()
                .createMock(settings, new ControllerRequestMappingParseMockHandler<>(settings));
    }

    /**
     * Perform a request
     */
    protected void perform(RequestBuilder requestBuilder) {
        try {
            mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    protected <R> MockHttpServletRequestBuilder getRequestBuilderWhenCall(R call) {
        if (mockController == null) {
            throw new UnsupportedOperationException();
        }
        return ControllerRequestMappingParseMockHandler.REQUEST_BUILDER_TH.get();
    }

    protected <R> void performWhenCall(R call) {
        RequestBuilder requestBuilder = this.getRequestBuilderWhenCall(call);
        this.perform(requestBuilder);
    }

    private class ControllerRequestMappingParseMockHandler<C> extends MockHandlerImpl<C> {

        private static final ThreadLocal<MockHttpServletRequestBuilder> REQUEST_BUILDER_TH = new ThreadLocal<>();
        private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        private final RequestMapping classRequestMapping;

        ControllerRequestMappingParseMockHandler(MockCreationSettings<C> settings) {
            super(settings);
            this.classRequestMapping = AnnotatedElementUtils.findMergedAnnotation(
                    this.getMockSettings().getTypeToMock(), RequestMapping.class);
        }

        @Override
        public Object handle(Invocation invocation) throws Throwable {

            Method method = invocation.getMethod();
            RequestMapping methodRequestMapping = AnnotatedElementUtils.getMergedAnnotation(method, RequestMapping.class);

            if (ObjectUtils.anyNotNull(classRequestMapping, methodRequestMapping)) {
                Object[] arguments = invocation.getArguments();

                Parameter[] parameters = method.getParameters();
                parameters = parameters == null ? new Parameter[0] : parameters;

                resolveCurrentUser(method, parameters, arguments);

                RequestMethod requestMethod = this.getRequestMethod(classRequestMapping, methodRequestMapping);
                String url = this.getUrl(classRequestMapping, methodRequestMapping);
                MultiValueMap<String, String> params = this.getParams(method, parameters, arguments);
                Object[] uriVariables = this.getUriVariables(method, parameters, url, arguments);
                Object requestBody = this.getRequestBody(parameters, arguments);

                MockHttpServletRequestBuilder requestBuilder = uriVariables == null
                        ? MockMvcRequestBuilders.request(requestMethod.asHttpMethod(), url)
                        : MockMvcRequestBuilders.request(requestMethod.asHttpMethod(), url, uriVariables);

                if (!CollectionUtils.isEmpty(params)) {
                    requestBuilder.params(params);
                }

                if (requestBody != null) {
                    requestBuilder.contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(requestBody));
                }

                REQUEST_BUILDER_TH.set(requestBuilder);

            }
            return super.handle(invocation);
        }

        private MultiValueMap<String, String> getParams(Method method, Parameter[] parameters, Object[] arguments) throws IllegalAccessException {
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            for (int i = 0; i < parameters.length; i++) {

                Object argument = arguments[i];
                MethodParameter methodParameter = new MethodParameter(method, i);
                methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);

                MultiValueMap<String, String> argumentParams = getParams(methodParameter, argument);
                if (argumentParams != null) {
                    params.addAll(argumentParams);
                }
            }
            return params;
        }

        private MultiValueMap<String, String> getParams(MethodParameter methodParameter, Object argument) throws IllegalAccessException {

            Parameter parameter = methodParameter.getParameter();
            if (ignoreParam(parameter, argument)) {
                return null;
            }

            Class<?> parameterType = parameter.getType();

            String parameterName = methodParameter.getParameterName();
            parameterName = parameterName == null ? parameter.getName() : parameterName;

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            if (parameterType.isArray()) {
                map.put(parameterName, getParamsFromArray(argument));
            } else if (isBasicParamType(parameterType)) {
                map.put(parameterName, Lists.newArrayList(String.valueOf(argument)));
            } else if (isTemporalAccessor(parameterType)) {
                // Year、YearMonth、LocalDate、MonthDay、LocalDate、LocalDateTime etc.
                map.put(parameterName, Lists.newArrayList(getTemporalAccessorValue(methodParameter, argument)));
            } else {
                MultiValueMap<String, String> beanParams = getParamsFromNormalBean(parameterType, argument);
                map.putAll(beanParams);
            }
            return map;
        }

        private List<String> getParamsFromArray(Object argument) {
            List<String> values = Lists.newArrayList();
            int length = Array.getLength(argument);
            for (int i = 0; i < length; i++) {
                values.add(String.valueOf(Array.get(argument, i)));
            }
            return values;
        }

        private boolean isBasicParamType(Class<?> parameterType) {
            return parameterType.isPrimitive()
                    || CharSequence.class.isAssignableFrom(parameterType)
                    || Boolean.class.isAssignableFrom(parameterType)
                    || Number.class.isAssignableFrom(parameterType);
        }

        private boolean isTemporalAccessor(Class<?> parameterType) {
            return Temporal.class.isAssignableFrom(parameterType);
        }

        private String getTemporalAccessorValue(MethodParameter methodParameter, Object argument) {
            String format = null;
            Schema schema = methodParameter.getParameterAnnotation(Schema.class);
            if (schema != null) {
                format = schema.format();
            }
            if (StringUtils.hasText(format)) {
                return DateTimeFormatter.ofPattern(format).format((TemporalAccessor) argument);
            } else {
                return String.valueOf(argument);
            }
        }

        private MultiValueMap<String, String> getParamsFromNormalBean(Class<?> parameterType, Object argument) throws IllegalAccessException {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            for (Field field : parameterType.getDeclaredFields()) {
                String fieldName = field.getName();
                field.setAccessible(true);
                Object fieldValue = field.get(argument);
                if (fieldValue != null) {
                    if (field.getClass().isArray()) {
                        params.put(fieldName, this.getParamsFromArray(fieldValue));
                    } else {
                        params.put(fieldName, Lists.newArrayList(String.valueOf(fieldValue)));
                    }
                }
            }
            return params;
        }

        private boolean ignoreParam(Parameter parameter, Object argument) {
            return argument == null
                    || parameter.isAnnotationPresent(CurrentUser.class)
                    || parameter.getType().isAssignableFrom(ServletRequest.class)
                    || parameter.getType().isAssignableFrom(ServletResponse.class)
                    || parameter.getType().isAssignableFrom(InputStreamSource.class);
        }

        private void resolveCurrentUser(Method method, Parameter[] parameters, Object... arguments) {
            CurrentUserMethodArgumentResolver resolver = new CurrentUserMethodArgumentResolver();
            for (int i = 0; i < parameters.length; i++) {
                MethodParameter methodParameter = new MethodParameter(method, i);
                if (arguments[i] != null && resolver.supportsParameter(methodParameter)) {
                    CurrentUserHolder.setCurrentUser((AuthUser) arguments[i], RandomStringUtil.make());
                    return;
                }
            }
        }

        private Object getRequestBody(Parameter[] parameters, Object... arguments) {
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                if (parameter.isAnnotationPresent(RequestBody.class)) {
                    return arguments[i];
                }
            }
            return null;
        }

        private RequestMethod getRequestMethod(RequestMapping classRequestMapping, RequestMapping methodRequestMapping) {
            if (methodRequestMapping != null) {
                RequestMethod[] methodRequestMethods = methodRequestMapping.method();
                if (methodRequestMethods.length > 0) {
                    return methodRequestMethods[0];
                }
            }
            if (classRequestMapping != null) {
                RequestMethod[] classRequestMethods = classRequestMapping.method();
                if (classRequestMethods.length > 0) {
                    return classRequestMethods[0];
                }
            }
            return RequestMethod.GET;
        }

        private String getRequestMappingPath(RequestMapping requestMapping) {
            if (requestMapping == null) {
                return null;
            }
            return Stream.of(requestMapping.path())
                    .filter(StringUtils::hasText)
                    .findFirst()
                    .map(this::formatPath)
                    .orElse(null);
        }

        private String formatPath(String path) {
            if (!StringUtils.hasText(path)) {
                return null;
            }
            path = path.trim();
            if (path.equals("/")) {
                return path;
            }
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 2);
            }
            return path;
        }

        private String getUrl(RequestMapping classRequestMapping, RequestMapping methodRequestMapping) {
            String classRequestMappingPath = getRequestMappingPath(classRequestMapping);
            String methodRequestMappingPath = getRequestMappingPath(methodRequestMapping);
            StringBuilder requestMappingPathBuilder = new StringBuilder();
            if (classRequestMappingPath != null) {
                requestMappingPathBuilder.append(classRequestMappingPath);
            }
            if (methodRequestMappingPath != null) {
                requestMappingPathBuilder.append(methodRequestMappingPath);
            }
            return requestMappingPathBuilder.toString();
        }

        private Object[] getUriVariables(Method method, Parameter[] parameters, String url, Object[] arguments) {
            if (parameters.length == 0) {
                return null;
            }

            List<String> pathVariables = resolvePathVariables(url);
            if (CollectionUtils.isEmpty(pathVariables)) {
                return null;
            }

            Map<String, Object> pathVariableNameAndArgumentMap = Maps.newLinkedHashMap();
            for (int i = 0; i < parameters.length; i++) {
                MethodParameter methodParameter = new MethodParameter(method, i);
                String pathVariableName = this.getPathVariableName(methodParameter);
                if (pathVariableName != null) {
                    pathVariableNameAndArgumentMap.put(pathVariableName, arguments[i]);
                }
            }
            return pathVariables.stream()
                    .map(pathVariableNameAndArgumentMap::get)
                    .toArray();
        }

        private String getPathVariableName(MethodParameter methodParameter) {
            methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);

            PathVariable pathVariable = methodParameter.getParameterAnnotation(PathVariable.class);
            if (pathVariable == null) {
                return null;
            }
            String value = pathVariable.value();
            if (StringUtils.hasText(value)) {
                return value;
            }
            String name = pathVariable.name();
            if (StringUtils.hasText(name)) {
                return name;
            }
            return methodParameter.getParameterName();
        }

        private List<String> resolvePathVariables(String url) {
            String groupName = "VARIABLE";
            String pathVariablesRegex = "\\{(?<" + groupName + ">[^}]*)}";
            Matcher matcher = Pattern.compile(pathVariablesRegex).matcher(url);
            List<String> pathVariables = Lists.newArrayList();
            while (matcher.find()) {
                pathVariables.add(matcher.group(groupName));
            }
            return pathVariables;
        }
    }

}

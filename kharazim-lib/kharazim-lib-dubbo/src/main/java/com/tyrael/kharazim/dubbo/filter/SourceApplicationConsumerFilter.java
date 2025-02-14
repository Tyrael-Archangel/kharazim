package com.tyrael.kharazim.dubbo.filter;

import com.tyrael.kharazim.dubbo.support.ApplicationContextHolder;
import com.tyrael.kharazim.dubbo.support.SourceDubboApplication;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.util.StringUtils;

/**
 * @author Tyrael Archangel
 * @since 2025/2/13
 */
@Activate(group = CommonConstants.CONSUMER)
public class SourceApplicationConsumerFilter implements Filter {

    private String applicationName;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        initApplicationNameIfNecessary();

        SourceDubboApplication sourceDubboApplication = buildSourceApplication();
        invocation.setAttachment(FilterAttachmentConstants.SOURCE_APPLICATION, sourceDubboApplication);

        return invoker.invoke(invocation);
    }

    private SourceDubboApplication buildSourceApplication() {
        SourceDubboApplication sourceDubboApplication = new SourceDubboApplication();
        sourceDubboApplication.setName(applicationName);

        RpcServiceContext serviceContext = RpcContext.getCurrentServiceContext();
        if (serviceContext != null) {
            sourceDubboApplication.setGroup(serviceContext.getGroup());
        }

        return sourceDubboApplication;
    }

    private synchronized void initApplicationNameIfNecessary() {
        if (!StringUtils.hasText(applicationName)) {
            applicationName = ApplicationContextHolder.getApplicationContext()
                    .getEnvironment()
                    .getProperty("spring.application.name");
        }
    }

}

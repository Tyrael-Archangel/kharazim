package com.tyrael.kharazim.dubbo.support;

import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.proxy.InvokerInvocationHandler;
import org.apache.dubbo.rpc.proxy.javassist.JavassistProxyFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Tyrael Archangel
 * @since 2025/2/25
 */
public class EnhanceJavassistProxyFactory extends JavassistProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Invoker<T> invoker, Class<?>[] interfaces) {
        return (T) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                interfaces,
                new HandleDefaultMethodInvokerInvocationHandler(invoker));
    }

    public static class HandleDefaultMethodInvokerInvocationHandler extends InvokerInvocationHandler {

        public HandleDefaultMethodInvokerInvocationHandler(Invoker<?> handler) {
            super(handler);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.isDefault()) {
                return MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup())
                        .unreflectSpecial(method, method.getDeclaringClass())
                        .bindTo(proxy)
                        .invokeWithArguments(args);
            }
            return super.invoke(proxy, method, args);
        }
    }

}

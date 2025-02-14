package com.tyrael.kharazim.dubbo.filter;

import com.tyrael.kharazim.dubbo.support.SourceApplicationHolder;
import com.tyrael.kharazim.dubbo.support.SourceDubboApplication;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author Tyrael Archangel
 * @since 2025/2/13
 */
@Activate(group = CommonConstants.PROVIDER)
public class SourceApplicationProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        try {
            SourceDubboApplication sourceDubboApplication = (SourceDubboApplication) invocation.getObjectAttachment(FilterAttachmentConstants.SOURCE_APPLICATION);
            if (sourceDubboApplication != null) {
                SourceApplicationHolder.setSourceApplication(sourceDubboApplication);
            }

            return invoker.invoke(invocation);
        } finally {
            SourceApplicationHolder.remove();
        }

    }

}

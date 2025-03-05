package com.tyrael.kharazim.dubbo.filter;

import com.tyrael.kharazim.authentication.Principal;
import com.tyrael.kharazim.authentication.PrincipalHolder;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author Tyrael Archangel
 * @since 2025/3/5
 */
@Activate(group = CommonConstants.CONSUMER)
public class CurrentPrincipalConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Principal principal = PrincipalHolder.getPrincipal();
        if (principal != null) {
            invocation.setAttachment(FilterAttachmentConstants.CURRENT_PRINCIPAL, principal);
        }
        return invoker.invoke(invocation);
    }

}

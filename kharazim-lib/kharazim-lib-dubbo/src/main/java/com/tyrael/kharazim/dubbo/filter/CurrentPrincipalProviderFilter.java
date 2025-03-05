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
@Activate(group = CommonConstants.PROVIDER)
public class CurrentPrincipalProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            Principal principal = (Principal) invocation.getObjectAttachment(FilterAttachmentConstants.CURRENT_PRINCIPAL);
            if (principal != null) {
                PrincipalHolder.setPrincipal(principal);
            }
            return invoker.invoke(invocation);
        } finally {
            PrincipalHolder.remove();
        }
    }

}

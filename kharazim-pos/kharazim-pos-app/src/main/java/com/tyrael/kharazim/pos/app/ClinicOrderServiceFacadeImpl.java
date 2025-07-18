package com.tyrael.kharazim.pos.app;

import com.tyrael.kharazim.pos.app.service.ClinicOrderLifecycleService;
import com.tyrael.kharazim.pos.app.vo.CreateClinicOrderCommand;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Tyrael Archangel
 * @since 2025/7/18
 */
@Service
@RequiredArgsConstructor
public class ClinicOrderServiceFacadeImpl implements ClinicOrderServiceFacade {

    private final ClinicOrderLifecycleService clinicOrderLifecycleService;

    @Override
    public String createOrder(CreateClinicOrderCommand command, AuthUser currentUser) {
        return clinicOrderLifecycleService.create(command, currentUser);
    }

}

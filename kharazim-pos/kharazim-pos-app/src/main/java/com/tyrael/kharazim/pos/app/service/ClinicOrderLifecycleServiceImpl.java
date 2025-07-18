package com.tyrael.kharazim.pos.app.service;

import com.tyrael.kharazim.pos.app.mapper.ClinicOrderMapper;
import com.tyrael.kharazim.pos.app.vo.CreateClinicOrderCommand;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tyrael Archangel
 * @since 2025/7/18
 */
@Service
@RequiredArgsConstructor
public class ClinicOrderLifecycleServiceImpl implements ClinicOrderLifecycleService {

    private final ClinicOrderMapper clinicOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreateClinicOrderCommand command, AuthUser currentUser) {
        return "";
    }

}

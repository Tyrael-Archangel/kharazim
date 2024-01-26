package com.tyrael.kharazim.application.recharge.service.impl;

import com.tyrael.kharazim.application.base.auth.AuthUser;
import com.tyrael.kharazim.application.config.BusinessCodeConstants;
import com.tyrael.kharazim.application.recharge.domain.RechargeCardType;
import com.tyrael.kharazim.application.recharge.mapper.RechargeCardTypeMapper;
import com.tyrael.kharazim.application.recharge.service.RechargeCardTypeService;
import com.tyrael.kharazim.application.recharge.vo.AddRechargeCardTypeRequest;
import com.tyrael.kharazim.application.recharge.vo.ModifyRechargeCardTypeRequest;
import com.tyrael.kharazim.application.system.service.CodeGenerator;
import com.tyrael.kharazim.common.exception.BusinessException;
import com.tyrael.kharazim.common.exception.DomainNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Tyrael Archangel
 * @since 2024/1/25
 */
@Service
@RequiredArgsConstructor
public class RechargeCardTypeServiceImpl implements RechargeCardTypeService {

    private final RechargeCardTypeMapper rechargeCardTypeMapper;
    private final CodeGenerator codeGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(AddRechargeCardTypeRequest addRequest) {

        boolean neverExpire = Boolean.TRUE.equals(addRequest.getNeverExpire());
        Integer validPeriodDays = addRequest.getValidPeriodDays();
        if (neverExpire) {
            validPeriodDays = null;
        } else if (validPeriodDays == null) {
            throw new BusinessException("请指定有效期限");
        }

        String code = codeGenerator.next(BusinessCodeConstants.RECHARGE_CARD_TYPE);

        RechargeCardType cardType = new RechargeCardType();
        cardType.setCode(code);
        cardType.setName(addRequest.getName());
        cardType.setDiscountPercent(addRequest.getDiscountPercent());
        cardType.setNeverExpire(neverExpire);
        cardType.setValidPeriodDays(validPeriodDays);
        cardType.setDefaultAmount(addRequest.getDefaultAmount());
        cardType.setCanCreateNewCard(Boolean.TRUE);

        rechargeCardTypeMapper.insert(cardType);

        return cardType.getCode();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(ModifyRechargeCardTypeRequest modifyRequest, AuthUser currentUser) {
        String code = modifyRequest.getCode();
        RechargeCardType cardType = rechargeCardTypeMapper.findByCode(code);
        DomainNotFoundException.assertFound(cardType, code);

        cardType.setName(modifyRequest.getName());
        cardType.setDiscountPercent(modifyRequest.getDiscountPercent());

        boolean neverExpire = Boolean.TRUE.equals(modifyRequest.getNeverExpire());
        Integer validPeriodDays = modifyRequest.getValidPeriodDays();
        if (neverExpire) {
            validPeriodDays = null;
        } else if (validPeriodDays == null) {
            throw new BusinessException("请指定有效期限");
        }

        cardType.setNeverExpire(neverExpire);
        cardType.setValidPeriodDays(validPeriodDays);
        cardType.setDefaultAmount(modifyRequest.getDefaultAmount());
        cardType.setUpdate(currentUser.getCode(), currentUser.getNickName());

        rechargeCardTypeMapper.updateById(cardType);
    }

}

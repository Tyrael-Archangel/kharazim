package com.tyrael.kharazim.finance.app.service.impl;

import com.tyrael.kharazim.base.dto.PageResponse;
import com.tyrael.kharazim.base.exception.BusinessException;
import com.tyrael.kharazim.base.exception.DomainNotFoundException;
import com.tyrael.kharazim.finance.app.constant.FinanceBusinessIdConstants;
import com.tyrael.kharazim.finance.app.domain.RechargeCardType;
import com.tyrael.kharazim.finance.app.mapper.RechargeCardTypeMapper;
import com.tyrael.kharazim.finance.app.service.RechargeCardTypeService;
import com.tyrael.kharazim.finance.app.vo.recharge.*;
import com.tyrael.kharazim.lib.idgenerator.IdGenerator;
import com.tyrael.kharazim.user.sdk.model.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyrael Archangel
 * @since 2024/1/25
 */
@Service
@RequiredArgsConstructor
public class RechargeCardTypeServiceImpl implements RechargeCardTypeService {

    private final RechargeCardTypeMapper rechargeCardTypeMapper;
    private final IdGenerator idGenerator;

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

        String code = idGenerator.next(FinanceBusinessIdConstants.RECHARGE_CARD_TYPE);

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
        cardType.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        rechargeCardTypeMapper.updateById(cardType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableCreateNewCard(String code, AuthUser currentUser) {
        RechargeCardType cardType = rechargeCardTypeMapper.findByCode(code);
        DomainNotFoundException.assertFound(cardType, code);

        if (cardType.forbidden()) {
            return;
        }
        cardType.setCanCreateNewCard(Boolean.FALSE);
        cardType.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        rechargeCardTypeMapper.updateById(cardType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableCreateNewCard(String code, AuthUser currentUser) {
        RechargeCardType cardType = rechargeCardTypeMapper.findByCode(code);
        DomainNotFoundException.assertFound(cardType, code);

        if (cardType.enabled()) {
            return;
        }
        cardType.setCanCreateNewCard(Boolean.TRUE);
        cardType.setUpdateUser(currentUser.getCode(), currentUser.getNickName());

        rechargeCardTypeMapper.updateById(cardType);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RechargeCardTypeVO> page(PageRechargeCardTypeRequest pageRequest) {
        PageResponse<RechargeCardType> cardTypePage = rechargeCardTypeMapper.page(pageRequest);
        List<RechargeCardTypeVO> cardTypes = cardTypePage.getData()
                .stream()
                .map(this::cardTypeVO)
                .collect(Collectors.toList());
        return PageResponse.success(cardTypes, cardTypePage.getTotalCount());
    }

    private RechargeCardTypeVO cardTypeVO(RechargeCardType cardType) {
        RechargeCardTypeVO cardTypeVO = new RechargeCardTypeVO();
        cardTypeVO.setId(cardType.getId());
        cardTypeVO.setCode(cardType.getCode());
        cardTypeVO.setName(cardType.getName());
        cardTypeVO.setDiscountPercent(cardType.getDiscountPercent());
        cardTypeVO.setNeverExpire(cardType.getNeverExpire());
        cardTypeVO.setValidPeriodDays(cardType.getValidPeriodDays());
        cardTypeVO.setDefaultAmount(cardType.getDefaultAmount());
        cardTypeVO.setCanCreateNewCard(cardType.getCanCreateNewCard());
        return cardTypeVO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RechargeCardTypeVO> list(ListRechargeCardTypeRequest listRequest) {
        List<RechargeCardType> cardTypes = rechargeCardTypeMapper.list(listRequest);
        return cardTypes.stream()
                .map(this::cardTypeVO)
                .collect(Collectors.toList());
    }

}

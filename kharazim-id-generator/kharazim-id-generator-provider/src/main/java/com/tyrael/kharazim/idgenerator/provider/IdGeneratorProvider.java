package com.tyrael.kharazim.idgenerator.provider;

import com.tyrael.kharazim.idgenerator.BusinessIdConstant;
import com.tyrael.kharazim.idgenerator.IdGenerator;
import com.tyrael.kharazim.idgenerator.app.service.IdGeneratorService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Tyrael Archangel
 * @since 2025/2/18
 */
@DubboService
@RequiredArgsConstructor
public class IdGeneratorProvider implements IdGenerator {

    private final IdGeneratorService idGeneratorService;

    @Override
    public <E extends Enum<E> & BusinessIdConstant<E>> String next(E businessCode) {
        return idGeneratorService.next(businessCode);
    }

    @Override
    public String next(String tag, int bit) {
        return idGeneratorService.next(tag, bit);
    }

    @Override
    public <E extends Enum<E> & BusinessIdConstant<E>> String dailyNext(E businessCode) {
        return idGeneratorService.dailyNext(businessCode);
    }

    @Override
    public <E extends Enum<E> & BusinessIdConstant<E>> String dailyTimeNext(E businessCode) {
        return idGeneratorService.dailyTimeNext(businessCode);
    }

}

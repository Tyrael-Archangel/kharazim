package com.tyrael.kharazim.pharmacy.app.mq;

import com.tyrael.kharazim.mq.MqConsumer;
import com.tyrael.kharazim.pharmacy.app.service.OutboundOrderService;
import com.tyrael.kharazim.pharmacy.sdk.model.message.CreatePrescriptionOutboundOrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Tyrael Archangel
 * @since 2025/4/2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreatePrescriptionOutboundOrderListener implements MqConsumer<CreatePrescriptionOutboundOrderMessage> {

    private final OutboundOrderService outboundOrderService;

    @Override
    public String getTopic() {
        return "CREATE_PRESCRIPTION_OUTBOUND_ORDER";
    }

    @Override
    public void consume(CreatePrescriptionOutboundOrderMessage body) {
        log.info("Received CreateOutboundOrderMessage: {}", body);
        outboundOrderService.createFromPrescription(body);
    }

}

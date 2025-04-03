package com.tyrael.kharazim.pharmacy.app.mq;


import com.tyrael.kharazim.mq.MqConsumer;
import com.tyrael.kharazim.pharmacy.model.message.CreateInboundOrderMessage;
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
public class CreateInboundOrderListener implements MqConsumer<CreateInboundOrderMessage> {

    @Override
    public String getTopic() {
        return "CREATE_INBOUND_ORDER";
    }

    @Override
    public void consume(CreateInboundOrderMessage body) {
        log.info("Received CreateInboundOrderMessage: {}", body);
    }

}

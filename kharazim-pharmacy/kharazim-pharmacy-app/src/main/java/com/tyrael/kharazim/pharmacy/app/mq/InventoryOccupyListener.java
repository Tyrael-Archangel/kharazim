package com.tyrael.kharazim.pharmacy.app.mq;

import com.tyrael.kharazim.mq.MqConsumer;
import com.tyrael.kharazim.mq.MqProducer;
import com.tyrael.kharazim.pharmacy.app.service.InventoryService;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.InventoryChangeCommand;
import com.tyrael.kharazim.pharmacy.app.vo.inventory.InventoryOccupyCommand;
import com.tyrael.kharazim.pharmacy.sdk.model.message.InventoryOccupyMessage;
import com.tyrael.kharazim.pharmacy.sdk.model.message.InventoryOccupyResultMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2025/4/8
 */
@Component
@RequiredArgsConstructor
public class InventoryOccupyListener implements MqConsumer<InventoryOccupyMessage> {

    private final InventoryService inventoryService;
    private final MqProducer mqProducer;

    @Override
    public String getTopic() {
        return "INVENTORY_OCCUPY";
    }

    @Override
    public void consume(InventoryOccupyMessage body) {
        List<InventoryChangeCommand.Item> occupyItems = body.getItems()
                .stream()
                .map(e -> new InventoryChangeCommand.Item(e.getSkuCode(), e.getQuantity()))
                .toList();
        InventoryOccupyCommand occupyCommand = new InventoryOccupyCommand(body.getBusinessCode(),
                body.getClinicCode(), occupyItems, body.getOperator(), body.getOperatorCode());

        boolean occupySuccess;
        try {
            inventoryService.occupy(occupyCommand);
            occupySuccess = true;
        } catch (Exception e) {
            occupySuccess = false;
        }

        InventoryOccupyResultMessage occupyResult = new InventoryOccupyResultMessage()
                .setOccupyMessage(body)
                .setSuccess(occupySuccess);
        mqProducer.send("INVENTORY_OCCUPY_RESULT", occupyResult);
    }

}

package com.tyrael.kharazim.web.controller.clinic;

import com.tyrael.kharazim.application.clinic.vo.AddClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.ListClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.ModifyClinicRequest;
import com.tyrael.kharazim.application.clinic.vo.PageClinicRequest;
import com.tyrael.kharazim.common.dto.Pair;
import com.tyrael.kharazim.common.dto.Pairs;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import org.junit.jupiter.api.Test;

/**
 * @author Tyrael Archangel
 * @since 2023/12/31
 */
class ClinicControllerTest extends BaseControllerTest<ClinicController> {

    public ClinicControllerTest() {
        super(ClinicController.class);
    }

    @Test
    void page() {
        PageClinicRequest pageRequest = new PageClinicRequest();
        super.performWhenCall(mockController.page(pageRequest));
    }

    @Test
    void list() {
        ListClinicRequest listRequest = new ListClinicRequest();
        super.performWhenCall(mockController.list(listRequest));
    }

    @Test
    void add() {

        Pairs<String, String> battlegrounds = new Pairs<String, String>()
                .append("沃斯卡娅工业区", "Volskaya Foundry")
                .append("奥特兰克战道", "Alterac Pass")
                .append("恐魔园", "Garden Terror")
                .append("蛛后墓", "Tomb of Spider Queen")
                .append("花村", "Hanamura Temple")
                .append("布莱克西斯禁区", "Braxis Holdout")
                .append("诅咒谷", "Cursed Hollow")
                .append("天空殿", "Sky Temple")
                .append("鬼灵矿", "Haunted Mines")
                .append("永恒战场", "Battlefield of Eternity")
                .append("炼狱圣坛", "Infernal Shrines")
                .append("弹头枢纽站", "Warhead Junction")
                .append("末日塔", "Towers of Doom")
                .append("黑心湾", "Blackhearts Bay")
                .append("巨龙镇", "Dragon Shire");

        for (Pair<String, String> battleground : battlegrounds) {
            AddClinicRequest addClinicRequest = new AddClinicRequest();
            addClinicRequest.setName(battleground.left());
            addClinicRequest.setEnglishName(battleground.right());
            super.performWhenCall(mockController.add(addClinicRequest));
        }

    }

    @Test
    void modify() {
        ModifyClinicRequest modifyClinicRequest = new ModifyClinicRequest();
        modifyClinicRequest.setCode("CL000001");
        modifyClinicRequest.setName("泰瑞尔诊所");
        modifyClinicRequest.setEnglishName("Dr.Tyrael Archangel Clinic");
        super.performWhenCall(mockController.modify(modifyClinicRequest, super.mockAdmin()));
    }

}
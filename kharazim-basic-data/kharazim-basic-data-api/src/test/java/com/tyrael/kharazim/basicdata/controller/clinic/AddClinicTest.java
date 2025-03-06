package com.tyrael.kharazim.basicdata.controller.clinic;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.basicdata.BasicDataApiApplication;
import com.tyrael.kharazim.basicdata.DubboReferenceHolder;
import com.tyrael.kharazim.basicdata.app.dto.clinic.AddClinicRequest;
import com.tyrael.kharazim.basicdata.sdk.model.UploadFileVO;
import com.tyrael.kharazim.basicdata.sdk.service.FileServiceApi;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2023/12/31
 */
@Slf4j
@SpringBootTest(classes = BasicDataApiApplication.class)
public class AddClinicTest extends BaseControllerTest<ClinicController> {

    @Autowired
    private FileServiceApi fileServiceApi;
    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    public AddClinicTest() {
        super(ClinicController.class);
    }

    @Test
    void add() {
        List<Battleground> battlegrounds = battlegrounds();
        for (Battleground battleground : battlegrounds) {
            if (random.nextInt(100) > 60) {
                PrincipalHolder.setPrincipal(dubboReferenceHolder.userServiceApi.mock());
                String name = battleground.name();
                String image = battleground.image();
                String imageFileId = null;
                if (StringUtils.isNotBlank(image)) {
                    try {
                        imageFileId = uploadBattlegroundImage(image);
                    } catch (Exception e) {
                        log.error("get battleground image error: {}", e.getMessage(), e);
                    }
                }
                AddClinicRequest addClinicRequest = new AddClinicRequest();
                addClinicRequest.setName(name);
                addClinicRequest.setEnglishName(battleground.englishName());
                addClinicRequest.setImage(imageFileId);
                super.performWhenCall(mockController.add(addClinicRequest));
            }
        }
    }

    private String uploadBattlegroundImage(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        byte[] body = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofByteArray())
                .body();

        UploadFileVO uploadFileVO = new UploadFileVO();
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        uploadFileVO.setFileName(fileName);
        uploadFileVO.setFileBytes(body);

        return fileServiceApi.upload(uploadFileVO);
    }

    private List<Battleground> battlegrounds() {
        return List.of(
                new Battleground("沃斯卡娅工业区", "Volskaya Foundry", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blt094218e04753607a/65bd596eaf7bc9b84bc9447a/carousel_media_1.webp"),
                new Battleground("奥特兰克战道", "Alterac Pass", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blt8b89ce09e87d6ce7/65bd596eab0207079b56c660/carousel_media_2.webp"),
                new Battleground("恐魔园", "Garden Terror", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blt7d33ce7037572f83/65bd596edc7247ba9ee8f1a4/carousel_media_3.webp"),
                new Battleground("蛛后墓", "Tomb of Spider Queen", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blt9b50b8ceae7a8483/65cacbe7857c8c74b1990f7d/media_gallery_1.webp"),
                new Battleground("花村", "Hanamura Temple", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blt06cd2eadefbbacf1/65cacbe7492c0f8557bb4062/media_gallery_2.webp"),
                new Battleground("布莱克西斯禁区", "Braxis Holdout", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/bltb7d581ff87a9230c/65cacbe788b8ab45d859c1c0/media_gallery_3.webp"),
                new Battleground("诅咒谷", "Cursed Hollow", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blt688ca0a519faab47/65cacbe7af7bc95b49c94620/media_gallery_4.webp"),
                new Battleground("天空殿", "Sky Temple", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blt6935b1ed03ecaab8/65cacbe7b3fe315681b54455/media_gallery_5.webp"),
                new Battleground("鬼灵矿", "Haunted Mines", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blte1ccf186dd7d44e3/65cacbe7dc7247a7c7e8f396/media_gallery_6.webp"),
                new Battleground("永恒战场", "Battlefield of Eternity", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/bltd5e30354a77232df/65cacbe8653c6888ef99f116/media_gallery_7.webp"),
                new Battleground("炼狱圣坛", "Infernal Shrines", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blt8ddd357385a0d5cb/65cacbe8d44e9b4cf0e43b5f/media_gallery_8.webp"),
                new Battleground("弹头枢纽站", "Warhead Junction", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blt0a9a4d923aa659da/65cacbe87aa31fae2ed29ed2/media_gallery_9.webp"),
                new Battleground("末日塔", "Towers of Doom", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blt211517a9020bf883/65cacbe8b3fe3182a9b54459/media_gallery_10.webp"),
                new Battleground("黑心湾", "Blackhearts Bay", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/bltfb8f861bd8fb944f/65cacbeb36c0de51334d97b7/media_gallery_11.webp"),
                new Battleground("巨龙镇", "Dragon Shire", "https://blz-contentstack-images.akamaized.net/v3/assets/blt0e00eb71333df64e/blt94d55eea54a186a1/65cacbebe469d005498381ea/media_gallery_12.webp"));
    }

    private record Battleground(String name, String englishName, String image) {
    }

}
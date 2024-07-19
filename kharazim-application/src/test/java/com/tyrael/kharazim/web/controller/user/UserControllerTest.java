package com.tyrael.kharazim.web.controller.user;

import com.tyrael.kharazim.application.system.dto.file.UploadFileVO;
import com.tyrael.kharazim.application.system.service.FileService;
import com.tyrael.kharazim.application.user.domain.Role;
import com.tyrael.kharazim.application.user.dto.user.request.AddUserRequest;
import com.tyrael.kharazim.application.user.dto.user.request.ChangePasswordRequest;
import com.tyrael.kharazim.application.user.dto.user.request.ModifyUserRequest;
import com.tyrael.kharazim.application.user.dto.user.request.PageUserRequest;
import com.tyrael.kharazim.application.user.enums.EnableStatusEnum;
import com.tyrael.kharazim.application.user.enums.UserCertificateTypeEnum;
import com.tyrael.kharazim.application.user.enums.UserGenderEnum;
import com.tyrael.kharazim.application.user.mapper.RoleMapper;
import com.tyrael.kharazim.common.dto.Pair;
import com.tyrael.kharazim.common.dto.Pairs;
import com.tyrael.kharazim.common.exception.ShouldNotHappenException;
import com.tyrael.kharazim.mock.MockAuth;
import com.tyrael.kharazim.mock.MockMultipartFile;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.tyrael.kharazim.application.user.enums.UserGenderEnum.FEMALE;
import static com.tyrael.kharazim.application.user.enums.UserGenderEnum.MALE;

/**
 * @author Tyrael Archangel
 * @since 2023/12/27
 */
@Slf4j
class UserControllerTest extends BaseControllerTest<UserController> {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private FileService fileService;

    UserControllerTest() {
        super(UserController.class);
    }

    @Test
    void get() {
        super.performWhenCall(mockController.getById(1L));
    }

    @Test
    void page() {
        PageUserRequest pageUserRequest = new PageUserRequest();
        pageUserRequest.setKeywords("admin");
        pageUserRequest.setStatus(EnableStatusEnum.ENABLED);
        super.performWhenCall(mockController.page(pageUserRequest));
    }

    @Test
    void add() throws Exception {

        List<Role> roles = roleMapper.listAll();
        Map<String, Role> roleMap = roles.stream()
                .collect(Collectors.toMap(Role::getName, e -> e));

        List<Hero> heroes = new ArrayList<>(heroes());
        Collections.shuffle(heroes);

        Pairs<String, String> heroAvatars = heroAvatars();
        Map<String, String> heroAvatarMap = heroAvatars.stream()
                .collect(Collectors.toMap(Pair::left, Pair::right));

        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(processors);

        Consumer<Hero> addHero = hero -> {
            Role role = roleMap.get(hero.getRole());

            AddUserRequest addUserRequest = new AddUserRequest();
            addUserRequest.setName(hero.getName());
            addUserRequest.setNickName(hero.getNickName());
            addUserRequest.setGender(hero.getGender());
            addUserRequest.setPhone("13812341234");
            String avatarUrl = heroAvatarMap.get(hero.getName());
            if (StringUtils.isNotBlank(avatarUrl)) {
                try {
                    String fileId = uploadAvatar(hero.getName(), avatarUrl);
                    addUserRequest.setAvatar(fileId);
                } catch (Exception e) {
                    log.error("get hero avatar error: {}", e.getMessage(), e);
                }
            }

            addUserRequest.setCertificateType(UserCertificateTypeEnum.ID_CARD);
            addUserRequest.setCertificateCode("510823202308010001");
            addUserRequest.setRoleId(role.getId());
            addUserRequest.setRemark(hero.getRemark());
            addUserRequest.setBirthday(hero.getRelease());

            super.performWhenCall(mockController.add(addUserRequest));
        };

        for (Hero hero : heroes) {
            threadPoolExecutor.execute(() -> addHero.accept(hero));
        }

        threadPoolExecutor.shutdown();
        if (!threadPoolExecutor.awaitTermination(10, TimeUnit.MINUTES)) {
            log.error("should not happen", new ShouldNotHappenException());
        }
    }

    private List<Hero> heroes() {
        return List.of(
                new Hero("Artanis", "阿塔尼斯", "斗士", MALE, "2015-10-27", "达拉姆的大主教"),
                new Hero("Chen", "陈", "斗士", MALE, "2014-09-09", "传奇酒仙"),
                new Hero("D.Va", "D.Va", "斗士", FEMALE, "2017-05-16", "MEKA驾驶员"),
                new Hero("Deathwing", "死亡之翼", "斗士", MALE, "2019-12-03", "灭世者"),
                new Hero("Dehaka", "德哈卡", "斗士", MALE, "2016-03-29", "原始虫群首领"),
                new Hero("Gazlowe", "加兹鲁维", "斗士", MALE, "2014-01-01", "棘齿城老板"),
                new Hero("Hogger", "霍格", "斗士", MALE, "2020-12-01", "艾尔文森林的祸患"),
                new Hero("Imperius", "英普瑞斯", "斗士", MALE, "2019-01-08", "勇气大天使"),
                new Hero("Leoric", "李奥瑞克", "斗士", MALE, "2015-07-21", "骷髅王"),
                new Hero("Malthael", "马萨伊尔", "斗士", MALE, "2017-06-13", "死亡化身"),
                new Hero("Ragnaros", "拉格纳罗斯", "斗士", MALE, "2016-12-14", "炎魔之王"),
                new Hero("Rexxar", "雷克萨", "斗士", MALE, "2015-09-08", "部落的勇士"),
                new Hero("Sonya", "桑娅", "斗士", FEMALE, "2014-03-13", "迷途野蛮人"),
                new Hero("Thrall", "萨尔", "斗士", MALE, "2015-01-13", "部落大酋长"),
                new Hero("Varian", "瓦里安", "斗士", MALE, "2016-11-15", "联盟的至高王"),
                new Hero("Xul", "祖尔", "斗士", MALE, "2016-03-01", "神秘的死灵法师"),
                new Hero("Yrel", "伊瑞尔", "斗士", MALE, "2018-06-12", "希望之光"),
                new Hero("Alexstrasza", "阿莱克丝塔萨", "治疗者", FEMALE, "2017-11-14", "生命缚誓者"),
                new Hero("Ana", "安娜", "治疗者", FEMALE, "2017-09-26", "老练的狙击手"),
                new Hero("Anduin", "安度因", "治疗者", MALE, "2019-04-30", "暴风城国王"),
                new Hero("Auriel", "奥莉尔", "治疗者", FEMALE, "2016-08-09", "希望大天使"),
                new Hero("Brightwing", "光明之翼", "治疗者", FEMALE, "2014-04-15", "精灵龙"),
                new Hero("Deckard", "迪卡德", "治疗者", MALE, "2018-04-24", "赫拉迪姆最后一人"),
                new Hero("Kharazim", "卡拉辛姆", "治疗者", MALE, "2015-08-18", "梵罗达尼武僧"),
                new Hero("Li Li", "丽丽", "治疗者", FEMALE, "2014-04-15", "世界行者"),
                new Hero("Lt. Morales", "莫拉莉斯中尉", "治疗者", FEMALE, "2015-10-06", "战地医疗兵"),
                new Hero("Lúcio", "卢西奥", "治疗者", MALE, "2017-02-14", "自由战士DJ"),
                new Hero("Malfurion", "玛法里奥", "治疗者", MALE, "2014-03-13", "大德鲁伊"),
                new Hero("Rehgar", "雷加尔", "治疗者", MALE, "2014-07-22", "大地之环萨满"),
                new Hero("Stukov", "斯托科夫", "治疗者", MALE, "2017-07-11", "被感染的中将"),
                new Hero("Tyrande", "泰兰德", "治疗者", FEMALE, "2014-03-13", "艾露恩的高阶女祭司"),
                new Hero("Uther", "乌瑟尔", "治疗者", MALE, "2014-03-13", "光明使者"),
                new Hero("Whitemane", "怀特迈恩", "治疗者", FEMALE, "2018-08-07", "大检察官"),
                new Hero("Alarak", "阿拉纳克", "近刺", MALE, "2016-09-13", "塔达林的高阶领主"),
                new Hero("Illidan", "伊利丹", "近刺", MALE, "2014-03-13", "背叛者"),
                new Hero("Kerrigan", "凯瑞甘", "近刺", FEMALE, "2014-03-13", "刀锋女王"),
                new Hero("Maiev", "玛维", "近刺", FEMALE, "2018-02-06", "守望者"),
                new Hero("Murky", "奔波尔霸", "近刺", FEMALE, "2014-05-22", "鱼人宝宝"),
                new Hero("Qhira", "琪拉", "近刺", FEMALE, "2019-08-06", "伊莱西亚赏金猎人"),
                new Hero("Samuro", "萨穆罗", "近刺", MALE, "2016-10-18", "剑圣"),
                new Hero("The Butcher", "屠夫", "近刺", MALE, "2015-06-30", "血肉雕刻者"),
                new Hero("Valeera", "瓦莉拉", "近刺", FEMALE, "2017-01-24", "无冕者之影"),
                new Hero("Zeratul", "泽拉图", "近刺", MALE, "2014-03-13", "黑暗教长"),
                new Hero("Azmodan", "阿兹莫丹", "远刺", MALE, "2014-10-14", "罪恶之王"),
                new Hero("Cassia", "卡西娅", "远刺", FEMALE, "2017-04-04", "亚马逊战争仕女"),
                new Hero("Chromie", "克罗米", "远刺", FEMALE, "2016-05-17", "时光守护者"),
                new Hero("Falstad", "弗斯塔德", "远刺", MALE, "2014-01-01", "蛮锤领主"),
                new Hero("Fenix", "菲尼克斯", "远刺", MALE, "2018-03-27", "圣堂武士管理者"),
                new Hero("Gall", "加尔", "远刺", MALE, "2015-11-17", "暮光之锤的酋长"),
                new Hero("Genji", "源氏", "远刺", MALE, "2017-04-25", "半机械忍者"),
                new Hero("Greymane", "格雷迈恩", "远刺", MALE, "2016-01-12", "狼人之王"),
                new Hero("Gul'dan", "古尔丹", "远刺", MALE, "2016-07-12", "黑暗的化身"),
                new Hero("Hanzo", "半藏", "远刺", MALE, "2017-12-12", "刺客大师"),
                new Hero("Jaina", "吉安娜", "远刺", FEMALE, "2014-12-02", "大法师"),
                new Hero("Junkrat", "狂鼠", "远刺", MALE, "2017-10-17", "渣客爆破手"),
                new Hero("Kael'thas", "凯尔萨斯", "远刺", MALE, "2015-05-12", "太阳之王"),
                new Hero("Kel'Thuzad", "克尔苏加德", "远刺", MALE, "2017-09-05", "纳克萨玛斯的大巫妖"),
                new Hero("Li-Ming", "李敏", "远刺", FEMALE, "2016-02-02", "狂热的魔法师"),
                new Hero("Lunara", "露娜拉", "远刺", FEMALE, "2015-12-15", "塞纳留斯的长女"),
                new Hero("Mephisto", "墨菲斯托", "远刺", MALE, "2018-09-04", "憎恨之王"),
                new Hero("Nazeebo", "纳兹波", "远刺", MALE, "2014-03-13", "异端巫医"),
                new Hero("Nova", "诺娃", "远刺", FEMALE, "2014-03-13", "帝国幽灵特工"),
                new Hero("Orphea", "奥菲娅", "远刺", FEMALE, "2018-11-13", "乌鸦庭继承人"),
                new Hero("Probius", "普罗比斯", "远刺", MALE, "2017-03-14", "充满好奇心的探机"),
                new Hero("Raynor", "雷诺", "远刺", MALE, "2014-03-13", "起义军指挥官"),
                new Hero("Sgt. Hammer", "重锤军士", "远刺", FEMALE, "2014-03-13", "攻城坦克驾驶员"),
                new Hero("Sylvanas", "希尔瓦娜斯", "远刺", FEMALE, "2015-03-24", "女妖之王"),
                new Hero("Tassadar", "塔萨达尔", "远刺", MALE, "2014-01-01", "圣堂救世主"),
                new Hero("Tracer", "猎空", "远刺", MALE, "2016-04-19", "守望先锋特工"),
                new Hero("Tychus", "泰凯斯", "远刺", MALE, "2014-03-18", "臭名昭著的罪犯"),
                new Hero("Valla", "维拉", "远刺", FEMALE, "2014-03-13", "猎魔人"),
                new Hero("Zagara", "扎加拉", "远刺", FEMALE, "2014-06-25", "虫群之母"),
                new Hero("Zul'jin", "祖尔金", "远刺", MALE, "2017-01-04", "阿曼尼督军"),
                new Hero("Abathur", "阿巴瑟", "支援者", MALE, "2014-03-13", "进化大师"),
                new Hero("Medivh", "麦迪文", "支援者", FEMALE, "2016-06-14", "最后的守护者"),
                new Hero("The Lost Vikings", "失落的维京人", "支援者", MALE, "2015-02-10", "麻烦三人组"),
                new Hero("Zarya", "查莉娅", "支援者", MALE, "2016-09-27", "俄罗斯卫士"),
                new Hero("Anub'arak", "阿努巴拉克", "坦克", MALE, "2014-10-07", "叛变的国王"),
                new Hero("Arthas", "阿尔萨斯", "坦克", MALE, "2014-03-13", "巫妖王"),
                new Hero("Blaze", "布雷泽", "坦克", MALE, "2018-01-09", "精英火蝠"),
                new Hero("Cho", "古", "坦克", MALE, "2015-11-17", "暮光之锤的酋长"),
                new Hero("Diablo", "迪亚波罗", "坦克", MALE, "2014-03-13", "恐惧之王"),
                new Hero("E.T.C.", "精英牛头人酋长", "坦克", MALE, "2014-01-01", "摇滚教父"),
                new Hero("Garrosh", "加尔鲁什", "坦克", MALE, "2017-08-08", "地狱咆哮之子"),
                new Hero("Johanna", "乔汉娜", "坦克", MALE, "2015-06-02", "萨卡兰姆圣教军"),
                new Hero("Mal'Ganis", "玛尔加尼斯", "坦克", MALE, "2018-10-16", "纳斯雷兹姆领主"),
                new Hero("Mei", "美", "坦克", FEMALE, "2020-06-23", "爱冒险的气象学家"),
                new Hero("Muradin", "穆拉丁", "坦克", MALE, "2014-03-13", "山丘之王"),
                new Hero("Stitches", "缝合怪", "坦克", MALE, "2014-01-01", "夜色镇的梦魇"),
                new Hero("Tyrael", "泰瑞尔", "坦克", MALE, "2014-03-13", "正义天使"));
    }

    private Pairs<String, String> heroAvatars() {
        return new Pairs<String, String>()
                .append("Hogger", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/hogger/circleIcon.png")
                .append("Mei", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/mei/circleIcon.png")
                .append("Deathwing", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/deathwing/circleIcon.png")
                .append("Qhira", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/qhira/circleIcon.png")
                .append("Anduin", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/anduin/circleIcon.png")
                .append("Imperius", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/imperius/circleIcon.png")
                .append("Orphea", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/orphea/circleIcon.png")
                .append("Mal'Ganis", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/malganis/circleIcon.png")
                .append("Mephisto", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/mephisto/circleIcon.png")
                .append("Whitemane", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/whitemane/circleIcon.png")
                .append("Yrel", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/yrel/circleIcon.png")
                .append("Deckard", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/deckard/circleIcon.png")
                .append("Fenix", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/fenix/circleIcon.png")
                .append("Maiev", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/maiev/circleIcon.png")
                .append("Blaze", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/blaze/circleIcon.png")
                .append("Hanzo", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/hanzo/circleIcon.png")
                .append("Alexstrasza", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/alexstrasza/circleIcon.png")
                .append("Junkrat", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/junkrat/circleIcon.png")
                .append("Ana", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/ana/circleIcon.png")
                .append("Kel'Thuzad", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/kelthuzad/circleIcon.png")
                .append("Garrosh", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/garrosh/circleIcon.png")
                .append("Stukov", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/stukov/circleIcon.png")
                .append("Malthael", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/malthael/circleIcon.png")
                .append("D.Va", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/dva/circleIcon.png")
                .append("Genji", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/genji/circleIcon.png")
                .append("Cassia", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/cassia/circleIcon.png")
                .append("Probius", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/probius/circleIcon.png")
                .append("Lúcio", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/lucio/circleIcon.png")
                .append("Valeera", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/valeera/circleIcon.png")
                .append("Zul'jin", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/zuljin/circleIcon.png")
                .append("Ragnaros", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/ragnaros/circleIcon.png")
                .append("Varian", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/varian/circleIcon.png")
                .append("Samuro", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/samuro/circleIcon.png")
                .append("Zarya", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/zarya/circleIcon.png")
                .append("Alarak", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/alarak/circleIcon.png")
                .append("Auriel", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/auriel/circleIcon.png")
                .append("Gul'dan", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/guldan/circleIcon.png")
                .append("Medivh", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/medivh/circleIcon.png")
                .append("Chromie", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/chromie/circleIcon.png")
                .append("Tracer", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/tracer/circleIcon.png")
                .append("Dehaka", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/dehaka/circleIcon.png")
                .append("Xul", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/xul/circleIcon.png")
                .append("Li-Ming", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/li-ming/circleIcon.png")
                .append("Greymane", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/greymane/circleIcon.png")
                .append("Lunara", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/lunara/circleIcon.png")
                .append("Cho", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/cho/circleIcon.png")
                .append("Gall", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/gall/circleIcon.png")
                .append("Artanis", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/artanis/circleIcon.png")
                .append("Lt. Morales", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/lt-morales/circleIcon.png")
                .append("Rexxar", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/rexxar/circleIcon.png")
                .append("Kharazim", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/kharazim/circleIcon.png")
                .append("Leoric", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/leoric/circleIcon.png")
                .append("The Butcher", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/the-butcher/circleIcon.png")
                .append("Johanna", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/johanna/circleIcon.png")
                .append("Kael'thas", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/kaelthas/circleIcon.png")
                .append("Sylvanas", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/sylvanas/circleIcon.png")
                .append("The Lost Vikings", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/the-lost-vikings/circleIcon.png")
                .append("Thrall", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/thrall/circleIcon.png")
                .append("Jaina", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/jaina/circleIcon.png")
                .append("Azmodan", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/azmodan/circleIcon.png")
                .append("Anub'arak", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/anubarak/circleIcon.png")
                .append("Chen", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/chen/circleIcon.png")
                .append("Rehgar", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/rehgar/circleIcon.png")
                .append("Zagara", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/zagara/circleIcon.png")
                .append("Murky", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/murky/circleIcon.png")
                .append("Brightwing", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/brightwing/circleIcon.png")
                .append("Li Li", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/li-li/circleIcon.png")
                .append("Tychus", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/tychus/circleIcon.png")
                .append("Abathur", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/abathur/circleIcon.png")
                .append("Arthas", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/arthas/circleIcon.png")
                .append("Diablo", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/diablo/circleIcon.png")
                .append("Illidan", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/illidan/circleIcon.png")
                .append("Kerrigan", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/kerrigan/circleIcon.png")
                .append("Malfurion", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/malfurion/circleIcon.png")
                .append("Muradin", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/muradin/circleIcon.png")
                .append("Nazeebo", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/nazeebo/circleIcon.png")
                .append("Nova", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/nova/circleIcon.png")
                .append("Raynor", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/raynor/circleIcon.png")
                .append("Sgt. Hammer", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/sgt-hammer/circleIcon.png")
                .append("Sonya", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/sonya/circleIcon.png")
                .append("Tyrael", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/tyrael/circleIcon.png")
                .append("Tyrande", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/tyrande/circleIcon.png")
                .append("Uther", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/uther/circleIcon.png")
                .append("Valla", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/valla/circleIcon.png")
                .append("Zeratul", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/zeratul/circleIcon.png")
                .append("E.T.C.", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/etc/circleIcon.png")
                .append("Falstad", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/falstad/circleIcon.png")
                .append("Gazlowe", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/gazlowe/circleIcon.png")
                .append("Stitches", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/stitches/circleIcon.png")
                .append("Tassadar", "https://static.heroesofthestorm.com/gd/6f704aac5aa2f1cfad17ee130347fb3b/heroes/tassadar/circleIcon.png");
    }

    private String uploadAvatar(String heroName, String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<byte[]> httpResponse = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofByteArray());
        byte[] body = httpResponse.body();

        UploadFileVO uploadFileVO = new UploadFileVO();
        uploadFileVO.setFileName(heroName + ".png");
        uploadFileVO.setFile(new MockMultipartFile(heroName, new ByteArrayInputStream(body)));

        return fileService.upload(uploadFileVO, super.mockAdmin()).getFileId();
    }

    @Test
    void modify() {
        ModifyUserRequest modifyUserRequest = new ModifyUserRequest();
        modifyUserRequest.setId(0L);
        modifyUserRequest.setNickName("超级管理员");
        modifyUserRequest.setEnglishName("admin");
        modifyUserRequest.setGender(MALE);
        modifyUserRequest.setRemark("admin");
        modifyUserRequest.setBirthday(LocalDate.of(2023, Month.AUGUST, 1));
        super.performWhenCall(mockController.modify(null, modifyUserRequest));
    }

    @Test
    void changePassword() {

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setNewPassword("123456789");
        changePasswordRequest.setOldPassword("123456");

        super.performWhenCall(mockController.changePassword(MockAuth.mockAdmin(), changePasswordRequest));
    }

    @Test
    void resetPassword() {
        super.performWhenCall(mockController.resetPassword(super.mockAdmin(), 3L));
    }

    @Getter
    private static class Hero {

        private final String name;
        private final String nickName;
        private final String role;
        private final UserGenderEnum gender;
        private final LocalDate release;
        private final String remark;

        Hero(String name, String nickName, String role, UserGenderEnum gender, String release, String remark) {
            this.name = name;
            this.nickName = nickName;
            this.role = role;
            this.gender = gender;
            this.release = LocalDate.parse(release, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.remark = remark;
        }
    }

}

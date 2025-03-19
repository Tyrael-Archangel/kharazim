package com.tyrael.kharazim.product.controller;

import com.tyrael.kharazim.authentication.PrincipalHolder;
import com.tyrael.kharazim.base.dto.DataResponse;
import com.tyrael.kharazim.product.DubboReferenceHolder;
import com.tyrael.kharazim.product.ProductApiApplication;
import com.tyrael.kharazim.product.app.service.ProductCategoryService;
import com.tyrael.kharazim.product.app.vo.category.AddProductCategoryRequest;
import com.tyrael.kharazim.test.mock.BaseControllerTest;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
@SpringBootTest(classes = ProductApiApplication.class)
public class AddProductCategoryTest extends BaseControllerTest<ProductCategoryController> {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductCategoryController productCategoryController;

    @Autowired
    private DubboReferenceHolder dubboReferenceHolder;

    public AddProductCategoryTest() {
        super(ProductCategoryController.class);
    }

    @Test
    void add() {
        String categories = """
                name: null
                children:
                  - name: 控制技能
                    children:
                      - name: 击晕
                      - name: 沉默
                      - name: 定身
                      - name: 昏迷
                      - name: 减速
                      - name: 强制位移
                  - name: 伤害技能
                    children:
                      - name: 指向性伤害
                      - name: 非指向性伤害
                      - name: 持续性伤害
                      - name: 延迟伤害
                  - name: 保护技能
                    children:
                      - name: 恢复血量
                      - name: 护盾
                      - name: 盾墙
                  - name: 位移技能
                    children:
                      - name: 闪现
                      - name: 翻滚
                      - name: 埋地移动
                  - name: 支援技能
                    children:
                      - name: 视野
                      - name: 传送门
                      - name: 激素
                """;
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        PrivateCategory category = yaml.loadAs(categories, PrivateCategory.class);

        PrincipalHolder.setPrincipal(dubboReferenceHolder.mockUser());
        recursiveAdd(category, null);
    }

    private void recursiveAdd(PrivateCategory category, String parentCode) {
        String name = category.getName();
        if (name != null) {
            Long parentId = null;
            if (parentCode != null) {
                parentId = productCategoryService.getByCode(parentCode).getId();
            }
            AddProductCategoryRequest addRequest = new AddProductCategoryRequest();
            addRequest.setParentId(parentId);
            addRequest.setName(name);
            addRequest.setRemark("--" + name + "--");
            DataResponse<String> response = productCategoryController.add(addRequest);
            category.setCode(response.getData());
        }

        for (PrivateCategory child : category.getChildren()) {
            recursiveAdd(child, category.getCode());
        }
    }

    @Data
    private static class PrivateCategory {
        private String name;
        private List<PrivateCategory> children = new ArrayList<>();
        private String code;
    }

}
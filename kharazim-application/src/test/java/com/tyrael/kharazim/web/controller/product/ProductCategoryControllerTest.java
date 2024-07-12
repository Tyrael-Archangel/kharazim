package com.tyrael.kharazim.web.controller.product;

import com.tyrael.kharazim.application.product.service.ProductCategoryService;
import com.tyrael.kharazim.application.product.vo.category.AddProductCategoryRequest;
import com.tyrael.kharazim.common.dto.DataResponse;
import com.tyrael.kharazim.mock.MockAuth;
import com.tyrael.kharazim.web.controller.BaseControllerTest;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/27
 */
class ProductCategoryControllerTest extends BaseControllerTest<ProductCategoryController> {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductCategoryController productCategoryController;

    ProductCategoryControllerTest() {
        super(ProductCategoryController.class);
    }

    @Test
    void categoryTree() {
        super.performWhenCall(mockController.categoryTree());
    }

    @Test
    void allCategories() {
        super.performWhenCall(mockController.allCategories());
    }

    @Test
    @SuppressWarnings("all")
    void add() {
        String categories = """
                name: null
                children:
                  - name: 中药
                    children:
                      - name: 解表药
                      - name: 清热药
                      - name: 止血药
                      - name: 安神药
                  - name: 西药
                    children:
                      - name: 抗感染类
                        children:
                          - name: 抗生素
                          - name: 抗病毒
                          - name: 抗结核
                      - name: 消化类
                        children:
                          - name: 抗酸药
                          - name: 促胃肠动力药
                          - name: 胃肠解痉药
                      - name: 神经类
                        children:
                          - name: 中枢兴奋药
                          - name: 非甾体抗炎药
                          - name: 镇静催眠药
                                """;
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        PrivateCategory category = yaml.loadAs(categories, PrivateCategory.class);
        MockAuth.mockCurrentAdmin();

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
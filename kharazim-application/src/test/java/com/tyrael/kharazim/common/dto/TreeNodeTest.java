package com.tyrael.kharazim.common.dto;

import lombok.Data;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Tyrael Archangel
 * @since 2024/1/5
 */
public class TreeNodeTest {

    public static void main(String[] args) {

        String str = """
                name: 总经办
                children:
                  - name: 投资部
                  - name: 财务部
                    children:
                      - name: 核算组
                      - name: 资金组
                      - name: 财务BP组
                      - name: 税务组
                  - name: 法务风控部
                    children:
                      - name: 法务组
                  - name: 商品中心
                    children:
                      - name: 质量组
                        children:
                          - name: 商品质量
                          - name: 产线
                          - name: 食品安全
                      - name: 计调组
                      - name: 商品部
                        children:
                          - name: 商品组
                          - name: 品牌组
                          - name: 采购组
                      - name: 售后组
                      - name: 产品部
                        children:
                          - name: 应用组
                          - name: 支持组
                          - name: 转化组
                          - name: 研发组
                  - name: 营销中心
                    children:
                      - name: 直销事业部
                      - name: 职能BP
                        children:
                          - name: HR组
                          - name: 财务BP组
                      - name: 电商部
                        children:
                          - name: 渠道
                          - name: 内容
                          - name: 直播
                      - name: 大客户组
                      - name: 大宗贸易部
                  - name: 行政人事部
                    children:
                      - name: 行政组
                      - name: 人力资源组
                  - name: 信息技术部
                    children:
                      - name: 产品组
                      - name: 实施组
                      - name: 研发组
                      - name: 测试组
                      - name: 运维组
                """;

        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        @SuppressWarnings("VulnerableCodeUsages")
        PrivateDept privateDept = yaml.loadAs(str, PrivateDept.class);

        Dept root = recursiveBuild(privateDept, new AtomicLong(), null);
        System.out.println(TreeNode.pretty(root));

    }

    private static Dept recursiveBuild(PrivateDept privateDept, AtomicLong id, Dept parent) {
        Dept dept = new Dept(id.incrementAndGet(), parent == null ? null : parent.getId(), privateDept.getName());
        List<Dept> children = new ArrayList<>();
        for (PrivateDept privateChild : privateDept.getChildren()) {
            children.add(recursiveBuild(privateChild, id, dept));
        }
        dept.setChildren(children);
        return dept;
    }

    @Data
    private static class Dept extends TreeNode<Dept, Long> {
        private String name;

        public Dept(Long id, Long parentId, String name) {
            super.id = id;
            super.parentId = parentId;
            this.name = name;
        }

        @Override
        public String toString() {
            return name + "(" + id + ")";
        }
    }

    @Data
    private static class PrivateDept {
        private String name;
        private List<PrivateDept> children = new ArrayList<>();
    }

}

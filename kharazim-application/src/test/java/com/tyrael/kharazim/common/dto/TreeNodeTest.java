package com.tyrael.kharazim.common.dto;

import lombok.Data;

import java.util.ArrayList;

/**
 * @author Tyrael Archangel
 * @since 2024/1/5
 */
public class TreeNodeTest {

    public static void main(String[] args) {
        long id = 0L;
        Dept root = new Dept(++id, null, "总经办");
        root.appendChild(++id, "投资部");
        Dept finance = root.appendChild(++id, "财务部");
        finance.appendChild(++id, "核算组");
        finance.appendChild(++id, "资金组");
        finance.appendChild(++id, "财务BP组");
        finance.appendChild(++id, "税务组");

        root.appendChild(++id, "法务风控部")
                .appendChild(++id, "法务组");

        Dept productCenter = root.appendChild(++id, "商品中心");
        Dept productQuality = productCenter.appendChild(++id, "质量组");
        productQuality.appendChild(++id, "商品质量");
        productQuality.appendChild(++id, "产线");
        productQuality.appendChild(++id, "食品安全");
        productCenter.appendChild(++id, "计调组");
        Dept productDept = productCenter.appendChild(++id, "商品部");
        productDept.appendChild(++id, "商品组");
        productDept.appendChild(++id, "品牌组");
        productDept.appendChild(++id, "采购组");
        productCenter.appendChild(++id, "售后组");
        Dept pdm = productCenter.appendChild(++id, "产品部");
        pdm.appendChild(++id, "应用组");
        pdm.appendChild(++id, "支持组");
        pdm.appendChild(++id, "转化组");
        pdm.appendChild(++id, "研发组");

        Dept saleCenter = root.appendChild(++id, "营销中心");
        saleCenter.appendChild(++id, "直销事业部");
        Dept functionBP = saleCenter.appendChild(++id, "职能BP");
        functionBP.appendChild(++id, "HR组");
        functionBP.appendChild(++id, "财务BP组");
        Dept eCommerce = saleCenter.appendChild(++id, "电商部");
        eCommerce.appendChild(++id, "渠道");
        eCommerce.appendChild(++id, "内容");
        eCommerce.appendChild(++id, "直播");
        saleCenter.appendChild(++id, "大客户组");
        saleCenter.appendChild(++id, "大宗贸易部");

        Dept administrative = root.appendChild(++id, "行政人事部");
        administrative.appendChild(++id, "行政组");
        administrative.appendChild(++id, "人力资源组");

        Dept it = root.appendChild(++id, "信息技术部");
        it.appendChild(++id, "产品组");
        it.appendChild(++id, "实施组");
        it.appendChild(++id, "研发组");
        it.appendChild(++id, "测试组");
        it.appendChild(++id, "运维组");

        String pretty = TreeNode.pretty(root);
        System.out.println(pretty);
    }

    @Data
    private static class Dept extends TreeNode<Dept, Long> {
        private String name;

        public Dept(Long id, Long parentId, String name) {
            super.id = id;
            super.parentId = parentId;
            this.name = name;
        }

        public Dept appendChild(Long id, String name) {
            if (this.getChildren() == null) {
                this.children = new ArrayList<>();
            }
            Dept child = new Dept(id, this.id, name);
            this.children.add(child);
            return child;
        }

        @Override
        public String toString() {
            return name + "(" + id + ")";
        }
    }

}

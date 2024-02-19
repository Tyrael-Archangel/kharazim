package com.tyrael.kharazim.demo.practice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyrael Archangel
 * @since 2024/2/19
 */
public class TreeNodeToStringDemo {

    public static void main(String[] args) {

        Node root = new Node(1);
        root.addChild(2);
        Node node3 = root.addChild(3);
        node3.addChild(6);
        node3.addChild(7).addChild(11).addChild(14);
        Node node4 = root.addChild(4);
        node4.addChild(8).addChild(12);
        Node node5 = root.addChild(5);
        node5.addChild(9).addChild(13);
        node5.addChild(10);
        System.out.println(root);
    }

    private static class Node {
        int val;
        List<Node> children;

        public Node(int val) {
            this.val = val;
        }

        public Node addChild(int val) {
            if (this.children == null) {
                this.children = new ArrayList<>();
            }
            Node child = new Node(val);
            this.children.add(child);
            return child;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            append(this, builder, 0);
            return builder.toString();
        }

        private void append(Node treeNode, StringBuilder builder, int deep) {
            if (treeNode == null) {
                return;
            }
            if (deep > 0) {
                builder.append("    ".repeat(deep - 1));
                builder.append("|...");
            }
            builder.append(treeNode.val);
            List<Node> children = treeNode.children;
            if (children != null) {
                for (Node child : children) {
                    builder.append(System.lineSeparator());
                    append(child, builder, deep + 1);
                }
            }
        }
    }

}

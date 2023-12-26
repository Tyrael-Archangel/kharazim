package com.tyrael.kharazim.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 树形节点
 *
 * @param <T> TreeNode
 * @param <U> ID
 * @author Tyrael Archangel
 * @since 2023/12/26
 */
@Data
public abstract class TreeNode<T, U> {

    protected U id;
    protected U parentId;
    protected Collection<T> children;

    public static <T extends TreeNode<T, U>, U> List<T> build(Collection<T> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return new ArrayList<>();
        }

        Map<U, T> nodeMap = nodes.stream()
                .collect(Collectors.toMap(T::getId, t -> t));

        List<T> roots = new ArrayList<>();
        for (T node : nodes) {
            T parent = nodeMap.get(node.getParentId());
            if (parent == null) {
                roots.add(node);
            } else {
                Collection<T> children = parent.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parent.setChildren(children);
                }
                children.add(node);
            }
        }
        return roots;
    }

    public static <T extends TreeNode<T, U>, U> String pretty(Collection<T> treeNodes) {
        if (treeNodes == null) {
            return null;
        }
        StringBuilder builder = null;
        for (T treeNode : treeNodes) {
            if (builder == null) {
                builder = new StringBuilder();
            } else {
                builder.append(System.lineSeparator());
            }
            append(treeNode, builder, 0);
        }
        return builder == null ? "" : builder.toString();
    }

    private static <T extends TreeNode<T, U>, U> void append(T treeNode, StringBuilder builder, int deep) {
        if (treeNode == null) {
            return;
        }
        builder.append("..".repeat(Math.max(0, deep)));
        builder.append(treeNode);
        Collection<T> children = treeNode.getChildren();
        if (children != null) {
            for (T child : children) {
                builder.append(System.lineSeparator());
                append(child, builder, deep + 1);
            }
        }
    }

}

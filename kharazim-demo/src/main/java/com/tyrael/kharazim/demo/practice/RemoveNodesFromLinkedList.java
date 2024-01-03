package com.tyrael.kharazim.demo.practice;

import com.tyrael.kharazim.demo.util.ListNode;

/**
 * 从链表中移除节点
 * <pre>
 * 给你一个链表的头节点 head，移除每个右侧有一个更大数值的节点，返回修改后链表的头节点 head。
 * </pre>
 *
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
public class RemoveNodesFromLinkedList {

    public static void main(String[] args) {
        ListNode head = ListNode.newListNode(5, 2, 13, 3, 8);
        System.out.println(head);
        ListNode node = removeBiggerNodes(head);
        System.out.println(node);
    }

    private static ListNode removeBiggerNodes(ListNode head) {
        if (head == null) {
            return null;
        }
        head.next = removeBiggerNodes(head.next);
        if (head.next == null || head.val > head.next.val) {
            return head;
        } else {
            return head.next;
        }
    }

}

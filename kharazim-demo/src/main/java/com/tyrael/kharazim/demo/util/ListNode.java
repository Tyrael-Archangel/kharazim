package com.tyrael.kharazim.demo.util;

import java.util.StringJoiner;

/**
 * @author Tyrael Archangel
 * @since 2024/1/3
 */
@SuppressWarnings("unused")
public class ListNode {

    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int x) {
        this.val = x;
    }

    public static ListNode newListNode(int... vals) {
        if (vals == null || vals.length == 0) {
            return null;
        }
        ListNode head = new ListNode(vals[0]);
        ListNode next = head;
        for (int i = 1; i < vals.length; i++) {
            next.next = new ListNode(vals[i]);
            next = next.next;
        }
        return head;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(" -> ", "[", "]");
        ListNode temp = this;
        do {
            joiner.add(Integer.toString(temp.val));
        } while ((temp = temp.next) != null);
        return joiner.toString();
    }

}

package com.henry.leetcode_traning_camp.week_01.day1.linkedlist;

public class Solution_reverse_listnode_01 {
    public static void main(String[] args) {
        // 创建一个链表对象
        ListNode node_1 = new ListNode(1);
        ListNode node_2 = new ListNode(2);
        node_1.next = node_2;
        ListNode node_3 = new ListNode(3);
        node_2.next = node_3;
        ListNode node_4 = new ListNode(4);
        node_3.next = node_4;

        // 对链表中的节点进行反转
        ListNode reverseRes = reverseList(node_1);

        while (reverseRes != null) {
            System.out.print(reverseRes.val + " -> ");
            reverseRes = reverseRes.next;
        }
    }

    // 递归手段
    private static ListNode reverseList(ListNode head) {
        if(head == null || head.next == null) return head;

        ListNode second = head.next;
        ListNode newHead = reverseList(second);

        head.next = null;
        second.next = head;

        return newHead;
    }
}

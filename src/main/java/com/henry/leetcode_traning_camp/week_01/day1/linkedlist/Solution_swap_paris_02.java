package com.henry.leetcode_traning_camp.week_01.day1.linkedlist;

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}

public class Solution_swap_paris_02 {
    public static void main(String[] args) {
        // 创建一个链表对象
        ListNode node_1 = new ListNode(1);
        ListNode node_2 = new ListNode(2);
        node_1.next = node_2;
        ListNode node_3 = new ListNode(3);
        node_2.next = node_3;
        ListNode node_4 = new ListNode(4);
        node_3.next = node_4;

        ListNode swapRes = swapPairs(node_1);

        while (swapRes != null) {
            System.out.print(swapRes.val + " ");
            swapRes = swapRes.next;
        }

    }

    private static ListNode swapPairs(ListNode head) {
        if(head == null || head.next == null) return head;

        // 准备一些指针，用于帮助反转节点指向
        ListNode dummy = new ListNode(-1); // 假节点 帮助反转节点的指向
        dummy.next = head;

        ListNode prev = dummy;
        ListNode curr = head;

        // 准备一个循环 在循环中：1 进行节点指向的反转； 2 更新指针的区间
        while (curr != null && curr.next != null) {
            ListNode temp = curr.next;

            // 执行节点的反转
            prev.next = temp;
            curr.next = temp.next;
            temp.next = curr;

            // 更新指针区间
            prev = curr;
            curr = prev.next; // prev指针与curr指针的初始位置总是一前一后相邻
        }

        return dummy.next;
    }
}

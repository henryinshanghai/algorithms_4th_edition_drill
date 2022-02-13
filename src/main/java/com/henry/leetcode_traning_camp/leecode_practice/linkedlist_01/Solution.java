package com.henry.leetcode_traning_camp.leecode_practice.linkedlist_01;

public class Solution {
    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) { // 两个链表结构
        // 初始化两个指针：指向链表的头部
        ListNode pA = headA, pB = headB;
        // 进行循环遍历
        while (pA != pB) {
            // 如果pA到达链表尾部，就让pA重新指向headB。否则沿着headA一路往下
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }
        // 当两个节点相等时，返回相等的节点 即为相交的节点
        return pA;
    }
}


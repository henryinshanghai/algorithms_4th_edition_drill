package com.henry.leetcode_traning_camp.week_01.day1.linkedlist;

public class Solution_listnode_with_loop_03 {
    public static void main(String[] args) {
        // 创建一个带有环的链表
        ListNode node_1 = new ListNode(1);
        ListNode node_2 = new ListNode(2);
        node_1.next = node_2;
        ListNode node_3 = new ListNode(3);
        node_2.next = node_3;
        ListNode node_4 = new ListNode(4);
        node_3.next = node_4;
        ListNode node_5 = new ListNode(5); // 能够快速生成一列递增的数据吗？
        node_4.next = node_5;

//        node_5.next = node_3; // 环形链表

        boolean result = withLoopOrNot(node_1);

        if (result) {
            System.out.println("链表中包含有环");
        } else {
            System.out.println("链表中不包含有环");
        }
    }

    private static boolean withLoopOrNot(ListNode head) {
        // 0
        if (head == null || head.next == null) return false;

        // 1 准备指针
        ListNode slow = head;
        ListNode fast = head;

        while(fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                return true;
            }
        }

        return false;
    }
}

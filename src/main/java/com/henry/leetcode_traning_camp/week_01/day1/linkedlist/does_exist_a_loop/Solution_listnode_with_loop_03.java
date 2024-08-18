package com.henry.leetcode_traning_camp.week_01.day1.linkedlist.does_exist_a_loop;


import com.henry.leetcode_traning_camp.week_01.day1.linkedlist.ListNode;

// 验证：可以使用 快慢指针的方式 来 判断链表中是否存在有环结构
public class Solution_listnode_with_loop_03 {
    public static void main(String[] args) {
        // 创建一个带有环的链表
        ListNode headNode = constructALinkedNodesWithLoop();

        boolean result = doesExistALoop(headNode);
        printOut(result);
    }

    private static void printOut(boolean result) {
        if (result) {
            System.out.println("链表中包含有环");
        } else {
            System.out.println("链表中不包含有环");
        }
    }

    private static ListNode constructALinkedNodesWithLoop() {
        ListNode node_1 = new ListNode(1);
        ListNode node_2 = new ListNode(2);
        node_1.next = node_2;
        ListNode node_3 = new ListNode(3);
        node_2.next = node_3;
        ListNode node_4 = new ListNode(4);
        node_3.next = node_4;
        ListNode node_5 = new ListNode(5); // 能够快速生成一列递增的数据吗？
        node_4.next = node_5;

        node_5.next = node_3; // 为链表引入一个环

        return node_1;
    }

    private static boolean doesExistALoop(ListNode headNode) {
        // #0 如果是空链表 或者 单结点链表，说明 不可能存在有环，则：返回false
        if (headNode == null || headNode.next == null) return false;

        // #1 准备快慢指针，并把它们设置在相同的起始位置
        ListNode slowCursor = headNode;
        ListNode fastCursor = headNode;

        // 在快指针 到达最后一个结点之前...
        while(fastCursor != null && fastCursor.next != null){
            // 慢指针每次移动一个位置，而快指针每次移动两个位置
            slowCursor = slowCursor.next;
            fastCursor = fastCursor.next.next;

            // 如果快指针与慢指针相遇，说明 链表结构中存在有环，则：返回true（这会结束循环 并跳出方法）
            if (slowCursor == fastCursor) {
                return true;
            }
        }

        // 如果循环结束后，方法并没有退出，说明链表中不存在有环，则：返回false
        return false;
    }
}

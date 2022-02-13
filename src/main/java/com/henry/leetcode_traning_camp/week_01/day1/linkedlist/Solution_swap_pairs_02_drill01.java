package com.henry.leetcode_traning_camp.week_01.day1.linkedlist;

// expr:同一个包中已经定义过的类就不需要重复定义了
//class ListNode{
//    int val;
//    ListNode next;
//
//    ListNode(int x){
//        val = x;
//    }
//}

public class Solution_swap_pairs_02_drill01 {
    public static void main(String[] args) {
        // 准备一个链表对象
        ListNode node_1 = new ListNode(1);
        ListNode node_2 = new ListNode(2);
        node_1.next = node_2;
        ListNode node_3 = new ListNode(3);
        node_2.next = node_3;
        ListNode node_4 = new ListNode(4);
        node_3.next = node_4;

        // 对链表对象中的节点进行两两交换
        ListNode swapRes = swapPairs(node_1);

        // 遍历打印节点交换后的链表
        while (swapRes != null) {
            System.out.print(swapRes.val + " -> ");
            swapRes = swapRes.next;
        }
    }

    // 迭代方式实现   实现完成后看一下国际站的代码
    private static ListNode swapPairs(ListNode head) {
        // 0
        if (head == null || head.next == null) {
            return head;
        }

        // 1 准备假节点与指针 用于交换节点
        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        ListNode prev = dummy;
        ListNode curr = head;

        // 2 准备一个循环来交换元素，并更新指针区间
        // prev    curr temp
        // dummy -> 1 -> 2 -> 3 -> 4
        // 2 -> 1 -> 4 -> 3
        while (curr != null && curr.next != null) {
            ListNode temp = curr.next;

            // 交换节点
            prev.next = temp;
            curr.next = temp.next;
            temp.next = curr;

            // 更新指针区间
            prev = curr;
            curr = prev.next;
        }

        return dummy.next;
    }

//    // 递归方式实现：成对地交换链表中的节点，并返回新链表的头节点
//    // 1 -> 2 -> 3 -> 4
//    // 2 -> 1 -> 4 -> 3
//    private static ListNode swapPairs(ListNode head) {
//        // 1 递归终结条件
//        if (head == null || head.next == null) return head;
//
//        // 2 本级递归要做的事情
//        ListNode second = head.next;
//        ListNode subListHead = second.next;
//
//        ListNode subListSwapRes = swapPairs(subListHead);
//
//        second.next = head;
//        head.next = subListSwapRes;
//
//        return second;
//    }

}

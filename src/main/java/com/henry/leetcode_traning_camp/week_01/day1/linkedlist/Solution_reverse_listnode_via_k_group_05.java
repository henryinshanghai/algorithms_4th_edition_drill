package com.henry.leetcode_traning_camp.week_01.day1.linkedlist;

public class Solution_reverse_listnode_via_k_group_05 {
    public static void main(String[] args) {
        // 创建一个链表对象
        ListNode node_1 = new ListNode(1);
        ListNode node_2 = new ListNode(2);
        node_1.next = node_2;
        ListNode node_3 = new ListNode(3);
        node_2.next = node_3;
        ListNode node_4 = new ListNode(4);
        node_3.next = node_4;
        ListNode node_5 = new ListNode(5);
        node_4.next = node_5;

        // k个一组反转链表中的节点
        // 1 -> 2 -> 3 -> 4 -> 5
        // 2 - 1 - 4 - 3 - 5
        ListNode resListNode = reverseKGroup(node_1, 2);

        // 打印反转后的新链表中的节点
        while (resListNode != null) {
            System.out.print(resListNode.val + " -> ");
            resListNode = resListNode.next;
        }
    }

    // 递归方法：k个一组地反转链表中的节点，并返回反转后的新链表
    private static ListNode reverseKGroup(ListNode head, int k) {
        if(head == null || head.next == null) return head;

        // 1 递归终结条件:链表的长度小于k
        // 手段：让一个指针沿着链表向后走n个节点
        ListNode cursor = head;
        for (int i = 0; i < k; i++) {
            if(cursor == null) return head;
            cursor = cursor.next;
        } // cursor指针指在第(k+1)个节点上

        /* 2 本级递归需要做的事情 */
        // 2-1 把链表中的前k个节点今进行反转，得到新链表A
        ListNode newHead = reverse(head, cursor);

        // 2-2 对剩下的节点所构成的rest链表执行“k个一组进行反转”的操作，得到新链表B
        head.next = reverseKGroup(cursor, k);

        return newHead; // 返回新链表的头节点
    }

    // 对链表中end节点之前的所有节点进行反转
    private static ListNode reverse(ListNode head, ListNode end) {
        if(head == null || head.next == null) return head;

        // 准备指针
        ListNode prev = null;
        ListNode curr = head;

        // 准备循环
        while (curr != end) { // curr == end时，指针指向的节点不会参与反转
            ListNode temp = curr.next;

            // 反转节点的连接方向
            curr.next = prev;

            // 更新指针的区间
            prev = curr;
            curr = temp;
        }

        return prev;
    }
}

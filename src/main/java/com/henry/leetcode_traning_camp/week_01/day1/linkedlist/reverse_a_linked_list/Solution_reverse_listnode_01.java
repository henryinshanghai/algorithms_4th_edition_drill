package com.henry.leetcode_traning_camp.week_01.day1.linkedlist.reverse_a_linked_list;

// 定义链表中的结点
class ListNode {
    // #1 结点的值
    int val;
    // #2 对下一个结点的引用
    ListNode next;

    ListNode(int x) { val = x; }
}

public class Solution_reverse_listnode_01 {
    public static void main(String[] args) {
        // 创建一个链表对象
        ListNode headNode = constructAListNode();
        printListNode(headNode);

        // 对链表中的节点进行反转
        ListNode reversedHeadNode = reverseList(headNode);
        printListNode(reversedHeadNode);
    }

    private static ListNode constructAListNode() {
        ListNode node_1 = new ListNode(1);
        ListNode node_2 = new ListNode(2);
        node_1.next = node_2;
        ListNode node_3 = new ListNode(3);
        node_2.next = node_3;
        ListNode node_4 = new ListNode(4);
        node_3.next = node_4;

        return node_1;
    }

    private static void printListNode(ListNode currentNode) {
        while (currentNode != null) {
            System.out.print(currentNode.val + " -> ");
            currentNode = currentNode.next;
        }
        System.out.print("null");
        System.out.println();
    }

    // 递归手段
    private static ListNode reverseList(ListNode currentNode) {
        // #0 递归终结条件
        if(currentNode == null || currentNode.next == null) return currentNode;

        // #1 子问题👇
        // 获取到当前结点的下一个结点
        ListNode second = currentNode.next;
        // 翻转 以该结点作为头结点的链表
        ListNode newHead = reverseList(second);

        // #2-1 使用子问题的答案 来 帮助解决原始问题👇
        currentNode.next = null;
        second.next = currentNode;

        // #2-2 原始问题👇
        // 返回 翻转后的链表的头结点
        return newHead;
    }
}

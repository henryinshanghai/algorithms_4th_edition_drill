package com.henry.leetcode_traning_camp.week_01.day1.linkedlist.swap_nodes_in_pair;

class ListNode {
    int val;
    ListNode next;

    public ListNode(int val) {
        this.val = val;
    }
}

// 验证：可以使用双指针的方式 来 实现对链表中结点对中结点的翻转
public class Solution_swap_paris_02 {
    public static void main(String[] args) {
        ListNode headNode = constructLinkedNode();
        printOut(headNode);

//        ListNode swappedHeadNode = swapNodesInPairViaIteration(headNode);
        ListNode swappedHeadNode = swapNodesInPairViaRecursion(headNode);
        printOut(swappedHeadNode);
    }

    private static void printOut(ListNode swappedHeadNode) {
        while (swappedHeadNode != null) {
            System.out.print(swappedHeadNode.val + " -> ");
            swappedHeadNode = swappedHeadNode.next;
        }

        System.out.print("null");
        System.out.println();
    }

    private static ListNode constructLinkedNode() {
        // 创建一个链表对象
        ListNode node_1 = new ListNode(1);
        ListNode node_2 = new ListNode(2);
        node_1.next = node_2;
        ListNode node_3 = new ListNode(3);
        node_2.next = node_3;
        ListNode node_4 = new ListNode(4);
        node_3.next = node_4;
        return node_1;
    }

    private static ListNode swapNodesInPairViaIteration(ListNode currentNode) {
        if (currentNode == null || currentNode.next == null) return currentNode;

        // 准备一个假节点 用于帮助 反转“节点的指向”
        ListNode dummy = new ListNode(-1);
        dummy.next = currentNode;

        // 准备一些指针，用于帮助反转节点指向
        ListNode previousCursor = dummy;
        ListNode currentCursor = currentNode;

        // 准备一个循环 在循环中：1 进行节点指向的反转； 2 更新指针的区间
        while (currentCursor != null && currentCursor.next != null) {
            ListNode temp = currentCursor.next;

            // 执行各个节点 指向的反转
            previousCursor.next = temp;
            currentCursor.next = temp.next;
            temp.next = currentCursor;

            // 更新 previousCursor与currentCursor 这一组指针的位置
            previousCursor = currentCursor;
            currentCursor = previousCursor.next; // prev指针与curr指针的初始位置总是一前一后相邻
        }

        return dummy.next;
    }

    // 递归方式实现：成对地交换链表中的节点，并返回新链表的头节点
    // 1 -> 2 -> 3 -> 4
    // 2 -> 1 -> 4 -> 3
    private static ListNode swapNodesInPairViaRecursion(ListNode currentNode) {
        // #0 递归终结条件 当前结点为链表中的最后一个结点 或者 null结点
        if (currentNode == null || currentNode.next == null) return currentNode;

        // 链表 = 头结点 + 第二个结点 + 剩余节点构成的子链表
        ListNode secondNode = currentNode.next;
        ListNode subListsHead = secondNode.next;

        // #1 解决 规模更小的子问题
        ListNode swappedHeadNode = swapNodesInPairViaRecursion(subListsHead);

        // #2 使用 子问题的答案 帮助解决原始问题
        secondNode.next = currentNode;
        currentNode.next = swappedHeadNode;

        // 返回原始问题的答案
        return secondNode;
    }
}

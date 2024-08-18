package com.henry.leetcode_traning_camp.week_01.day1.linkedlist.reverse_nodes_in_group;

import com.henry.leetcode_traning_camp.week_01.day1.linkedlist.ListNode;

// 验证：可以使用递归(拆分子问题&&使用子问题的解来帮助解决原始问题)的方式 来 实现对元素序列的k个一组的组内元素翻转
public class Solution_reverse_listnode_via_k_group_05 {
    public static void main(String[] args) {
        // 创建一个链表对象
        ListNode headNode = constructLinkedNode();

        // k个一组反转链表中的节点
        // 1 -> 2 -> 3 -> 4 -> 5
        // 2 - 1 - 4 - 3 - 5
        ListNode resListNode = reverseNodesInGroup(headNode, 2); // 3 / 4 / 5 / 6

        // 打印反转后的新链表中的节点
        printOut(resListNode);
    }

    private static void printOut(ListNode resListNode) {
        while (resListNode != null) {
            System.out.print(resListNode.val + " -> ");
            resListNode = resListNode.next;
        }
    }

    private static ListNode constructLinkedNode() {
        ListNode node_1 = new ListNode(1);
        ListNode node_2 = new ListNode(2);
        node_1.next = node_2;
        ListNode node_3 = new ListNode(3);
        node_2.next = node_3;
        ListNode node_4 = new ListNode(4);
        node_3.next = node_4;
        ListNode node_5 = new ListNode(5);
        node_4.next = node_5;
        return node_1;
    }

    // 递归方法：k个一组地反转链表中的节点，并返回反转后的新链表
    private static ListNode reverseNodesInGroup(ListNode currentNode, int groupSize) {
        // #0 终结条件：当前结点是null结点 或者 最后一个结点
        if(currentNode == null || currentNode.next == null) return currentNode;

        /* 本级递归需要做的事情👇 */
        // 设置一个前进指针，让它沿着链表向后走groupSize个节点
        ListNode forwardCursor = currentNode;
        for (int i = 0; i < groupSize; i++) {
            // 特殊情况：当前组没有groupSize这么多个元素, 则 不做任何操作，直接返回原始链表
            if(forwardCursor == null) return currentNode;
            forwardCursor = forwardCursor.next;
        } // cursor指针会指在第(k+1)个节点上

        // #1 把链表中的前k个节点进行反转，得到新链表A
        ListNode reversedHeadNode = reverseNodesUpTo(currentNode, forwardCursor);

        // #2-1 对剩下的节点所构成的子链表 执行“k个一组进行反转”的操作（子问题），得到新链表B
        // #2-2 使用子问题来帮助解决原始问题👇
        currentNode.next = reverseNodesInGroup(forwardCursor, groupSize);

        // 返回新链表的头节点
        return reversedHeadNode;
    }

    // 对链表中end节点之前的所有节点进行反转
    private static ListNode reverseNodesUpTo(ListNode startNode, ListNode endNode) {
        if(startNode == null || startNode.next == null) return startNode;

        // 准备指针
        ListNode previousCursor = null;
        ListNode currentCursor = startNode;

        // 准备循环
        while (currentCursor != endNode) { // currentCursor == end时，指针指向的节点不会参与反转
            // 反转节点的连接方向
            ListNode temp = currentCursor.next;
            currentCursor.next = previousCursor;

            // 更新当前指针
            previousCursor = currentCursor;
            currentCursor = temp;
        }

        return previousCursor;
    }
}

package com.henry.leetcode_traning_camp.week_01.day1.linkedlist.find_the_loop_entrance;

import com.henry.leetcode_traning_camp.week_01.day1.linkedlist.ListNode;

public class Solution_loops_entree_04 {
    public static void main(String[] args) {
        // 创建一个带有环的链表
        ListNode node_1 = constructALinkedListContainLoop();

        ListNode entranceNode = detectLoopIn(node_1);

        if (entranceNode != null) {
            System.out.println("环的入口节点的值为： " + entranceNode.val);
        }
    }

    private static ListNode constructALinkedListContainLoop() {
        ListNode node_1 = new ListNode(1);
        ListNode node_2 = new ListNode(2);
        node_1.next = node_2;
        ListNode node_3 = new ListNode(3);
        node_2.next = node_3;
        ListNode node_4 = new ListNode(4);
        node_3.next = node_4;
        ListNode node_5 = new ListNode(5); // 能够快速生成一列递增的数据吗？
        node_4.next = node_5;

        node_5.next = node_2; // 把结点5指向结点2，从而为链表中引入环

        return node_1;
    }

    private static ListNode detectLoopIn(ListNode headNode) {
        if (headNode == null || headNode.next == null) return null;

        ListNode slowCursor = headNode;
        ListNode fastCursor = headNode;

        while (fastCursor != null && fastCursor.next != null) {
            slowCursor = slowCursor.next;
            fastCursor = fastCursor.next.next;

            if (slowCursor == fastCursor) { // 在快慢指针相遇之后，由推导出的结果a=c可知：
                // #1 把某个指针重置到 链表的头结点
                slowCursor = headNode;

                // #2 让两个指针以相同的速度前进，直到相遇。则：从头出发的指针 在相遇时所在的位置，就是环的入口节点
                while (slowCursor != fastCursor) {
                    slowCursor = slowCursor.next;
                    fastCursor = fastCursor.next;
                }

                return slowCursor; // 这就是环的入口节点
            }
        }

        // 如果循环结束后，方法并没有退出，说明链表中不存在有环，则：返回null 表示不存在这样的入口节点
        return null;
    }
}

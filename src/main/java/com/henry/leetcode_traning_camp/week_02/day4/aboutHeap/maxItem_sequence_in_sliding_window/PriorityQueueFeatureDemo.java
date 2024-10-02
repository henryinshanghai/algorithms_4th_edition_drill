package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap.maxItem_sequence_in_sliding_window;

import java.util.PriorityQueue;

public class PriorityQueueFeatureDemo {
    public static void main(String[] args) {
        // 默认情况下，优先队列 是一个”小顶堆“
        PriorityQueue<Integer> priorityQueue = new PriorityQueue();

        // #1 向优先队列中添加元素
        priorityQueue.add(10);
        priorityQueue.add(3);
        priorityQueue.add(5);
        priorityQueue.add(9);
        priorityQueue.add(2);
        priorityQueue.add(0);
        priorityQueue.add(7);

        // #2 查看优先队列中的最小元素
        Integer peekItem = priorityQueue.peek();
        System.out.println("peeked item is: " + peekItem); // 0

        // 打印优先队列中的所以元素 - 队列中的元素 顺序构成了一个小顶堆
        print(priorityQueue); // 0->3->2->10->9->5->7->

        // #3 移除优先队列中的指定元素
        priorityQueue.remove(10);
        print(priorityQueue);
        System.out.println("peeked item after removing: " + priorityQueue.peek());

        priorityQueue.remove(0);
        print(priorityQueue);
        System.out.println("peeked item after another remove: " + priorityQueue.peek());
    }

    private static void print(PriorityQueue<Integer> priorityQueue) {
        for (Integer currentItem : priorityQueue) {
            System.out.print(currentItem + "->");
        }

        System.out.println();
    }
}

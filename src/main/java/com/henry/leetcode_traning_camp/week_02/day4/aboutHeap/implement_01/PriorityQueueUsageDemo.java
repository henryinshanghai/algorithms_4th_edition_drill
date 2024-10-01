package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap.implement_01;

import java.util.PriorityQueue;

// 🐖 优先队列(逻辑-偏用户层)数据结构
public class PriorityQueueUsageDemo {
    public static void main(String[] args) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();

        priorityQueue.add(1);
        priorityQueue.add(5);
        priorityQueue.add(3);
        priorityQueue.add(8);
        priorityQueue.add(2);
//        priorityQueue.add(0);

        Integer removedItem = priorityQueue.remove();
        System.out.println("removed item: " + removedItem); // removed的始终是最小的item
    }
}

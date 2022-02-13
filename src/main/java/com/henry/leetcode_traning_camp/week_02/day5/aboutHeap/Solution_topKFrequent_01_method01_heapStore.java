package com.henry.leetcode_traning_camp.week_02.day5.aboutHeap;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Solution_topKFrequent_01_method01_heapStore {
    public static void main(String[] args) {
        int[] arr = {1,1,1,2,2,3};
        int k = 2;

        int[] res = topKFrequent(arr, k);

        for (int item : res) {
            System.out.print(item + " -> ");
        }
    }

    private static int[] topKFrequent(int[] arr, int k) {
        if(arr.length == 0 || k == 0) return new int[0];

        Map<Integer, Integer> counts =  new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            counts.put(arr[i], counts.getOrDefault(arr[i], 0) + 1);
        } // 元素 -> 统计次数

        PriorityQueue<Integer> pq = new PriorityQueue<>(((o1, o2) -> counts.get(o1) - counts.get(o2)));

        // 这里需要处理的是元素本身 而不是统计次数的数值
        for (int num : counts.keySet()) { // 键集合 aka 元素的集合
            pq.add(num);

            if (pq.size() > k) pq.poll(); // 就当作一个队列来使用
        }

        // 把pq中的元素添加到一个数组中
        int[] topK = new int[k];
        int index = 0;
        for (int num : pq) {
            topK[index++] = num;
        }

        return topK;
    }
}

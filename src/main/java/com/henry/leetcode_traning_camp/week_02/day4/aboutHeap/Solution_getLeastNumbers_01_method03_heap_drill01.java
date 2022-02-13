package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap;

import java.util.PriorityQueue;
import java.util.Queue;

public class Solution_getLeastNumbers_01_method03_heap_drill01 {
    public static void main(String[] args) {
        int[] originalArr = {3,2,1};
//        int[] originalArr = {0,1,2,1};

        int k = 2;

        int[] leastKNumbers = getLeastNumbers(originalArr, k);

        for (int i = 0; i < leastKNumbers.length; i++) {
            System.out.print(leastKNumbers[i] + ", ");
        }

    }

    private static int[] getLeastNumbers(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k == 0) return new int[0];

        Queue<Integer> pq = new PriorityQueue<>((v1, v2) -> v2 - v1);

        for (Integer num : arr) {
            if (pq.size() < k) {
                pq.offer(num);
            } else if (num < pq.peek()) {
                pq.poll();
                pq.offer(num);
            }
        }

        int[] res = new int[pq.size()];
//        for (int i = 0; i < res.length; i++) {
//            res[i] = pq.poll();
//        } // 这种做法会把优先队列中的元素清空 不好
        int index = 0;
        for (int val : pq) { // expr:由于pq是一个线性结构，因此可以直接使用for-each进行遍历
            res[index++] = val;
        }

        return res;
    }
}

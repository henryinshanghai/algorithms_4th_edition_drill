package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap;

import java.util.PriorityQueue;

public class Solution_maxSlidingWindow_02_method_01_heapStore {
    public static void main(String[] args) {
        int[] nums = {1,3,-1,-3,5,3,6,7}; //  和 k = 3
        int k = 3;

        int[] max_val_in_window = maxSlidingWindow(nums, k);

        for (int val : max_val_in_window) {
            System.out.print(val + "->");
        }
    }

    private static int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length == 0 || k == 0) return new int[0];

        int n = nums.length;
        int[] max_val_in_windows = new int[n - k + 1];

        // 回顾一下lambda表达式的知识
        PriorityQueue<Integer> pq = new PriorityQueue<>((o1, o2) -> (o2 - o1));

        for (int i = 0; i < n; i++) {
            // 1 判断是否需要从堆中删除旧的窗口元素
            int start = i - k; // i == k时，就已经滑动到(k+1)个元素了。这时候需要把旧元素删除
            if (start >= 0) {
                // 优先队列提供了API来：从队列中删除指定的元素
                pq.remove(nums[start]);
            }

            // 2
            pq.offer(nums[i]);

            // 3
            if (pq.size() == k) {
                max_val_in_windows[i - k + 1] = pq.peek(); // 这里之所以会使用PQ这种数据类型，是因为使用它能够O(1)时间获取到一组数中的最大值
            }

        }

        return max_val_in_windows;
    }
}

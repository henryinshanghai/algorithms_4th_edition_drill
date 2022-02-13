package com.henry.leetcode_traning_camp.week_01.day4;

import java.util.LinkedList;

public class Solution_sliding_windows_biggest_item_01 {
    public static void main(String[] args) {
        int[] arr = {1,3,-1,-3,5,3,6,7};
        int k = 3;

        int[] maxCollection = maxSlideWindow(arr, k);

        System.out.println("滑动窗口中的最大值依次为： " );
        for (int i = 0; i < maxCollection.length; i++) {
            System.out.print(maxCollection[i] + " -> ");
        }
    }

    // 找到所有滑动窗口子数组中的最大值，并添加到一个数组中返回；
    private static int[] maxSlideWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0) return nums;

        // 准备一些需要的变量
        int n = nums.length;
        int[] maxCollection = new int[n - k + 1];

        LinkedList<Integer> index_deque = new LinkedList<>();

        // 准备一个循环：在循环中，创建滑动窗口子数组，并找到最大值添加到maxCollection中
        for (int i = 0; i < n; i++) {
            /* 把当前元素添加到滑动窗口子数组中 */
            // 1 当前元素的索引 与 队列中队首元素值的大小关系
            if (!index_deque.isEmpty() && (i - k + 1) > index_deque.peek()) {
                index_deque.poll();
            }

            // 2 添加元素之前，先把队列中所有小于被添加元素的其他元素都移除掉     作用：保证单调队列
            while (!index_deque.isEmpty() && nums[i] > nums[index_deque.peekLast()]) {
                index_deque.pollLast();
            }

            // 3
            index_deque.offer(i);

            // 把滑动窗口子数组中的最大值添加到结果数组中
            if (i >= (k - 1)) {
                maxCollection[i - k + 1] = nums[index_deque.peek()];
            }
        }

        return maxCollection;
    } // use deque to keep the index rather than the item itself
}

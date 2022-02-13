package com.henry.leetcode_traning_camp.week_01.day4;

import java.util.LinkedList;

public class Solution_sliding_windows_biggest_item_01_drill01 {
    public static void main(String[] args) {
        int[] arr = {1,3,-1,-3,5,3,6,7};
        int k = 3;

        int[] maxCollection = maxSlideWindow(arr, k);

        System.out.println("滑动窗口中的最大值依次为： " );
        for (int i = 0; i < maxCollection.length; i++) {
            System.out.print(maxCollection[i] + " -> ");
        }
    }

    private static int[] maxSlideWindow(int[] nums, int k) {
        if(nums == null || nums.length == 0) return nums;

        // 准备一些变量
        int n = nums.length;

        // 准备一个新数组，用于存储滑动窗口中的最大值
        int[] maxCollection = new int[n - k + 1];

        // 准备一个双端队列 用于实现单调队列    手段：new LinkedLis()
        LinkedList<Integer> index_deque = new LinkedList<>();

        // 准备一个循环：在循环中维护滑动窗口子数组，并把滑动窗口子数组中的最大值添加到maxCollection中
        for (int i = 0; i < n; i++) {
            /* 把当前索引添加到滑动窗口子数组中 */
            // 1 判断滑动窗口中的队首元素是否已经过时
            // 手段：滑动窗口的左边界的索引 是不是比起 队列（用于存储索引）的首元素更大
            if (!index_deque.isEmpty() && (i - k + 1) > index_deque.peekFirst()) {
                index_deque.pollFirst();
            }

            // 2 判断预期添加到队列中的索引值 与 当前队列尾部元素的大小，以维持单调栈的单调递减特性
            while (!index_deque.isEmpty() && nums[i] >= nums[index_deque.peekLast()]) { // 这里是 >=
                index_deque.pollLast();
            }

            // 3 把当前索引添加到单调栈中
            index_deque.offer(i);

            // 准备一个if语句 如果滑动窗口子数组已经形成，就把子数组中的最大值添加到maxCollection中
            if ((i - k + 1) >= 0) { // EXPR:注意这里的符号，它会导致程序输出结果不正确（因为错误的边界）
                maxCollection[i - k + 1] = nums[index_deque.peek()];
            }

        }

        return maxCollection;
    }
}

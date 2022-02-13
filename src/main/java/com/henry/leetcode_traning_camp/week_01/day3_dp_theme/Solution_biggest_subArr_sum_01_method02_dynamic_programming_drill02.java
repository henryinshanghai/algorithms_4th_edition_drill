package com.henry.leetcode_traning_camp.week_01.day3_dp_theme;

public class Solution_biggest_subArr_sum_01_method02_dynamic_programming_drill02 {
    public static void main(String[] args) {
        // 从这个数组中找到一个满足特定条件的子数组
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

        int subArr_max_sum = maxSubArr(arr);

        System.out.println("原始数组的子数组集合中，元素加和值最大的子数组的元素加和结果为： " + subArr_max_sum);
    }

    // 动态规划解法
    private static int maxSubArr(int[] nums) {
        if(nums == null || nums.length == 0) return 0;

        // 1 以nums[i]元素作为结尾元素的子数组（这是一个子数组集合）中，元素加和值最大的子数组的元素加和值为dp[i]
        int[] dp = new int[nums.length];
        // base case
        dp[0] = nums[0];
        // for loop + equation
        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
        }

        // 2 get the biggest sum in dp[]
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < dp.length; i++) {
            max = Math.max(max, dp[i]);
        }

        return max;
    }
}

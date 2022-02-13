package com.henry.leetcode_traning_camp.week_01.day3_dp_theme;

public class Solution_biggest_subArr_sum_01_method02_dynamic_programming_drill01 {
    public static void main(String[] args) {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

        int biggest_sub_arr_sum = maxSubArr(arr);

        System.out.println("原始数组中所能找到的 <元素值加和最大的子数组的加和值为>: " + biggest_sub_arr_sum);
    }

    private static int maxSubArr(int[] nums) {
        // 手段：动态规划
        // 核心：状态转移方程

        // 1 准备一个dp[]数组     用于存储状态转移方程中各个项的值
        // dp[i]表示：nums[]数组中的<元素加和值最大的子数组>的元素加和值
        int[] dp = new int[nums.length];

        // 1-1 状态转移方程的base case 最基本的子数组-只有一个元素
        dp[0] = nums[0]; // 第一个元素前面没有子数组

        // 1-2 使用状态转移方程 + for loop 来求出dp[]数组中每一项的值
        for (int i = 1; i < nums.length; i++) {
            dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
        }

        // 2 在计算出dp[]数组所有元素的值之后，求出dp[]数组中的最大元素      aka     nums[]数组中的<元素加和值最大的子数组>的元素加和值
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < dp.length; i++) {
            max = Math.max(max, dp[i]);
        }

        return max;

    }
}

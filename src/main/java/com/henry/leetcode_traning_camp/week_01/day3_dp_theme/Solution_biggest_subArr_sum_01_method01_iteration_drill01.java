package com.henry.leetcode_traning_camp.week_01.day3_dp_theme;

public class Solution_biggest_subArr_sum_01_method01_iteration_drill01 {
    public static void main(String[] args) {
        int[] arr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

        int biggest_sub_arr_sum = maxSubArr(arr);

        System.out.println("原始数组中所能找到的 <元素值加和最大的子数组的加和值为>: " + biggest_sub_arr_sum);
    }

    private static int maxSubArr(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        // 1 准备int变量    记录当前子数组的元素加和值
        int curr_subArr_sum = nums[0];

        // 2 准备int变量    记录子数组元素加和值的最大值
        int max_subArr_sum = curr_subArr_sum;

        // 3 准备一个循环
        for (int i = 1; i < nums.length; i++) {
            // 大于0，就接着加加看 小于0，直接重置
            curr_subArr_sum = (curr_subArr_sum > 0) ? curr_subArr_sum + nums[i] : nums[i];

            max_subArr_sum = Math.max(curr_subArr_sum, max_subArr_sum);
        }

        return max_subArr_sum;
    }
}

package com.henry.leetcode_traning_camp.week_06.day02;

import java.util.Arrays;

public class Solution_maximumSubArray_02_method02_dpWithSpaceOptimal_by_huashouDapeng_drill01 {
    public static void main(String[] args) {
        int[] nums = {-2,1,-3,4,-1,2,1,-5,4};

        int maxRes = maxSubArray(nums);

        System.out.println("在当前数组：" + Arrays.toString(nums) + "中加和值最大的连续子数组的sum为：" + maxRes);
    }

    private static int maxSubArray(int[] nums) {
        // 准备指针变量
        int currResult = 0;
        int max = nums[0];

        // 遍历每个变量，计算currResult 并更新 max
        for (int i = 0; i < nums.length; i++) {
            if (currResult > 0) {
                currResult += nums[i];
            } else {
                currResult = nums[i];
            }

            max = Math.max(currResult, max);
        }

        return max;
    }
}

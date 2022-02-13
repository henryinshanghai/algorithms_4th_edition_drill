package com.henry.leetcode_traning_camp.week_06.day02;

import java.util.Arrays;

public class Solution_maximumSubArray_02_method02_dpWithSpaceOptimal_by_huashouDapeng {
    public static void main(String[] args) {
        int[] nums = {-2,1,-3,4,-1,2,1,-5,4};

        int maxRes = maxSubArray(nums);

        System.out.println("在当前数组：" + Arrays.toString(nums) + "中加和值最大的连续子数组的sum为：" + maxRes);
    }

    private static int maxSubArray(int[] nums) {
        // 准备用于更新的变量
        int currResult = 0; // 用于存储当前子序列的加和值
        int max = nums[0]; // 用于存储最大子序列的加和值

        for (int i = 0; i < nums.length; i++) {
            if (currResult > 0) {
                currResult += nums[i];
            } else {
                currResult = nums[i];
            }

            max = Math.max(max, currResult);
        }

        return max;

    }
}
/*
Bingo！

如果递推公式中，只有dpTable[]相邻两项的关系。
那么就可以使用一个变量来代替dpTable[]
 */

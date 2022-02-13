package com.henry.leetcode_traning_camp.week_06.preview;

import java.util.Arrays;

public class Solution_maxSubArray_03_method02_dp_spaceOptimal {
    public static void main(String[] args) {
        int[] nums = {-2,1,-3,4,-1,2,1,-5,4};

        int maxRes = maxSubArray(nums);

        System.out.println("在当前数组：" + Arrays.toString(nums) + "中加和值最大的连续子数组的sum为：" + maxRes);
    }

    private static int maxSubArray(int[] nums) {
        /*
        这里的优化是：不需要准备一个一维的dpTable[]
        因为我们能够在生成dpTable[]元素的过程中，顺便就直接比较得到最大值max
        */
        // 准备一个int类型的变量并初始化 用于存储最终得到的子数组的sum最大值
        int maxSubArrSum = nums[0];
        // 准备一个int类型的变量并初始化 用于存储以当前元素作为结束的所有子数组集合中，加和值最大的那个子数组的sum
        int sum = 0;

        // 准备一个循环 在循环中遍历每一个原始数组的元素...
        for(int num: nums) {
            /* 在每次循环中，遍历当前数组元素 并用它来更新 子数组sum的最大值变量*/
            if(sum > 0) {
                sum += num; // sum>0 则：把之前扩展的子数组的和加进来
            } else {
                sum = num; // sum<0,则：把先前扩展的子数组直接砍掉，自立门户
            }
            maxSubArrSum = Math.max(maxSubArrSum, sum);
        }

        // 循环结束后，返回计算得到的最大值max
        return maxSubArrSum;
    }
}

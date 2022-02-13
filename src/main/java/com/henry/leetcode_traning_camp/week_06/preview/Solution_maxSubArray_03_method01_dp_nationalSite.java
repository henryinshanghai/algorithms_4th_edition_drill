package com.henry.leetcode_traning_camp.week_06.preview;

import java.util.Arrays;

public class Solution_maxSubArray_03_method01_dp_nationalSite {
    public static void main(String[] args) {
        int[] nums = {-2,1,-3,4,-1,2,1,-5,4};

        int maxRes = maxSubArray(nums);

        System.out.println("在当前数组：" + Arrays.toString(nums) + "中加和值最大的连续子数组的sum为：" + maxRes); // 6
    }

    private static int maxSubArray(int[] A) {
        /* 〇 准备一个dpTable */
        int n = A.length;
        int[] dp = new int[n];//dp[i] means the maximum subarray ending with A[i];

        /* Ⅰ 初始化dpTable[]中的一些数据，以便进行递推 */
        dp[0] = A[0];
        int max = dp[0];

        /* Ⅱ 遍历原始数组arr中的每一个元素
            按照给定的规则，使用递推的方式来计算dpTable[]中剩余元素的值 */
        for(int i = 1; i < n; i++){
            // 1 计算得到dpTable[]中的当前元素
            // 要么是 当前的数组元素 A[i]————砍掉了先前扩展出的数组
            // 要么是 把当前的数组元素 A[i] 扩展到 当前的子数组中
            dp[i] = A[i] + (dp[i - 1] > 0 ? dp[i - 1] : 0); // 所以A[i]肯定是有的，主要看 先前扩展出来的子数组还要不要了

            // 2 使用计算得到的dpTable[i]来时时更新max变量的值
            max = Math.max(max, dp[i]);
        }

        /* Ⅲ 循环结束后，返回max的值 */
        return max;
    }
}

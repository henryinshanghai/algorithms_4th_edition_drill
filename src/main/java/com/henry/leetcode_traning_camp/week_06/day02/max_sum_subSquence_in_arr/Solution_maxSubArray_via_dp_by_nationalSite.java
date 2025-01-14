package com.henry.leetcode_traning_camp.week_06.day02.max_sum_subSquence_in_arr;

import java.util.Arrays;

// 验证：对于 原始数组中存在的子数组的最大加和结果 的问题，可以使用 动态规划的手段 来 得到答案
// 最优子结构：原始数组的 “最大子数组元素加和结果” 包含有 “原始数组刨去当前元素后的子数组”的“最大子数组加和结果”
// dp[i]数组与下标的具体含义：当前位置 -> 以当前位置作为结束位置的所有子数组中的 最大加和值 => currentSpotToMaxSumOfSubArrEndAtIt
// 原理：如果 当前位置子数组的maxSum大于0，那么 就可以使用它来继续扩展子数组。否则，可以重开子数组
public class Solution_maxSubArray_via_dp_by_nationalSite {
    public static void main(String[] args) {
        int[] numArr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int maxSumOfSubArrs = getSumOfMaxSubArrayIn(numArr);
        System.out.println("在当前数组：" + Arrays.toString(numArr) + "中加和值最大的连续子数组的sum为：" + maxSumOfSubArrs);
    }

    private static int getSumOfMaxSubArrayIn(int[] numArr) {
        /* 〇 准备一个dpTable */
        int[] currentSpotToMaxSumOfSubArrEndAtIt = new int[numArr.length];

        /* Ⅰ 初始化dpTable[]中的一些数据，以便进行递推 */
        currentSpotToMaxSumOfSubArrEndAtIt[0] = numArr[0];
        int max_sum_of_sub_arr = numArr[0];

        /* Ⅱ 遍历原始数组arr中的每一个元素
            按照给定的规则，使用递推的方式来计算dpTable[]中剩余元素的值 */
        System.out.print("原始数组为：");
        printArr(numArr);
        for (int current_spot = 1; current_spot < numArr.length; current_spot++) {
//            currentSpotToMaxSumOfSubArrEndAtIt[current_spot] = currentSpotToMaxSumOfSubArrEndAtIt[current_spot - 1] > 0 ? currentSpotToMaxSumOfSubArrEndAtIt[current_spot - 1] + nums[current_spot] : nums[current_spot];
            int current_num = numArr[current_spot];
            currentSpotToMaxSumOfSubArrEndAtIt[current_spot]
                    = current_num // 当前位置上的元素
                    + (Math.max(currentSpotToMaxSumOfSubArrEndAtIt[current_spot - 1], 0)); // 前一个位置的 以其结尾的最大子数组的和 或者 0（如果和为负数的话）

            printArr(currentSpotToMaxSumOfSubArrEndAtIt);

            // 使用 当前位置的 以其结尾的最大子数组的和 与 当前最大子数组的和 比较 来 更新 最大子数组的和
            max_sum_of_sub_arr = Math.max(max_sum_of_sub_arr, currentSpotToMaxSumOfSubArrEndAtIt[current_spot]);
        }

        /* Ⅲ 循环结束后，返回max的值 */
        return max_sum_of_sub_arr;
    }

    private static void printArr(int[] numArr) {
        for (int i = 0; i < numArr.length; i++) {
            System.out.print(numArr[i] + " ");
        }
        System.out.println();
    }
}
/*
特征：在dpTable[]中递推式，其实值使用到了相邻两项的元素
这种情况可以使用两个变量/指针来代替数组————空间优化

事实上，由于dpTable[i]只依赖于dpTable[i-1]。所以我们可以只是用一个变量
通过不断更新变量的值来记录需要的结果（而不用存储每一个中间结果）
 */

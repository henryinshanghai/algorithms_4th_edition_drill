package com.henry.leetcode_traning_camp.week_06.day02.max_sum_subSquence_in_arr;

import java.util.Arrays;

// 验证：对于动态规划的应用，如果在递推公式中，只存在有 dpTable[]相邻两项之间的关系。那么就可以 使用一个单一的变量 来 代替dpTable[]
// 从而节省空间
public class Solution_maxSubArray_via_dp_with_optimal_by_huashouDapeng {
    public static void main(String[] args) {
        int[] numArr = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        printArr(numArr);

        int maxRes = getMaxSumOfSubArrayIn(numArr);
        System.out.println("在当前数组：" + Arrays.toString(numArr) + "中加和值最大的连续子数组的sum为：" + maxRes);
    }

    private static void printArr(int[] numArr) {
        for (int i = 0; i < numArr.length; i++) {
            System.out.print(numArr[i] + " ");
        }
        System.out.println();
    }

    private static int getMaxSumOfSubArrayIn(int[] numArr) {
        // 准备用于更新的变量
        int currentSubArrSum = 0; // 用于存储当前子序列的加和值
        int maxSumOfSubArr = numArr[0]; // 用于存储最大子序列的加和值

        for (int current_spot = 0; current_spot < numArr.length; current_spot++) {
            /* 🐖 每次面对一个新的数组数字时，都会面临一个选择：#1 把这个数字计入当前子数组中; OR #2 从这个数字重新开始 作为新的子数组 */
            // 如果当前子数组的加和结果大于0，说明 可以在此基础上继续 添加当前数字进入子数组（因为加和结果 相比于 由当前数字本身组成的子数组 更大）
            if (currentSubArrSum > 0) {
                // 则：把 当前数组元素 累计到 当前子数组中
                currentSubArrSum += numArr[current_spot];
            } else { // 如果 当前子数组的加和结果 小于或等于0，说明 应该以当前数字重开子数组，
                // 则：把 当前数组元素 作为新的子数组 来 “重开”子数组
                currentSubArrSum = numArr[current_spot];
            }

            // 尝试 使用 当前子数组的加和值 来 更新“最大的加和值”
            maxSumOfSubArr = Math.max(maxSumOfSubArr, currentSubArrSum);
            System.out.println("到当前位置" + current_spot + "为止，所有子数组中，最大的加和值为：" + maxSumOfSubArr);
        }

        return maxSumOfSubArr;

    }
}
/*
Bingo！

如果递推公式中，只 存在有 dpTable[]相邻两项的关系。
那么就可以使用 单一的一个变量 来 代替dpTable[]
 */

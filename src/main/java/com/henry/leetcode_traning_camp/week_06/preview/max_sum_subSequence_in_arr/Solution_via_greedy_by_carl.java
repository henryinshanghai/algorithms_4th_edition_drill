package com.henry.leetcode_traning_camp.week_06.preview.max_sum_subSequence_in_arr;

import java.util.Arrays;

// 贪心策略：尽量把“当前元素” 包含进 “当前子序列”中，除非“当前子序列”的sum为负数，则 以当前元素为开始 重算“当前子序列”
public class Solution_via_greedy_by_carl {
    public static void main(String[] args) {
        int[] integerSequence = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int subArrayMaxSum = getSubArrayMaxSumIn(integerSequence);
        System.out.println("在当前数组：" + Arrays.toString(integerSequence) + "中加和值最大的连续子数组的sum为：" + subArrayMaxSum); // 6
    }

    private static int getSubArrayMaxSumIn(int[] integerArr) {
        if (integerArr.length == 1) {
            return integerArr[0];
        }

        int subArrMaxSum = Integer.MIN_VALUE;
        int subArrCurrentSum = 0;

        for (int currentCursor = 0; currentCursor < integerArr.length; currentCursor++) {
            int currentInteger = integerArr[currentCursor];
            // 把当前整数 累加到 “当前子数组的sum”中
            subArrCurrentSum += currentInteger;

            /* 累加后 */
            // #1 判断包含当前元素后，连续子数组的sum是不是更大了 如果是，取较大者，如果不是，则 保持原值
            subArrMaxSum = Math.max(subArrMaxSum, subArrCurrentSum); // 取区间累计的最大值（相当于不断确定最大子序终止位置）
            // #2 如果包含当前元素后，连续子数组的sum小于等于0，说明 subArrCurrentSum 不会帮助“下一个整数元素”更大，则：
            if (subArrCurrentSum <= 0) {
                // 重新开始记录 “连续子数组”   手段：把subArrCurrentSum置为0，重开
                subArrCurrentSum = 0;
            }
        }
        return subArrMaxSum;
    }
}

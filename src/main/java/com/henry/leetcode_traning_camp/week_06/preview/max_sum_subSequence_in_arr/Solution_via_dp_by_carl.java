package com.henry.leetcode_traning_camp.week_06.preview.max_sum_subSequence_in_arr;

import java.util.Arrays;

// dp[i]数组的含义：当前位置i => 以当前位置i作为结束位置的所有子数组中，加和值最大的子数组的sum
// 最优子结构/子问题：dp[i] 与 dp[i-1]有什么关系呢？
// 如果dp[i-1]>0（说明会有助于sum）,则：dp[i] = dp[i-1] + current_item;
// 如果dp[i-1]<=0(说明不会有助于sum)，则：dp[i] = current_item; 取两者中的较大者 作为dp[i]的元素值
public class Solution_via_dp_by_carl {
    public static void main(String[] args) {
        int[] integerSequence = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int subArrayMaxSum = getSubArrayMaxSumIn(integerSequence);
        System.out.println("在当前数组：" + Arrays.toString(integerSequence) + "中加和值最大的连续子数组的sum为：" + subArrayMaxSum); // 6
    }

    private static int getSubArrayMaxSumIn(int[] integerSequence) {
        if (integerSequence.length == 0) {
            return 0;
        }

        // currentSpot => 以currentSpot作为结束位置的 加和值最大的子数组的sum
        int[] currentSpotToMaxSunArrEndWithIt = new int[integerSequence.length];
        currentSpotToMaxSunArrEndWithIt[0] = integerSequence[0];

        int subArrMaxSum = integerSequence[0];
        for (int currentSpot = 1; currentSpot < integerSequence.length; currentSpot++) { // 从左往右地计算dp[]元素
            // 计算 当前dp[]元素的值
            int currentInteger = integerSequence[currentSpot];

            // 递推公式：dp[i] = max(dp[i-1] + current_item, current_item);
            currentSpotToMaxSunArrEndWithIt[currentSpot]
                    = Math.max(
                        currentSpotToMaxSunArrEndWithIt[currentSpot - 1] + currentInteger, // option1: 在“当前连续子数组”的基础上，累加 当前元素
                        currentInteger); // option2：使用当前元素，重新开始“子数组”

            // 🐖 subArrMaxSum 不一定出现在最后的位置（dp[length-1]），所以需要通过不断地比较 来 得到最大值
            subArrMaxSum = Math.max(subArrMaxSum, currentSpotToMaxSunArrEndWithIt[currentSpot]);
        }

        // 返回dp[]数组的最大值
        return subArrMaxSum;
    }
}

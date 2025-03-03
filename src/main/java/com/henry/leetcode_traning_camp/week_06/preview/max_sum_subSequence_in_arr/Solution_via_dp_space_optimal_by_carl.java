package com.henry.leetcode_traning_camp.week_06.preview.max_sum_subSequence_in_arr;

import java.util.Arrays;

// 递推公式：dp[i] = max(dp[i-1] + current_item, current_item);
// 因为递推公式中，对于dp[]数组中的元素，dp[i] 就只与dp[i-1]有关，
// 因此 可以通过 重复使用/更新单个变量的方式 来 把一维dp[]数组压缩掉，从而得到 两个动态变量。
// 这样优化后，算法的空间复杂度 由O(N) => O(1)
// 🐖 之所以可以使用单个变量，是因为 dp[]元素的其他值，我们其实并不需要。我们只是想要动态地计算出最大值
public class Solution_via_dp_space_optimal_by_carl {
    public static void main(String[] args) {
        int[] integerSequence = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        int maxRes = getMaxSumSubArrayOf(integerSequence);
        System.out.println("在当前数组：" + Arrays.toString(integerSequence) + "中加和值最大的连续子数组的sum为：" + maxRes); // 6
    }

    private static int getMaxSumSubArrayOf(int[] integerSequence) {
        int subArrMaxSum = integerSequence[0];
        int subArrMaxSumEndWithCurrentSpot = integerSequence[0];

        for(int currentSpot = 1; currentSpot < integerSequence.length; currentSpot++) {
            subArrMaxSumEndWithCurrentSpot =
                    Math.max(subArrMaxSumEndWithCurrentSpot + integerSequence[currentSpot], // option01: 把 当前元素 包含进 当前子数组
                            integerSequence[currentSpot]); // option02: 从 当前元素 重新开始子数组

            // 迭代出/反复比较出 所有位置上 最大的subArrMaxSumEndWithCurrentSpot
            subArrMaxSum = Math.max(subArrMaxSum, subArrMaxSumEndWithCurrentSpot);
        }

        // 返回 最大的subArrMaxSumEndWithCurrentSpot
        return subArrMaxSum;
    }

}

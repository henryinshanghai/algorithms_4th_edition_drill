package com.henry.leetcode_traning_camp.week_04.day05;

import java.util.HashMap;
import java.util.Map;

public class Solution_coinChange_01_method01_dp_tushor_bottomToUp_drill01 {
    public static void main(String[] args) {
        int total = 13; // 总共要凑出的钱数
        int coins[] = {7, 3, 2, 6}; // 可用面额的硬币

        Map<Integer, Integer> map = new HashMap<>();
        int bottomUpValue = minimumCoinBottomUp(total, coins);

//        System.out.print(String.format("top down result is that for %s money we need %s coins", total, topDownValue));
        System.out.print(String.format("bottom up result is that for %s money we need %s coins", total, bottomUpValue));
    }

    /**
     * 自底向上地解决凑硬币的问题
     * @param total
     * @param coins
     * @return
     */
    private static int minimumCoinBottomUp(int total, int[] coins) {
        /* 〇 准备两个数组   用于不断更新，以获取到凑出total金额所需要的硬币个数 */
        int[] T = new int[total + 1];
        int[] R = new int[total + 1];
        T[0] = 0; // EXPR: 数组中的第一个位置需要绑定初始值0

        /* Ⅰ 为T[]、R[]数组中的每一个元素(除了i为0的位置 这是一个无效位置)绑定初始值 */
        for (int i = 1; i < total+1; i++) {
            T[i] = Integer.MAX_VALUE - 1;
            R[i] = -1;
        }

        /* Ⅱ 逐一遍历每一种面额的硬币   在循环体中，使用当前面额的硬币来更新T[] 与 R[] */
        for (int i = 0; i < coins.length; i++) {
            for (int j = 0; j < total + 1; j++) {
                if (coins[i] <= j) {
                    if (T[j] > T[j - coins[i]] + 1) {
                        T[j] = T[j - coins[i]] + 1;
                        R[j] = i;
                    }
                }
            }
        }

        /* Ⅳ 返回T[]数组的最后一个元素 */
        return T[total];
    }
}

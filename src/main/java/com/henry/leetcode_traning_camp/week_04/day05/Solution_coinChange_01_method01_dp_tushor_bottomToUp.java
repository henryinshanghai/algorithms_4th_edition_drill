package com.henry.leetcode_traning_camp.week_04.day05;

import java.util.HashMap;
import java.util.Map;

public class Solution_coinChange_01_method01_dp_tushor_bottomToUp {
    public static void main(String[] args) {
        int total = 13; // 总共要凑出的钱数
        int coins[] = {7, 3, 2, 6}; // 可用面额的硬币

        Map<Integer, Integer> map = new HashMap<>();
        int bottomUpValue = minimumCoinBottomUp(total, coins);

//        System.out.print(String.format("top down result is that for %s money we need %s coins", total, topDownValue));
        System.out.print(String.format("bottom up result is that for %s money we need %s coins", total, bottomUpValue));

    }

    private static int minimumCoinBottomUp(int total, int[] coins) {
        /* 〇 准备两个数组   用于不断更新，以获取到凑出total金额所需要的硬币个数 */
        int[] T = new int[total + 1]; // T[i]表示凑出i的钱数最少所需要的硬币的个数
        int[] R = new int[total + 1]; // R[i]表示为了凑出当前金额，使用了第n-th个硬币
        T[0] = 0;

        /* Ⅰ 为T[]、R[]数组中的每一个元素(除了i为0的位置 这是一个无效位置)绑定初始值 */
        for (int i = 1; i <= total; i++) {
            T[i] = Integer.MAX_VALUE - 1;
            R[i] = -1;
        }

        /* Ⅱ 逐一遍历每一种面额的硬币   在循环体中，使用当前面额的硬币来更新T[] 与 R[] */
        for (int j = 0; j < coins.length; j++) {
            // 使用当前面额的硬币来更新T[]与R[]
            for (int i = 0; i <= total; i++) {
                if (coins[j] <= i) { // EXPR：这里可以取等于号 说明使用与i价值相同的硬币来直接凑i aka 只需要一个硬币
                    // Options：是否选择当前面额的硬币？
                    // 如果选择，则：所需要的硬币数量 = T[i - coins[j]] + 1
                    // 如果不选择，则：T[i]已经有了旧的数值
                    if (T[i] > T[i - coins[j]] + 1) { // 如果选择当前硬币后，所需要的硬币数量更少。则:
                        // 更新：凑出面额i所需要的最少硬币个数T[i]
                        T[i] = T[i - coins[j]] + 1;
                        // 更新：当前使用的是硬币序列中的第n-th个硬币
                        R[i] = j;
                    }
                }
            }
        }

        /* Ⅲ（Optional）打印 为了凑出金额i使用了哪些个硬币 */
        /* Ⅳ 返回T[]数组的最后一个元素 */
        return T[total];
    }

}

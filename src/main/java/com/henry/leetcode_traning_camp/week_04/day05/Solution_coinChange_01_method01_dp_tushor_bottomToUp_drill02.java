package com.henry.leetcode_traning_camp.week_04.day05;

public class Solution_coinChange_01_method01_dp_tushor_bottomToUp_drill02 {
    public static void main(String[] args) {
        int total = 13;
        int[] coins = {7, 3, 2, 6};

        int bottomUpValue = minimumCoinBottomUp(total, coins);
        System.out.println(String.format("bottom up result is that for %s money we need %s coins", total, bottomUpValue));
    }

    private static int minimumCoinBottomUp(int total, int[] coins) {
        int[] T = new int[total + 1]; // T[i]表示凑出面额i所需要的最少硬币数量
        int[] R = new int[total + 1]; // R[i]表示凑出面额i所使用的第几个硬币
        T[0] = 0;

        for (int i = 1; i < total + 1; i++) { // EXPR: 这里是从1开始而不是0
            T[i] = Integer.MAX_VALUE - 1;
            R[i] = -1;
        }


        for (int i = 0; i < coins.length; i++) {
            for (int j = 0; j < total + 1; j++) { // 计算出数组中的每一个元素
                if (coins[i] <= j) {
                    if (T[j] > T[j - coins[i]] + 1) {
                        T[j] = T[j - coins[i]] + 1; // 更新凑出面额j所需要的最小硬币数量
                        R[j] = i; // 更新当前使用的n-th硬币
                    }
                }
            }
        }

        // 返回题目所需要的信息
        return T[total];
    }
}

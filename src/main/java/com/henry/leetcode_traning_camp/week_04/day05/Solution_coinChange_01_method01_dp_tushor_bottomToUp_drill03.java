package com.henry.leetcode_traning_camp.week_04.day05;

public class Solution_coinChange_01_method01_dp_tushor_bottomToUp_drill03 {
    public static void main(String[] args) {
        int total = 13;
        int[] coins = {7, 3, 2, 6};

        int bottomUpValue = minimumCoinBottomUp(total, coins);
        System.out.println(String.format("bottom up result is that for %s money we need %s coins", total, bottomUpValue));
    }

    private static int minimumCoinBottomUp(int total, int[] coins) {
        // 〇
        int[] T = new int[total + 1];
        int[] R = new int[total + 1];
        T[0] = 0;
        
        // Ⅰ
        for (int i = 1; i < total + 1; i++) {
            T[i] = Integer.MAX_VALUE - 1;
            R[i] = -1;
        }
        
        // Ⅱ
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

        // Ⅲ
        return T[total];
    }
}

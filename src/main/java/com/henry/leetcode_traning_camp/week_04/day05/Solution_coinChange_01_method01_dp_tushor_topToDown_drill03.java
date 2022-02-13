package com.henry.leetcode_traning_camp.week_04.day05;

import java.util.HashMap;
import java.util.Map;

public class Solution_coinChange_01_method01_dp_tushor_topToDown_drill03 {
    public static void main(String[] args) {
        int total = 13;
        int[] coins = {7, 3, 2, 6};

        Map<Integer, Integer> map = new HashMap<>();
        int topDownValue = minimumCoinTopDown(total, coins, map);

        System.out.print(String.format("top down result is that for %s money we need %s coins", total, topDownValue));
    }

    private static int minimumCoinTopDown(int total, int[] coins, Map<Integer, Integer> map) {
        if (total == 0) {
            return 0;
        }

        if (map.containsKey(total)) {
            return map.get(total);
        }

        int minimumNumber = Integer.MAX_VALUE;
        for (int i = 0; i < coins.length; i++) {
            if (coins[i] > total) {
                continue;
            }

            // 使用当前硬币来凑出total面额的钱
            int demandNumber = minimumCoinTopDown(total - coins[i], coins, map);

            if (demandNumber < minimumNumber) {
                minimumNumber = demandNumber;
            }
        }

        minimumNumber = (minimumNumber == Integer.MAX_VALUE)
                ? minimumNumber : minimumNumber + 1;

        map.put(total, minimumNumber);

        return minimumNumber;
    }
}

package com.henry.leetcode_traning_camp.week_04.day05;

import java.util.HashMap;
import java.util.Map;

public class Solution_coinChange_01_method01_dp_tushor_topToDown_drill01 {
    public static void main(String[] args) {
        int total = 13; // 总共要凑出的钱数
        int coins[] = {7, 3, 2, 6}; // 可用面额的硬币

        // 准备一个哈希表/map对象    用于存储已经解决的子问题
        Map<Integer, Integer> map = new HashMap<>();
        int topDownValue = minimumCoinTopDown(total, coins, map);

        System.out.print(String.format("top down result is that for %s money we need %s coins", total, topDownValue));

    }

    private static int minimumCoinTopDown(int total, int[] coins, Map<Integer, Integer> map) {
        // 〇
        if (total == 0) return 0;

        // Ⅰ
        if (map.containsKey(total)) {
            return map.get(total);
        }

        // Ⅱ
        int minimumNumberOfCoins = Integer.MAX_VALUE;

        for (int i = 0; i < coins.length; i++) {
            // 比较 需要凑出的总金额total 与 当前硬币面额 的大小
            if (coins[i] > total) {
                continue;
            }
  
            // 否则，使用当前面额的硬币来凑出total
            int demandCoins = minimumCoinTopDown(total - coins[i], coins, map);

            if (demandCoins < minimumNumberOfCoins) {
                minimumNumberOfCoins = demandCoins;
            }
        }

        // Ⅲ 准备返回递归得到的结果
        minimumNumberOfCoins =
                (minimumNumberOfCoins == Integer.MAX_VALUE) ?
                minimumNumberOfCoins :
                minimumNumberOfCoins + 1;

        map.put(total, minimumNumberOfCoins);

        return minimumNumberOfCoins;
    }
}

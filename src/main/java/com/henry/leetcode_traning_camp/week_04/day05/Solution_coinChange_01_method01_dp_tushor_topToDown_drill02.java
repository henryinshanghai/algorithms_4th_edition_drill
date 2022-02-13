package com.henry.leetcode_traning_camp.week_04.day05;

import java.util.HashMap;
import java.util.Map;

public class Solution_coinChange_01_method01_dp_tushor_topToDown_drill02 {
    public static void main(String[] args) {
        int total = 13; // 总共要凑出的钱数
        int coins[] = {7, 3, 2, 6}; // 可用面额的硬币

        // 准备一个哈希表/map对象    用于存储已经解决的子问题
        Map<Integer, Integer> map = new HashMap<>();
        int topDownValue = minimumCoinTopDown(total, coins, map);

        System.out.print(String.format("top down result is that for %s money we need %s coins", total, topDownValue));
    }

    private static int minimumCoinTopDown(int total, int[] coins, Map<Integer, Integer> map) {
        /* 〇 鲁棒性代码：如果total为0，就不需要做任何的事情 返回0即可 */
        if (total == 0) {
            return 0;
        }

        /* Ⅰ 在对total进行计算之前，查看下之前有没有计算过它 */
        if (map.containsKey(total)) {
            return map.get(total);
        }

        /* Ⅱ 遍历所有面额的硬币，并默认剩下的钱已经被凑好了（递归调用时，传入更新后的参数）*/
        int minimumNumber = Integer.MAX_VALUE;
        for (int i = 0; i < coins.length; i++) {
            if (coins[i] > total) {
                continue;
            }

            // EXPR:这里需要使用一个新的变量来记录凑出 更小规模的面额需要几个硬币
            int demandCoins = minimumCoinTopDown(total - coins[i], coins, map);

            if (demandCoins < minimumNumber) { // EXPR2：当需要的硬币更少时，对minimumNumber变量进行更新
                minimumNumber = demandCoins;
            }
        }

        minimumNumber = (minimumNumber == Integer.MAX_VALUE) ?
                Integer.MAX_VALUE : minimumNumber + 1;

        map.put(total, minimumNumber);

        /* Ⅲ 返回计算出的“凑出total所需要的最少硬币个数min” */
        return minimumNumber;
    }
}

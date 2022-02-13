package com.henry.leetcode_traning_camp.week_04.day05;

import java.util.HashMap;
import java.util.Map;

public class Solution_coinChange_01_method01_dp_tushor_topToDown {
    public static void main(String[] args) {
        int total = 13; // 总共要凑出的钱数
        int coins[] = {7, 3, 2, 6}; // 可用面额的硬币

        Map<Integer, Integer> map = new HashMap<>();
        int topDownValue = minimumCoinTopDown(total, coins, map);

        System.out.print(String.format("top down result is that for %s money we need %s coins", total, topDownValue));
    }

    /**
     * 计算凑出total金额的钱最少需要多少个硬币
     * @param total 需要凑出的总金额
     * @param coins 可供选择的不同面额的硬币
     * @param map 用于缓存中间结果的map
     * @return
     */
    private static int minimumCoinTopDown(int total, int[] coins, Map<Integer, Integer> map) {
        // 〇 鲁棒性代码
        if (total == 0) {
            return 0;
        }

        // Ⅰ 在开始计算之前，查看下之前有没有计算过 当前规模的子问题
        if (map.get(total) != null) {
            return map.get(total);
        }

        // Ⅱ 遍历所有面额的硬币，并默认剩下的钱已经被凑好了（递归调用时，传入更新后的参数）
        // 准备一个最小值变量    用于表示凑出total所需要的最少硬币数量
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < coins.length; i++) {
            // 1 判断当前硬币的面值 与 需要凑出的总金额total 的大小关系
            if (coins[i] > total) {
                continue; // 这个硬币用不上，跳过这个选择
            }

            // 获取到凑出 total - coins[i]金额的钱所需要的最少硬币数
            int val = minimumCoinTopDown(total - coins[i], coins, map);

            // 更新min的值
            if (val < min) {
                min = val;
            }
        }

        // Ⅲ 返回计算出的“凑出total所需要的最少硬币个数min”
        // 1 修正min的值为正确的值
        min = (min == Integer.MAX_VALUE) ? min : min + 1;

        // 2 缓存中间的计算结果    避免重复计算
        map.put(total, min);

        return min;
    }
}

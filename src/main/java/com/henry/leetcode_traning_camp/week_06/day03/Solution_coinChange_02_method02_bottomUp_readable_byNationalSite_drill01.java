package com.henry.leetcode_traning_camp.week_06.day03;

public class Solution_coinChange_02_method02_bottomUp_readable_byNationalSite_drill01 {
    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        int amount = 11;

        int minCoinNumberToPoll = coinChange(coins, amount);

        System.out.println("凑出 " + amount + " 金额所需要的最少硬币数量为：" + minCoinNumberToPoll);

    }

    private static int coinChange(int[] coins, int amount) {
        /* 〇 准备dpTable[] */
        int[] dpTable = new int[amount + 1];

        /* Ⅰ 使用递推公式来填充dpTable[] */
        // EXPR1: 从金额1开始凑，因为金额0的answer=0已经默认填写
        // EXPR2: 一直凑到金额amount（作为dpTable的下标）
        for (int i = 1; i <= amount; i++) { // 1 把大问题 amount 拆分成小问题 amount-1
            // 2 对每一个小问题 使用同样的 可用硬币组合来求解其 “使用最少硬币的组合”
            int min = Integer.MAX_VALUE;
            for (int coin : coins) {
                // ① 尝试使用当前硬币 coin 来 凑出当前金额i
                // 如果当前硬币可以作为 凑出i的最后一个硬币，则：
                // 使用dpTable[i - coin]来临时更新 min
                if (coin <= i && dpTable[i - coin] != -1) {
                    min = (dpTable[i - coin] < min) ? dpTable[i - coin] : min;
                }
            }

            // 3 如果min没有被更新，说明使用 coins数组 凑不出来 当前金额i。则：把dpTable[i]设置为 -1
            dpTable[i] = (min == Integer.MAX_VALUE) ? -1 : min + 1;
        }

        /* Ⅱ 返回dpTable[]中合适位置的元素 */
        return dpTable[amount];
    }
}

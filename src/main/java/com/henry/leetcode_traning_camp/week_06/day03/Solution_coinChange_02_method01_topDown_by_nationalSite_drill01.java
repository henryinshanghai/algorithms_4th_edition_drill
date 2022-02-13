package com.henry.leetcode_traning_camp.week_06.day03;

public class Solution_coinChange_02_method01_topDown_by_nationalSite_drill01 {
    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        int amount = 11;

        int minCoinNumberToPoll = coinChange(coins, amount);

        System.out.println("凑出" + amount + "金额所需要的最少硬币数量为：" + minCoinNumberToPoll);
    }

    private static int coinChange(int[] coins, int amount) {
        if (amount < 1) return 0;

        // 添加一个 容量等于amount的数组 作为参数
        return helper(coins, amount, new int[amount]);
    }

    private static int helper(int[] coins, int currAmount, int[] dpTable) {
        /* 〇 列举边界条件 */
        if (currAmount < 0) return -1;
        if (currAmount == 0) return 0;

        /* Ⅰ 记忆化：判断当前的子问题是不是已经计算过了 */
        // 如果是，就直接返回 对应的count[]数组中的元素
        if (dpTable[currAmount - 1] != 0) { // 这里 金额12 对应的元素是 dpTable[11]
            return dpTable[currAmount - 1];
        }

        /* Ⅱ 遍历每一个硬币，并尝试使用当前硬币来凑出 给定金额rem */
        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            // 1 先凑出 rem - coin 的金额，得到所需要的最小硬币数量
            int coinNumberForSubAmount = helper(coins, currAmount - coin, dpTable);
            // 2 判断1中的计算结果是否有效 如果是，说明：本轮循环中计算得到了一种凑出 res金额的硬币组合(而且该组合的最后一个硬币使用了coin)
            // 判断 所使用的硬币数量 是不是小于 min。 如果是，说明xxx，则：更新min
            if (coinNumberForSubAmount >= 0 && coinNumberForSubAmount < min) { // EXPR: >=0
                min = coinNumberForSubAmount + 1; // EXPR2: +1
            }
        }
        /* Ⅲ 返回最终得到的计算结果 count[rem - 1] */
        dpTable[currAmount - 1] = (min == Integer.MAX_VALUE) ? -1 : min;
        // 在return之前，把此次子问题的结算结果添加到 dpTable[]中  以避免重复计算
        return dpTable[currAmount - 1];
    }
}

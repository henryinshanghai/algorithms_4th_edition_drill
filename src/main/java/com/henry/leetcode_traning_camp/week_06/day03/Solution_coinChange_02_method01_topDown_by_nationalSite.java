package com.henry.leetcode_traning_camp.week_06.day03;

public class Solution_coinChange_02_method01_topDown_by_nationalSite {
    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        int amount = 11;

        int minCoinNumberToPoll = coinChange(coins, amount);

        System.out.println("凑出 " + amount + " 金额所需要的最少硬币数量为：" + minCoinNumberToPoll);
    }

    private static int coinChange(int[] coins, int amount) {
        if (amount < 1) return 0;

        // 添加一个 容量等于amount的数组 作为参数
        return helper(coins, amount, new int[amount]);
    }

    /**
     * 递归方法/DFS
     *
     * @param coins
     * @param rem   还需要凑出的金额数值
     * @param count count[i] 表示 凑出金额i所需要的最少硬币个数
     * @return
     */
    private static int helper(int[] coins, int rem, int[] count) {
        /* 〇 列举边界条件 */
        if (rem < 0) return -1; // not valid
        if (rem == 0) return 0; // completed

        /* Ⅰ 记忆化：判断当前的子问题是不是已经计算过了 */
        // 如果是，就直接返回 对应的count[]数组中的元素
        // already computed, so reuse
        // use an array as a cache(because array can store something)
        if (count[rem - 1] != 0) return count[rem - 1];

        /* Ⅱ 遍历每一个硬币，并尝试使用当前硬币来凑出 给定金额rem */
        // go through each coin to make up to the rem
        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            // 1 先凑出 rem - coin 的金额，得到所需要的最小硬币数量
            // once choose curr coin, next goal would be 'make up to the rest'
            int res = helper(coins, rem - coin, count);

            // 2 判断1中的计算结果是否有效 如果是，说明：本轮循环中计算得到了一种凑出 res金额的硬币组合(而且该组合的最后一个硬币使用了coin)
            // 判断 所使用的硬币数量 是不是小于 min。 如果是，说明xxx，则：更新min
            // suppose that we need res as minCoinNumberToPoll to sum up to 'amount - coin'
            // with that answer in mind, how many minCoinNumberToPoll we need to sum up to amount
            // the answer would be: plus 1(this one come from the curr coin)
            if (res >= 0 && res < min) // we need make sure the res-result is proper
                min = 1 + res;
        }

        /* Ⅲ 返回最终得到的计算结果 count[rem - 1] */
        // 在return之前，把此次子问题的结算结果添加到 dpTable[]中  以避免重复计算
        // now we know the answer to the original problem. that is to say, min
        // but before we return it, let's store the curr-result in arr to avoid the repeat calcu
        // this index can be a bit tricky.
        // keep in mind: the biggest index is capacity - 1
        count[rem - 1] = (min == Integer.MAX_VALUE) ? -1 : min;

        // return it as we mention
        return count[rem - 1];
    }
}
/*
for this approach, we break down the original problem.
and suppose we already have the answer for a sub-problem(aka smaller problem)
then use the sub-problem's answer, we add up to the original problem answer.

this is so called top-down.
we break the problem, and drill down (via recursion) till the basic condition.
where you know the answer right there without any demand of thinking
 */

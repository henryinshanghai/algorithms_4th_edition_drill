package com.henry.leetcode_traning_camp.week_06.day03;

public class Solution_coinChange_02_method02_bottomUp_byNationalSite {
    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        int amount = 11;

        int minCoinNumberToPoll = coinChange(coins, amount);

        System.out.println("凑出" + amount + "金额所需要的最少硬币数量为：" + minCoinNumberToPoll);
    }

    /**
     * dynamic programming - bottom up
     *
     * @param coins  可选的硬币集合
     * @param amount 需要凑出的金额
     * @return
     */
    private static int coinChange(int[] coins, int amount) {
        /* 〇 参数判断*/
        if (amount < 1) return 0;

        /* Ⅰ 准备递推需要的数组 & 一些初始值 */
        int[] dp = new int[amount + 1];
        int sum = 0;

        /* Ⅱ 准备一个循环 开始进行递推 */
        // 对于每一个要凑出的金额 都需要尝试每一种面额的硬币
        // 每一个金额：while()循环
        // 每一种硬币：for()循环
        while (++sum <= amount) {
            int min = -1;
            for (int coin : coins) {
                // 只有在coin比起sum更小时，才会用coin来凑sum
                if (sum >= coin && dp[sum - coin] != -1) {
                    // this is a bit hard to follow since it's using nested xxx ? xxx : xxx
//                    int temp = dp[sum - coin] + 1;
//                    min = min < 0 ? temp : (temp < min ? temp : min);
                }
            }

            // for every sum value, we have a minCoinNumberToPoll calculated
            // store it
            dp[sum] = min;
        }

        // return back the right item of the dpTable check!
        return dp[amount];
    }
}

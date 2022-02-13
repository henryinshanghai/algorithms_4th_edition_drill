package com.henry.leetcode_traning_camp.week_06.day03;

public class Solution_coinChange_02_method02_bottomUp_readable_byNationalSite {
    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        int amount = 11;

        int minCoinNumberToPoll = coinChange(coins, amount);

        System.out.println("凑出" + amount + "金额所需要的最少硬币数量为：" + minCoinNumberToPoll);
    }

    private static int coinChange(int[] coins, int amount) {
        /* 〇 准备dpTable[] */
        int dp[] = new int[amount + 1];

        /* Ⅰ 使用递推公式来填充dpTable[] */
        for (int i = 1; i <= amount; i++) { // 1 把大问题 amount 拆分成小问题 amount-1
            int min = Integer.MAX_VALUE;
            // 2 对每一个小问题 使用同样的 可用硬币组合来求解其 “使用最少硬币的组合”
            for (int coin : coins) {
                // ① 如果当前硬币可以作为 凑出i的最后一个硬币，则：
                if (i - coin >= 0 && dp[i - coin] != -1)
                    // 使用dpTable[i - coin]来临时更新 min
                    min = dp[i - coin] < min ? dp[i - coin] : min;
            }

            // Set dp[i] to -1 if i (current amount) can not be reach by  coins array
            // 3 如果min没有被更新，说明使用 coins数组 凑不出来 当前金额。则：把dpTable[i]设置为 -1
            dp[i] = min == Integer.MAX_VALUE ? -1 : 1 + min;
        }

        /* Ⅱ 返回dpTable[]中合适位置的元素 */
        return dp[amount];
    }
}
/*
this can also be done by BFS, but why?
 */

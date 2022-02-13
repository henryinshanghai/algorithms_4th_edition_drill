package com.henry.leetcode_traning_camp.week_06.day03;

public class Solution_coinChange_02_method01_topDown_by_nationalSite_drill02 {
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

    private static int helper(int[] coins, int currentAmount, int[] answerTable) {
        if (currentAmount < 0) return -1;
        if (currentAmount == 0) return 0;

        if (answerTable[currentAmount - 1] != 0) {
            return answerTable[currentAmount - 1];
        }

        int min = Integer.MAX_VALUE;
        for (int coin : coins) {

            int answerForSubAmount = helper(coins, currentAmount - coin, answerTable);
            if (answerForSubAmount >= 0 && answerForSubAmount < min) {
                min = answerForSubAmount + 1; // 这里的1就是 coin
            }
        }

        answerTable[currentAmount - 1] = (min == Integer.MAX_VALUE) ? -1 : min;

        return answerTable[currentAmount - 1];
    }
}

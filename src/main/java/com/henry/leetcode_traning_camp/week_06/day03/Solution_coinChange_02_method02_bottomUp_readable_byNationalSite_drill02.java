package com.henry.leetcode_traning_camp.week_06.day03;

public class Solution_coinChange_02_method02_bottomUp_readable_byNationalSite_drill02 {
    public static void main(String[] args) {
        int[] coins = {1, 2, 5};
        int amount = 11;

        int minCoinNumberToPoll = coinChange(coins, amount);

        System.out.println("凑出 " + amount + " 金额所需要的最少硬币数量为：" + minCoinNumberToPoll);
    }

    private static int coinChange(int[] coins, int amount) {
        int[] answerTable = new int[amount + 1];

        for (int i = 1; i <= amount; i++) {
            int min = Integer.MAX_VALUE;
            for (int coin : coins) {
                if (coin <= i && answerTable[i - coin] != -1) {
                    // 找到 解决子问题所需要的最小硬币数量
                    min = (answerTable[i - coin] < min) ? answerTable[i - coin] : min;
                }
            }

            // 计算出 解决当前问题所需要使用的最小硬币数量 并绑定到answerTable[]数组对应的元素上
            answerTable[i] = (min == Integer.MAX_VALUE) ? -1 : min + 1;
        }

        return answerTable[amount];
    }
}

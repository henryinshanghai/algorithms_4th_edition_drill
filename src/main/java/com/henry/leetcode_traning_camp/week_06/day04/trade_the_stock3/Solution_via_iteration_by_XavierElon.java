package com.henry.leetcode_traning_camp.week_06.day04.trade_the_stock3;

import java.util.Arrays;

public class Solution_via_iteration_by_XavierElon {
    public static void main(String[] args) {
        int[] currentDayToItsStockValue = {3, 3, 5, 0, 0, 3, 1, 4}; // 6

        int maxProfit = getMaxProfitFrom(currentDayToItsStockValue);
        System.out.println("在最多只能进行两次交易的情况下，从该股票的股价序列 " + Arrays.toString(currentDayToItsStockValue) +
                "中所能赚取的最大利润为： " + maxProfit);
    }

    private static int getMaxProfitFrom(int[] currentDayToItsStockValue) {
        int netBalanceAfterFirstBuy = -currentDayToItsStockValue[0];
        int netBalanceAfterSecondBuy = -currentDayToItsStockValue[0];

        int netBalanceAfterFirstSell = 0, totalProfit = 0;

        for (int currentDayStockValue : currentDayToItsStockValue) {
            // 原则：只有卖出股票，才能够收获利润
            netBalanceAfterFirstBuy = Math.max(netBalanceAfterFirstBuy, -currentDayStockValue);
            // 收获利润*1
            netBalanceAfterFirstSell = Math.max(netBalanceAfterFirstSell, currentDayStockValue + netBalanceAfterFirstBuy);

            netBalanceAfterSecondBuy = Math.max(netBalanceAfterSecondBuy, netBalanceAfterFirstSell - currentDayStockValue);
            // 收获利润*2
            totalProfit = Math.max(totalProfit, currentDayStockValue + netBalanceAfterSecondBuy);
        }

        // 返回所有可能收获的利润中的最大利润
        return totalProfit;
    }
}

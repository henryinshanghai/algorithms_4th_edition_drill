package com.henry.leetcode_traning_camp.week_06.day04;

import java.util.Arrays;

public class Solution_buyAndSellStock_04_method01_byXavierElon {
    public static void main(String[] args) {
        int[] stockValuesThroughDays = {3,3,5,0,0,3,1,4}; // 6 

        int maxProfit = maxProfit(stockValuesThroughDays);

        System.out.println("在既定规则下，从股票序列 " + Arrays.toString(stockValuesThroughDays) +
                "中所能赚取的最大利润为： " + maxProfit);
    }

    private static int maxProfit(int[] stockValuesThroughDays) {
        int buy1 = -stockValuesThroughDays[0];
        int buy2 = -stockValuesThroughDays[0];

        int profit1 = 0, totalProfit = 0;

        for (int stockValue : stockValuesThroughDays) {
            buy1 = Math.max(buy1, -stockValue);
            profit1 = Math.max(profit1, stockValue + buy1);

            buy2 = Math.max(buy2, profit1 - stockValue);
            totalProfit = Math.max(totalProfit, stockValue + buy2);
        }

        return totalProfit;
    }
}

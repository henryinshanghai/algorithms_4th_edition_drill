package com.henry.leetcode_traning_camp.week_06.day04;

import java.util.Arrays;

public class Solution_buyAndSellStockii_03_method01_byKevinNaughton {
    public static void main(String[] args) {
//        int[] stockValuesThroughDays = {7,1,5,3,6,4}; // 7
//        int[] stockValuesThroughDays = {1,2,3,4,5}; // 4
        int[] stockValuesThroughDays = {7,6,4,3,1}; // 0

        int maxProfit = maxProfit(stockValuesThroughDays);

        System.out.println("在可以连续交易的情况下，对于本质股票的价格序列: " + Arrays.toString(stockValuesThroughDays) +
                "所能赚取的最大利润为： " + maxProfit);
    }

    private static int maxProfit(int[] stockValuesThroughDays) {
        /* Ⅰ 对给定的价格数组进行判断 */
        if (stockValuesThroughDays == null || stockValuesThroughDays.length == 0) {
            return 0; // 赚不到钱
        }

        /* Ⅱ 遍历价格序列中每一天的价格，以“低买高卖”的原则把每一份可能的利润添加进来(以一次交易的形式) */
        int profit = 0;
        for (int i = 0; i < stockValuesThroughDays.length - 1; i++) {
            // 1 如果明天的价格更高，说明今天就可以“低买”+明天就可以“高买”(aka 利润)。则：把利润计入profit
            if (stockValuesThroughDays[i] < stockValuesThroughDays[i + 1]) {
                profit += (stockValuesThroughDays[i + 1] - stockValuesThroughDays[i]);
            }
            // 2 如果没有更高，就继续遍历。直到遇到价格更高的某一天 再卖出赚钱
        }

        /* Ⅲ 返回最终得到的累计利润 */
        return profit;
    }
}


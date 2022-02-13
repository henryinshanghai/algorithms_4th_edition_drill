package com.henry.leetcode_traning_camp.week_04.day05;

public class Solution_maxProfitOfStock_03_method01_bruteForce {
    public static void main(String[] args) {

        int[] prices = {1, 2, 3, 4, 5};

        int profit = maxProfit(prices);

        System.out.println("当前股价序列中所能赚到的最大利润为： " + profit);
    }

    private static int maxProfit(int[] prices) {
        // 〇
        if (prices == null || prices.length == 0) {
            return 0;
        }

        // Ⅰ 根据相邻两天的股价决定买入 & 卖出
        int profit = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            profit += prices[i + 1] - prices[i];
        } // {1,2,3,4,5} 1-2、2-3、3-4、4-5 【买入-卖出】

        return profit;
    }
}

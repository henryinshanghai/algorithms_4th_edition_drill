package com.henry.leetcode_traning_camp.week_06.day04;

import java.util.Arrays;

public class Solution_buyAndSellStock_04_method02_byJayatiTiwari_drill01 {
    public static void main(String[] args) {

//        int[] stockValuesThroughDays = {3,3,5,0,0,3,1,4}; // 6
//        int[] stockValuesThroughDays = {1,2,3,4,5}; // 4
        int[] stockValuesThroughDays = {7,6,4,3,1}; // 0

        int maxProfit = maxProfit(stockValuesThroughDays);

        System.out.println("某只股票价格序列为: " + Arrays.toString(stockValuesThroughDays));
        System.out.println("在最多只能进行两次交易的情况下，所能赚取的最大利润为: " + maxProfit);
    }

    private static int maxProfit(int[] stockValuesThroughDays) {
        /* 〇 对参数进行特殊值判断 */
        if (stockValuesThroughDays.length == 0) return 0;

        /* Ⅰ 准备一些变量并绑定初始值————用于在循环过程中更新 */
        // 初始化 买入的价格 为 Integer.MIN_VALUE
        int mihAfterFirstBuy = Integer.MIN_VALUE, mihAfterSecondBuy = Integer.MIN_VALUE;
        // 初始化 卖出的价格 为 0
        int mihAfterFirstSell = 0, mihAfterSecondSell = 0;

        /* Ⅱ 准备循环，遍历每一天的股价*/
        // note that: you don't know what the real profit gonna be until the last day, therefore
        // all you need to do it keep it maximum all the way to the end
        // 这种方式中间可能会出现赔钱的情况，但是通过max()函数会去除掉这些情况
        for (int i = 0; i < stockValuesThroughDays.length; i++) {
            /* first transaction */
            // 尝试获取till today,买入股票的最低价格 aka MIN(money in hand)的最大值
            mihAfterFirstBuy = Math.max(mihAfterFirstBuy, -stockValuesThroughDays[i]);
            mihAfterFirstSell = Math.max(mihAfterFirstSell, mihAfterFirstBuy + stockValuesThroughDays[i]);

            /* second transaction */
            mihAfterSecondBuy = Math.max(mihAfterSecondBuy, mihAfterFirstSell - stockValuesThroughDays[i]);
            mihAfterSecondSell = Math.max(mihAfterSecondSell, mihAfterSecondBuy + stockValuesThroughDays[i]);

        }

        /* Ⅲ 返回循环结束后得到的min_ss */
        return mihAfterSecondSell;
    }
}
// nail it, get up to shower!
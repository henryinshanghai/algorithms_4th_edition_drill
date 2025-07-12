package com.henry.leetcode_traning_camp.week_06.day04.trade_the_stock2;

import java.util.Arrays;

// 验证：对于给定一只股票在连续多天的股价&&可以进行尽可能多次的交易的情况，可以
// 通过不断比较 当前股价 与 明天股价 的方式，来不断收获所有可能的利润，从而得到 maxProfit
public class Solution_via_iteration_by_KevinNaughton {
    public static void main(String[] args) {
//        int[] currentDayToItsStockValue = {7,1,5,3,6,4}; // 7
//        int[] currentDayToItsStockValue = {1,2,3,4,5}; // 4
        int[] currentDayToItsStockValue = {7, 6, 4, 3, 1}; // 0

        int maxProfit = getMaxProfitFrom(currentDayToItsStockValue);

        System.out.println("在可以连续交易的情况下，对于本质股票的价格序列: " + Arrays.toString(currentDayToItsStockValue) + "所能赚取的最大利润为： " + maxProfit);
    }

    private static int getMaxProfitFrom(int[] currentDayToItsStockValueArr) {
        /* Ⅰ 对给定的价格数组进行判断 */
        if (currentDayToItsStockValueArr == null || currentDayToItsStockValueArr.length == 0) {
            return 0; // 赚不到钱
        }

        /* Ⅱ 遍历价格序列中每一天的价格，以“低买高卖”的原则把每一份”可能的利润“添加进来(以一次交易的形式) */
        int accumulatedProfit = 0;
        for (int currentDay = 0; currentDay < currentDayToItsStockValueArr.length - 1; currentDay++) {
            int currentDayStockValue = currentDayToItsStockValueArr[currentDay];
            int nextDayStockValue = currentDayToItsStockValueArr[currentDay + 1];

            // #1 如果明天的股价更高，说明今天就可以 用“今天低买”+“明天高买” 的方式 来 获取可能的利润(aka 利润)，则：
            if (currentDayStockValue < nextDayStockValue) {
                // 先计算当前交易的利润
                int profitOfCurrentTrade = nextDayStockValue - currentDayStockValue;
                // 再把利润累加到总利润中
                accumulatedProfit += profitOfCurrentTrade;
            }
            // #2 如果没有更高，就继续遍历。直到遇到价格更高的某一天 再卖出赚钱
        }

        /* Ⅲ 返回最终得到的累计利润 */
        return accumulatedProfit;
    }

    private static boolean nextDaysValueIsBigger(int[] stockValuesThroughDays, int currentDay) {
        // 当前股票的价格 小于 第二天股票的价格
        return stockValuesThroughDays[currentDay] < stockValuesThroughDays[currentDay + 1];
    }
}


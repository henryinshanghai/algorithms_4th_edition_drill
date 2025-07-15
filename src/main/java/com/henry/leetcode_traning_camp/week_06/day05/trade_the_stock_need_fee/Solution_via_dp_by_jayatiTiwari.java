package com.henry.leetcode_traning_camp.week_06.day05.trade_the_stock_need_fee;

import java.util.Arrays;

// 对于买卖股票的最佳时机(交易需要支付手续费)的问题，可以使用 当天持有股票时的净余额 & 当天不持有股票时的净余额
// 来 得到能够获取到的最大利润 aka 最后一天不持有股票时的净余额
// 🐖 这里使用的规则是：在购入股票时，支付手续费用
public class Solution_via_dp_by_jayatiTiwari {
    public static void main(String[] args) {
        int[] currentDayToItsStockValue = {1, 3, 2, 8, 4, 9}; // 8
        int tradeFee = 2; // 手续费

        int maxProfit = maxProfitCouldGetFrom(currentDayToItsStockValue, tradeFee);

        System.out.println("如果可以进行无数次交易，并且每次交易需要收取费用，则：" + tradeFee + ",");
        System.out.println("股价序列：" + Arrays.toString(currentDayToItsStockValue) + "所能赚取的最大利润为： " + maxProfit);
    }

    private static int maxProfitCouldGetFrom(int[] currentDayToItsStockValue, int tradeFee) {
        /* 〇 对参数进行判断 */
        int daysAmount = currentDayToItsStockValue.length;
        if (daysAmount <= 1) {
            return 0;
        }

        /* Ⅰ 准备一个dpTable[<current_day>][<hold_stock_flag>]    用于逐步得到最终结果  */
        int[][] currentConditionToItsMaxNetBalance = new int[daysAmount][2];

        /* Ⅱ 初始化dpTable[][]的第一个元素 */
        // 第0天不持有股票 aka 什么都不做，则：手上的净余额为0
        currentConditionToItsMaxNetBalance[0][0] = 0;
        // 第0天持有股票 aka 买入股票，则：手上的净余额为 (负的股价 - 交易所需要的手续费)
        currentConditionToItsMaxNetBalance[0][1] = -currentDayToItsStockValue[0]; //  - tradeFee 国际通用规则：在卖出股票时，支付手续费。

        /* Ⅲ 计算dp[]数组的元素值 */
        for (int currentDay = 1; currentDay < daysAmount; currentDay++) {
            // 计算 在今天不持有股票的情况下 手中的净余额;
            currentConditionToItsMaxNetBalance[currentDay][0] =
                    Math.max(currentConditionToItsMaxNetBalance[currentDay - 1][1] + currentDayToItsStockValue[currentDay] - tradeFee, // 前一天持有股票 & 今天卖出股票，并支付手续费
                            currentConditionToItsMaxNetBalance[currentDay - 1][0]); // 前一天就 不持有股票 & 今天什么都不做
            // 计算 在今天持有股票的情况下 手中的净余额；
            currentConditionToItsMaxNetBalance[currentDay][1] =
                    Math.max(currentConditionToItsMaxNetBalance[currentDay - 1][0] - currentDayToItsStockValue[currentDay], // 前一天不持有股票 & 今天买入
                        currentConditionToItsMaxNetBalance[currentDay - 1][1]); // 前一天就 持有股票 & 今天什么都不做

        }

        /* Ⅳ 返回dpTable中的正确的元素值：最后一天不持有股票时手上的净余额 */
        return currentConditionToItsMaxNetBalance[daysAmount - 1][0];
    }
}

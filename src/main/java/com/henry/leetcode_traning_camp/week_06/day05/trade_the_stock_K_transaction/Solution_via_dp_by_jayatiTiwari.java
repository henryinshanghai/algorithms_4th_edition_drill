package com.henry.leetcode_traning_camp.week_06.day05.trade_the_stock_K_transaction;

import java.util.Arrays;

// 对于买卖股票的最佳时机（最多进行K次交易）的问题，可以使用 维护“本次交易买入股票后手上的净余额”
// & “本次交易卖出股票后手上的净余额” 这两个数组 来 得到最终的最大利润
// 完成最后一次交易后，手上的净余额 就是 最大利润。
public class Solution_via_dp_by_jayatiTiwari {
    public static void main(String[] args) {
        int[] currentDayToItsStockValue = {3, 2, 6, 5, 0, 3}; // 7
        int tradeTimes = 2;
        int maxProfit = getMaxProfitFrom(currentDayToItsStockValue, tradeTimes);

        System.out.println("当前股票的股价序列: " + Arrays.toString(currentDayToItsStockValue));
        System.out.println("按照冷冻期等相关规则所能赚取的最大利润为：" + maxProfit);
    }

    private static int getMaxProfitFrom(int[] currentDayToItsStockValue, int tradeTimes) {
        int days = currentDayToItsStockValue.length;
        if (days <= 1 || tradeTimes <= 0) {
            return 0;
        }

        int profit = 0;
        // 如果允许的交易次数很多的话，说明 可以按照低价买入 + 高价卖出的原则 来 收获所有可能的利润，则：
        if (tradeTimes >= days / 2) {
            for (int currentDay = 0; currentDay < days - 1; currentDay++) {
                // 遍历股价序列，来
                if (currentDayToItsStockValue[currentDay] < currentDayToItsStockValue[currentDay + 1]) {
                    // 收获所有可能的利润
                    profit += (currentDayToItsStockValue[currentDay + 1] - currentDayToItsStockValue[currentDay]);
                }
            }

            return profit;
        }

        // 数组元素的初始化
        int[] currentBuyToItsMaxNetBalance = new int[tradeTimes];
        Arrays.fill(currentBuyToItsMaxNetBalance, Integer.MIN_VALUE); // 买入时的元素初始值为 整型最小值
        int[] currentSellToItsMaxNetBalance = new int[tradeTimes];

        // 对于每一天...
        for (int currentDay = 0; currentDay < currentDayToItsStockValue.length; currentDay++) {
            // 获取到当前天数的股价
            int currentDayStockValue = currentDayToItsStockValue[currentDay];

            /* 计算 当前天数 的 在所有交易条件下的最大净余额 */
            // 🐖 每次交易都要保证手上的净余额最大
            for (int currentTrade = 0; currentTrade < tradeTimes; currentTrade++) {
                // 买入
                currentBuyToItsMaxNetBalance[currentTrade] =
                        Math.max(currentBuyToItsMaxNetBalance[currentTrade], // 保持原始的计算值
                                currentTrade == 0 // 如果是第一次买入
                                        ? -currentDayStockValue // 则：手上的净余额为 负的股票价值
                                        : (currentSellToItsMaxNetBalance[currentTrade - 1] - currentDayStockValue)); // 否则：净余额 = 在上一次交易的基础上 - 当前股票的价值
                // 卖出
                currentSellToItsMaxNetBalance[currentTrade] =
                        Math.max(currentSellToItsMaxNetBalance[currentTrade], // 保持原值
                                currentBuyToItsMaxNetBalance[currentTrade] + currentDayStockValue); // 净余额 = 在买入操作的基础上 + 当前股票的价值
            }
        }

        // 返回 最后一次交易(tradeTimes - 1)后，手中的余额 aka 净利润
        return currentSellToItsMaxNetBalance[tradeTimes - 1];
    }
}

package com.henry.leetcode_traning_camp.week_06.day04.trade_the_stock3;

import java.util.Arrays;

// 验证：对于给定一只股票在连续多天的股价&&最多只允许进行两次交易的情况，可以
// 通过 为每一天都维护4个变量（当天首次买入的余额、当天首次卖出的余额、当天第二次买入的余额、当天第二次卖出的余额）的方式
// 来 得到maxProfit = 第二次卖出时手上的余额
public class Solution_via_iteration_by_JayatiTiwari {
    public static void main(String[] args) {
        int[] currentDayToItsStockValue = {3, 3, 5, 0, 0, 3, 1, 4}; // 6
//        int[] currentDayToItsStockValue = {1,2,3,4,5}; // 4
//        int[] currentDayToItsStockValue = {7, 6, 4, 3, 1}; // 0

        int maxProfit = getMaxProfitFrom(currentDayToItsStockValue);

        System.out.println("该只股票的价格序列为: " + Arrays.toString(currentDayToItsStockValue));
        System.out.println("在最多只能进行两次交易的情况下，所能赚取的最大利润为: " + maxProfit);
    }

    private static int getMaxProfitFrom(int[] currentDayToItsStockValueArr) {
        /* 〇 对参数进行特殊值判断 */
        if (currentDayToItsStockValueArr.length == 0) {
            return 0;
        }

        /* Ⅰ 准备一些变量并绑定初始值————用于在循环过程中更新 */
        // 初始化 买入的价格 为 Integer.MIN_VALUE
        int netBalanceAfterFirstBuy = Integer.MIN_VALUE,
                netBalanceAfterSecondBuy = Integer.MIN_VALUE;
        // 初始化 卖出的价格 为 0
        int netBalanceAfterFirstSell = 0, netBalanceAfterSecondSell = 0;

        /* Ⅱ 准备循环，遍历每一天的股价*/
        // 🐖 除非到了最后一天，你是不可能知道真正的利润会是多少 因此你需要在整个过程中不断取最大值
        for (int currentDay = 0; currentDay < currentDayToItsStockValueArr.length; currentDay++) {
            // 获取到 当天的股价
            int currentDayStockValue = currentDayToItsStockValueArr[currentDay];

            /* 首次交易 */
            // 如果在今天进行 “第一次买入”的操作，则：计算出“第一次买入后手上的余额”👇
            netBalanceAfterFirstBuy = Math.max(-currentDayStockValue, netBalanceAfterFirstBuy); // buying make the minus
            // 如果在今天进行 “第一次卖出”的操作，则：👇
            netBalanceAfterFirstSell = Math.max(netBalanceAfterFirstBuy + currentDayStockValue, netBalanceAfterFirstSell); // netBalanceAfterFirstSell as pre computed value

            /* 第二次交易 */
            // 如果在今天进行“第二次买入”的操作，则👇
            netBalanceAfterSecondBuy = Math.max(netBalanceAfterFirstSell - currentDayStockValue, netBalanceAfterSecondBuy); // either... or ...
            // 如果在今天进行“第二次卖出”的操作，则👇
            netBalanceAfterSecondSell = Math.max(currentDayStockValue + netBalanceAfterSecondBuy, netBalanceAfterSecondSell);

            System.out.println("该只股票的价格序列为: " + Arrays.toString(currentDayToItsStockValueArr));
            System.out.println("第" + (currentDay) + "天的所有变量如下");
            System.out.println("第一次买入： " + netBalanceAfterFirstBuy);
            System.out.println("第一次卖出： " + netBalanceAfterFirstSell);
            System.out.println("第二次买入： " + netBalanceAfterSecondBuy);
            System.out.println("第二次卖出： " + netBalanceAfterSecondSell);
            System.out.println();
        } // note that：本轮循环得到的min_ss的值没有任何具体的含义，必须在循环结束后 它才会存储我们预期的值

        /* Ⅲ 返回循环结束后得到的min_ss 也就是第二次卖出股票后手头上的剩余价值 */
        return netBalanceAfterSecondSell; // this for sure will be the max profit of all~
    }
}

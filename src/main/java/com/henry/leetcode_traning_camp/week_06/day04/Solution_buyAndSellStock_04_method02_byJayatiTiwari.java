package com.henry.leetcode_traning_camp.week_06.day04;

import java.util.Arrays;

public class Solution_buyAndSellStock_04_method02_byJayatiTiwari {
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
        if (stockValuesThroughDays.length == 0) {
            return 0;
        }

        /* Ⅰ 准备一些变量并绑定初始值————用于在循环过程中更新 */
        // 初始化 买入的价格 为 Integer.MIN_VALUE
        int mih_fb = Integer.MIN_VALUE, mih_sb = Integer.MIN_VALUE;
        // 初始化 卖出的价格 为 0
        int mih_fs = 0, mih_ss = 0;

        /* Ⅱ 准备循环，遍历每一天的股价*/
        // note that: you don't know what the real profit gonna be until the last day, therefore
        // all you need to do it keep it maximum all the way to the end
        for (int i = 0; i < stockValuesThroughDays.length; i++) {
            /* first transaction */
            // 尝试获取till today,买入股票的最低价格 aka MIN(money in hand)的最大值
            mih_fb = Math.max(-stockValuesThroughDays[i], mih_fb); // buying make the minus

            mih_fs = Math.max(mih_fb + stockValuesThroughDays[i], mih_fs); // mih_fs as pre computed value

            /* second transaction */
            mih_sb = Math.max(mih_fs - stockValuesThroughDays[i], mih_sb); // either... or ...
            mih_ss = Math.max(stockValuesThroughDays[i] + mih_sb, mih_ss);
        } // note that：本轮循环得到的min_ss的值没有任何具体的含义，必须在循环结束后 它才会存储我们预期的值

        /* Ⅲ 返回循环结束后得到的min_ss */
        return mih_ss; // this for sure will be the max profit of all~
    }
}

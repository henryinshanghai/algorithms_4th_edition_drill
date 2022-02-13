package com.henry.leetcode_traning_camp.week_06.day05;

import java.util.Arrays;

public class Solution_buyAndSellWithTransactionFee_05_method01_dp_by_jayatiTiwari {
    public static void main(String[] args) {
        int[] stockValuesThroughDays = {1, 3, 2, 8, 4, 9}; // 8
        int fee = 2;

        int maxProfit = maxProfit(stockValuesThroughDays, fee);

        System.out.println("如果可以进行无数次交易，并且每次交易需要收取费用" + fee + ",");
        System.out.println("则：股价序列：" + Arrays.toString(stockValuesThroughDays) + "所能赚取的最大利润为： " + maxProfit);
    }

    private static int maxProfit(int[] stockValuesThroughDays, int fee) {
        /* 〇 对参数进行判断 */
        int len = stockValuesThroughDays.length;
        if (len <= 1) {
            return 0;
        }

        /* Ⅰ 准备一个dpTable[][]    用于逐步得到最终结果  */
        int[][] dpTable = new int[len][2];

        /* Ⅱ 初始化dpTable[][]的第一行元素     用于 build up the dpTable */
        // 第一行的元素
        dpTable[0][0] = 0;
        dpTable[0][1] = 0 - stockValuesThroughDays[0] - fee;

        /* Ⅲ build up the dpTable */
        for (int i = 1; i < len; i++) {
            // for each day,calculate its value for:
            // if I hold the stock today;
            dpTable[i][0] = Math.max(dpTable[i-1][1] + stockValuesThroughDays[i], dpTable[i-1][0]);
            // if I sell the stock today;
            dpTable[i][1] = Math.max(dpTable[i-1][0] - stockValuesThroughDays[i] - fee, dpTable[i-1][1]);

        }

        /* Ⅳ 返回dpTable中的合适的元素值 */
        return dpTable[len-1][0];
    }
}

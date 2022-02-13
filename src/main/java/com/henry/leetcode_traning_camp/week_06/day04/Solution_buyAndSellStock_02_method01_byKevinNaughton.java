package com.henry.leetcode_traning_camp.week_06.day04;

import java.util.Arrays;

public class Solution_buyAndSellStock_02_method01_byKevinNaughton {
    public static void main(String[] args) {
//        int[] stackValuesThroughDays = {7,1,5,3,6,4}; // 5
        int[] stackValuesThroughDays  = {7,6,4,3,1}; // 0

        int maxProfit = maxProfit(stackValuesThroughDays);

        System.out.println("在股票价格序列: " + Arrays.toString(stackValuesThroughDays) + "中" +
                "一次交易所能赚取的最大利润为：" + maxProfit);
    }

    private static int maxProfit(int[] stackValuesThroughDays) {
        /* Ⅰ 准备一个max变量并初始化    用于记录(track)当前赚取的最大利润 */
        int max = 0;

        /* Ⅱ 准备一个min变量并初始化    用于记录当前所查找到的最小股价 */
        int min = Integer.MAX_VALUE; // 设置成为最大值，这样能够保证min会被更新

        /* Ⅲ 遍历股票价格序列     在循环过程中，持续更新max */
        for (int i = 0; i < stackValuesThroughDays.length; i++) {
            // 尝试计算今天卖出所能赚取的利润：当天的股票价格 - 股票的买入价格
//            stackValuesThroughDays[i] - buyPrice;
            // 如何计算股票的买入价格呢？ 尽量在股价低的时候买入
            // 但是子股价最低时买入并不能保证利润是最大的，所以需要一个比较的操作（以得出利润最大值）

            // 1 记录并更新当前所遇到的最低价格
            if (stackValuesThroughDays[i] < min) {
                min = stackValuesThroughDays[i];
            } else {
                // 2 尝试交易，并在交易后更新最大利润max
                max = Math.max(max, stackValuesThroughDays[i] - min);
            }
        }

        // Ⅳ 返回计算得到的最大利润max
        return max;
    }
}

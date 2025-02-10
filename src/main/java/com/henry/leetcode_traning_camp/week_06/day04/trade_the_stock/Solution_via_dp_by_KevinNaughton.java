package com.henry.leetcode_traning_camp.week_06.day04.trade_the_stock;

import java.util.Arrays;

// 验证：对于 单只股票连续多天股价已知&仅允许一次交易的情况，可以采用不断 max(当前股价 - 当前最低价)的方式 来 得到最大收益
public class Solution_via_dp_by_KevinNaughton {
    public static void main(String[] args) {
        int[] stackValuesThroughDays = {7, 1, 5, 3, 6, 4}; // 5
//        int[] stackValuesThroughDays = {7, 6, 4, 3, 1}; // 0

        int maxProfit = maxProfitCouldGetFrom(stackValuesThroughDays);

        System.out.println("在股票价格序列: " + Arrays.toString(stackValuesThroughDays) + "中" +
                "一次交易所能赚取的最大利润为：" + maxProfit);
    }

    private static int maxProfitCouldGetFrom(int[] stackValuesThroughDays) {
        /* Ⅰ 准备一个max变量并初始化    用于记录(track)当前赚取的最大利润 */
        int maxProfit = 0;

        /* Ⅱ 准备一个min变量并初始化    用于记录当前所查找到的最小股价 */
        int minStockValueSoFar = Integer.MAX_VALUE; // 设置成为最大值，这样能够保证min会被更新

        /* Ⅲ 遍历股票价格序列     在循环过程中，持续更新max */
        for (int currentDay = 0; currentDay < stackValuesThroughDays.length; currentDay++) {
            // 尝试计算今天卖出所能赚取的利润：当天的股票价格 - 股票的买入价格
            // 如何获利？答：尽量在股价低的时候买入，在股价高的时候尝试卖出
            // 但是在股价最低时买入并不能保证利润是最大的(因为后面可能没有高股价了)，所以需要一个和”当前利润“做一个比较的操作（以得出利润最大值）

            // 如果 当前股价 比起 当前所记录的最低股价 还要小，说明 它是一个更低的股价，则：
            if (stackValuesThroughDays[currentDay] < minStockValueSoFar) {
                // 使用它 来 更新“最低股价”的变量
                minStockValueSoFar = stackValuesThroughDays[currentDay];
            } else { // 如果 当前股价比起“当前最低股价”更高，说明 可以进行交易来获利，则：
                // 比较 “在当前天数进行交易”所能的获利 与 “当前最大获利”，取两者中的最大值 作为“最大利润‘
                maxProfit = Math.max(
                        maxProfit,
                        stackValuesThroughDays[currentDay] - minStockValueSoFar);
            }
        }

        // Ⅳ 返回计算得到的最大利润max
        return maxProfit;
    }
}

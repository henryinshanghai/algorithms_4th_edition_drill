package com.henry.leetcode_traning_camp.week_06.day05;

import java.util.Arrays;

public class Solution_buyAndSellWithCoolDown_01_method01_dp_by_jayatiTiwari_drill01 {
    public static void main(String[] args) {
        int[] stockValuesThroughDays = {1,2,3,0,2}; // 3

        int maxProfit = maxProfit(stockValuesThroughDays);

        System.out.println("当前股票的股价序列: " + Arrays.toString(stockValuesThroughDays));
        System.out.println("按照冷冻期等相关规则所能赚取的最大利润为：" + maxProfit);
    }

    private static int maxProfit(int[] stockValuesThroughDays) {
        /* 〇 对入参进行判断 */
        int len = stockValuesThroughDays.length;
        if (len <= 1) return 0;

        /* Ⅰ 准备二维数组中的两个初始值 to build up dbTable from them */
        // 1 边界条件
        if (len == 2 && stockValuesThroughDays[1] > stockValuesThroughDays[0]) {
            return stockValuesThroughDays[1] - stockValuesThroughDays[0];
        } else if (len == 2 && stockValuesThroughDays[0] > stockValuesThroughDays[1]) {
            return 0;
        }
        // 2 准备一个 dpTable
        int[][] dpTable = new int[len][2];

        // 3 准备初始值
        dpTable[0][0] = 0;
        dpTable[0][1] = -stockValuesThroughDays[0];

        dpTable[1][0] = Math.max(dpTable[0][0], dpTable[0][1] + stockValuesThroughDays[1]);
        dpTable[1][1] = Math.max(dpTable[0][1], dpTable[0][0] - stockValuesThroughDays[1]);

        /* Ⅱ build up the dpTable following the 子问题解的递推公式  */
        for (int i = 2; i < len; i++) {
            // 今天没有持股   情况1：昨天没有持股，今天carry forward； 情况2：昨天持股，今天清仓
            dpTable[i][0] = Math.max(dpTable[i - 1][0], dpTable[i - 1][1] + stockValuesThroughDays[i]);
            // 今天持股了    情况1：昨天持股，今天保持   情况2：昨天没有持股，而且是冷冻期。今天持股【冷冻期的约束在这里体现】
            dpTable[i][1] = Math.max(dpTable[i - 1][1], dpTable[i - 2][0] - stockValuesThroughDays[i]);
        }

        /* Ⅲ 最后一天不再持有任何股票 全部转成现金 */
        return dpTable[len - 1][0];
    }
}

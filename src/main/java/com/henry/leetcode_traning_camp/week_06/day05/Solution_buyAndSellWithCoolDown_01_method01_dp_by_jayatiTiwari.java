package com.henry.leetcode_traning_camp.week_06.day05;

import java.util.Arrays;

public class Solution_buyAndSellWithCoolDown_01_method01_dp_by_jayatiTiwari {
    public static void main(String[] args) {
        int[] stockValuesThroughDays = {1,2,3,0,2}; // 3

        int maxProfit = maxProfit(stockValuesThroughDays);

        System.out.println("当前股票的股价序列: " + Arrays.toString(stockValuesThroughDays));
        System.out.println("按照冷冻期等相关规则所能赚取的最大利润为：" + maxProfit);
    }

    /*
    0 因为需要记录“当前是不是有持有股票”，所以 dpTable 需要是一个二维数组————第一维记录天数i、第二维记录“是否...”
    1 dp[i][0] 表示的是什么意思？
        通过 这种方式， 我的mih(money in hand)
    2 根据给出的题设条件来 “找出子问题之间的关联关系公式”：
        case 1: We have a stock on day i, which represent by dp[i][1]
            - I bought it today;
                dp[i - 2][0] - prices[i] // 前天清仓（no stock），今天买股票
            - I am carry forwarding(own stock);
                dp[i - 1][1] // 昨天的股票， 今天carry forward

        case 2: We have no stock on day i, represent by dp[i][0]
            - I sold it today;
                dp[i - 1][1] + prices[i] // 昨天持股，今天清仓
            - I am carry forwarding(own no stock)
                dp[i - 1][0] // 昨天清仓，今天 carry forward
     */
    private static int maxProfit(int[] stockValuesThroughDays) {
        /* 〇 对入参进行判断 */
        int len = stockValuesThroughDays.length;
        if(len <= 1) return 0;

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
        dpTable[0][1] = -stockValuesThroughDays[0]; // 第一天就买了股票，mih为负数
        dpTable[1][0] = Math.max(dpTable[0][0], dpTable[0][1] + stockValuesThroughDays[1]);// 第二天没有股票； 可能性1：carry forwarding； 可能性2：第一天买了股票，第二天卖掉了
        dpTable[1][1] = Math.max(dpTable[0][1], dpTable[0][0] - stockValuesThroughDays[1]);

        /* Ⅱ build up the dpTable following the 子问题解的递推公式  */
        for (int i = 2; i < len; i++) {
            // 今天没有持股
            dpTable[i][0] = Math.max(dpTable[i - 1][0], dpTable[i - 1][1] + stockValuesThroughDays[i]);
            // 今天持股了
            dpTable[i][1] = Math.max(dpTable[i - 1][1], dpTable[i - 2][0] - stockValuesThroughDays[i]);
        }

        /* Ⅲ 最后一天不再持有任何股票 全部转成现金 */
        return dpTable[len - 1][0];
    }
}

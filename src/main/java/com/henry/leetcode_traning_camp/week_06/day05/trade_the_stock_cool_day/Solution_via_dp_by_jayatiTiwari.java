package com.henry.leetcode_traning_camp.week_06.day05.trade_the_stock_cool_day;

import java.util.Arrays;

// 验证：对于 给定某只股票在一段日期内的股价序列&包含冷冻期限制的问题，可以使用
// 动态规划数组 currentConditionToItsNetBalance[<current_day>][<hold_stock_flag>] + 递推公式 max(option1, option2)
// 来 得到所能赚取的最大利润
// 🐖 冷冻期的限制发生在“买入股票的时机”：
// 按照规则（不能同时参与多笔交易）如果买入股票，说明之前已经出售了股票（如果有的话）
// 且出售股票的时机至少是在前天(因为卖出股票后，需要经过一天冷冻期后，才能再买入股票)
public class Solution_via_dp_by_jayatiTiwari {
    public static void main(String[] args) {
        int[] currentDayToItsStockValue = {1, 2, 3, 0, 2}; // 3
        int maxProfit = getMaxProfitFrom(currentDayToItsStockValue);

        System.out.println("当前股票的股价序列: " + Arrays.toString(currentDayToItsStockValue));
        System.out.println("按照冷冻期等相关规则所能赚取的最大利润为：" + maxProfit);
    }

    /*
    根据给出的题设条件来 “找出子问题之间的关联关系公式”：
        case 1: dp[i][1] 今日持股
            - option1：前天（冷冻期约束）清仓，今日购入股票；
                dp[i - 2][0] - prices[i]
            - option2：昨天持股，今日保持
                dp[i - 1][1]

        case 2: dp[i][0] 今日不持股
            - option1：今日卖出股票
                dp[i - 1][1] + prices[i] // 昨天持股，今天清仓
            - option2：昨日清仓，今日保持
                dp[i - 1][0] // 昨天清仓，今天 carry forward
     */
    private static int getMaxProfitFrom(int[] currentDayToItsStockValueArr) {
        /* 〇 对入参进行判断 */
        int daysAmount = currentDayToItsStockValueArr.length;
        if (daysAmount <= 1) return 0;

        // 对特殊情况的判断：如果股价序列就只有两天
        if (daysAmount == 2 && currentDayToItsStockValueArr[1] > currentDayToItsStockValueArr[0]) {
            return currentDayToItsStockValueArr[1] - currentDayToItsStockValueArr[0];
        } else if (daysAmount == 2 && currentDayToItsStockValueArr[0] > currentDayToItsStockValueArr[1]) {
            return 0;
        }

        /* Ⅰ 准备二维数组中的两个初始值 to build up dbTable from them */
        // #1 准备一个 二维dp[][]数组
        // 第一维i - 表示当前天数； 第二维j - 表示当前天数的持股状态（持股 OR 不持股）
        int[][] currentConditionToItsNetBalance = new int[daysAmount][2];

        // #2 初始化dp[][]数组的元素
        currentConditionToItsNetBalance[0][0] = 0; // 第一天&没有持有股票时手中的净余额 手中余额为0
        currentConditionToItsNetBalance[0][1] = -currentDayToItsStockValueArr[0]; // 第一天持有股票的情况； 说明第一天就买了股票，当前手中余额为负数
        currentConditionToItsNetBalance[1][0] = // 第二天没有股票时 手中的净余额；
                Math.max(currentConditionToItsNetBalance[0][0], // 可能性1：第一天就没有股票，第二天也没有购入股票；
                        currentConditionToItsNetBalance[0][1] + currentDayToItsStockValueArr[1]); // 可能性2：第一天买了股票，第二天卖掉了
        currentConditionToItsNetBalance[1][1] = // 第二天持有股票时 手中的净余额
                Math.max(currentConditionToItsNetBalance[0][1], // 可能性1：第一天持有股票，第二天没有卖掉
                        currentConditionToItsNetBalance[0][0] - currentDayToItsStockValueArr[1]); // 可能性2：第一天没有股票，第二天买入股票

        /* 使用递推公式 来 逐步计算出dp数组的元素值 */
        for (int currentDay = 2; currentDay < daysAmount; currentDay++) {
            // 每天都有两种状态：#1 未持股； #2 持股；
            // 今天没有持股时 手上的净余额
            currentConditionToItsNetBalance[currentDay][0]
                    = Math.max(currentConditionToItsNetBalance[currentDay - 1][0], // option01: 前一天没有持股，今天没有买入
                    currentConditionToItsNetBalance[currentDay - 1][1] + currentDayToItsStockValueArr[currentDay]); // option02: 前一天持股 + 今天卖出股票（第二天是冷冻期）

            // 今天持股时 手上的净余额
            currentConditionToItsNetBalance[currentDay][1]
                    = Math.max(currentConditionToItsNetBalance[currentDay - 1][1], // option01: 前一天(昨天)持股，今天没有卖出
                    currentConditionToItsNetBalance[currentDay - 2][0] - currentDayToItsStockValueArr[currentDay]); // option02: 前两天(前天)没有持股,清仓卖出 + (经过一天冷冻期后)今天再购入股票
        }

        /* Ⅲ 获取最大利润 => 最后一天不再持有任何股票 aka 全部转成现金/利润 */
        return currentConditionToItsNetBalance[daysAmount - 1][0];
    }
}

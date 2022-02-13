package com.henry.leetcode_traning_camp.week_06.day05;

import java.util.Arrays;

public class Solution_buyAndSellWithKTransaction_04_categorize_by_jayatiTiwari_drill01 {
    public static void main(String[] args) {
        int k = 2;
        int[] stockValuesThroughDays = {3,2,6,5,0,3};

        int maxProfit = maxProfit(stockValuesThroughDays, k); // 7

        System.out.println("如果最多只允许进行 " + k + "次交易，则：");
        System.out.println("在当前股价序列 " + Arrays.toString(stockValuesThroughDays) + "中，所能赚取到的最大利润为： " + maxProfit);

    }

    private static int maxProfit(int[] stockValuesThroughDays, int k) {
        /* 〇 对参数进行判断 */
        int len = stockValuesThroughDays.length;
        if (len <= 1 || k <= 0) {
            return 0;
        }
        // 对k的大小进行分类讨论
        int profit = 0;
        if (k >= len / 2) {
            /* Ⅰ case01：k比较大的时候，相当于没有交易次数上的限制 */
            for (int i = 0; i < len - 1; i++) {
                if (stockValuesThroughDays[i] < stockValuesThroughDays[i + 1]) {
                    profit += (stockValuesThroughDays[i+1] - stockValuesThroughDays[i]);
                }
            }

            return profit;
        }
        /* Ⅱ case02:k比较小的时候，需要通过动态更新buy 与 sell操作来确定最终的profit */
        // 1 准备数组buy  存储执行第(i+1)次buy操作后手上的现金
        int[] moneyInHandAfterBuy = new int[k];
        Arrays.fill(moneyInHandAfterBuy, Integer.MIN_VALUE);

        // 2 准备数组sell  存储执行第(i+1)次sell操作后手上的现金
        int[] moneyInHandAfterSell = new int[k];
        
        // 3 开始遍历每一天的股价 start the traversal
        for (int i = 0; i < stockValuesThroughDays.length; i++) {
            for (int j = 0; j < k; j++) {
                // 如果今天(第i天)买入的话，说明xxx。则：记录 第(j+1)次buy操作后手上的现金值为xxx // 今天的买入金额为xxx
                // 如果是第一次交易，则 第(j+1)次buy操作 后手上的现金为：0 - prices[i] // 当前的股价
                // 如果不是第一次交易，则 第(j+1)次buy操作 后手上的现金为：当前手上的现金 - 当前buy股票的股价
                moneyInHandAfterBuy[j] = Math.max(moneyInHandAfterBuy[j], // EXPR2: this is j rather than i
                        j == 0
                                ? 0 - stockValuesThroughDays[i]
                                : moneyInHandAfterSell[j - 1] - stockValuesThroughDays[i]);

                // 如果是今天(第i天)卖出的话，说明xxx。则记录 第(j+1)次sell操作后，手上的现金值为xxx
                // 执行第(j+1)次卖出操作后，手上的现金 = 执行第(j+1)次buy操作后手上的现金 + 当前的股价
                moneyInHandAfterSell[j] = Math.max(moneyInHandAfterSell[j], moneyInHandAfterBuy[j] + stockValuesThroughDays[i]);
            }
        }

        /* Ⅲ 返回最后一次交易后手上的现金 */
        return moneyInHandAfterSell[k - 1];
    }
}

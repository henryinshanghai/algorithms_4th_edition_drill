package com.henry.leetcode_traning_camp.week_06.day05;

import java.util.Arrays;

public class Solution_buyAndSellWithKTransaction_04_categorize_by_jayatiTiwari {
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
        /* Ⅰ case01：k比较大的时候，相当于没有交易次数上的限制 */
        int profit = 0;
        if (k >= len / 2) { // 这里的限制为什么是 len / 2?
            for (int i = 0; i < len - 1; i++) {
                // 只要有赚钱的机会，就把利润添加进来
                if (stockValuesThroughDays[i] < stockValuesThroughDays[i + 1]) {
                    profit += stockValuesThroughDays[i + 1] - stockValuesThroughDays[i];
                }
            }

            return profit;
        }

        /* Ⅱ case02:k比较小的时候，需要通过动态更新buy 与 sell操作来确定最终的profit */
        // 1 准备数组buy  存储执行第(i+1)次buy操作后手上的现金
        int[] buy = new int[k];
        Arrays.fill(buy, Integer.MIN_VALUE); // 初始化buy[]数组中的元素值为 最小值————方便使用更大值来更新元素值

        // 2 准备数组sell  存储执行第(i+1)次sell操作后手上的现金
        int[] sell = new int[k]; // 初始化手上的现金流为0

        // 3 开始遍历每一天的股价 start the traversal
        for (int i = 0; i < stockValuesThroughDays.length; i++) {
            /* 对于任意一天的股价，我们更新所有的买入与卖出值 */
            // do what? for any particular current day, we update all the buy and sell value(for that day???)
            // no, the decision you make today will have an impact to other days' decision.
            // that is to say, they are interdependent. so we need to update not just for today. but for all days' profit
            // see 2 transaction problem to compare more

            System.out.println("===== 第" + i + "天开始👇 ===========" );
            // because they are relative to each other.
            // if we try to minimize all of them as discussed in the other problem with two transaction,
            // we can finally say, okay, the biggest profit of k transaction will be this much
            // 因为每次交易之间会相互影响，所以要找到最佳的交易 需要“过程中持续更新，最终在结果出取值”
            for (int j = 0; j < k; j++) {
                // 如果今天(第i天)买入的话，说明xxx。则：记录 第(j+1)次buy操作后手上的现金值为xxx // 今天的买入金额为xxx
                // 如果是第一次交易，则 第(j+1)次buy操作 后手上的现金为：0 - prices[i] // 当前的股价
                // 如果不是第一次交易，则 第(j+1)次buy操作 后手上的现金为：当前手上的现金 - 当前buy股票的股价
                buy[j] = Math.max(buy[j],
                        j == 0
                                ? 0 - stockValuesThroughDays[i]
                                : sell[j - 1] - stockValuesThroughDays[i]);

                // 如果是今天(第i天)卖出的话，说明xxx。则记录 第(j+1)次sell操作后，手上的现金值为xxx
                // 执行第(j+1)次卖出操作后，手上的现金 = 执行第(j+1)次buy操作后手上的现金 + 当前的股价
                sell[j] = Math.max(sell[j], buy[j] + stockValuesThroughDays[i]);
//                sell[j] = buy[j] + stockValuesThroughDays[i]; // EXPR1：没有Math.max() 会导致最终结果不正确

                System.out.println("第" + (j+1) + "次交易的买入价格为：" + buy[j] + ",卖出价格为：" + sell[j]);
            }

            System.out.println("===== 第" + i + "天结束👆 ===========" );
            System.out.println();
        }

        System.out.println("buy[]数组的值为： " + Arrays.toString(buy));
        System.out.println("sell[]数组的值为： " + Arrays.toString(sell));

        /* Ⅲ 返回最后一次交易后手上的现金 */
        return sell[k - 1];
    }
} /* not quite following... */
// {3,2,6,5,0,3};
// 有点子反常理，因为某一天怎么能够进行两次交易呀？
// 可以认为 最优的买入 与 最优的卖出操作 被分布在不同的day。
// 为了记录下最佳买入的day_buy 与 最佳卖出的day_sell，所以需要把它们分别存储到一个数组中

/*
现在的做法：

 */

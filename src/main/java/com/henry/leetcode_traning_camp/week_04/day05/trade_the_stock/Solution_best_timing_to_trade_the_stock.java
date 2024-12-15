// 概念：持有股票、不持有股票
// 假设：每一天 都有 这一天的最大利润（最优子结构） - 这样最后一天的利润 才会是 最大利润；
// 原理：任何情况下，卖出股票 总会得到 相比于 持有股票 更大的利润。
// 手段：从所有可能的情况中，选取较大值 作为当前元素的值；
class Solution_best_timing_to_trade_the_stock {
    public static void main(String[] args) {
        int[] consecutivePriceSequence = {7, 1, 5, 3, 6, 4};
        int maxProfit = maxProfitFrom(consecutivePriceSequence);

        System.out.println("单笔交易所能得到的最大利润是： " + maxProfit);
    }

    // 实现1：二维数组dp[i][j] 来 存储 第i天(交易完成后,手中状态为j时)所能够得到的最大利润
    // i表示 当前是第几天;
    // 0表示 不持有股票的状态; 1表示 持有股票的状态;
    public static int maxProfitFrom(int[] priceSequence) {
        // 创建二维数组 来 存储状态
        int daysAmountInSequence = priceSequence.length;
        int[][] currentDayToItsMaxProfitWhenStatusIs = new int[daysAmountInSequence][2];

        // 初始状态
        currentDayToItsMaxProfitWhenStatusIs[0][0] = 0; // 如果第0天不持有股票，说明 要么是 买入股票后又卖出，要么是 没有进行股票交易，则：收益为0
        currentDayToItsMaxProfitWhenStatusIs[0][1] = -priceSequence[0]; // 如果第0天持有股票([1])，说明 当天买入了股票，则：收益为 -股票的价格

        // 对于股票价格序列中的每一天...
        for (int currentDayNo = 1; currentDayNo < daysAmountInSequence; ++currentDayNo) {
            // #1 计算这一天 "不持有"股票的状态下的最大利润
            currentDayToItsMaxProfitWhenStatusIs[currentDayNo][0] = Math.max(currentDayToItsMaxProfitWhenStatusIs[currentDayNo - 1][0], // option1： 前一天就”不持有“股票
                    currentDayToItsMaxProfitWhenStatusIs[currentDayNo - 1][1] + priceSequence[currentDayNo]); // option2： 前一天持有股票，但今天”卖出“股票
            // #2 同时计算这一天 “持有股票”状态下的最大利润
            currentDayToItsMaxProfitWhenStatusIs[currentDayNo][1] = Math.max(currentDayToItsMaxProfitWhenStatusIs[currentDayNo - 1][1], // option1 前一天就”持有“股票
                    currentDayToItsMaxProfitWhenStatusIs[currentDayNo - 1][0] - priceSequence[currentDayNo]); // option2 前一天”不持有“股票，但今天”买入“股票
        }

        // 打印计算得到的dp[][]数组
        print(currentDayToItsMaxProfitWhenStatusIs);

        // #1 卖出股票收益高于持有股票收益，因此取[0]
        // #2 越是后面的元素，元素值就会更高 因此这里是 currentDayToItsMaxProfitWhenStatusIs[sequenceDay]
        return currentDayToItsMaxProfitWhenStatusIs[daysAmountInSequence - 1][0];
    }

    private static void print(int[][] currentDayToItsMaxProfit) {
        for (int[] currentDaysMaxProfits : currentDayToItsMaxProfit) {
            for (int maxProfitUnderCurrentStatus : currentDaysMaxProfits) {
                System.out.print(maxProfitUnderCurrentStatus + " ");
            }

            System.out.println();
        }
    }
}
// 概念：持有股票、不持有股票
// 原理：最后一天是不持有股票的状态时，才会获得最大的利润。
// 手段：从所有可能的情况中，选取较大值 作为当前元素的值
class Solution_best_timing_to_trade_the_stock {
    public static void main(String[] args) {
        int[] priceSequence = {7, 1, 5, 3, 6, 4};
        int maxProfit = maxProfitFrom(priceSequence);

        System.out.println("单笔交易所能得到的最大利润是： " + maxProfit);
    }

    // 实现1：二维数组存储
    // 可以将每天持有与否的情况分别用 dp[i][0] 和 dp[i][1] 来进行存储
    // 时间复杂度：O(n)，空间复杂度：O(n)
    public static int maxProfitFrom(int[] priceSequence) {
        int sequenceDays = priceSequence.length;
        int[][] dp = new int[sequenceDays][2];     // 创建二维数组存储状态

        // 初始状态
        dp[0][0] = 0; // [x][0] 表示”持有“股票
        dp[0][1] = -priceSequence[0]; // [x][1]表示”不持有“股票

        for (int currentDayNo = 1; currentDayNo < sequenceDays; ++currentDayNo) {
            // 第 currentDayNo 天，"不持有"股票
            dp[currentDayNo][0] = Math.max(dp[currentDayNo - 1][0], // #1 前一天就”不持有“股票
                    dp[currentDayNo - 1][1] + priceSequence[currentDayNo]); // #2 前一天持有股票，但今天”卖出“股票
            // 第 currentDayNo 天，”持有“股票
            dp[currentDayNo][1] = Math.max(dp[currentDayNo - 1][1], // #1 前一天就”持有“股票
                    dp[currentDayNo - 1][0] - priceSequence[currentDayNo]); // #2 前一天”不持有“股票，但今天”买入“股票
        }

        print(dp);

        // #1 卖出股票收益高于持有股票收益，因此取[0]
        // #2 越是后面的元素，元素值就会更高 因此这里是 dp[sequenceDay]
        return dp[sequenceDays - 1][0];
    }

    private static void print(int[][] dp) {
        for (int[] currentRow : dp) {
            for (int currentItem : currentRow) {
                System.out.print(currentItem + " ");
            }

            System.out.println();
        }
    }
}
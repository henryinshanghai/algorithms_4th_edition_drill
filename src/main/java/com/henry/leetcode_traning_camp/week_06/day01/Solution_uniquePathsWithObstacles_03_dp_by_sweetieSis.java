package com.henry.leetcode_traning_camp.week_06.day01;

public class Solution_uniquePathsWithObstacles_03_dp_by_sweetieSis {
    public static void main(String[] args) {
        int[][] map = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };

        int pathCounts = uniquePathsWithObstacles(map);
        System.out.println("在二维数组中，满足条件的路径有" + pathCounts + "个");
    }

    private static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        /* 〇 参数判空 */
        if (obstacleGrid == null || obstacleGrid.length == 0) {
            return 0;
        }

        /* Ⅰ 定义 dp 数组并初始化第 1 行和第 1 列。 */
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m && obstacleGrid[i][0] == 0; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n && obstacleGrid[0][j] == 0; j++) {
            dp[0][j] = 1;
        }

        /* Ⅱ 根据状态转移方程 dp[i][j] = dp[i - 1][j] + dp[i][j - 1] 进行递推。 */
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 0) { // EXPR1:其实比起 unique-path 的代码就只多了这一个if判断语句
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                } // 二维数组元素默认绑定的初始值为0
            }
        }

        /* Ⅲ 返回二维数组中满足提议要求的元素（二维数组中的其他元素都是为了能够记录DP的中间状态值）*/
        return dp[m - 1][n - 1];
    }
}

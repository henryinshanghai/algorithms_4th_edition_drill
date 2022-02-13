package com.henry.leetcode_traning_camp.week_06.day01;

public class Solution_uniquePathsWithObstacles_03_dp_by_sweetieSis_drill01 {
    public static void main(String[] args) {

        int[][] map = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };

        int pathCounts = uniquePathsWithObstacles(map);
        System.out.println("在二维数组中，满足条件的路径有" + pathCounts + "个"); // 2
    }

    private static int uniquePathsWithObstacles(int[][] map) {
        /* 〇 参数判空 */
        if (map == null || map.length == 0) {
            return 0;
        }

        /* Ⅰ 定义 dp 数组并初始化第 1 行和第 1 列。 */
        int rows = map.length;
        int cols = map[0].length;
        int[][] dpTable = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            dpTable[0][i] = 1;
        }
        for (int i = 0; i < cols; i++) {
            dpTable[i][0] = 1;
        }

        /* Ⅱ 根据状态转移方程 dp[i][j] = dp[i - 1][j] + dp[i][j - 1] 进行递推。 */
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (map[i][j] == 0) { // 判断当前方格是不是一个空地，如果是，说明可以走这个方格，则：计算它的dpTable[][]元素值
                    dpTable[i][j] = dpTable[i - 1][j] + dpTable[i][j - 1];
                }
            }
        }

        /* Ⅲ 返回二维数组dpTable中满足提议要求的元素（二维数组中的其他元素都是为了能够记录DP的中间状态值）*/
        return dpTable[rows-1][cols - 1];
    }
} // EXPR: dpTable[][]一般需要单独创建一个新的对象
// 参考：https://leetcode-cn.com/problems/unique-paths-ii/solution/jian-dan-dpbi-xu-miao-dong-by-sweetiee/
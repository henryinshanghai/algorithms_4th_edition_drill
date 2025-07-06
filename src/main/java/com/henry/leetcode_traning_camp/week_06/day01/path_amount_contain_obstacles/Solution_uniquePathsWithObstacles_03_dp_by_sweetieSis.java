package com.henry.leetcode_traning_camp.week_06.day01.path_amount_contain_obstacles;

// 验证：对于 在有障碍物的棋盘上从起始方格按照特定规则走到结束方格的走法数量 这样的问题，可以使用 动态规划的思路 来 得出答案；
// 逆向思维：想要到达 当前方格，按照 题目规则的话，必须要先到达 其左侧方格 或者 其上方的方格。
// 因此 path_amount(current_grid) = path_amount(upper_one_grid) + path_amount(lefty_one_grid)
// 识别题目特征：最优子结构 - 一个问题的最优解 包含了 其子问题的最优解。
public class Solution_uniquePathsWithObstacles_03_dp_by_sweetieSis {
    public static void main(String[] args) {
        int[][] boardWithObstacles = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };

        int pathAmount = pathAmountTowardsTargetGrid(boardWithObstacles);
        System.out.println("在二维数组中，满足条件的路径有" + pathAmount + "个");
    }

    private static int pathAmountTowardsTargetGrid(int[][] boardWithObstacles) {
        /* 〇 参数判空 */
        if (boardWithObstacles == null || boardWithObstacles.length == 0) {
            return 0;
        }

        /* Ⅰ 定义 pathAmountTowardCurrentPosition 数组并初始化第 1 行和第 1 列。 */
        int rows = boardWithObstacles.length,
                columns = boardWithObstacles[0].length;

        int[][] pathAmountTowardCurrentPosition = new int[rows][columns];
        // 🐖 只有当方格是 非障碍方格(值为0)的时候，才会方格的值进行初始化
        // 初始化第一列的方格的值
        for (int currentRow = 0; currentRow < rows && boardWithObstacles[currentRow][0] == 0; currentRow++) {
            if(pathAmountTowardCurrentPosition[currentRow][0] == 1) break;
            pathAmountTowardCurrentPosition[currentRow][0] = 1;
        }
        // 初始化第一行的方格的值
        for (int currentColumn = 0; currentColumn < columns && boardWithObstacles[0][currentColumn] == 0; currentColumn++) {
            if(pathAmountTowardCurrentPosition[0][currentColumn] == 1) break;
            pathAmountTowardCurrentPosition[0][currentColumn] = 1;
        }

        /* Ⅱ 根据状态转移方程 pathAmountTowardCurrentPosition[i][j] = pathAmountTowardCurrentPosition[i - 1][j] + pathAmountTowardCurrentPosition[i][j - 1] 进行递推。 */
        for (int currentRow = 1; currentRow < rows; currentRow++) {
            for (int currentColumn = 1; currentColumn < columns; currentColumn++) {
                // 如果当前方格的值为0，说明它是一个非障碍方格，因此 它是“由起点方格开始可达的”
                if (boardWithObstacles[currentRow][currentColumn] == 0) { // EXPR1:其实比起 unique-path 的代码就只多了这一个if判断语句
                    // 则：更新 dp[][]数组在此位置上的元素值
                    pathAmountTowardCurrentPosition[currentRow][currentColumn]
                            = pathAmountTowardCurrentPosition[currentRow - 1][currentColumn] // 左边前一个方格的元素值
                            + pathAmountTowardCurrentPosition[currentRow][currentColumn - 1]; // 上面一个方格的元素值
                } else {
                    // 虽然二维数组元素默认绑定的初始值为0，但是 当是障碍物方格时，我们还是显式地将之设置为0.
                    pathAmountTowardCurrentPosition[currentRow][currentColumn] = 0;
                }
            }
        }

        /* Ⅲ 返回二维数组中满足题意要求的元素（也就是 二维数组右下角的元素）*/
        // 🐖 二维数组中的其他元素 都是为了 能够记录DP的中间状态值
        return pathAmountTowardCurrentPosition[rows - 1][columns - 1];
    }
}

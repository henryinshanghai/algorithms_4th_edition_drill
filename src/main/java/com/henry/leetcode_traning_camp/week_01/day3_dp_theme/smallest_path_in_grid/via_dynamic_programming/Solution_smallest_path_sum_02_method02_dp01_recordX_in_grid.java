package com.henry.leetcode_traning_camp.week_01.day3_dp_theme.smallest_path_in_grid.via_dynamic_programming;

// 验证：对于 在矩形方格中找最小路径 && 走法限定在向下与向右 的问题，可以使用 动态规划的方式 来 找到 到达终点的最小路径
public class Solution_smallest_path_sum_02_method02_dp01_recordX_in_grid {
    public static void main(String[] args) {
        int[][] grid = {
                {1, 3, 1}, {1, 5, 1}, {4, 2, 1}
        };

        int minPathSum = minPathSum(grid);

        System.out.println("给定二维数组中满足条件的路径的元素加和值为： " + minPathSum);
    }

    // 动态规划的解法
    // 手段：计算得到X（到当前位置的最小路径的元素和）的二维数组
    // dp[][]的定义：dp[i][j] 从起始方格到达(i, j)方格的最小路径的路径值
    // 递推公式：dp[i][j] = current_value + min(dp[i-1][j], dp[i][j-1])
    // 初始化：用于触发递推公式；
    // 遍历顺序：上一个位置 - 当前位置 - 下一个位置; 因为只能 向右 或者 向下，所以顺序是 从左往右、从上往下
    // dp[][]数组的打印：for debug purpose
    private static int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        for (int currentRow = 0; currentRow < rows; currentRow++) {
            for (int currentColumn = 0; currentColumn < cols; currentColumn++) {
                if(currentRow == 0 && currentColumn == 0) {
                    // 只需要初始化 左上角方格的dp值即可
                    grid[currentRow][currentColumn] = grid[0][0]; // 这里相当于重新赋值了...
                }
                else if (currentRow == 0) {
                    // dp[][]首行元素的递推公式
                    grid[currentRow][currentColumn] = grid[currentRow][currentColumn] + grid[currentRow][currentColumn - 1];
                } else if (currentColumn == 0) { // currentRow != 0 &&
                    // dp[][]首列元素的递推公式
                    grid[currentRow][currentColumn] = grid[currentRow][currentColumn] + grid[currentRow - 1][currentColumn];
                } else {
                    // dp[][]任意位置元素的递推公式
                    grid[currentRow][currentColumn] = grid[currentRow][currentColumn] + Math.min(grid[currentRow][currentColumn - 1], grid[currentRow - 1][currentColumn]);
                }
            }
        }

        // 返回dp[][]右下角元素的值
        return grid[rows - 1][cols - 1];
    }
}

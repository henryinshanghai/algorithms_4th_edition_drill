package com.henry.leetcode_traning_camp.week_01.day3_dp_theme;

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
    private static int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int rows = grid.length;
        int cols = grid[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(i == 0 && j == 0) grid[i][j] = grid[0][0]; // 这里相当于重新赋值了...
                else if (i == 0) { // && j != 0
                    grid[i][j] = grid[i][j] + grid[i][j - 1];
                } else if (j == 0) { // i != 0 &&
                    grid[i][j] = grid[i][j] + grid[i - 1][j];
                } else {
                    grid[i][j] = grid[i][j] + Math.min(grid[i][j - 1], grid[i - 1][j]);
                }
            }
        }

        return grid[rows - 1][cols - 1];
    }
}

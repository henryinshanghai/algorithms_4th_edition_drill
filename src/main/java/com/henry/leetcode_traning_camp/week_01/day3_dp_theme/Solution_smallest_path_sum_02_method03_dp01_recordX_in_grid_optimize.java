package com.henry.leetcode_traning_camp.week_01.day3_dp_theme;

public class Solution_smallest_path_sum_02_method03_dp01_recordX_in_grid_optimize {
    public static void main(String[] args) {
        int[][] grid = {
                {1, 3, 1}, {1, 5, 1}, {4, 2, 1}
        };

        int minPathSum = minPathSum(grid);

        System.out.println("给定二维数组中满足条件的路径的元素加和值为： " + minPathSum);
    }

    private static int minPathSum(int[][] grid) {
        // 计算第一列的X
        for(int i=1; i<grid.length; i++) {
            grid[i][0] = grid[i-1][0] + grid[i][0];
        }

        // 计算第一行的X
        for(int j=1; j<grid[0].length; j++) {
            grid[0][j] = grid[0][j-1] + grid[0][j];
        }

        // 双重循环 省掉已经计算的部分
        for(int i=1; i<grid.length; i++) {
            for(int j=1; j<grid[0].length; j++) {
                // 计算X的通用公式 注：由于特殊的边界情况已经单独求解，这里就不再需要额外判断了
                grid[i][j] = Math.min(grid[i-1][j], grid[i][j-1]) + grid[i][j];
            }
        }

        // 返回grid[][]右下角元素
        return grid[grid.length-1][grid[0].length-1];
    }
}
/*
    优化手段：把双重循环中的if/else分支拿到循环体外单独计算。
    虽然算法的时间复杂度没有降低，但是运行耗时变小了许多

    可能是在循环中执行判断操作本身就有较大的成本，所以如果可以避免尽量避免

    X：到达当前位置(i, j)的最小路径的元素和；
    使用grid[][]原始数组来存放X，这样会破坏原始数组，但是免去了dist[][]的额外空间
 */

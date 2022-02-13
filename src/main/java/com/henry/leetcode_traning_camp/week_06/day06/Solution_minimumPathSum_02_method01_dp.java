package com.henry.leetcode_traning_camp.week_06.day06;

public class Solution_minimumPathSum_02_method01_dp {
    public static void main(String[] args) {
        int[][] grid = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}
        };

        int sumOfPath = minimumPathSum(grid);

        System.out.println("矩阵中从左上角到右下角的最小路径的加和值等于： " + sumOfPath);
    }

    private static int minimumPathSum(int[][] grid) {
        // 〇
        if (grid == null || grid.length == 0) {
            return 0;
        }

        // Ⅰ prepare dpTable[]
        int[][] dpTable = new int[grid.length][grid[0].length];

        // Ⅱ populate the dpTable[] with the rules that provide in problem description
        for (int i = 0; i < dpTable.length; i++) {
            for (int j = 0; j < dpTable[i].length; j++) {
                // this ain't seems a initialization to me
                dpTable[i][j] += grid[i][j];

                if (i > 0 && j > 0) {
                    dpTable[i][j] += Math.min(dpTable[i - 1][j], dpTable[i][j - 1]);
                } else if (i > 0) {
                    dpTable[i][j] += dpTable[i - 1][j];
                } else if (j > 0) {
                    dpTable[i][j] += dpTable[i][j - 1];
                }

            }
        }

        return dpTable[dpTable.length - 1][dpTable[0].length - 1];
    }
}

// reference: https://www.youtube.com/watch?v=ItjZdu6jEMs
package com.henry.leetcode_traning_camp.week_04.day04;

public class Solution_numIslands_01_method01_dfs_for_each_grid {
    public static void main(String[] args) {
        char[][] grid = {
                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}
        };

        int number = numIsLands(grid);
        System.out.println("给定的二维数组中有 " + number + "个岛屿");
    }

    private static int numIsLands(char[][] grid) {
        int count = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    count++;

                    helper(grid, i, j);
                }
            }
        }

        return count;
    }

    private static void helper(char[][] grid, int i, int j) {
        // goal / base case 索引越界 或者 网格不是陆地
        if ((i < 0) || (j < 0)
            || (i > grid.length)
            || (j > grid[0].length)
            || (grid[i][j] != '1')) {
            return;
        }

        // 本级递归要做的事情    注：递归方法在执行时总是DFS
        grid[i][j] = '*';
        helper(grid, i, j + 1);
        helper(grid, i, j - 1);
        helper(grid, i + 1, j);
        helper(grid, i - 1, j);
    }
}

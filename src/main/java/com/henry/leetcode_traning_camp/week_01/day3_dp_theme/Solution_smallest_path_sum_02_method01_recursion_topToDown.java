package com.henry.leetcode_traning_camp.week_01.day3_dp_theme;

public class Solution_smallest_path_sum_02_method01_recursion_topToDown {
    public static void main(String[] args) {
//        [
//          [1,3,1],
//          [1,5,1],
//          [4,2,1]
//        ]
        int[][] grid = {{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};

        int minPathSum = minPathSum(grid);

        System.out.println("给定二维数组中满足条件的路径的元素加和值为： " + minPathSum);

    }

    // 作用：找到给定二维数组中满足条件的路径，并返回路径的元素加和值
    private static int minPathSum(int[][] grid) {
        if (grid == null) return 0;

        int row = grid.length - 1;
        int col = grid[0].length - 1;

        return minPathSumToGivenDest(grid, row, col);
    }

    private static int minPathSumToGivenDest(int[][] grid, int row, int col) {
        // 1
        if (row == 0 && col == 0) return grid[0][0];

        // 2
        int minPathSum = -1;
        if(row == 0) minPathSum = grid[row][col] + minPathSumToGivenDest(grid, row, col - 1);
        else if (col == 0) minPathSum = grid[row][col] + minPathSumToGivenDest(grid, row - 1, col);
        else { // expr: 参考leetcode插件 #64    引用：为了使各个语句块能够并列，这里必须使用if... else if ... else的语法————这是使用变量minPathSum所需要做出的代码调整
            minPathSum = grid[row][col] +
                    Math.min(minPathSumToGivenDest(grid, row - 1, col),
                            minPathSumToGivenDest(grid, row, col - 1)
                    );
        }

        return minPathSum;
    }
} // 由于递归方式中有很多重复的计算（参考递归树），因此这种方法其实会超时（time limited）

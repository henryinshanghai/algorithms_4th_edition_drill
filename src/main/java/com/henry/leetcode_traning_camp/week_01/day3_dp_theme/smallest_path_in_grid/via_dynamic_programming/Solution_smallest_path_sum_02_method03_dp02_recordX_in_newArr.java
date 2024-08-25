package com.henry.leetcode_traning_camp.week_01.day3_dp_theme.smallest_path_in_grid.via_dynamic_programming;

public class Solution_smallest_path_sum_02_method03_dp02_recordX_in_newArr {
    public static void main(String[] args) {
        int[][] grid = {
                {1, 3, 1}, {1, 5, 1}, {4, 2, 1}
        };

        int minPathSum = minPathSum(grid);

        System.out.println("给定二维数组中满足条件的路径的元素加和值为： " + minPathSum);
    }

    private static int minPathSum(int[][] grid) {
        if(grid == null || grid.length == 0) return 0;

        int m = grid.length;
        int n = grid[0].length;

        // 准备一个二维数组 用于存储X - 到当前位置(i,j)的最小路径的元素和X
        int[][] dist = new int[m][n];

//        // 计算第一列的X
//        for(int i=1; i<grid.length; i++) {
//            grid[i][0] = grid[i-1][0] + grid[i][0];
//        }
//
//        // 计算第一行的X
//        for(int j=1; j<grid[0].length; j++) {
//            grid[0][j] = grid[0][j-1] + grid[0][j];
//        }

        for(int x = 0; x < m; x++){ // 0
            for(int y = 0; y < n; y++){ // 0

                if(x == 0 && y == 0){ // 如果是是起点位置，说明路径刚刚出发。则：...
                    // X就等于(i, j)位置的值
                    dist[0][0] = grid[0][0];
                }else{
                    // 使用X的通用公式计算X，并绑定到dist[][]数组的对应位置
                    dist[x][y] = Math.min(getDist(dist, x-1, y), getDist(dist, x, y-1))  + grid[x][y];
                }

//                dist[x][y] = Math.min(grid[x-1][y], grid[x][y-1])  + grid[x][y];
            }
        }

        // 返回dist[][]数组的右下角元素
        return dist[m-1][n-1];
    }

    // 作用：获取到dist[][]数组指定位置(i, j)上的元素值
    // 特征：在这个方法中进行边界处理
    private static int getDist(int[][] dist, int x, int y){
        /* 边界处理的手段：如果超过边界，就返回一个超级大的值 */
        // 如果出现参数小于0的情况...   aka 边界处理
        if(x < 0 || y < 0){
            // 则返回Integer.MAX_VALUE 作用：使这种情况在min()方法中不起作用
            return Integer.MAX_VALUE;
        }

        // 返回dist[][]数组中的元素值
        return dist[x][y];
    }
}
/*
    1 创建一个新的数组dist来存储X，避免了对grid原始数组的修改
    2 在处理边界时，使用了新的手段（而不是在循环中添加if/else if子句）
 */

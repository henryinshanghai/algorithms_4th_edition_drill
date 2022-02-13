package com.henry.leetcode_traning_camp.week_06.day02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution_triangle_01_method03_dp_bySweetiee {
    public static void main(String[] args) {
        // 如何能够创建一个表示 元素为列表的列表 的对象？
        List<List<Integer>> triangle = new ArrayList<>();

        triangle.add(new ArrayList<Integer>(Arrays.asList(2)));
        triangle.add(new ArrayList<Integer>(Arrays.asList(3, 4)));
        triangle.add(new ArrayList<Integer>(Arrays.asList(6, 5, 7)));
        triangle.add(new ArrayList<Integer>(Arrays.asList(4, 1, 8, 3)));

        int minimumSum = minimumTotal(triangle);

        System.out.println("按照特定规则在三角形中找到的最小路径的数值和为： " + minimumSum);
    }

    /*
    从自顶向下的递归 => 自底向上的递推
    DP三步曲：
        1、状态定义：
        dp[i][j]： dp[i][j] 表示从点(i, j)到底边的最小路径和。

        2、状态转移/DP方程：
        dp[i][j] = min(dp[i + 1][j], dp[i + 1][j + 1]) + triangle[i][j]dp[i][j]=min(dp[i+1][j],dp[i+1][j+1])+triangle[i][j]

     */
    private static int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();

        // dp[i][j] 表示从点 (i, j) 到底边的最小路径和。
        int[][] dp = new int[n + 1][n + 1];

        // 从三角形的最后一行开始递推。
        for (int i = n - 1; i >= 0; i--) { // 遍历当前行
            for (int j = 0; j <= i; j++) { // 遍历当前行中所有的列
                // 计算并填充二维数组中的元素
                dp[i][j] = Math.min(dp[i + 1][j], dp[i + 1][j + 1]) + triangle.get(i).get(j);
            }
        }
        return dp[0][0];
    }
}
/*
时间复杂度：O(N^2)，N 为三角形的行数。
空间复杂度：O(N^2)，N 为三角形的行数。

对于空间还可以进一步优化~ 因为在计算的过程中，不需要行信息i
 */

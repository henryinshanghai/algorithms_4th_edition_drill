package com.henry.leetcode_traning_camp.week_06.day02.min_path_in_triangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 验证：对于最小路径和这样的问题，可以使用 动态规划的手段 来 得到答案
// dp[][]数组以及下标的含义：当前位置 -> 由此位置到达最后一行的最小路径的值 => currentPositionToMinPathStartFromIt
// 递推公式 dp[current_row][current_column] = current_item + min(dp[i+1][j], dp[i+1][j+1]  表明：我们需要从下往上地生成dp[]数组
public class Solution_via_dp_by_Sweetiee {
    public static void main(String[] args) {
        // 如何能够创建一个表示 元素为列表的列表 的对象？
        List<List<Integer>> triangle = initTriangle();
        int minPathValue = getMinPathTowardsEndIn(triangle);
        System.out.println("按照特定规则在三角形中找到的最小路径的数值和为： " + minPathValue);
    }

    private static List<List<Integer>> initTriangle() {
        List<List<Integer>> triangle = new ArrayList<>();

        triangle.add(new ArrayList<Integer>(Arrays.asList(2)));
        triangle.add(new ArrayList<Integer>(Arrays.asList(3, 4)));
        triangle.add(new ArrayList<Integer>(Arrays.asList(6, 5, 7)));
        triangle.add(new ArrayList<Integer>(Arrays.asList(4, 1, 8, 3)));
        return triangle;
    }

    /*
    从自顶向下的递归 => 自底向上的递推
    DP三步曲：
        1、状态定义：
        dp[i][j]： dp[i][j] 表示从点(i, j)到底边的最小路径和。

        2、状态转移/DP方程：
        dp[i][j] = min(dp[i + 1][j], dp[i + 1][j + 1]) + triangle[i][j]
     */
    private static int getMinPathTowardsEndIn(List<List<Integer>> triangle) {
        int rowsAmount = triangle.size();

        // currentPositionToMinPathStartFromIt[i][j] 表示从点 (i, j) 到底边的最小路径和。
        int[][] currentPositionToMinPathStartFromIt = new int[rowsAmount + 1][rowsAmount + 1];

        // 从三角形的最后一行开始递推。
        // 为什么这里从最后一行开始递推?? 因为 只有最后一行才能作为递推的起点
        for (int currentRow = rowsAmount - 1; currentRow >= 0; currentRow--) { // 遍历当前行（从下往上）
            for (int currentColumn = 0; currentColumn <= currentRow; currentColumn++) { // 遍历当前行中所有的列(从左往右)
                // 计算并填充二维数组中的元素
                Integer currentItemValue = triangle.get(currentRow).get(currentColumn);

                currentPositionToMinPathStartFromIt[currentRow][currentColumn]
                        = currentItemValue +
                        Math.min(currentPositionToMinPathStartFromIt[currentRow + 1][currentColumn], // 下一行的同索引元素 => 因此要在创建数组时，预留出多余的一行
                                currentPositionToMinPathStartFromIt[currentRow + 1][currentColumn + 1]); // 下一个的 索引+1的元素
            }

            System.out.println("当前循环中所得到的dp[][]数组 👇");
            printArr(currentPositionToMinPathStartFromIt);
            System.out.println();
        }

        System.out.println("最终得到的dp[][]数组 👇");
        printArr(currentPositionToMinPathStartFromIt);
        return currentPositionToMinPathStartFromIt[0][0];
    }

    private static void printArr(int[][] twoDimensionArr) {
        for (int currentRow = 0; currentRow < twoDimensionArr.length; currentRow++) {
            for (int currentColumn = 0; currentColumn < twoDimensionArr[currentRow].length; currentColumn++) {
                System.out.print(twoDimensionArr[currentRow][currentColumn] + " ");
            }

            System.out.println();
        }
    }
}
/*
时间复杂度：O(N^2)，N 为三角形的行数。
空间复杂度：O(N^2)，N 为三角形的行数。

对于空间还可以进一步优化~ 因为在计算的过程中，不需要行信息i
 */

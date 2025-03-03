package com.henry.leetcode_traning_camp.week_06.preview.min_path_in_triangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 验证：如果使用二维dp[][]数组时发现 递推公式中的dp[i][j] 只与 其相邻行的元素值有关，则
// 可以通过压缩 行的维度/重复利用某一行的空间 来 得到 一维的dp[]数组，从而 节省空间。
// 原理 👇
//在实际递推中我们发现，计算 dp[i][j] 时，只用到了下一行的 dp[i+1][j] 和 dp[i+1][j+1]。
//因此 可以通过 重复使用单行中的位置 来 把二维dp[][]数组的行的维度压缩掉，从而得到 一维数组dp[]。
public class Solution_via_dp_space_optimal_by_sweetiee {
    public static void main(String[] args) {
//        int[][] triangle = {
//                {2},
//                {3, 4},
//                {6, 5, 7},
//                {4, 1, 8, 3}
//        };

        // 如何能够创建一个表示 元素为列表的列表 的对象？
        List<List<Integer>> triangle = new ArrayList<>();

        triangle.add(new ArrayList<Integer>(Arrays.asList(2)));
        triangle.add(new ArrayList<Integer>(Arrays.asList(3, 4)));
        triangle.add(new ArrayList<Integer>(Arrays.asList(6, 5, 7)));
        triangle.add(new ArrayList<Integer>(Arrays.asList(4, 1, 8, 3)));

        int minimumSum = minimumTotal(triangle);

        System.out.println("按照特定规则在三角形中找到的最小路径的数值和为： " + minimumSum);
    }


    private static int minimumTotal(List<List<Integer>> triangle) {
        int rowsAmount = triangle.size();
        int[] currentColumnToItsMinPathTowardsEnd = new int[rowsAmount + 1];

        for (int backwardsRowCursor = rowsAmount - 1; backwardsRowCursor >= 0; backwardsRowCursor--) {
            for (int currentColumnCursor = 0; currentColumnCursor <= backwardsRowCursor; currentColumnCursor++) {
                Integer currentItemValue = triangle.get(backwardsRowCursor).get(currentColumnCursor);

                // 使用一维dp[]数组时的递推公式：dp[backwardsRowCursor] =  Min(dp[backwardsRowCursor], dp[backwardsRowCursor+1]) + curVal
                // 🐖 相比于二维数组的递推公式，其实就是单纯地把行的维度i给去掉了
                currentColumnToItsMinPathTowardsEnd[currentColumnCursor]
                        = currentItemValue +
                        Math.min(currentColumnToItsMinPathTowardsEnd[currentColumnCursor], // option1: dp[]数组 当前列上 元素的值
                                currentColumnToItsMinPathTowardsEnd[currentColumnCursor + 1]); // option2: dp[]数组 下一列上 元素的值
            }
        }
        return currentColumnToItsMinPathTowardsEnd[0];
    }
}
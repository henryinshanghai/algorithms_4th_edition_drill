package com.henry.leetcode_traning_camp.week_06.day02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution_triangle_01_method02_recursionWithMemo_bySweetiee {
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

    // 准备一个二维数组 用作缓存，存储递归过程中计算的中间结果
    private static Integer[][] memo;

    private static int minimumTotal(List<List<Integer>> triangle) {
        memo = new Integer[triangle.size()][triangle.size()];

        // 从指定的坐标点(i, j)开始来扩展路径到三角形底部
        return dfs(triangle, 0, 0);
    }

    private static int dfs(List<List<Integer>> triangle, int i, int j) {
        // 递归的触底返回条件
        if (i == triangle.size()) {
            return 0;
        }

        // 在计算之前，先判断要计算的值在缓存中是不是已经存在了
        if (memo[i][j] != null) { // null是创建二维数组后，数组元素的默认值
            return memo[i][j];
        }

        // 计算路径和的值，并返回给上一级调用。并把本次计算的结果添加到用于缓存的数据结构中
        return memo[i][j] = Math.min(dfs(triangle, i + 1, j), dfs(triangle, i + 1, j + 1));
    }
}
/*
时间复杂度：O(N^2)
空间复杂度：O(N^2)
 */
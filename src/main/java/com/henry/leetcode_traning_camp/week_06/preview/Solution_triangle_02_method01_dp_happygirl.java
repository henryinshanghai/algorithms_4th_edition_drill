package com.henry.leetcode_traning_camp.week_06.preview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution_triangle_02_method01_dp_happygirl {
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
        /* 〇 */
        if (triangle.size() == 0) return 0;

        /* Ⅰ 获取到三角形的行数 */
        int rows = triangle.size();

        /* Ⅱ 准备一维的dpTable数组 */
        int[] dpTable = new int[rows + 1];

        /* Ⅲ 根据dpTable数组中的初始值来计算并填充dpTable中的每一个位置 */
        for (int i = rows - 1; i >= 0; i--) { // EXPR1：从下往上来扩展路径    作用：方便使用相邻节点的题设
            for (int j = 0; j <= i; j++) { // EXPR2：每一层需要处理的元素 与 当前层数level 有关

                // 递推公式：dp[i] =  Min(dp[i], dp[i+1]) + curVal
                dpTable[j] = triangle.get(i).get(j) +
                        Math.min(dpTable[j], dpTable[j + 1]);
            }

        }

        /* Ⅳ 返回dpTable数组中第一个元素的值 */
        return dpTable[0];
    }
}
/*
认了 想破脑袋也想不出这种做法的有效逻辑
 */

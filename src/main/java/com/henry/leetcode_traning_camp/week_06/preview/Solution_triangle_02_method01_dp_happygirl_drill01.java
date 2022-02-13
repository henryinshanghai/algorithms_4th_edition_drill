package com.henry.leetcode_traning_camp.week_06.preview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution_triangle_02_method01_dp_happygirl_drill01 {
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

    private static int minimumTotal(List<List<Integer>> triangle) {
        if (triangle.size() == 0) {
            return 0;
        }

        int rows = triangle.size();

        int[] dpTable = new int[rows + 1]; // TEMPLATE1: +1

        for (int i = rows - 1; i >= 0; i--) { // TEMPLATE2: rows-1
            for (int j = 0; j <= i; j++) { // TEMPLATE3: <= i
                dpTable[j] =
                        Math.min(dpTable[j], dpTable[j + 1])
                                + triangle.get(i).get(j);
            }
        }

        return dpTable[0];
    }
}

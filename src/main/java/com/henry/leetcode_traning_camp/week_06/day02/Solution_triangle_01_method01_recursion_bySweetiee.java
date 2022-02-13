package com.henry.leetcode_traning_camp.week_06.day02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution_triangle_01_method01_recursion_bySweetiee {
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

    /**
     * 返回在特定的三角形中，从第一行元素到最后一行元素的最小路径的元素和
     * @param triangle
     * @return
     */
    private static int minimumTotal(List<List<Integer>> triangle) {
        /*
            递归的过程总是深度优先的
            指定当前元素在三角形中的坐标(i, j)
         */
        return dfs(triangle, 0, 0);
    }

    private static int dfs(List<List<Integer>> triangle, int i, int j) {
        // 递归触底返回条件 行坐标i已经等于三角形的行数
        if (i == triangle.size()) {
            return 0;
        }

        return Math.min(dfs(triangle, i + 1, j), dfs(triangle, i + 1, j + 1)) + triangle.get(i).get(j);

    }
}
/*
    这种傻递归的过程中会有很多重复的计算；
    优化：在代码中添加一个用于缓存“已经计算过的值”的数据结构————数组、map等
    参考：method02
 */

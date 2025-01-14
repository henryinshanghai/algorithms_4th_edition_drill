package com.henry.leetcode_traning_camp.week_06.day02.min_path_in_triangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 验证：对于递归过程中出现的重复计算，可以使用一个数组 来 #1 在每次计算后，把计算结果存储到数组中； & #2 在每次计算前，判断“要计算的结果”是否已经存在
// 以此手段 来 避免可能的重复计算
public class Solution_via_recursion_with_memo_by_Sweetiee {
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

    // 准备一个二维数组 用作缓存，存储递归过程中计算的中间结果
    private static Integer[][] currentPositionToItsMinPath;

    private static int getMinPathTowardsEndIn(List<List<Integer>> triangle) {
        currentPositionToItsMinPath = new Integer[triangle.size()][triangle.size()];

        // 从指定的坐标点(i, j)开始来扩展路径到三角形底部
        return getMinPathStartWith(triangle, 0, 0);
    }

    private static int getMinPathStartWith(List<List<Integer>> triangle, int currentRow, int currentHorizontalCursor) {
        // 递归的触底返回条件
        if (currentRow == triangle.size()) {
            return 0;
        }

        // #1 在计算之前，先判断所要计算的值 在二维数组中是不是已经存在了
        // 如果存在，则直接返回，而不用再去计算了
        if (currentPositionToItsMinPath[currentRow][currentHorizontalCursor] != null) { // 🐖 null是创建二维数组后，数组元素的默认值
            return currentPositionToItsMinPath[currentRow][currentHorizontalCursor];
        }

        // 计 算路径和的值，并 返回给上一级调用。
        // #2 并 把 本次计算的结果 添加到 用于缓存的数据结构中
        Integer startItemInPath = triangle.get(currentRow).get(currentHorizontalCursor);
        return currentPositionToItsMinPath[currentRow][currentHorizontalCursor]
            = startItemInPath +
                Math.min(getMinPathStartWith(triangle, currentRow + 1, currentHorizontalCursor),
                        getMinPathStartWith(triangle, currentRow + 1, currentHorizontalCursor + 1));
    }
}
/*
时间复杂度：O(N^2)
空间复杂度：O(N^2)
 */
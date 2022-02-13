package com.henry.leetcode_traning_camp.week_04.day07;

import java.util.Arrays;

public class Solution_searchMatrix_04_method01_binarySearch_nickWhite {
    public static void main(String[] args) {
        int[][] matrix = {
                {1, 3, 5, 7},
                {10, 11, 16, 20},
                {23, 30, 34, 50}
        };

        int target = 100;

        boolean res = searchMatrix(matrix, target);

        System.out.println(target + "在二维数组" + Arrays.toString(matrix) + "中是否存在？" + res);
    }

    private static boolean searchMatrix(int[][] matrix, int target) {
        // 〇 corner case
        if (matrix.length == 0) return false;

        // Ⅰ 获取二维数组的行列情况
        int rows = matrix.length;
        int columns = matrix[0].length;

        /* Ⅱ 准备进行二分查找 */
        // 1 计算左、右边界
        int left = 0;
        int right = rows * columns - 1;

        // 2 准备while循环，直到：1 left与right边界相遇； OR 2 找到了target元素
        while (left <= right) { // EXPR1： <=
            // 2-1 计算数组的中间位置        技术处理：避免Integer Overflow
            int midPoint = left + (right - left) / 2; // EXPR2: left + (right - left) / 2;

            // 2-2 计算中间位置在二维数组中的坐标，并从坐标索引到二维数组中的元素
            /*
                // 如何根据在一维数组中的位置，找到其在二维数组中的位置？
                // ➗ 列数 = 在二维数组中的行
                // % 列数 = 在二维数组中的列
             */
            int midPoint_element = matrix[midPoint / columns][midPoint % columns];


            // 2-3 判断当前的中间位置上的元素是不是等于target
            if (midPoint_element == target) { // 如果是，直接返回元素值
                return true;
            } else if (target < midPoint_element) { // 如果不是调整区间边界
                right = midPoint - 1; // EXPR3：-1
            } else if (target > midPoint_element) {
                left = midPoint + 1; // EXPR4：+1
            }
        }

        // Ⅲ 没有查找到😳
        return false;

    }
}

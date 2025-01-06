package com.henry.leetcode_traning_camp.week_04.day07.search_target_in_square;

import java.util.Arrays;

// 验证：对于 在有序的二维矩阵中查找目标元素的问题，可以 把有序的二维矩阵 顺序连接成为 一个单一的一维有序数组，并对此数组进行二分查找
// 原理：一个行内有序&&行间有序的二维矩阵，可以 按行顺序连接成 一个有序的一维数组；
// 而在一个 有序的一维数组中进行二分查找 是简单的
public class Solution_searchMatrix_via_binarySearch_nickWhite {
    public static void main(String[] args) {
        int[][] itemMatrix = {
                {1, 3, 5, 7},
                {10, 11, 16, 20},
                {23, 30, 34, 50}
        };

        int targetItem = 100;

        boolean searchResult = searchWithinMatrix(itemMatrix, targetItem);
        System.out.println(targetItem + "在二维数组" + Arrays.toString(itemMatrix) + "中是否存在？" + searchResult);
    }

    private static boolean searchWithinMatrix(int[][] itemMatrix, int targetItem) {
        // 〇 corner case
        if (itemMatrix.length == 0) return false;

        // Ⅰ 获取二维数组的行列情况
        int rowAmount = itemMatrix.length;
        int columnAmount = itemMatrix[0].length;

        /* Ⅱ 准备进行二分查找 */
        // 1 计算左、右边界
        int leftBar = 0;
        int rightBar = rowAmount * columnAmount - 1;

        // 2 准备while循环，直到：1 left与right边界相遇； OR 2 找到了target元素
        while (leftBar <= rightBar) { // EXPR1： <=
            // 2-1 计算数组的中间位置        技术处理：避免Integer Overflow
            int middle_position = leftBar + (rightBar - leftBar) / 2; // EXPR2: leftBar + (rightBar - leftBar) / 2;

            // 2-2 计算中间位置在二维数组中的坐标，并从坐标索引到二维数组中的元素
            /*
                // 如何根据在一维数组中的位置，找到其在二维数组中的位置？
                // ➗ 列数 = 在二维数组中的行
                // % 列数 = 在二维数组中的列
             */
            int item_on_middle_position = itemMatrix[middle_position / columnAmount][middle_position % columnAmount];


            // 2-3 如果 当前的中间位置上的元素 等于target，说明 xxx
            if (item_on_middle_position == targetItem) {
                // 则：直接返回true 表示 targetItem 存在
                return true;
            } else if (targetItem < item_on_middle_position) { // 如果xxx, 说明ooo
                // 则：调整区间的右边界
                rightBar = middle_position - 1; // EXPR3：-1
            } else if (targetItem > item_on_middle_position) {
                // 则：调整区间的左边界
                leftBar = middle_position + 1; // EXPR4：+1
            }
        }

        // Ⅲ 没有查找到😳
        return false;

    }
}

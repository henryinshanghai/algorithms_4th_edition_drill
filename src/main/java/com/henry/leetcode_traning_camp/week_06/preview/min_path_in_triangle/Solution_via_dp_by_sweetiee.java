package com.henry.leetcode_traning_camp.week_06.preview.min_path_in_triangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// dp[][]数组的含义：当前位置 => 由当前位置开始，到最后一行结束位置结束的路径的最小sum；
// 最优子结构：由当前位置（当前行×当前列）出发的最小路径 = 当前元素 + 由当前位置的下一个位置(下一行×当前列/下一列) 出发的最小路径
// https://leetcode.cn/problems/triangle/solutions/329394/di-gui-ji-yi-hua-dp-bi-xu-miao-dong-by-sweetiee/
public class Solution_via_dp_by_sweetiee {
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
        int[][] currentSpotToItsMinPathSumTowardsEnd = new int[rows + 1][rows + 1];

        /* Ⅲ 根据dpTable数组中的初始值来计算并填充dpTable中的每一个位置 */
        // 🐖 由于最优子结构(dp[][]数组的递推公式)，索引 这里是从下往上地 来 构建最小路径的结果的
        for (int backwardsRowCursor = rows - 1; backwardsRowCursor >= 0; backwardsRowCursor--) { // EXPR1：从下往上来扩展路径    原因：递推公式 求当前dp[]元素 依赖于 其下一行的“计算过的dp[]元素”
            for (int currentColumnCursor = 0; currentColumnCursor <= backwardsRowCursor; currentColumnCursor++) { // EXPR2：每一层需要处理的元素 与 当前层数level 有关 🐖 这里从左往右 或是 从右往左都行
                // 当前位置上的元素值
                Integer currentItemValue = triangle.get(backwardsRowCursor).get(currentColumnCursor);

                currentSpotToItsMinPathSumTowardsEnd[backwardsRowCursor][currentColumnCursor] =
                        currentItemValue + // 当前元素的值
                        Math.min(currentSpotToItsMinPathSumTowardsEnd[backwardsRowCursor + 1][currentColumnCursor], // option1：dp[][]数组 下一行的相同列上的 元素的值
                                currentSpotToItsMinPathSumTowardsEnd[backwardsRowCursor + 1][currentColumnCursor + 1]); // option2: dp[][]数组 下一行的下一列上的 元素的值
            }
        }

        /* Ⅳ 返回dpTable数组中第一个元素的值 */
        return currentSpotToItsMinPathSumTowardsEnd[0][0];
    }
}
/*
认了 想破脑袋也想不出这种做法的有效逻辑
 */

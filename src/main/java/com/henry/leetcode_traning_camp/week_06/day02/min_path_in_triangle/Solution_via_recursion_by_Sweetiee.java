package com.henry.leetcode_traning_camp.week_06.day02.min_path_in_triangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 验证：对于 找到三角形矩阵中的最小路径的路径和 这样的问题，可以使用 递归的手段得到答案；
// 原理：可以用分而治之的思路 对原始问题进行拆解 => 路径 = 路径的首结点 + 子路径；
public class Solution_via_recursion_by_Sweetiee {
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

    /**
     * 返回在特定的三角形中，从第一行元素到最后一行元素的最小路径的元素和
     *
     * @param triangle
     * @return
     */
    private static int getMinPathTowardsEndIn(List<List<Integer>> triangle) {
        /*
            递归的过程总是深度优先的
            指定当前元素在三角形中的坐标(i, j)
         */
        return getMinPathStartWith(triangle, 0, 0);
    }


    private static int getMinPathStartWith(List<List<Integer>> triangle, int currentRow, int currentHorizontalCursor) {
        // 递归触底返回条件 行坐标i已经等于三角形的行数
        if (currentRow == triangle.size()) {
            return 0;
        }

        Integer startItemInPath = triangle.get(currentRow).get(currentHorizontalCursor);
        return startItemInPath + // 路径的起始元素的值
                Math.min(getMinPathStartWith(triangle, currentRow + 1, currentHorizontalCursor), // 以 下一行相同下标的元素 作为起始元素的最小路径
                        getMinPathStartWith(triangle, currentRow + 1, currentHorizontalCursor + 1)); // 以 下一行“下标+1的元素” 作为起始元素的最小路径

    }
}
/*
    这种傻递归的过程中会有很多重复的计算；
    优化：在代码中添加一个用于缓存“已经计算过的值”的数据结构————数组、map等
    参考：method02
 */

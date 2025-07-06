package com.henry.leetcode_traning_camp.week_06.day02.min_path_in_triangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 验证：对于动态规划，如果递推公式中 二维数组的元素值 仅仅依赖于 其相邻行的元素值，说明
// 我们可以通过复用一维数组的方式 来 节省空间，则：我们可以使用 与二维数组时候相同的遍历顺序 来 生成一维dp[]数组
public class Solution_via_dp_optimal_by_Sweetiee {
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

    /*
    空间优化的原理：
        在实际递推中我们发现，计算 dp[i][j]dp[i][j] 时，只用到了下一行的 dp[i + 1][j] 和 dp[i + 1][j + 1]。
        所以定义dpTable时，可以不用存储行信息i,而是不断更新 一维数组的元素值。
        最终一维数组的元素值 = 二维数组的元素 在竖直方向上的投影
     */
    private static int getMinPathTowardsEndIn(List<List<Integer>> triangle) {
        /* 〇 准备dpTable（根据情况空间适当+1，这样能够避免代码中多余的边界判断）*/
        int rowsAmount = triangle.size();
        // 和优化前的版本一样，这里的初始空间也可以设置成 rowsAmount，但会需要对应的改动：① for循环下标；② 数组元素的初始化
        int[] currentColumnToItsMinPathValue = new int[rowsAmount + 1];

        /* Ⅰ 遍历每一层的每一个元素，根据当前元素与其相邻子元素的关系来更新dpTable */
        for (int currentRow = rowsAmount - 1; currentRow >= 0; currentRow--) {
            System.out.println("当前循环开始");
            for (int currentColumn = 0; currentColumn <= currentRow; currentColumn++) { // 外层for循环一毛一样
                // 获取到 当前元素
                Integer currentItem = triangle.get(currentRow).get(currentColumn);

                System.out.println("原始矩阵中的当前元素： " + currentItem + ", dp[]数组的当前列元素：" + currentColumnToItsMinPathValue[currentColumn] + ", " +
                        "dp[]数组的下一个元素：" + currentColumnToItsMinPathValue[currentColumn + 1]);
                currentColumnToItsMinPathValue[currentColumn] = // 更新 dp[current_column]
                        currentItem + // 当前元素
                                Math.min(currentColumnToItsMinPathValue[currentColumn], // 选择 同下标结果
                                        currentColumnToItsMinPathValue[currentColumn + 1]); // 或 下标+1结果的较小值 进行加和
                System.out.println("经递推后，dp[]数组的当前元素为： " + currentColumnToItsMinPathValue[currentColumn]);
            }
            System.out.println("当前循环结束后，得到的dp数组如下 👇");
            printArr(currentColumnToItsMinPathValue);
            System.out.println();
        }

        System.out.println();
        System.out.println("最终得到的dp[]数组如下 👇");
        printArr(currentColumnToItsMinPathValue);

        /* Ⅱ 返回dpTable[]中符合题设条件的元素 */
        return currentColumnToItsMinPathValue[0];
    }

    private static void printArr(int[] currentRowToItsMinPathValue) {
        for (int currentRow = 0; currentRow < currentRowToItsMinPathValue.length; currentRow++) {
            System.out.print(currentRowToItsMinPathValue[currentRow] + " ");
        }
        System.out.println();
    }
}

package com.henry.leetcode_traning_camp.week_06.day02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution_triangle_01_method04_dpOptimal_bySweetiee {
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

    /*
    空间优化的原理：
        在实际递推中我们发现，计算 dp[i][j]dp[i][j] 时，只用到了下一行的 dp[i + 1][j] 和 dp[i + 1][j + 1]。
        所以定义dpTable时，可以不用存储行信息i aka 只需要一个维度
     */
    private static int minimumTotal(List<List<Integer>> triangle) {
        /* 〇 准备dpTable（根据情况空间适当+1，这样能够避免代码中多余的边界判断）*/
        int n = triangle.size();
        int[] dp = new int[n + 1];

        /* Ⅰ 遍历每一层的每一个元素，根据当前元素与其相邻子元素的关系来更新dpTable */
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= i; j++) { // 外层for循环一毛一样
                dp[j] = Math.min(dp[j], dp[j + 1]) + triangle.get(i).get(j);
            }
        }

        /* Ⅱ 返回dpTable[]中符合题设条件的元素 */
        return dp[0];
    }
}

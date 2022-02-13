package com.henry.leetcode_traning_camp.week_06.day01;

public class Solution_uniquePaths_02_method01_dp_byKavinNaughton {
    public static void main(String[] args) {
        int m = 3;
        int n = 7;

        int pathCounts = uniquePaths(m, n);

        System.out.println("在一个" + m + "*" + n + "的二维数组中，从" +
                "左上角方格到右下角方格的路径一共有" + pathCounts + "种");

    }

    private static int uniquePaths(int m, int n) {
        /* 〇 准备一个二维数组，用来作为dpTable */
        int[][] dpTable = new int[m][n];

        /* Ⅰ 初始化dpTable中的第一行与第一列元素的值为1————为递推求出其他元素的值做准备
        dpTable[i][j]的含义/状态定义：从左上角到达当前位置(i, j)一共有多少种走法
        */
        // 填充第一行的元素
        for (int i = 0; i < dpTable.length; i++) {
            dpTable[i][0] = 1;
        }

        // 填充第一列的元素
        for (int i = 0; i < dpTable[0].length; i++) {
            dpTable[0][i] = 1;
        }

        /* Ⅲ 按照题设规则进行递推，求出dpTable[]中其他元素的值 */
        for (int i = 1; i < dpTable.length; i++) {
            for (int j = 1; j < dpTable[i].length ; j++) {
                // 达到当前方格的走法 = 到达左边方格的走法 + 到达上面一个方格的走法
                dpTable[i][j] = dpTable[i - 1][j] + dpTable[i][j - 1];
            }

        }

        /* Ⅳ 返回dpTable[]中满足题意得元素值 */
        return dpTable[m - 1][n -1];
    }
}

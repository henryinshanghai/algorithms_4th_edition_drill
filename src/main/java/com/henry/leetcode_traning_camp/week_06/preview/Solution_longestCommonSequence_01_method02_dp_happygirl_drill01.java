package com.henry.leetcode_traning_camp.week_06.preview;

public class Solution_longestCommonSequence_01_method02_dp_happygirl_drill01 {
    public static void main(String[] args) {
        String str1 = "ABCDGHLQR";
        String str2 = "AEDPHR"; // ADHR  公共子序列的长度为4

//        int result = lcsDynamic(str1.toCharArray(), str2.toCharArray());

        int result = longestCommonSequence(str1, str2);
        System.out.println(str1 + " 与 " + str2 + "之间的最大公共序列的长度为：" + result);
    }

    private static int longestCommonSequence(String text1, String text2) {
        // EXPR1：确定需要创建的DP二维数组的大小
        // EXPR2：在创建DP数组时，多出一行 & 一列
        // EXPR3：在进行计算数组元素值时，从0开始
        // EXPR4：最后返回DP数组最右下角的元素值


        /* 〇 准备一些DP所需要的基础变量 */
        int rows_base = text1.length();
        int columns_base = text2.length();

        int[][] dpTable = new int[rows_base +1][columns_base + 1];

        /* Ⅰ 准备两层的嵌套循环    在循环中，填充二维数组中的每一个位置 */
        for (int i = 0; i < rows_base; i++) { // NEW EXPR:这里需要控制遍历的次数，以防止越界
            for (int j = 0; j < columns_base; j++) {
                char c1 = text1.charAt(i), c2 = text2.charAt(j);

                if (c1 == c2) {
                    dpTable[i + 1][j + 1] = dpTable[i][j] + 1;
                } else {
                    dpTable[i + 1][j + 1] = Math.max(dpTable[i + 1][j], dpTable[i][j + 1]);
                }
            }
        }

        /* Ⅱ 返回最大值 */
        return dpTable[rows_base][columns_base]; // 二维数组所能使用的索引值 比起 声明数组时使用的初始尺寸大小 要小1

    }
}

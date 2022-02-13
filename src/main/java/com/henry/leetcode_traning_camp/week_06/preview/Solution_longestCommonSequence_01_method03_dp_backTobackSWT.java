package com.henry.leetcode_traning_camp.week_06.preview;

public class Solution_longestCommonSequence_01_method03_dp_backTobackSWT {
    public static void main(String[] args) {
        String str1 = "ABCDGHLQR";
        String str2 = "AEDPHR"; // ADHR  公共子序列的长度为4

//        int result = lcsDynamic(str1.toCharArray(), str2.toCharArray());

        int result = longestCommonSequence(str1, str2);
        System.out.println(str1 + " 与 " + str2 + "之间的最大公共序列的长度为：" + result);

    }

    private static int longestCommonSequence(String text1, String text2) {
        int rows = text1.length();
        int columns = text2.length(); // EXPR1：确定需要创建的DP二维数组的大小

        int[][] dpTable = new int[rows + 1][columns + 1]; // EXPR2：在创建DP数组时，多出一行 & 一列

        // 在填充DPTable的过程中，我们会先计算第一行 & 第一列的值
        // 所以需要先对第一行 & 第一列进行初始化吗？   答：使用默认初始化的值（为0）即可
        for (int i = 0; i < rows; i++) { // EXPR3：在进行计算数组元素值时，从0开始
            for (int j = 0; j < columns; j++) {

                char c1 = text1.charAt(i), c2 = text2.charAt(j);

                if (c1 == c2) {
                    dpTable[i + 1][j + 1] = dpTable[i][j] + 1; // EXPR3：计算数组元素时，不需要再去计算第0-th行与第0-th列的元素值
                    System.out.println("当前text1与text2的字符相同，公共序列的长度值为： " + dpTable[i][j]);
                } else {
                    dpTable[i + 1][j + 1] = Math.max(dpTable[i + 1][j], dpTable[i][j + 1]);
                    System.out.println("当前text1与text2的字符不相同，公共序列的长度值为： " + dpTable[i][j]);
                }
            }
        }

        return dpTable[rows][columns]; // EXPR4：最后返回DP数组最右下角的元素值

    }
}
/*
    启示：纸上得来终觉浅，绝知此事要躬行
    backToBackSWT的代码暂时看不到，这里是自己拼写的代码
 */

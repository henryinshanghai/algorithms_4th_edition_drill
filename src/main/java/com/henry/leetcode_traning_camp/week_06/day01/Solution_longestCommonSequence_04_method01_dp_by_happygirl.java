package com.henry.leetcode_traning_camp.week_06.day01;

public class Solution_longestCommonSequence_04_method01_dp_by_happygirl {
    public static void main(String[] args) {
        String str1 = "ABCDGHLQR";
        String str2 = "AEDPHR"; // ADHR  公共子序列的长度为4

//        int result = lcsDynamic(str1.toCharArray(), str2.toCharArray());

        int result = longestCommonSequence(str1, str2);
        System.out.println(str1 + " 与 " + str2 + "之间的最大公共序列的长度为：" + result);
    }

    private static int longestCommonSequence(String str1, String str2) {
        /* Ⅰ 准备dpTable[][] */
        int lengthOfStr1 = str1.length();
        int lengthOfStr2 = str2.length();

        int[][] dpTable = new int[lengthOfStr1 + 1][lengthOfStr2 + 1];

        /* Ⅱ 计算并填充dpTable[][]二维数组中元素的值 */
        for (int i = 0; i < lengthOfStr1; i++) {
            for (int j = 0; j < lengthOfStr2; j++) {
                char ch1 = str1.charAt(i), ch2 = str2.charAt(j);

                if (ch1 == ch2) {
                    // EXPR:这里计算的是 dpTable[i + 1][j + 1]
                    dpTable[i + 1][j + 1] = dpTable[i][j]+1;
                } else {
                    dpTable[i + 1][j + 1] = Math.max(dpTable[i + 1][j], dpTable[i][j + 1]);
                }
            }
        }

        /* Ⅲ 返回dpTable[][]中正确的值 */
        return dpTable[lengthOfStr1][lengthOfStr2];
    }
}

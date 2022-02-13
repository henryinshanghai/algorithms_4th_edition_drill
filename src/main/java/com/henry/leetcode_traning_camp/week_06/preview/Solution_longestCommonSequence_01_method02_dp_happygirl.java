package com.henry.leetcode_traning_camp.week_06.preview;

public class Solution_longestCommonSequence_01_method02_dp_happygirl {
    public static void main(String[] args) {
        String str1 = "ABCDGHLQR";
        String str2 = "AEDPHR"; // ADHR  公共子序列的长度为4

//        int result = lcsDynamic(str1.toCharArray(), str2.toCharArray());

        int result = longestCommonSequence(str1, str2);
        System.out.println(str1 + " 与 " + str2 + "之间的最大公共序列的长度为：" + result);


    }

    private static int longestCommonSequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();

        // (m+1 * n+1)的二维数组
        int[][] dp = new int[m + 1][n + 1]; // 这里把二维数组设置大一行、大一列的做法可以不用

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 获取到当前的两个指针所指向的字符
                char c1 = text1.charAt(i), c2 = text2.charAt(j);

                // 比较这两个字符是否相同
                if (c1 == c2) {
                    // 如果相同，说明这个字符可以用来构成公共子序列。则：
                    dp[i + 1][j + 1] = dp[i][j] + 1;
                } else {
                    // 如果不相同，说明这个字符不能够用来构成公共子序列。则：...
                    dp[i + 1][j + 1] = Math.max(dp[i + 1][j], dp[i][j + 1]);
                }
            }
        }

        return dp[m][n]; // 二维数组的最右下角
    }
}

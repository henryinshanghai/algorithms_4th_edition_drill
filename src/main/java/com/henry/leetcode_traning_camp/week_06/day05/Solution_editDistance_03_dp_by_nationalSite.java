package com.henry.leetcode_traning_camp.week_06.day05;

public class Solution_editDistance_03_dp_by_nationalSite {
    public static void main(String[] args) {
        String word1 = "horse";
        String word2 = "ros";

        int operationTimes = minDistance(word1, word2); // 3

        System.out.println("从 " + word1 + "得到 " + word2 + "所需要的最少操作数为：" + operationTimes);
    }


    private static int minDistance(String word1, String word2) {
        /* 〇 把字符串转化成为字符数组   方便得到子字符序列 */
        char[] arr1 = word1.toCharArray();
        char[] arr2 = word2.toCharArray();

        /* Ⅰ 准备一个dpTable[]  这里有两个需要比较的字符串，所以dpTable[][] */
        int[][] dp = new int[arr1.length+1][arr2.length+1]; // 多出一个位置用来列举""空字符串的情况

        /* Ⅱ 初始化dpTable[][]中的一些位置  用来build up the dpTable[][] from */
        for(int i = 1; i < dp.length; i++) {
            dp[i][0] = i;
        } // 第一行 insert all the way up to the end

        for(int i = 1; i < dp[0].length; i++) {
            dp[0][i] = i;
        } // 第一列 insert all the way up to the end

        /* Ⅲ 使用 已经初始化的元素值 + 不同规模子问题之间的递推关系 来 填充dpTable[][] */
        for(int i = 1; i <= arr1.length; i++) {
            for(int j = 1; j <= arr2.length; j++) {
                // 1 比较最后一个位置的字符是否相等  如果相等,说明xxx。则问题可以直接简化为...
                if(arr1[i-1] == arr2[j-1]) {
                    // 当前问题的值 就等于 子问题的元素值
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    // 否则，找出回退得到的子问题的最小解
                    dp[i][j] = 1 + Math.min( // this 1 represent the current move
                            dp[i-1][j], // current move is:try do a char-insert and others
                            Math.min(dp[i][j-1], // current move is: try to do char-delete and others
                                    dp[i-1][j-1])); // current move is: try to do char-replace
                }
            }
        }

        /* Ⅳ 返回dpTable[][]中最右下角的值 */
        return dp[arr1.length][arr2.length];
    }
}

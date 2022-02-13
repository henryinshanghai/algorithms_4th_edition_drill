package com.henry.leetcode_traning_camp.week_06.day02;

public class Solution_climbingStairs_04_method01_dp_by_kevinNaughton {
    public static void main(String[] args) {
        int stairs = 6;
        int methods = climbingStairs(stairs);
        System.out.println("走完" + stairs + "级台阶可以有" + methods + "种走法");
    }

    private static int climbingStairs(int stairs) {
        int dp[] = new int[stairs + 1];

        dp[0] = 1;
        dp[1] = 1;

        for (int i = 2; i < stairs; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        return dp[stairs];
    }
}
/*
参考：https://www.youtube.com/watch?v=uHAToNgAPaM
 */

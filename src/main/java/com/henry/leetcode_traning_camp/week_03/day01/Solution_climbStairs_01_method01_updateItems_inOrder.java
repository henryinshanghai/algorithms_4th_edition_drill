package com.henry.leetcode_traning_camp.week_03.day01;

public class Solution_climbStairs_01_method01_updateItems_inOrder {
    public static void main(String[] args) {
        int stairs = 10;
        int methods = climbStairs(stairs);
        System.out.println("爬上第" + stairs + "级台阶共有: " + methods + "种走法");
    }

    private static int climbStairs(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;

        int prior_one = 2;
        int prior_two = 1;
        int curr_stair = 0;

        for (int i = 2; i < n; i++) {
            curr_stair = prior_one + prior_two;
            prior_two = prior_one;
            prior_one = curr_stair;
        }

        return curr_stair;
    }
}

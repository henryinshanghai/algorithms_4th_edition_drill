package com.henry.leetcode_traning_camp.week_03.day01.climb_stairs_01;

// 验证：可以使用递推的方式(current = prior_1 + prior_2) 来 得到在指定的规则下，到达底N级台阶有多少种走法
public class Solution_climbStairs_approaches {
    public static void main(String[] args) {
        int targetStair = 5;
        int approachAmount = climbStairsTill(targetStair);
        System.out.println("爬上第" + targetStair + "级台阶共有: " + approachAmount + "种走法"); // 8
    }

    private static int climbStairsTill(int targetStair) {
        if (targetStair <= 0) return 0;
        // 到达台阶1，有1种走法
        if (targetStair == 1) return 1;
        // 到达台阶2，有两种走法
        if (targetStair == 2) return 2;

        // 到达当前台阶的走法数量、到达前一个台阶的走法数量、到达前两个台阶的走法数量
        int prior_one = 2;
        int prior_two = 1;
        int approach_to_curr_stair = 0;

        // 对于当前台阶（从台阶3开始）
        for (int current_stair = 3; current_stair <= targetStair; current_stair++) {
            // 计算出 到达当前台阶的走法数量
            approach_to_curr_stair = prior_one + prior_two;

            // 更新 前面一个台阶的走法数量的变量、前面两个台阶的走法数量的变量
            prior_two = prior_one;
            prior_one = approach_to_curr_stair;
        }

        // 循环结束后，”到达当前台阶的走法变量“的值 就是 ”走法数量“
        return approach_to_curr_stair;
    }
}

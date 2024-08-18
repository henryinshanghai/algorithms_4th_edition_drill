package com.henry.leetcode_traning_camp.week_01.day1.arrays.approaches_to_reach_given_stair;

// 验证：到达到某一级台阶的方式 = 到达它的前一级台阶的方式 + 到达它的前两级台阶的方式
// 原理：到达它的前一级台阶后，剩下就只有一种走法 跳一级台阶；
// 到达它的前两级台阶后，也只剩下一种做法 跳两级台阶；
// 思路：可以通过到达前两级台阶的走法 推导出 到达其他台阶的走法
public class Solution_climb_stairs {
    public static void main(String[] args) {
        int n = 10;
        int approaches = approachesToClimbTillN(n); // 89   89
        System.out.println("到达第" + n + "级台阶共有：" + approaches + "种走法");
    }


    private static int approachesToClimbTillN(int targetSpot) {
        // 0 找到正确的子序列：1, 2, 3, 5, 8...
        // 1 列举基本情况，也就是不需要进行任何计算的情况
        if(targetSpot <= 2 ) return targetSpot;

        /* 2 n更大时就需要计算得到结果 */
        // 2-1 初始化最开始的三项
        int approachesTill1 = 1, approachesTill2 = 2; // approachesTill3 = 3;
        int approachesTillCurrentSpot = -1;

        // 2-2 准备一个循环，用于更新这三个数字 以得到第n项的数值
        // 特征：对于每一个当前位置，都有一个 它所对应的approaches
        // 计算手段：current_spot_approach = previous_spot_approach + previous_2_spot_approach
        for(int currentSpot = 3; currentSpot <= targetSpot; currentSpot++){ // i只用于控制循环的次数； 在循环中，更新当前区间的3个数值
            // 更新顺序：approachesTillCurrentSpot、approachesTill1、approachesTill2
            approachesTillCurrentSpot = approachesTill1 + approachesTill2;
            approachesTill1 = approachesTill2;
            approachesTill2 = approachesTillCurrentSpot;
        }

        return approachesTillCurrentSpot;
    }
}

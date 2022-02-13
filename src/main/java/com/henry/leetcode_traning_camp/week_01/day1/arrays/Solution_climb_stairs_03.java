package com.henry.leetcode_traning_camp.week_01.day1.arrays;

public class Solution_climb_stairs_03 {
    public static void main(String[] args) {

        int n = 10;
        int methods = methodsToClimbToN(n); // 89   89
        System.out.println("到达第" + n + "级台阶共有：" + methods + "种走法");
    }


    private static int methodsToClimbToN(int n) {
        // 0 找到正确的子序列： 1, 2, 3, 5, 8...
        // 1 列举基本情况，也就是不需要进行任何计算的情况
        if(n <= 2 ) return n;

        /* 2 n更大时就需要计算得到结果 */
        // 2-1 初始化最开始的三项
        int f1 = 1, f2 = 2, f3 = 3;

        // 2-2 准备一个循环，用于更新这三个数字 以得到第n项的数值
        for(int i = 2; i < n; i++){ // i只用于控制循环的次数； 在循环中，更新当前区间的3个数值
            // 更新顺序：f3、f1、f2
            f3 = f1 + f2;
            f1 = f2;
            f2 = f3;
        }

        return f3;
    }
}

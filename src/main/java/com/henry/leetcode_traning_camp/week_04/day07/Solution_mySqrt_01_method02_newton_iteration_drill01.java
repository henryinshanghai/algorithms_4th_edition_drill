package com.henry.leetcode_traning_camp.week_04.day07;

public class Solution_mySqrt_01_method02_newton_iteration_drill01 {
    public static void main(String[] args) {
        int x = 8;
        int res = mySqrt(x);
        System.out.println(x + "的平方根的整数部分为：" + res);
    }

    private static int mySqrt(int x) {
        if (x == 0 || x == 1) {
            return x;
        }

        long currAnswer = x;
        while (currAnswer > x / currAnswer) {
            // 牛顿迭代法公式(非普遍公式) 更近似的解x1 = 1/2 * （当前近似解x0 + N/当前近似解x0）
            currAnswer = (currAnswer + x / currAnswer) / 2;
        }

        return (int) currAnswer;
    }
} // 参考：https://leetcode.com/problems/sqrtx/discuss/25198/3-JAVA-solutions-with-explanation

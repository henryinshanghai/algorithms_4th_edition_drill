package com.henry.leetcode_traning_camp.week_03.day05;

public class Solution_my_power_02_method02_recursion {
    public static void main(String[] args) {
        // 底数x的范围为：xxx   指数n的范围为：ooo
        double x = 2.0;
        int n = -2;

        double res = myPow(x, n);

        System.out.println("x的n次方的结果为： " + res);
    }

    // 求x的n次方
    private static double myPow(double x, int n) {
        // 把指数位置的数字转化为long类型
        long N = n;

        // 分类讨论：
        // 如果指数值N大于等于零，则直接调用quickMul()
        // 否则，使用计算公式： a^(-n) = 1 / a^n
        return N >= 0 ? quickMul(x, N) : 1.0 / quickMul(x, -N);
    }

    // 求x的n次方的值 只需要考虑n大于等于0的情况
    public static double quickMul(double x, long N) {
        // 1 N等于0时，直接返回1.0
        if (N == 0) {
            return 1.0;
        }

        // 2 N大于0时，求出a^(N/2)的值
        double y = quickMul(x, N / 2);
        // 3 然后根据N的奇偶性，来使用2种的结果求出对应的结果
        return N % 2 == 0 ? y * y : y * y * x;
    }

}

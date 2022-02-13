package com.henry.leetcode_traning_camp.week_03.day05;

public class Solution_my_power_02_method01_cutInHalve_drill01 {
    public static void main(String[] args) {

        double x = 2.0;
        int n = -2;

        double res = myPow(x, n);

        System.out.println("x的n次方的结果为： " + res);
    }

    private static double myPow(double x, int n) {
        if (x == 0.0f) { // double类型数据的判等 ==两边需要都是double类型
            return 0.0d; // 0的double表示方法
        }

        long b = n;
        double res = 1.0;

        // 如果b<0，则：通过公式把b转换成大于零的情况
        if (b < 0) {
            x = 1 / x;
            b = -b;
        }

        // 如果b大于0，则：使用二分法逐步把指数减小到0
        while (b > 0) {
            // 判断b的奇偶性  如果b是一个奇数，就先把多出的一个x乘进来
            if ((b & 1) == 1) res *= x;

            // 更新底数的值   底数变成旧值得平方
            x = x * x;

            // 更新指数的值
            b >>= 1;
        }

        return res;
    }
}

package com.henry.leetcode_traning_camp.week_03.day05;

public class Solution_my_power_02_method01_cutInHalve {
    public static void main(String[] args) {
        double x = 2.0;
        int n = -2;

        double res = myPow(x, n);

        System.out.println("x的n次方的结果为： " + res);
    }

    private static double myPow(double x, int n) {
        // 鲁棒性代码 底数为0时，所有的结果都为0
        if(x == 0.0f) return 0.0d;

        // 计算x的n次方
        /*
            手段：
                1 根据n的奇偶性来确定计算x的n次方的公式；
                2 根据对应的公式来求取结果；
         */
        // 使用一个long类型的变量来对n实现“向上转型”     作用：能够通过某些特殊的测试用例
        long b = n;
        // 准备一个double类型的变量      作用：用x的n次方的计算结果 初始值设置为1
        double res = 1.0;

        // 如果指数b小于0，说明计算公式为xxx。则：
        if(b < 0) {
            x = 1 / x; // 2 -> 1/2
            b = -b; // -3 -> 3
        }

        // 如果指数b大于0，说明计算公式为ooo。则：
        while(b > 0) { // 不断更新指数的值，直到指数为0
            // 判断指数奇偶性，如果为奇数，就把这一个多出的底数先乘进去到res中
            if((b & 1) == 1) res *= x;

            // 更新x的值为x的平方
            x *= x;

            // 把指数b向下整除后，进入下一轮循环
            b >>= 1;
        }
        return res;
    }
}
// 这是使用循环 + 二分查找的方式

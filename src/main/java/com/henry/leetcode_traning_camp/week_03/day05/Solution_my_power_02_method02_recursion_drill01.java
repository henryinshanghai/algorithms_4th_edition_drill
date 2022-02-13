package com.henry.leetcode_traning_camp.week_03.day05;

public class Solution_my_power_02_method02_recursion_drill01 {
    public static void main(String[] args) {
        // 底数x、指数n
        double x = 2.0;
        int n = -2;

        double res = myPow(x, n);

        System.out.println("x的n次方结果为： " + res);
    }

    private static double myPow(double x, int n) {
        long N = n;

//        if (N >= 0) {
//            return quickPow(x, N);
//        } else {
//            return 1.0 / quickPow(x, -N);
//        }
        return N >= 0 ? quickPow(x, N) : 1.0 / quickPow(x, -N);
    }

    private static double quickPow(double x, long N) {
        if (N == 0) { // 任何数的0次方都是1
            return 1.0;
        }

        double halve = quickPow(x, N / 2);

        return N % 2 == 0 ? halve * halve : halve * halve * x;
    }
}

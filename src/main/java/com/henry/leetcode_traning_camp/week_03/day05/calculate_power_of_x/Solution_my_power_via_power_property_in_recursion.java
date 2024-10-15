package com.henry.leetcode_traning_camp.week_03.day05.calculate_power_of_x;

public class Solution_my_power_via_power_property_in_recursion {
    public static void main(String[] args) {
        // 底数x的范围为：xxx   指数n的范围为：ooo
        double baseNumber = 2.0;
        int power = -2;

        double calculateResult = myPower(baseNumber, power);
        System.out.println("x的n次方的结果为： " + calculateResult);
    }

    // 求x的n次方
    private static double myPower(double baseNumber, int powerNumber) {
        // 把指数位置的数字转化为long类型
        long powerInLong = powerNumber;

        // 分类讨论：
        // 如果指数值N大于等于零，则直接调用quickMul()
        // 否则，使用计算公式： a^(-n) = 1 / a^n
        return powerInLong >= 0
                ? quickMul(baseNumber, powerInLong)
                : 1.0 / quickMul(baseNumber, -powerInLong);
    }

    // 求x的n次方的值 只需要考虑n大于等于0的情况
    public static double quickMul(double baseNumber, long power) {
        // #1 N等于0时，直接返回1.0
        if (power == 0) {
            return 1.0;
        }

        // #2 计算出出a^(N/2)的值
        // 子问题：计算出 底数的(一半的指数)的乘方的结果
        double resultWhenPowerInHalve = quickMul(baseNumber, power / 2);

        // #3 使用子问题的解 来 帮助解决原始问题
        // 然后根据N的奇偶性，来使用2种的结果求出对应的结果
        return isEvenNumber(power)
                ? resultWhenPowerInHalve * resultWhenPowerInHalve // do square operation
                : resultWhenPowerInHalve * resultWhenPowerInHalve * baseNumber; // do square operation, then multiply the baseNumber
    }

    private static boolean isEvenNumber(long power) {
        return power % 2 == 0;
    }

}

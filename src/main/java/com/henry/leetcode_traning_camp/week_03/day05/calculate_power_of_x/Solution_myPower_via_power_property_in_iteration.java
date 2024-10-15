package com.henry.leetcode_traning_camp.week_03.day05.calculate_power_of_x;

// 验证：可以使用 x^y = (x)^(2*1/2*y) = (x^2)^(1/2*y) 的等价公式 来 通过square的操作 来 计算power；
public class Solution_myPower_via_power_property_in_iteration {
    public static void main(String[] args) {
        double baseNumber = 2.0;
        int power = -2;

        double calculatedResult = myPower(baseNumber, power);
        System.out.println("baseNumber的power次方的结果为： " + calculatedResult);
    }

    private static double myPower(double base, int power) {
        // 鲁棒性代码 底数为0时，所有的结果都为0
        if (base == 0.0f) return 0.0d;

        // 使用一个long类型的变量来对n实现“向上转型”     作用：能够通过某些特殊的测试用例
        long powerInLong = power;
        // 准备一个double类型的变量      作用：用x的n次方的计算结果 初始值设置为1
        double calculateResult = 1.0;

        // 如果指数b小于0，说明计算公式为 底数的倒数的次方。则：
        if (powerInLong < 0) {
            base = reciprocal(base);
            powerInLong = toggle(powerInLong);
        }

        // 如果指数b大于0，说明计算公式为 指数个底数相等。则：
        while (powerInLong > 0) { // 不断更新指数的值，直到指数为0
            // #1 判断指数奇偶性，如果为奇数，则：
            if (isOddNumber(powerInLong)) {
                // 把这一个多出的底数先乘进去到 计算结果中
                calculateResult *= base;
            }

            // #2 底数 square[做乘方操作]
            base = squareOfBase(base);

            // #3 把指数 power/2[做除2的操作] 后，进入下一轮循环
            powerInLong = halveOf(powerInLong);
        }

        return calculateResult;
    }

    // 把指数/2
    private static long halveOf(long powerInLong) {
        // 手段: 符合运算符 >>=
        powerInLong >>= 1;
        return powerInLong;
    }

    // 底数的平方
    private static double squareOfBase(double base) {
        base *= base;
        return base;
    }

    // 判断一个数字是否是奇数
    private static boolean isOddNumber(long powerInLong) {
        return (powerInLong & 1) == 1;
    }

    // 求倒数的操作
    private static double reciprocal(double base) {
        base = 1 / base; // 2 -> 1/2
        return base;
    }

    // 取相反数的操作
    private static long toggle(long powerInLong) {
        powerInLong = -powerInLong; // -3 -> 3
        return powerInLong;
    }
}
// 这是使用循环 + 二分查找的方式

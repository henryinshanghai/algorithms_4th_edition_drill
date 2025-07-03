package com.henry.leetcode_traning_camp.week_04.day07.sqrt_of_x;

// 验证：可以使用 牛顿迭代法 来 计算出给定整数值的平方根的整数部分
// 原理：牛顿迭代法(切线逼近) 能够逐步逼近，得到 x^2-N=0 方程的精确解
// 简化的公式：x1 = (x0 + N/x0) * 1/2
public class Solution_mySqrt_via_newton_iteration {
    public static void main(String[] args) {
        int givenIntegerNumber = 8;
        int integerPartOfSqrtResult = mySqrtOf(givenIntegerNumber);
        System.out.println(givenIntegerNumber + "的平方根的整数部分为：" + integerPartOfSqrtResult);
    }

    private static int mySqrtOf(int givenIntegerNumber) {
        if (givenIntegerNumber == 0) return 0;


        // Ⅰ 任意取一个数字作为 x^2-targetNum=0 方程的"当前近似解"
        // 🐖 这里选择targetNum本身，这是一个 偏差很大的数值
        long approximateResult = givenIntegerNumber;

        // Ⅲ 判断计算得到的 “当前近似解” 是不是 比“精确的平方根值”更大,
        // 如果 当前近似解 偏大，说明 需要继续查找 “更精确的近似解”，则：继续迭代👇
        // 🐖 这里用到了一个小技巧：把乘法转化成为除法
        while (approximateResult > givenIntegerNumber / approximateResult) {
            // Ⅱ 使用 “当前近似解” + “牛顿迭代法” 来 计算出 “更精确的近似解x1” 并将之作为“当前近似解”
            // 公式：x1 = (x0 + N/x0) * 1/2
            approximateResult = (approximateResult + givenIntegerNumber / approximateResult) / 2;
        }

        // Ⅳ 循环结束后，会得到一个 “满足条件的近似解”————取这个近似解的整数部分即可
        return (int) approximateResult;
    }
}

package com.henry.leetcode_traning_camp.week_04.day07.valid_perfect_square;

public class Solution_isPerfectSquare_via_newton_iteration {
    public static void main(String[] args) {
        int givenIntegerNumber = 16;
//        int givenIntegerNumber = 14;
        boolean isOrNot = isPerfectSquare(givenIntegerNumber);

        if (isOrNot) {
            System.out.println("givenIntegerNumber " + givenIntegerNumber +  " 是一个完全平方数.");
        } else {
            System.out.println("givenIntegerNumber " + givenIntegerNumber + " 不是一个完全平方数.");
        }
    }

    private static boolean isPerfectSquare(int givenIntegerNumber) {
        // 牛顿迭代法不是用来求近似解的吗？要怎么转化为“判断是否为平方根”的作用的？
        /*
            1 使用牛顿迭代法求出 num的近似平方根；
            2 如果num是一个完全平方数，那么求得的近似平方根肯定是一个整数。则：
                对此结果求平方，肯定能够得到原始数值
              而如果num不是一个完全平方数，那么求得的近似平方根就会有损耗。则：
                对这个根求平方，得到的结果不等于原始值
         */
        if (givenIntegerNumber == 0 || givenIntegerNumber == 1) {
            return true;
        }

        // Ⅰ 任意取一个数字作为 x^2-targetNum=0 方程的"当前近似解"
        long currentApproximateResult = givenIntegerNumber;

        // Ⅲ 判断计算得到的 “当前近似解” 是不是 比“精确的平方根值”更大,
        // 如果 当前近似解 偏大，说明 需要继续查找 “更精确的近似解”，则：继续迭代👇
        // 🐖 这里用到了一个小技巧：把乘法转化成为除法
        while (currentApproximateResult > givenIntegerNumber / currentApproximateResult) {
            // Ⅱ 使用 “当前近似解” + “牛顿迭代法” 来 计算出 “更精确的近似解x1” 并将之作为“当前近似解”
            // 公式：x1 = (x0 + 10/x0) * 1/2
            currentApproximateResult = (currentApproximateResult + givenIntegerNumber / currentApproximateResult) / 2;
        }

        // Ⅳ 循环结束后，会得到一个 “满足条件的近似解”————比较该平方根的平方结果 与 给定的原始整数是否相等
        return currentApproximateResult * currentApproximateResult == givenIntegerNumber;
    }
}

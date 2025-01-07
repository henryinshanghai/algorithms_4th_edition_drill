package com.henry.leetcode_traning_camp.week_06.day01.fibonacci_sequence;

// 验证：对于 求斐波那契数列第N项数字 的问题，可以使用 递归的手段来实现
// 特征：这种手段 存在有很多的重复计算 - 重复计算某一项斐波那契数值
public class Solution_fibonacci_via_recursion_byNickWhite {
    public static void main(String[] args) {
        int ordinalNumber = 20;
        int fibonacciItem = fibonacciItemOn(ordinalNumber);
        System.out.println("斐波那契数列的第" + ordinalNumber + "项的元素值为：" + fibonacciItem); // 6765
    }

    // 获取到 第n个斐波那契数的值
    private static int fibonacciItemOn(int ordinalNumber) {
        /* 〇 递归触底返回条件 */
        // 进行递推的初始项
        if (ordinalNumber == 0) {
            return 0;
        }

        if (ordinalNumber == 1) {
            return 1;
        }

        // 根据斐波那契数的定义：当前的斐波那契数 = 它的前一项的值 + 它的前两项的值
        return fibonacciItemOn(ordinalNumber - 1) + fibonacciItemOn(ordinalNumber - 2);
    }
}

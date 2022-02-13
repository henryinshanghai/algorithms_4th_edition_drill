package com.henry.leetcode_traning_camp.week_06.day01;

public class Solution_fibonacci_01_method01_recursion_byNickWhite {
    public static void main(String[] args) {
        int item = 20;
        int result = fibonacci(item);
        System.out.println("斐波那契数列的第" + item + "项的元素值为：" + result); // 6765
    }

    private static int fibonacci(int n) {
        /* 〇 递归触底返回条件 */
        // 进行递推的初始项
        if (n == 0) {
            return 0;
        }

        if (n == 1) {
            return 1;
        }

        return fibonacci(n - 1) + fibonacci(n - 2);
    }
}

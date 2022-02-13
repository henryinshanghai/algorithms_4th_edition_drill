package com.henry.leetcode_traning_camp.week_06.day01;

public class Solution_fibonacci_01_method03_recursion_withMemo {
    public static void main(String[] args) {
        int item = 20;
        int[] memo = new int[item + 1];
        int result = fibonacci(item, memo);
        System.out.println("斐波那契数列的第" + item + "项的元素值为：" + result); // 6765
    }

    private static int fibonacci(int n, int[] memo) {
        if (n <= 1) {
            return n;
        }

        if (memo[n] == 0) { // 如果某个值没有被计算过，则：
            // 进行计算，并存储结果到数组中————作为缓存，供之后的计算使用
            memo[n] = fibonacci(n - 1, memo) + fibonacci(n - 2, memo);
        }

        return memo[n];
    }
}
// 注：这种方式的时间复杂度是线性的
/*
优化：
    使用数组元素来逐步向后计算；
    自顶向下地计算，从较小值计算得到最大值

方法1：分治 + 记忆化；
方法2：自底向上进行递推
 */

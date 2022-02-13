package com.henry.leetcode_traning_camp.week_06.day01;

public class Solution_fibonacci_01_method02_dpWithPointer_byNickWhite {
    public static void main(String[] args) {
        int item = 20;
        int result = fibonacci(item);
        System.out.println("斐波那契数列的第" + item + "项的元素值为：" + result); // 6765
    }

    private static int fibonacci(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        /* 准备需要用到的指针,并初始化指针的指向 */
        int a = 0;
        int b = 1;
        int sum = a + b;

        /* 使用递归的方式求出 F(N) */
        while (n > 1) {
            // 求出fibonacci数组的下一项
            sum = a + b;
            a = b;
            b = sum;

            n--; // 这里会需要稍微调整调整以得到正确的结果
        }

        return sum; // 返回sum指针所指向的数组项 即为 F(N)
    }
}
/*
时间复杂度为线性
属于一种 semi-DP的解法
 */

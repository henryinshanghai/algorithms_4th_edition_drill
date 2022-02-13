package com.henry.leetcode_traning_camp.week_04.day07;

public class Solution_mySqrt_01_method01_binary_search_drill01 {
    public static void main(String[] args) {
        int num = 8;

        int res = mySqrt(num);

        System.out.println(num + "的平方根结果的整数部分为：" + res);
    }

    private static int mySqrt(int num) {
        if (num == 0 || num == 1) {
            return num;
        }

        // NOTE 这里定义成为long类型，以便通过leetcode的一些测试数据
        long leftBoundary = 0, rightBoundary = num;
        long mid = 1;

        while (leftBoundary <= rightBoundary) {
            // 1 计算mid
            // 当left、right俩个数足够大时，相加会越界。
            // 所以使用 left + （right  - left）/ 2 防止越界。
            mid = leftBoundary + (rightBoundary - leftBoundary) / 2;

            if (mid * mid > num) {
                rightBoundary = mid - 1;
            } else {
                leftBoundary = mid + 1;
            }
        }

        return (int) rightBoundary;
    }
}

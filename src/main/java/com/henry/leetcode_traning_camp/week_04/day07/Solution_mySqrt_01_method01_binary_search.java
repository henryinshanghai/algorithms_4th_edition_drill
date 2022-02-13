package com.henry.leetcode_traning_camp.week_04.day07;

public class Solution_mySqrt_01_method01_binary_search {
    public static void main(String[] args) {
        int num = 8;

        int res = mySqrt(num);

        System.out.println(num + "的平方根结果的整数部分为：" + res);
    }

    private static int mySqrt(int x) {
        // 〇 参数判断
        if (x == 0 || x == 1) {
            return x;
        }

        // Ⅰ 初始化边界位置 与 中间位置
        long left = 1, right = x;
        long mid = 1; // EXPR1：mid的值初始化为多少都无所谓，因为它的值都会被更新

        // Ⅱ 进行循环，不断缩小查找区间————直到左边界与右边界相遇
        while (left <= right) { // EXPR2：这里是 <= 符号————这会导致一个问题：left = right时，循环还会继续执行
            // 这会给步骤Ⅲ 带来一些麻烦：到底是返回right还是left？？？
            mid = left + (right - left) / 2;
            if (mid * mid > x) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        // Ⅲ 返回跳出循环前范围的某个边界————具体是左边界还是右边界需要进行调试，因为循环结束时，left与right指针的位置顺序可能已经发生变化
        // EXPR3:随缘返回 对了再说
        return (int) right; // 2
//        return (int) left; // 3
    }
} // 不断更新区间，直到区间左右边界相遇/交叉 就能找到平方根的结果

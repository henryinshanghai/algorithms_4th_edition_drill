package com.henry.leetcode_traning_camp.week_04.day07.valid_perfect_square;

// 验证：对于判断一个正整数 是不是一个 完全平方数的问题，可以通过 使用二分法来找到它的平方根，并判断平方根值是否为整数的方式 来 得出答案
// 原理：完全平方数的平方根 一定会是一个 整数；
// 概念：区间的左边界、区间的右边界、区间的中间位置
// 应用：如果二分过程中，存在有 区间中间位置上的数字(是一个整数)的平方 等于 给定的整数，则：说明该给定的整数 是一个完全平方数；
public class Solution_isPerfectSquare_via_binarySearch {
    public static void main(String[] args) {
//        int givenIntegerNumber = 16;
        int givenIntegerNumber = 14;
        boolean isOrNot = isPerfectSquare(givenIntegerNumber);

        if (isOrNot) {
            System.out.println("givenIntegerNumber " + givenIntegerNumber +  " 是一个完全平方数.");
        } else {
            System.out.println("givenIntegerNumber " + givenIntegerNumber + " 不是一个完全平方数.");
        }
    }

    private static boolean isPerfectSquare(int givenIntegerNumber) {
        // Ⅰ
        long leftBarNum = 0,
             rightBarNum = givenIntegerNumber;
        long middleNum = 1;

        // 对于 左闭右闭区间[leftBarNum, rightBarNum],使用二分法 不断缩小此区间 来 找到 给定整数数字的平方根
        while (leftBarNum <= rightBarNum) {
            // 计算得到 当前区间[leftBarNum, rightBarNum]的中间位置 middleNum
            middleNum = leftBarNum + (rightBarNum - leftBarNum) / 2;

            // 如果 中间位置数字的平方 等于 给定的整数数字，说明 给定的整数数字是一个 完全平方数，则：
            if (middleNum * middleNum == givenIntegerNumber) {
                // 返回true
                return true;
            } else if (middleNum * middleNum < givenIntegerNumber) { // 如果 中间位置上的数字的平方 小于 给定的整数数字，说明 中间位置的数字 小于 给定整数数字的平方根，则：
                // 更新区间的左边界 来 缩小区间本身
                leftBarNum = middleNum + 1;
            } else {
                // 更新区间的右边界 来 缩小区间本身
                rightBarNum = middleNum - 1;
            }
        }

        // 如果区间的左右边界已经相遇，说明已经通过二分法找到了 平方根值，但它不是一个整数，则：
        // 返回false
        return false;
    }
}

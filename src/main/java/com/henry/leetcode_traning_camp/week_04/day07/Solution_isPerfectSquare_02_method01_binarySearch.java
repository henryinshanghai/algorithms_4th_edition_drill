package com.henry.leetcode_traning_camp.week_04.day07;

public class Solution_isPerfectSquare_02_method01_binarySearch {
    public static void main(String[] args) {
//        int num = 16;
        int num = 14;

        boolean res = isPerfectSquare(num);

        System.out.println("num" + "是一个完全平方数？" + res);
    }

    private static boolean isPerfectSquare(int num) {
        // 〇

        // Ⅰ
        long left = 0, right = num;
        long mid = 1;

        while (left <= right) {
            mid = left + (right - left) / 2;

            if (mid * mid == num) {
                return true;
            } else if (mid * mid < num){
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return false;
    }
}

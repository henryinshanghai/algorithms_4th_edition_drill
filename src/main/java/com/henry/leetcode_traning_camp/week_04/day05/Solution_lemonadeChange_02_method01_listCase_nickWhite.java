package com.henry.leetcode_traning_camp.week_04.day05;

public class Solution_lemonadeChange_02_method01_listCase_nickWhite {
    public static void main(String[] args) {
//        int[] bills = {5, 5, 10};
        int[] bills = {5,5,10,10,20};

        boolean result = lemonadeChange(bills);

        System.out.println("按照当前的bills顺序，店主能够为每个顾客进行找零？" + result);
    }

    private static boolean lemonadeChange(int[] bills) {
        // 店主手上持有的钞票
        int fives = 0;
        int tens = 0;

        for (Integer bill : bills) {
            if (bill == 5) { // 顾客给的钞票
                fives++; // 交易完成
            } else if (bill == 10) {
                tens++;
                fives--;
            } else {
                if (tens > 0) {
                    tens--;
                    fives--;
                } else {
                    fives -= 3;
                }
            }

            // 每次交易完成后，判断店主手中是否还有5元钞票
            if (fives < 0) return false;
        }

        return true;
    }
}

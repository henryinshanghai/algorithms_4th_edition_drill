package com.henry.leetcode_traning_camp.week_04.day05;

public class Solution_lemonadeChange_02_method01_listCase_nickWhite_drill01 {
    public static void main(String[] args) {
        int[] bills = {5,5,10,10,20};

        boolean result = lemonadeChange(bills);

        System.out.println("按照当前的bills顺序，店主能够为每个顾客进行找零？" + result);
    }

    private static boolean lemonadeChange(int[] bills) {
        int fives = 0;
        int tens = 0;

        for (int bill : bills) {
            if (bill == 5) {
                fives++;
            } else if (bill == 10) {
                fives--;
                tens++;
            } else {
                if (tens > 0) {
                    tens--;
                    fives--;
                } else {
                    fives -= 3;
                }
            }

            if (fives < 0) {
                return false;
            }
        }

        return true;
    }
}

package com.henry.leetcode_traning_camp.week_03.day06;

import java.util.ArrayList;
import java.util.List;

public class Solution_letterCombinations_02_method02_backToBack_drill02 {
    public static void main(String[] args) {
        String originalDigits = "23";
        List<String> res = letterCombine(originalDigits);
        System.out.println("数字键组成的所有字母组合可能为： " + res);
    }

    private static List<String> letterCombine(String originalDigits) {
        // 准备一个字符串列表
        List<String> res = new ArrayList<>();

        // 准备一个字符数组
        char[] currCharCombine = new char[originalDigits.length()];

        backtracking(res, currCharCombine, originalDigits, 0);

        return res;
    }

    private static String[] MAPPING = {
            "0",
            "1",
            "ABC",
            "DEF",
            "GHI",
            "JKL",
            "MNO",
            "PQRS",
            "TUV",
            "WXYZ"
    };

    private static void backtracking(List<String> res, char[] currCharCombine, String phoneNumber, int digit) {
        // goal / base case
        if (digit == phoneNumber.length()) {
            res.add(new String(currCharCombine));
        } else {
            // choices: make those branches
            for (int i = 0; i < MAPPING[phoneNumber.charAt(digit) - '0'].length(); i++) { // here you need to get the char-options that related to the digit button
                // place the letter
                char currChar = MAPPING[phoneNumber.charAt(digit) - '0'].charAt(i);
                currCharCombine[digit] = currChar;

                // drill down for next digit
                backtracking(res, currCharCombine, phoneNumber, digit + 1);
            }
        }
    }
}

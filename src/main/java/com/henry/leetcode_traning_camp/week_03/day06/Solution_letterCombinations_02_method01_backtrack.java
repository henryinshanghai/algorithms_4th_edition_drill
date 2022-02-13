package com.henry.leetcode_traning_camp.week_03.day06;

import java.util.LinkedList;
import java.util.List;

public class Solution_letterCombinations_02_method01_backtrack {
    private static final String[] KEYS = { "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };

    public static void main(String[] args) {
        String originalStr = "23";

        List<String> res = letterCombination(originalStr);

        System.out.println("字母所有可能的组合结果为： " + res);
    }

    private static List<String> letterCombination(String digits) {
        List<String> res = new LinkedList<String>();
        // 回溯函数的参数：prefix用于表示当前字符串的结果，digits用于表示用户输入的原始数字
        combination("", digits, 0, res);
        return res;
    }

    private static void combination(String prefix, String digits, int offset, List<String> res) {
        if (offset >= digits.length()) {
            res.add(prefix);
            return;
        }
        String letters = KEYS[(digits.charAt(offset) - '0')];
        for (int i = 0; i < letters.length(); i++) {
            combination(prefix + letters.charAt(i), digits, offset + 1, res);
        }
    }
}

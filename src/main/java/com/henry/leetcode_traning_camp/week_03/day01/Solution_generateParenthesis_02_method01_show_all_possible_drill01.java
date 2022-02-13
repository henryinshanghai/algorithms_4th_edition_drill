package com.henry.leetcode_traning_camp.week_03.day01;

import java.util.List;

// 生成n对括号所有可能组合成的字符串
public class Solution_generateParenthesis_02_method01_show_all_possible_drill01 {
    static int count = 0;
    public static void main(String[] args) {
        int pairs = 3;
        List<String> res = generateParenthesis(pairs);
        System.out.println("递归一共调用了： " + count + "次");
    }


    private static List<String> generateParenthesis(int n) {
        generate(0, 2 * n, "");
        return null;
    }

    private static void generate(int start, int end, String curr) {
        // terminator
        count++;
        if (start == end) {
            System.out.println(curr);
            return;
        }

        // current level
        generate(start + 1, end, curr + "(");
        generate(start + 1, end, curr + ")");
    }
}

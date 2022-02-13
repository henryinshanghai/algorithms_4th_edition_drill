package com.henry.leetcode_traning_camp.week_03.day01;

import java.util.List;

public class Solution_generateParenthesis_02_method01_show_all_possible_drill02 {
    public static void main(String[] args) {
        int pairs = 3;
        List<String> res = generateParenthesis(pairs);
    }

    private static List<String> generateParenthesis(int n) {
        // 使用递归来生成满足条件的字符串
        generate(0, 2 * n, "");
        return null;
    }

    private static void generate(int start, int end, String s) {
        // 递归地填充字符串

        // 递归结束的条件：区间的左右边界相碰撞
        if (start == end) {
            System.out.println(s);
            return;
        }

        // 本级递归要完成的事情
        // 添加一个左括号
        generate(start+1, end, s + "(");
        // 添加一个右括号
        generate(start+1, end, s + ")");
    }
}

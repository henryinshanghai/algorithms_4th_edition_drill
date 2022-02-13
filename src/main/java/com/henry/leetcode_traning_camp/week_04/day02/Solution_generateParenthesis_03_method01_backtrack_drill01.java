package com.henry.leetcode_traning_camp.week_04.day02;

import java.util.ArrayList;
import java.util.List;

public class Solution_generateParenthesis_03_method01_backtrack_drill01 {
    public static void main(String[] args) {
        int pairs = 3;

        List<String> res =  generateParenthesis(pairs);

        System.out.println(res);
    }

    private static List<String> generateParenthesis(int pairs) {
        List<String> res = new ArrayList<>();

        helper(res, "", pairs, pairs);

        return res;
    }

    private static void helper(List<String> res, String curr, int left, int right) {
        // 递归树触底返回到上一级的情况
        if (left < 0 || left > right) {
            return;
        }

        // 括号组合构造完成的情况  这时候也要返回到上一级
        if (left == 0 && right == 0) {
            res.add(curr);
            return;
        }

        // 其他情况 做选择，递归树向下调用
        helper(res, curr + "(", left - 1, right);
        helper(res, curr + ")", left, right - 1);
    }
}

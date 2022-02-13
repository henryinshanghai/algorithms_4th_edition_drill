package com.henry.leetcode_traning_camp.week_04.day02;

import java.util.ArrayList;
import java.util.List;

public class Solution_generateParenthesis_03_method02_backtrack_addUp_drill01 {
    public static void main(String[] args) {
        int paris = 3;
        List<String> res = generateParenthesis(paris);
        System.out.println(res); // expect there're 5 of combination
    }

    private static List<String> generateParenthesis(int pairs) {
        List<String> res = new ArrayList<>();
        backtrack(res, "", 0, 0, pairs);
        return res;
    }

    private static void backtrack(List<String> res, String curr, int left, int right, int pairs) {
        // base case / 递归触底返回条件 / goal
        if (curr.length() == 2 * pairs) {
            res.add(curr);
            return;
        }

        // constraints and choice
        // add left parenthesis
        if (left < pairs) {
            backtrack(res, curr + "(", left + 1, right, pairs);
        }

        // add right parenthesis
        if (right < left) {
            backtrack(res, curr + ")", left, right + 1, pairs);
        }
    }
}

package com.henry.leetcode_traning_camp.week_04.day02;

import java.util.ArrayList;
import java.util.List;

public class Solution_generateParenthesis_03_method02_backtrack_addUp {
    public static void main(String[] args) {
        int pairs = 3;
        List<String> res = generateParenthesis(pairs);
        System.out.println(res);
    }

    private static List<String> generateParenthesis(int pairs) {
        List<String> res = new ArrayList<>();

        helper(res, "", 0, 0, pairs);

        return res;
    }

    private static void helper(List<String> res, String curr, int open, int close, int paris) {
        // base case 找到了满足条件的括号组合
        if (curr.length() == 2 * paris) {
            res.add(curr);
            return;
        }

        // 当左括号有剩余时，继续添加左括号
        if (open < paris) {
            helper(res, curr + "(", open + 1, close, paris);
        }

        // 当括号组合中的开括号更多时，可以继续添加右括号
        if (open > close) {
            helper(res, curr + ")", open, close + 1, paris);
        }
    }
} // this seems better. is this got a better time complicity? no I don't think so, it is expo...

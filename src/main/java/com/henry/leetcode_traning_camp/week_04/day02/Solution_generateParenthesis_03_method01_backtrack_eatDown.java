package com.henry.leetcode_traning_camp.week_04.day02;

import java.util.ArrayList;
import java.util.List;

public class Solution_generateParenthesis_03_method01_backtrack_eatDown {
    public static void main(String[] args) {
        int pairs = 3;

        List<String> res =  generateParenthesis(pairs);

        System.out.println(res);
    }

    private static List<String> generateParenthesis(int n) {
        // 准备一个元素为列表的列表 用于存储所有组合的结果
        List<String> res = new ArrayList<>();
        helper(res, "", n, n);
        return res;
    }

    private static void helper(List<String> res, String curr, int left, int right) {
        // 非法情况：当得到一个非法的括号组合时，就马上返回
        if (left < 0 || left > right) { // 剩余的左括号的个数 > 剩余的右括号的个数
            return;
        }

        // 第二种情况：add string to the list
        if (left == 0 && right == 0) {
            res.add(curr);
            return;
        }

        // 剩下的两种情况：进行递归调用
        helper(res, curr + "(", left - 1, right);
        helper(res, curr + ")", left, right - 1);

    }
}

package com.henry.leetcode_traning_camp.week_03.day01;

import java.util.List;

public class Solution_generateParenthesis_02_method02_show_all_valid_drill01 {
    static int counter = 0;
    public static void main(String[] args) {
        int pairs = 3;
        List<String> res = generateParenthesis(pairs);
        
    }

    private static List<String> generateParenthesis(int n) {
        generate(0, 0, n, "");
        System.out.println("递归函数被调用了" + counter + "次");
        return null;
    }

    /**
     * 使用left个左括号、right个右括号来创建有效的临时字符串
     * @param left_braces 使用的左括号的个数
     * @param right_braces 使用的右括号的个数
     * @param pairs 左右括号的最大可用个数
     * @param valid_temp_str 当前有效的临时字符串
     */
    private static void generate(int left_braces, int right_braces, int pairs, String valid_temp_str) {
        counter++;
        if (left_braces == pairs && right_braces == pairs) {
            System.out.println(valid_temp_str);
            return;
        }

        // 本级递归要做的事情    使用left个左括号与right个右括号来创建
        if (left_braces < pairs) {
            generate(left_braces + 1, right_braces, pairs, valid_temp_str + "(");
        }

        if (right_braces < left_braces) {
            generate(left_braces, right_braces + 1, pairs, valid_temp_str + ")");
        }
    }
}

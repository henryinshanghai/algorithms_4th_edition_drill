package com.henry.leetcode_traning_camp.week_03.day01;

import java.util.List;

public class Solution_generateParenthesis_02_method01_show_all_possible {
    public static void main(String[] args) {
        int n = 3;

        List<String> res = generateParenthesis(n);
    }

    private static List<String> generateParenthesis(int n) {
        // 使用n组括号来生成字符串
        generate(0, 2 * n, "");
        return null;
    }

    /**
     * 递归方法的作用（简述）：使用n组左右括号来生成所有可能的字符串（所有的括号都要被使用）
     * （包含参数的描述）在指定字符串s的基础上，在第level个位置开始添加括号，直到添加到第max个位置
     * 手段：在每一级递归中添加一个括号
     * @param level 当前递归的层级
     * @param max   递归的最大层级
     * @param s 当前生成的字符串序列
     */
    private static void generate(int level, int max, String s) {
        // terminator   当层数大于等于预期的最大层数时,说明括号字符串就已经创建完成了。则:
        if (level >= max) {
            // 直接输出当前构建出的字符串
            System.out.println(s);
            // 返回方法，避免执行不必要的代码
            return;
        }

        // process current logic:left, right
        // 当前层的两种情况：1 在原有基础上添加了左括号； 2 在原有基础上添加了右括号
        String s1 = s + "(";
        String s2 = s + ")";

        // drill down
        // 考虑下一层的两种情况：
        // 在当前字符串s1的基础上，在第level+1个位置添加左括号或者添加右括号     会打印s1这一支所有可能的字符串
        generate(level + 1, max, s1);
        // 在当前字符串s2的基础上，在第level+1个位置添加左括号或者添加右括号     会打印s2这一支所有可能的字符串
        generate(level + 1, max, s2);

        // reverse status
    }
}

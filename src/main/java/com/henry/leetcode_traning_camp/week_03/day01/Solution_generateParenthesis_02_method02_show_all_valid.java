package com.henry.leetcode_traning_camp.week_03.day01;

import java.util.List;

public class Solution_generateParenthesis_02_method02_show_all_valid {
    public static void main(String[] args) {
        int n = 3;

        List<String> res = generateParenthesis(n);

    }

    private static List<String> generateParenthesis(int n) {
        generate(0, 0, n, "");
        return null;
    }

    /**
     * 思路：
     * 左边括号随时可以添加，只要还有左括号可以使用
     * 右括号必须出现在左括号的后面 并且 左括号的个数需要大于右括号的个数
     * aka 逐渐添加括号，使之成长为一个符号条件的字符串
     *
     * 递归方法的作用（简述）：使用n对左右括号，来生成符合特定规则的字符串
     * 方法作用（含参数的描述）：
     *      使用n对左右括号中的left个左括号 与 right个右括号来“逐步生成”所有满足条件的字符串
     *          手段：在当前满足规则的中间字符串上添加左括号... & 在当前满足条件的中间字符串上添加右括号
     * @param left 当前字符串s中所使用的所括号的数量
     * @param right 当前字符串s中所使用的右括号的数量
     * @param n 左括号与右括号的配额
     * @param s 用于不断累加的字符串
     */
    private static void generate(int left, int right, int n, String s) {
        // 如果当前字符串使用了n个左括号与n个右括号，说明当前字符串已经用完了所有可用的字符串(而它又一定是一个有效字符串)。则：
        if (left == n && right == n) {
            // 打印当前字符串
            System.out.println(s);
            // 返回方法     避免执行不必要的代码
            return;
        }

        // 逐步生成有效字符串的规则1 左括号只要没有用完就可以尽情添加
        // 如果当前正在形成的有效字符串s中使用的左括号的数量小于n，说明左括号还没有被用完。则:...
        if (left < n) {
            // 添加一个左括号，用以生成符合条件的字符串s的中间状态1
            generate(left + 1, right, n, s + "(");
        }

        // 逐步生成有效字符串的规则2 添加右括号时，不仅要求右括号没有用完，而且右括号的数量要小于左括号的数量
        // 如果当前字符串s中使用的右括号数量小于左括号数量，说明右括号还没有被用完。则：
        if (left > right) { // && right < n
            // 添加一个右括号，用以生成符合条件的字符串的中间状态2
            generate(left, right + 1, n, s + ")");
        }

    }
}

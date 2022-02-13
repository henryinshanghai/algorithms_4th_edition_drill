package com.henry.leetcode_traning_camp.week_03.day05;

import java.util.ArrayList;
import java.util.List;

public class Solution_generate_valid_bracelet_01_method01_recursion {
    public static void main(String[] args) {
        int pair = 3;

        List<String> res = generateParenthesis(pair);

        System.out.println(res);
    }

    /**
     * 构建有效的括号组合
     * @param n
     * @return
     */
    private static List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        // 特判
        if (n == 0) {
            return res;
        }

        // 执行深度优先遍历，搜索可能的结果
        dfs("", n, n, res);
        return res;
    }

    /**
     * @param curStr 组合的当前值
     * @param left  当前可用的左括号的数量
     * @param right 当前可用的右括号的数量
     * @param res 最终构造出的所有组合的集合
     */
    private static void dfs(String curStr, int left, int right, List<String> res) {
        // 因为每一次尝试，都使用新的字符串变量，所以无需回溯
        // 在递归终止的时候，直接把它添加到结果集即可，注意与「力扣」第 46 题、第 39 题区分
        if (left == 0 && right == 0) {
            res.add(curStr);
            return;
        }

        // 剪枝（如图，左括号可以使用的个数严格大于右括号可以使用的个数，才剪枝，注意这个细节）
        if (left > right) {
            return;
        }

        if (left > 0) {
            dfs(curStr + "(", left - 1, right, res);
        }

        if (right > 0) {
            dfs(curStr + ")", left, right - 1, res);
        }
    }
}

package com.henry.leetcode_traning_camp.week_03.day01.generate_valid_parenthesis_str;

import java.util.List;

// step1: 打印出所有可能的括号字符组合👇
// 这个任务是diguideable的吗？从spot0到spotN的N个位置 填充括号字符
// 规模更小的同类型任务：在“更小的位置区间”中 填充括号字符
// 验证：可以使用递归的手段 来 解决填充类别的问题。因为 子问题的解 就是 原始问题的解的一部分
public class Solution_generate_all_possible_parenthesis_str {
    public static void main(String[] args) {
        int pairAmount = 3;
        generateParenthesis(pairAmount);
    }

    private static List<String> generateParenthesis(int pairAmount) {
        // 使用n组括号来生成字符串
        printConstructedStr("", 0, 2 * pairAmount);
        return null;
    }

    /**
     * 递归方法的作用（简述）：基于 当前生成的字符串，在 指定的位置区间中 填充括号字符 来 得到 最终的括号字符串
     * （包含参数的描述）在指定字符串s的基础上，在第level个位置开始添加括号，直到添加到第max个位置
     * @param currentGeneratedStr 当前生成的字符串序列
     * @param startSpotToFill         待填充括号字符的当前位置
     * @param maxSpotToFill             最大位置
     */
    private static void printConstructedStr(String currentGeneratedStr, int startSpotToFill, int maxSpotToFill) {
        // #0 终结条件：如果 当前“待填充字符的位置” 已经到达 “最大位置”，说明 字符串已经“生成完成”，则：
        if (startSpotToFill >= maxSpotToFill) {
            // 打印出 生成完成的字符串
            System.out.println(currentGeneratedStr);
            // 返回上一级
            return;
        }

        // #1 本级递归要做的事情：在当前位置上，要么选择 左括号，要么选择 右括号
        String possibleStr1 = currentGeneratedStr + "(";
        String possibleStr2 = currentGeneratedStr + ")";

        // 规模更小的子问题，如何帮助解决原始问题?
        // 答：在更小位置区间中填充出的字符串，可以直接用来 拼接出原始字符串 / 子问题的解就是原始问题解的一部分
        // 在更小的位置区间中，填充括号字符串 - 这里之所以把构造出的字符串 作为参数传递，因为后继的构造 要基于它继续进行
        printConstructedStr(possibleStr1, startSpotToFill + 1, maxSpotToFill);
        printConstructedStr(possibleStr2, startSpotToFill + 1, maxSpotToFill);
    }
}

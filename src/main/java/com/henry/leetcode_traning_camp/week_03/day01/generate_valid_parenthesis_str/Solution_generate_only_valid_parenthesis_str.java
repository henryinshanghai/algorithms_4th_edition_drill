package com.henry.leetcode_traning_camp.week_03.day01.generate_valid_parenthesis_str;

import java.util.List;

// 验证：可以使用递归的方式 来 在给定括号数量的情况下，得到所有“合法的括号字符串”；
// 可以使用递归的原理：#1 只有在一个合法的字符串的基础上，继续合法地添加括号字符，才能继续得到一个合法的字符串；
// #2 合法地添加括号字符：只有在字符串中的左括号更多时，才能继续向其中 合法地添加右括号字符；
public class Solution_generate_only_valid_parenthesis_str {
    public static void main(String[] args) {
        int pairAmount = 3;
        generateValidParenthesisStr(pairAmount);
    }

    private static List<String> generateValidParenthesisStr(int pairAmount) {
        // 从一个空字符串开始，生成 包含有 pairAmount个左括号及pairAmount个右括号的“合法字符串”
        printConstructedStr("", 0, 0, pairAmount);
        return null;
    }

    /**
     * 思路：在添加字符后，得到一个合法的字符串；
     * 保证合法的做法：#1 左括号随时可以添加，只要还有左括号可以使用； #2 右括号必须出现在左括号的后面 并且 左括号的个数需要大于右括号的个数
     * <p>
     * 递归方法的作用（简述）：基于当前生成的字符串，添加特定数量的左右括号字符 来 得到“指定长度的”、“合法的”括号字符串
     *
     * @param currentGeneratedStr           用于不断累加的字符串
     * @param currentRightParenthesisAmount 当前字符串s中所使用的右括号的数量
     * @param currentLeftParenthesisAmount  当前字符串s中所使用的所括号的数量
     * @param maxParenthesisAmount          左括号与右括号的最大数量
     */
    private static void printConstructedStr(String currentGeneratedStr, int currentRightParenthesisAmount, int currentLeftParenthesisAmount, int maxParenthesisAmount) {
        // #0 递归终结条件：
        // 如果 字符串中 左括号的数量 以及 右括号的数量 都达到 最大数量，说明 字符串已经生成完成，则：
        if (currentLeftParenthesisAmount == maxParenthesisAmount && currentRightParenthesisAmount == maxParenthesisAmount) {
            // 打印出 生成完成的字符串
            System.out.println(currentGeneratedStr);
            // 并返回上一级
            return;
        }

        // #1 本级递归需要做的事情：向当前字符串中 合法地添加一个括号字符
        // 原始问题：在空字符串的基础上，合法地添加括号字符，得到“指定长度的合法括号字符串”
        // 规模更小的子问题：在“合法的子字符串”基础上，合法地添加括号字符，得到“指定长度的合法字符串”
        // Ⅰ 如果左括号没有用尽，说明 向当前字符串中添加左括号 是合法的，则：
        if (currentLeftParenthesisAmount < maxParenthesisAmount) {
            // 向当前字符串中 添加左括号（左括号数量+1）
            printConstructedStr(currentGeneratedStr + "(", currentRightParenthesisAmount, currentLeftParenthesisAmount + 1, maxParenthesisAmount);
        }

        // Ⅱ 如果当前字符串中左括号的数量更多，说明 向当前字符串中添加右括号 是合法的，则：
        if (currentLeftParenthesisAmount > currentRightParenthesisAmount) { // && right < n
            // 向当前字符串中 添加右括号（右括号数量+1）
            printConstructedStr(currentGeneratedStr + ")", currentRightParenthesisAmount + 1, currentLeftParenthesisAmount, maxParenthesisAmount);
        }

    }
}

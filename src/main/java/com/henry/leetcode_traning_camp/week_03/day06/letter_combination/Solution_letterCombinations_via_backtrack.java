package com.henry.leetcode_traning_camp.week_03.day06.letter_combination;

import java.util.LinkedList;
import java.util.List;

// 验证：对于从多个集合中增量式地构建结果的问题，可以使用“选择 + 递归 + 回溯” + “收集结果”的做法 来 得到最终的结果集
// 对于字母组合问题，递归方法需要一个参数 来 记录“当前被处理的数字字符”
public class Solution_letterCombinations_via_backtrack {
    private static final String[] digitToLettersArr = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public static void main(String[] args) {
        String inputDigitsStr = "23";

        List<String> letterCombinationList = generateAllPossibleLetterCombinationFrom(inputDigitsStr);

        System.out.println("字母所有可能的组合结果为： " + letterCombinationList);
    }

    private static List<String> letterCombinationList = new LinkedList<String>();
    private static String currentGeneratedCombination = "";

    private static List<String> generateAllPossibleLetterCombinationFrom(String inputDigits) {
        generateAndCollectLegitLetterCombinations(inputDigits, 0);
        return letterCombinationList;
    }

    // #0 递归方法的作用：从“数字字符序列”的[当前位置, 末尾位置)区间中，对于每个数字字符，逐个选取“其所对应的字母元素”来构造组合。
    // 🐖 #1 底层对应的其实就是构建N叉树的过程；
    // #2 参数越多，递归方法的作用越不容易解释 所以我们把 currentGeneratedCombination参数给去掉，使之成为一个成员变量
    private static void generateAndCollectLegitLetterCombinations(String inputDigits, int currentDigitCursor) {
        // #1 递归终止条件
        // 如果 数字指针已经指向数字序列的末尾，说明 所有数字都已经处理完成，则：
        if (currentDigitCursor >= inputDigits.length()) {
            // 把“当前生成的字母组合”添加到列表中
            letterCombinationList.add(currentGeneratedCombination);
            // 返回上一级调用
            return;
        }

        /* #2 本级递归要做的事情👇 */
        // #2-1 得到 当前输入数字 所对应的字母序列
        char currentDigitCharacter = inputDigits.charAt(currentDigitCursor);
        int currentDigitInInt = currentDigitCharacter - '0';
        String currentDigitsLetterSequence = digitToLettersArr[currentDigitInInt];

        // #2-2 对于“当前数字所对应的字母序列”中的每一个字母...
        for (int currentLetterCursor = 0; currentLetterCursor < currentDigitsLetterSequence.length(); currentLetterCursor++) {
            // Ⅰ 使用它 来 在当前字母组合的基础上继续构造“字母组合”
            char currentPickedLetter = currentDigitsLetterSequence.charAt(currentLetterCursor);
            addItInCombination(currentPickedLetter);

            // Ⅱ 在“剩下的字符序列”（规模更小的子问题）中，继续选择 字母元素 来 构造字母组合
            generateAndCollectLegitLetterCombinations(inputDigits, currentDigitCursor + 1);

            // Ⅲ 回溯“所选择的字母”
            backtrackPickedLetter();
        }
    }

    private static void backtrackPickedLetter() {
        currentGeneratedCombination = currentGeneratedCombination.substring(0, currentGeneratedCombination.length() - 1);
    }

    private static void addItInCombination(char currentPickedLetter) {
        currentGeneratedCombination = currentGeneratedCombination + currentPickedLetter;
    }
}

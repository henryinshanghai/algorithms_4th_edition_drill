package com.henry.leetcode_traning_camp.week_04.day02.generate_parenthesis_str;

import java.util.ArrayList;
import java.util.List;

// 结论：在递归调用前，抽取一个新的String变量作为参数 最终能够得到正确的结果。
public class Solution_generateParenthesis_extract_new_variable {
    public static void main(String[] args) {
        generateValidParenthesisStr(
                "",
                0,
                0);
        System.out.println(validParenthesisStrList);
    }

    // 使用成员变量 来 减少递归方法的参数
    private static int parenthesisPairAmount = 3;
    private static List<String> validParenthesisStrList = new ArrayList<>();

    // 🐖 这里的参数使用 当前括号字符串所使用的括号数量，代码更容易理解
    private static void generateValidParenthesisStr(String currentGeneratedParenthesisStr,
                                                    int currentUsingLeftParenthesisAmount,
                                                    int currentUsingRightParenthesisAmount) {
        // base case 找到了满足条件的括号组合
        if (currentGeneratedParenthesisStr.length() == 2 * parenthesisPairAmount) {
            validParenthesisStrList.add(currentGeneratedParenthesisStr);
            return;
        }

        // 如果左括号没有用完，说明 可以继续合法地 添加左括号，则：
        if (currentUsingLeftParenthesisAmount < parenthesisPairAmount) {
            // 🐖 由于currentGeneratedParenthesisStr是递归方法的参数，所以每次是参数发生了变化，而变量本身并没有发生变化。
            // 因此这里看不到对变量的回溯过程 👇
            // 向“括号组合字符串”中 继续添加左括号
            String appendLeftParenthesis = currentGeneratedParenthesisStr + "(";
            generateValidParenthesisStr(appendLeftParenthesis,
                    currentUsingLeftParenthesisAmount + 1,
                    currentUsingRightParenthesisAmount);
        }

        // 如果 “当前括号组合字符串”中的左括号更多，说明可以继续添加右括号，则：
        if (currentUsingLeftParenthesisAmount > currentUsingRightParenthesisAmount) {
            // 向“括号组合字符串”中继续添加右括号
            String appendRightParenthesis = currentGeneratedParenthesisStr + ")";
            generateValidParenthesisStr(appendRightParenthesis,
                    currentUsingLeftParenthesisAmount,
                    currentUsingRightParenthesisAmount + 1);
        }
    }
} // this seems better. is this got a better time complicity? no I don't think so, it is expo...

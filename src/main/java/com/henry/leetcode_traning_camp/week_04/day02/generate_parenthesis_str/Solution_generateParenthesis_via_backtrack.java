package com.henry.leetcode_traning_camp.week_04.day02.generate_parenthesis_str;

import java.util.ArrayList;
import java.util.List;

// 验证：可以使用 在每个位置上选择左括号或右括号的方式以得到二叉树模型 来 解决“有效的括号字符串组合”问题
// 组合出/添加出“有效的括号字符串”的手段 {① 添加左括号; ② 左括号更多时，添加右括号}
public class Solution_generateParenthesis_via_backtrack {
    public static void main(String[] args) {
        generateValidParenthesisStr("", 0, 0);
        System.out.println(validParenthesisStrList);
    }

    // 使用成员变量 来 减少递归方法的参数
    private static int parenthesisPairAmount = 3;
    private static List<String> validParenthesisStrList = new ArrayList<>();

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
            generateValidParenthesisStr(currentGeneratedParenthesisStr + "(", currentUsingLeftParenthesisAmount + 1, currentUsingRightParenthesisAmount);
        }

        // 如果 “当前括号组合字符串”中的左括号更多，说明可以继续添加右括号，则：
        if (currentUsingLeftParenthesisAmount > currentUsingRightParenthesisAmount) {
            // 向“括号组合字符串”中继续添加右括号
            generateValidParenthesisStr(currentGeneratedParenthesisStr + ")", currentUsingLeftParenthesisAmount, currentUsingRightParenthesisAmount + 1);
        }
    }
} // this seems better. is this got a better time complicity? no I don't think so, it is expo...
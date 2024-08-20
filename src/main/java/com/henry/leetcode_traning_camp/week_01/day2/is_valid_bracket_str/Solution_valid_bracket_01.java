package com.henry.leetcode_traning_camp.week_01.day2.is_valid_bracket_str;

import java.util.Stack;

// 验证：利用栈的FILO特性 能够很好地处理/判断 成对括号是否匹配的工作
// 手段：遇到左括号的时候，向栈中添加对应的右括号
public class Solution_valid_bracket_01 {
    public static void main(String[] args) {
        String testStr = "{}()[](";

        if (isValidBracketStr(testStr)) {
            System.out.println("this is valid brace string");
        } else {
            System.out.println("nah~");
        }
    }

    private static boolean isValidBracketStr(String bracketCharacterSequence) {
        Stack<Character> rightBracketStack = new Stack<>();

        // 当前字符 要么是左括号，要么是右括号
        for (char currentCharacter : bracketCharacterSequence.toCharArray()) {
            // 如果 当前字符是左括号的话，就把其对应的右括号添加到 栈结构中
            if (currentCharacter == '(') {
                rightBracketStack.push(')');
            } else if (currentCharacter == '[') {
                rightBracketStack.push(']');
            } else if (currentCharacter == '{') {
                rightBracketStack.push('}');
            }
            // 如果 当前字符 是右括号的话；
            else if (rightBracketStack.isEmpty() || currentCharacter != rightBracketStack.pop()) {
                // #1 如果栈已经空了，说明在此字符之前 不存在与之匹配的左括号(否则栈不为空)；则：字符串不是“有效括号对字符串” 或者
                // #2 如果当前字符 与 栈顶字符(某种类型的右括号) 不相等，说明 左右括号没有匹配，则：字符串不是“有效括号对字符串”
                return false;
            }
        }

        return rightBracketStack.isEmpty();
    }
}

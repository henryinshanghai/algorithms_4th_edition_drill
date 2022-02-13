package com.henry.leetcode_traning_camp.week_01.day2;

import java.util.Stack;

public class Solution_valid_bracket_01 {
    public static void main(String[] args) {
        String testStr = "{}()[";

        if (isValidBrace(testStr)) {
            System.out.println("this is valid brace string");
        } else {
            System.out.println("nah~");
        }
    }

    private static boolean isValidBrace(String s) {
        Stack<Character> stack = new Stack<>();

        for (char ch : s.toCharArray()) {
            if (ch == '(') {
                stack.push(')');
            } else if (ch == '[') {
                stack.push(']');
            } else if (ch == '{') {
                stack.push('}');
            } else if (stack.isEmpty() || ch != stack.pop()) {
                return false;
            }
        }

        return stack.isEmpty();
    }
}

package com.henry.leetcode_traning_camp.week_03.day06;

import java.util.ArrayList;
import java.util.List;

public class Solution_letterCombinations_02_method02_backToBack_drill01 {
    public static void main(String[] args) {

        String phoneNumber = "23";

        List<String> res = phoneMnenomic(phoneNumber);

        System.out.println("字母所有可能的组合结果为： " + res);

    }

    private static List<String> phoneMnenomic(String phoneNumber) {
        // 准备一个字符串列表
        List<String> res = new ArrayList<>();

        // 准备一个字符数组用于存储“满足条件的字符组合”
        char[] currCharCombine = new char[phoneNumber.length()];

        // 调用回溯方法   实现目标：向res中添加所有满足条件的字符串
        backtracking(phoneNumber, 0, currCharCombine, res); // 这里需要一个字符数组类型的参数来表示字符组合

        return res;
    }

    // 准备一个静态的字符串数组来存储手机键盘上“数字键 与 可选字母”的对应关系
    private static String[] MAPPING = {
            "0",
            "1",
            "ABC",
            "DEF",
            "GHI",
            "JKL",
            "MNO",
            "PQRS",
            "TUV",
            "WXYZ"
    }; // 一共9个键（不是每个数字键都有自己的可选字母）


    private static void backtracking(String phoneNumber, int digit,
                                     char[] currCharCombine,
                                     List<String> res) {
        // base case: 目标————把满足添加的字符串添加到res中
        if (digit == phoneNumber.length()) { // 当前字符组合已经填充了N个格子
            res.add(new String(currCharCombine));
        } else {
            // choices：做选择 根据当前数字对应的字符逐一进行选择
            // 当前的数字键：从phoneNumber中获取  phoneNumber.chatAt(当前位置)     结果得到的是一个char类型的值
            // 数字键对应的可选字母： 从MAPPING数组中找到数字键映射的结果    MAPPING[数字键的索引]  数字键的索引 = '数字字符' - '0字符'
            for (int i = 0; i < MAPPING[phoneNumber.charAt(digit) - '0'].length(); i++) { // for循环中的遍历变量i，即便在循环体中没有用到也不会变成灰色 因为循环表达式中已经用到它了
                // 获取到当前的字符
                char currChar = MAPPING[phoneNumber.charAt(digit) - '0'].charAt(i); // EXPR：做选择时，这里应该是i
                // 添加到“字母组合”中
                currCharCombine[digit] = currChar;

                // 当前选择完成后，进入下一个数字键的字母选择
                backtracking(phoneNumber, digit + 1, currCharCombine, res);

                // 选择完成后，会直接返回上一级（相当于回溯）。所以不需要removal
            }
        }
    }

}
// EXPR：做选择时，需要使用的变量是 遍历变量i————这样才能对当前数字键对应的字符组合进行选择
package com.henry.leetcode_traning_camp.week_03.day06;

import java.util.ArrayList;
import java.util.List;

public class Solution_letterCombinations_02_method02_backToBack {
    public static void main(String[] args) {
        String originalStr = "23";

        List<String> res = phoneMnenomic(originalStr);

        System.out.println("字母所有可能的组合结果为： " + res);
    }

    private static List<String> phoneMnenomic(String phoneNumber) {
        // 准备一个与“用户输入的数字字符串”大小相同的字符数组；  用于存储字符组合的结果
        char[] partialMnemonic = new char[phoneNumber.length()];

        // 准备一个字符串列表    用于存储所有可能的字符组合
        List<String> mnemonics = new ArrayList<>();

        // 调用回溯方法，为phoneNumber.length()个格子添加元素
        phoneMnemonicHelper(phoneNumber, 0, partialMnemonic, mnemonics);
        return mnemonics;
    }

    // 准备一个字符串数组（静态）
    private static final String[] MAPPING = {
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
    };

    /**
     *
     * @param phoneNumber 需要处理的原始数字字符串
     * @param digit 当前在为第几个格子来添加字符
     * @param partialMnemonic 当前构造出的字符组合
     * @param mnemonics 所有字符组合的集合
     */
    private static void phoneMnemonicHelper(String phoneNumber, int digit,
                                              char[] partialMnemonic,
                                              List<String> mnemonics) {
        // 回溯的三个要点：choice、constraint、goal
        // 这个问题并没有给出约束，所以只需要关心：选择与目标

        /* goal：把n个格子都放满字符 得到预期的字符组合 */
        if (digit == phoneNumber.length()) { // 如果当前的digit大小 已经与 用户输入的原始字符串的长度 相同，说明字符组合已经构造完成
            // 则：把构造完成的字符串添加到字符串列表中
            mnemonics.add(new String(partialMnemonic));
        } else {
            // 递归方法的作用：在n个格子中放置与数字键匹配的字母
            // 开始进行选择   选择会从0开始，然后遍历MAPPING中字符串持有的字符数
            for (int i = 0;  i < MAPPING[phoneNumber.charAt(digit) - '0'].length(); i++) { // 遍历“用户键入的数字字符”所关联的“所有可选字符”
                // A - B - C
                // 获取到当前的可选字符
                // 当digit发生变化时，字符c会随之发生变化
                char c = MAPPING[phoneNumber.charAt(digit) - '0'].charAt(i);
                // 把得到的字符c 放置到 字符组合中预期的位置
                partialMnemonic[digit] = c; // 只有递归执行到底时，才会把partialMnemonic添加到res中

                // 选择结束后，开始再次调用递归函数：把digit改动成为digit+1
                // 作用：处理下一个digit
                phoneMnemonicHelper(phoneNumber, digit+1, partialMnemonic, mnemonics);

                // 为什么没有回溯所需的经典removal？
                // 可能与String的API有关
            }
        } // place - choose - make a choice     explore it + make another place
    // 每一层递归都会做出多个选择（对应当前层的多个节点）
    }
}
// 时间复杂度： O(n * 4^n)
// 空间复杂度：O(4^n)

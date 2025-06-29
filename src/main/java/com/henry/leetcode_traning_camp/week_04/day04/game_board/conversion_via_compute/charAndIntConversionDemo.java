package com.henry.leetcode_traning_camp.week_04.day04.game_board.conversion_via_compute;

// 验证：#1 对于int类型的变量，可以通过 +'0'，并且(char)强制转换的手段 来 得到其所对应的字符；
// #2 对于char类型的变量，可以通过-'0'的手段 来 得到其所对应的int值；
// 原理：Java中的类型提升规则。int×char->int; (char)int->char; char×char->int;
public class charAndIntConversionDemo {
    public static void main(String[] args) {
        /* int -> char  手段：intNumber + '0' */
        // 为什么int变量 与 char变量相加，结果是一个int类型呢? 答：Java中的类型提升规则，向取值范围更大的类型转换
        int intNumber = 8;
        int conversionIntResult = intNumber + '0'; // '0'的ASCII码值是48，因此表达式运算为 8 + 48 = 56
        System.out.println("int与char的二元运算会得到int的结果：" + conversionIntResult);
        char conversionCharResult = (char) conversionIntResult; // 查看ASCII码表，56所对应的字符为'8'
        System.out.println("再经(char)强制转换后得到的结果为：" + conversionCharResult);

        /* char -> int  手段：char - '0' */
        // 为什么 char变量 与 char变量 相见，结果是一个int类型呢？
        // 答：Java中的类型提升规则 - 当对小于int的整数类型进行二元算术运算时，会将它们提升为int类型
        char char1 = '8';
        char char2 = '0';
        int computeResult = char1 - char2;
        System.out.println("两个char变量相减得到的结果为：" + computeResult);
    }
}

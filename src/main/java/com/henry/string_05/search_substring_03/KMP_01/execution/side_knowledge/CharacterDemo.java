package com.henry.string_05.search_substring_03.KMP_01.execution.side_knowledge;

// 验证：可以使用 Character的getNumericValue()方法 来 实现 numChar -> numInt的字面转换
// 输入：一个整数字符； 输出：与字符字面等值的整数；
public class CharacterDemo {
    public static void main(String[] args) {
        // #1 整数字符输入，会得到 与字面值相同的整数值
        char numChar = '8';
        int numResult = Character.getNumericValue(numChar);
        System.out.println("numResult: " + numResult);

        // #2 非整数字符输入，也会得到一个整数值（具体规则未知）
        char[] capitalAlphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',};
        for (char currentCharacter : capitalAlphabet) {
            System.out.println("value for " + currentCharacter + " is: " + Character.getNumericValue(currentCharacter));
        }

        char[] lowerCaseAlphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',};
        for (char currentCharacter : lowerCaseAlphabet) {
            System.out.println("value for " + currentCharacter + " is: " + Character.getNumericValue(currentCharacter));
        }
    }
}

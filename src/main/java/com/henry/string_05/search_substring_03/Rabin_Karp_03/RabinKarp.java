package com.henry.string_05.search_substring_03.Rabin_Karp_03;

/******************************************************************************
 *  Compilation:  javac RabinKarp.java
 *  Execution:    java RabinKarp pat txt
 *  Dependencies: StdOut.java
 *
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  Las Vegas version of the Rabin-Karp algorithm.
 *
 *  % java RabinKarp abracadabra abacadabrabracabracadabrabrabracad
 *  pattern: abracadabra
 *  text:    abacadabrabracabracadabrabrabracad
 *  match:                 abracadabra
 *
 *  % java RabinKarp rab abacadabrabracabracadabrabrabracad
 *  pattern: rab
 *  text:    abacadabrabracabracadabrabrabracad
 *  match:           rab
 *
 *  % java RabinKarp bcara abacadabrabracabracadabrabrabracad
 *  pattern: bcara
 *  text:         abacadabrabracabracadabrabrabracad
 *
 *  %  java RabinKarp rabrabracad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad
 *
 *  % java RabinKarp abacad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

import java.math.BigInteger;
import java.util.Random;

// 验证：使用Rabin-Karp算法（快速计算文本子字符串的散列值，并在txtSubStrHash与patStrHash相同时逐个比较字符），能够实现 在文本字符串中查找 与模式字符串匹配的子字符串
// 概念：current_clip，current_tried_match_spot
public class RabinKarp {
    private String patternStr;      // 模式字符串
    private long patternStrHash;    // 模式字符串的哈希值
    private int patStrLength;           // 模式字符串的长度

    private long largePrime;          // 一个大的素数，但是避免long溢出
    private int characterOptionsAmount;           // 可供选择的字符数量
    private long weightOfFirstDigit;         // R^(M-1) % Q ??? 第一个数字的权重??

    /**
     * Preprocesses the pattern string.
     * 预处理模式字符串
     * @param pattern                the pattern string 模式字符串
     * @param characterOptionsAmount the alphabet size 字母表的大小
     */
    public RabinKarp(char[] pattern, int characterOptionsAmount) {
        this.patternStr = String.valueOf(pattern);
        this.characterOptionsAmount = characterOptionsAmount;
        throw new UnsupportedOperationException("Operation not supported yet");
    }

    /**
     * Preprocesses the pattern string.
     * 预处理模式字符串
     * @param patternStr the pattern string
     */
    public RabinKarp(String patternStr) {
        this.patternStr = patternStr;      // 用于保存模式字符串（仅仅对于 Las Vegas适用）
        characterOptionsAmount = 256; // aka baseSize
        patStrLength = patternStr.length();
        largePrime = longRandomPrime();

        // precompute R^(m-1) % q for use in removing leading digit
        // #1 预计算出 R^(m-1) % q [首字符(leading digit)对应的权重] - 用于移除首位数字
        weightOfFirstDigit = 1;
        for (int currentRound = 1; currentRound <= patStrLength - 1; currentRound++)
            weightOfFirstDigit = (weightOfFirstDigit * characterOptionsAmount) % largePrime;

        // #2 计算出 模式字符串的hash值
        patternStrHash = hashOfSubStrTill(patternStr, patStrLength);
    }

    // Compute hash for key[0..m-1].
    // 计算 key[0..m-1]截取得到的子字符串 的hash值
    private long hashOfSubStrTill(String passedStr, int rightBoundary) {
        long currentHashValue = 0;

        for (int currentSpot = 0; currentSpot < rightBoundary; currentSpot++) {
            // 计算公式：当前数值结果 * 进制数 + 当前数码 * 1
            char currentCharacter = passedStr.charAt(currentSpot);
            currentHashValue = (currentHashValue * characterOptionsAmount + currentCharacter * 1) % largePrime;
        }

        // 返回计算出的hash值
        return currentHashValue;
    }

    // Las Vegas version: does pat[] match txt[i..i-m+1] ?
    private boolean compareCharactersThenDetermineMatchOrNot(String txtStr, int startingPointToCompare) {
        // 匹配的定义：对于特定的偏移量offset，文本字符串T[offset, offset + patLength]的子字符串 与 patternStr字符串 完全相同
        for (int currentSpot = 0; currentSpot < patStrLength; currentSpot++) {
            // #1 获取 当前位置的模式字符
            char currentPatternCharacter = patternStr.charAt(currentSpot);
            // #2 获取 对应位置(起点位置 + 当前比较位置)上的文本字符
            char currentTxtCharacter = txtStr.charAt(startingPointToCompare + currentSpot);

            if (currentPatternCharacter != currentTxtCharacter)
                return false;
        }
        return true;
    }

    // Monte Carlo version: always return true
    // private boolean check(int i) {
    //    return true;
    //}

    // 返回 模式字符串在文本字符串中第一次出现的位置
    // 如果文本字符串中不存在这样的match，则返回 n
    public int search(String txtStr) {
        int txtStrLength = txtStr.length();
        if (txtStrLength < patStrLength) return txtStrLength;

        // #1-Ⅰ 从文本字符串中截取 [0..patStrLength]的子字符串clip，并计算出 其hash值
        long txtSubStrHash = hashOfSubStrTill(txtStr, patStrLength);

        // #1-Ⅱ 检查 位置0是不是一个 能够发生匹配的位置(matched_spot) 如果匹配，则：返回位置0
        if (findAMatchInSpot0(txtStr, txtSubStrHash))
            return 0;

        // #2 对于 文本字符串中从patStrLength开始的每一个位置（可能发生匹配的位置）...
        for (int currentTxtCursor = patStrLength; currentTxtCursor < txtStrLength; currentTxtCursor++) {
            // #2-Ⅰ 为 以“当前文本指针所指向的位置”为结束位置的 子字符串，计算其hash值
            txtSubStrHash = calculateHashForNextSubStr(txtSubStrHash, txtStr, currentTxtCursor);

            // #2-Ⅱ 判断 “当前文本指针”所对应的“当前尝试匹配位置” 是否 产生了一个匹配
            // 计算出 当前尝试匹配位置
            int currentTriedMatchSpot = (currentTxtCursor - patStrLength) + 1;
            // 如果 发生了匹配，则：返回 当前尝试匹配的位置
            if (findAMatchOn(txtStr, txtSubStrHash, currentTriedMatchSpot))
                return currentTriedMatchSpot;
        }

        // #3 如果代码执行到此处，说明 所有“可能发生匹配的位置”，都没有发生匹配。则：
        // 返回文本字符串的长度 来 表示匹配失败
        return txtStrLength;
    }

    // 移除leading数码，添加trailing数码，从而 得到新的文本子字符串的hash值
    // 示例：从123456中截取长度为5的clip   初级计算公式：(12345 - 1 * 10^4) * 10 + 6 = 23456
    // 经验：有些上下文信息（参数的具体上下文）应该由真实的入参提供，这样方法的形参中就可以不包含这些信息，由此来简化方法中的变量名
    // implement_SOP_05:（🐖 在txtHash的计算中，额外加上了一个Q 来 保证所有的数都为正，这样 取余操作 才能够得到预期的结果）
    private long calculateHashForNextSubStr(long currentSubStrHash, String txtStr, int clipEndSpot) {
        // #0 计算出 当前首字符的真实值
        int clipStartSpot = clipEndSpot - patStrLength;
        char startCharacter = txtStr.charAt(clipStartSpot);
        long startCharactersTrueValue = (startCharacter * weightOfFirstDigit) % largePrime; // 10000

        // #1 计算出 “移除首字符的真实值”之后的剩余差值；
        // 手段：计算公式 = 当前哈希结果值 - 首位数字的真实值
        // 🐖 这里 +大素数、%大素数的操作 是为了不溢出吗???
        currentSubStrHash = (currentSubStrHash + largePrime - startCharactersTrueValue) % largePrime; // 2345

        // #2 基于#1中的差值，计算出新的字符串的哈希结果；
        // 手段：计算公式 = 差值 * 进制基数/baseSize + 下一个字符的数值
        // 🐖 这里%大素数的操作 是为了哈希结果能够落在散列表中??
        char endCharacter = txtStr.charAt(clipEndSpot);
        currentSubStrHash = (currentSubStrHash * characterOptionsAmount + endCharacter * 1) % largePrime; // 2345*10 + 6*1

        return currentSubStrHash; // 23456
    }

    private boolean findAMatchOn(String txtStr, long subTxtHash, int triedMatchSpot) {
        // #1 当前clip的hash值 与 模式字符串的hash值是否相等
        // #2 逐个字符比较 判断是否匹配
        return (patternStrHash == subTxtHash) && compareCharactersThenDetermineMatchOrNot(txtStr, triedMatchSpot);
    }

    private boolean findAMatchInSpot0(String passedTxtStr, long txtSubStrHash) {
        // #1 文本字符串的子字符串的hash值 是不是 与模式字符串的hash值 相同；并且
        // #2 检查 子字符串 与 模式字符串 是不是 在每个字符都是匹配的
        return equalsToPatStrHash(txtSubStrHash) && everyCharacterMatchWithPatStr(passedTxtStr);
    }

    private boolean everyCharacterMatchWithPatStr(String passedTxtStr) {
        int offsetFromSpot0InTxtStr = 0;
        return compareCharactersThenDetermineMatchOrNot(passedTxtStr, offsetFromSpot0InTxtStr);
    }

    private boolean equalsToPatStrHash(long txtHash) {
        return patternStrHash == txtHash;
    }


    // 生成一个随机的31位的素数
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    /**
     * Takes a pattern string and an input string as command-line arguments;
     * searches for the pattern string in the text string; and prints
     * the first occurrence of the pattern string in the text string.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String patternStr = args[0];
        String txtStr = args[1];

        // #1 预计算出 pat_str的hash值 与 clip首字符的权重
        RabinKarp searcher = new RabinKarp(patternStr);
        // 手段：search()方法接收文本字符串 来 得到匹配所需的偏移量
        int offset = searcher.search(txtStr);

        // print results
        StdOut.println("text:    " + txtStr);

        // from brute force search method 1
        StdOut.print("pattern: ");
        for (int currentSpot = 0; currentSpot < offset; currentSpot++)
            StdOut.print(" ");
        StdOut.println(patternStr);
    }
}
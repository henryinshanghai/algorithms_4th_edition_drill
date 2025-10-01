package com.henry.string_05.search_substring_03.Rabin_Karp_03.execution;

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
     * 预处理 模式字符串
     * @param pattern                模式字符串
     * @param characterOptionsAmount 字母表的大小
     */
    public RabinKarp(char[] pattern, int characterOptionsAmount) {
        this.patternStr = String.valueOf(pattern);
        this.characterOptionsAmount = characterOptionsAmount;
        throw new UnsupportedOperationException("Operation not supported yet");
    }

    /**
     * 预处理 模式字符串
     * @param patternStr 模式字符串
     */
    public RabinKarp(String patternStr) {
        this.patternStr = patternStr;      // 用于保存模式字符串（仅仅对于 Las Vegas适用）
        characterOptionsAmount = 256; // 字母表的大小
        patStrLength = patternStr.length();
        largePrime = longRandomPrime();

        // #1 预计算出 首字符(leading digit)对应的权重：R^(m-1) % Q - 用于 在 计算下一个子字符串的hash值 时，移除首位数字
        // 例子：256
        weightOfFirstDigit = 1;
        for (int currentRound = 1; currentRound <= patStrLength - 1; currentRound++)
            weightOfFirstDigit = (weightOfFirstDigit * characterOptionsAmount) % largePrime;

        // #2 计算出 模式字符串的hash值
        patternStrHash = hashOfSubStrTill(patternStr, patStrLength);
    }

    // 计算 key[0..m-1]区间 所截取得到的 子字符串的hash值
    // 手段：把 字符串 视为 一个R进制的M位数的数字，计算 该数字的数值 即可
    private long hashOfSubStrTill(String passedStr, int rightBoundary) {
        long currentHashValue = 0;

        for (int currentSpot = 0; currentSpot < rightBoundary; currentSpot++) {
            // 计算公式：当前数值结果 * 进制数 + 当前数码 * 1
            char currentCharacter = passedStr.charAt(currentSpot);
            currentHashValue = (currentHashValue * characterOptionsAmount + currentCharacter * 1) % largePrime;
        }

        // 返回 计算出的hash值
        return currentHashValue;
    }

    // 拉斯维加斯的版本：模式字符串 是否匹配 文本字符串的[i..i-m+1]的子字符串?
    private boolean characterMatchOnEachSpot(String txtStr, int startingPointToCompare) {
        // 匹配的定义：对于特定的偏移量offset，文本字符串T[offset, offset + patLength]的子字符串 与 patternStr字符串 完全相同
        for (int currentSpot = 0; currentSpot < patStrLength; currentSpot++) {
            // #1 获取 当前位置的模式字符
            char currentPatternCharacter = patternStr.charAt(currentSpot);
            // #2 获取 对应位置(文本指针起点位置 + 当前比较位置)上的文本字符
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

    // 返回 模式字符串 在文本字符串中 第一次出现的位置
    // 如果 文本字符串中 不存在 这样的match，则 返回 n
    public int search(String txtStr) {
        int txtStrLength = txtStr.length();
        if (txtStrLength < patStrLength) return txtStrLength;

        // #1-Ⅰ 从 文本字符串 中 截取出 [0..patStrLength]的子字符串clip，并 计算出 其hash值
        long txtSubStrHash = hashOfSubStrTill(txtStr, patStrLength);

        // #1-Ⅱ 检查 位置0是不是一个 能够发生匹配的位置(matched_spot) 如果匹配，则：返回位置0
        if (findAMatchOnSpot0(txtStr, txtSubStrHash))
            return 0;

        // #2 对于 文本字符串中 从patStrLength开始的 每一个位置（aka 所有 可能发生匹配的 位置）...
        // 🐖 奇奇怪怪的实现：突然使用 clipEndSpot 来 截取 文本子字符串了
        for (int currentClipEndSpot = patStrLength; currentClipEndSpot < txtStrLength; currentClipEndSpot++) {
            // #2-Ⅰ 为 以“当前文本指针所指向的位置”为结束位置的 子字符串，计算其hash值
            txtSubStrHash = calculateHashForNextSubStr(currentClipEndSpot, txtStr, txtSubStrHash);

            // #2-Ⅱ 判断 “当前文本指针”所对应的“当前尝试匹配位置” 是否 产生了一个匹配
            // 计算出 当前尝试匹配位置
            int currentTriedMatchSpot = (currentClipEndSpot - patStrLength) + 1;
            // 如果 发生了匹配，则：返回 当前尝试匹配的位置
            if (findAMatchOn(txtStr, txtSubStrHash, currentTriedMatchSpot))
                return currentTriedMatchSpot;
        }

        // #3 如果代码 执行到了 此处，说明 所有“可能发生匹配的位置”都 没有发生匹配。则：
        // 返回 文本字符串的长度 来 表示匹配失败
        return txtStrLength;
    }

    // 作用：移除 leading数码，添加 trailing数码，从而 得到 新的文本子字符串的hash值
    // 示例：从123456中 截取 长度为5的clip   初级计算公式：(12345 - 1 * 10^4) * 10 + 6 = 23456
    // 经验：有些上下文信息（参数的具体上下文）应该 由真实的入参提供，这样方法的形参中 就可以 不包含这些信息，由此 来 简化方法中的变量名
    // implement_SOP_05:（🐖 在 txtHash的计算 中，额外加上了一个Q 来 保证所有的数 都为正，这样 取余操作 才能够得到 预期的结果）
    private long calculateHashForNextSubStr(int clipEndSpot, String txtStr, long currentSubStrHash) {
        // #0 计算出 当前首字符的真实值
        int clipStartSpot = clipEndSpot - patStrLength;
        char startCharacter = txtStr.charAt(clipStartSpot);
        long startCharactersTrueValue = (startCharacter * weightOfFirstDigit) % largePrime; // 10000

        // #1 计算出 “移除首字符的真实值”之后的 剩余差值；
        // 手段：计算公式 = 当前哈希结果值 - 首位数字的真实值
        // 🐖 这里 +大素数、%大素数的操作 是为了不溢出吗???
        currentSubStrHash = (currentSubStrHash + largePrime - startCharactersTrueValue) % largePrime; // 2345

        // #2 基于 #1中的差值，计算出 新的字符串的哈希结果；
        // 手段：计算公式 = 差值 * 进制基数R/baseSize + 下一个字符的数值 * 1
        // 🐖 这里%大素数的操作 是为了哈希结果能够落在散列表中??
        char endCharacter = txtStr.charAt(clipEndSpot);
        currentSubStrHash = (currentSubStrHash * characterOptionsAmount + endCharacter * 1) % largePrime; // 2345*10 + 6*1

        return currentSubStrHash; // 23456
    }

    private boolean findAMatchOn(String txtStr, long subTxtHash, int triedMatchSpot) {
        // #1 当前clip的hash值 与 模式字符串的hash值是否相等
        // #2 逐个字符比较 判断是否匹配
        return equalsToPatStrHash (subTxtHash) && characterMatchOnEachSpot(txtStr, triedMatchSpot);
    }

    private boolean findAMatchOnSpot0(String passedTxtStr, long txtSubStrHash) {
        // #1 文本字符串的 子字符串的hash值 是不是 与 模式字符串的hash值 相同；并且
        // #2 检查 子字符串 与 模式字符串 是不是 在每个字符都是匹配的
        return equalsToPatStrHash(txtSubStrHash) && everyCharacterMatchWithPatStr(passedTxtStr);
    }

    private boolean everyCharacterMatchWithPatStr(String passedTxtStr) {
        int offsetFromSpot0InTxtStr = 0;
        return characterMatchOnEachSpot(passedTxtStr, offsetFromSpot0InTxtStr);
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
     * 接受一个 模式字符串 以及 一个输入字符串 作为 命令行参数；
     * 在 文本字符串 中 搜索模式字符串；
     * 打印出 模式字符串 在文本字符串中 第一次出现的位置
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        String patternStr = args[0];
        String txtStr = args[1];

        // #1 预计算出 pat_str的hash值 与 clip首字符的权重
        RabinKarp searcher = new RabinKarp(patternStr);
        // #2 获取到 发生匹配的位置
        // 手段：search()方法 接收 文本字符串 来 得到 发生匹配的位置 (aka 匹配所需的偏移量)
        int offset = searcher.search(txtStr);

        /* #3 打印匹配结果 */
        StdOut.println("text:    " + txtStr);

        StdOut.print("pattern: ");
        for (int currentSpot = 0; currentSpot < offset; currentSpot++)
            StdOut.print(" ");
        StdOut.println(patternStr);
    }
}
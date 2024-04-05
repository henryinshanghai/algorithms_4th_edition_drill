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

/**
 * The {@code RabinKarp} class finds the first occurrence of a pattern string
 * in a text string.
 * 这个类 用于从文本字符串中寻找模式字符串第一个出现的位置(first occurrence)
 * <p>
 * This implementation uses the Rabin-Karp algorithm.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/53substring">Section 5.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
// 验证：使用Rabin-Karp算法（快速计算文本子字符串的散列值，并在txtSubStrHash与patStrHash相同时逐个比较字符），能够实现 在文本字符串中查找 与模式字符串匹配的子字符串
public class RabinKarp {
    private String patternStr;      // the pattern  // needed only for Las Vegas
    private long patternStrHash;    // pattern hash value
    private int patStrLength;           // pattern length

    private long largePrime;          // a large prime, small enough to avoid long overflow
    private int characterOptionsAmount;           // radix
    private long weightOfFirstDigit;         // R^(M-1) % Q ???

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
     * @param passedPatternStr the pattern string
     */
    public RabinKarp(String passedPatternStr) {
        this.patternStr = passedPatternStr;      // save pattern (needed only for Las Vegas)
        characterOptionsAmount = 256; // aka baseSize
        patStrLength = passedPatternStr.length();
        largePrime = longRandomPrime();

        // precompute R^(m-1) % q for use in removing leading digit
        // 预计算出 首字符对应的权重
        weightOfFirstDigit = 1;
        for (int currentRound = 1; currentRound <= patStrLength - 1; currentRound++)
            weightOfFirstDigit = (weightOfFirstDigit * characterOptionsAmount) % largePrime;

        patternStrHash = subStrHashOfRange(passedPatternStr, patStrLength);
    }

    // Compute hash for key[0..m-1].
    private long subStrHashOfRange(String passedStr, int rightBoundary) {
        long currentHashValue = 0;

        for (int currentSpot = 0; currentSpot < rightBoundary; currentSpot++) {
            // 计算公式：当前数值结果 * 进制数 + 当前数码 * 1
            char currentCharacter = passedStr.charAt(currentSpot);
            currentHashValue = (currentHashValue * characterOptionsAmount + currentCharacter * 1) % largePrime;
        }

        return currentHashValue;
    }

    // Las Vegas version: does pat[] match txt[i..i-m+1] ?
    private boolean compareCharactersThenDetermineMatchOrNot(String passedTxt, int startingPointToCompare) {
        // 匹配的定义：对于特定的偏移量offset，文本字符串T[offset, offset + patLength]的子字符串 与 patternStr字符串 完全相同
        for (int currentSpot = 0; currentSpot < patStrLength; currentSpot++) {
            char currentPatternCharacter = patternStr.charAt(currentSpot);
            char currentTxtCharacter = passedTxt.charAt(startingPointToCompare + currentSpot);

            if (currentPatternCharacter != currentTxtCharacter)
                return false;
        }
        return true;
    }

    // Monte Carlo version: always return true
    // private boolean check(int i) {
    //    return true;
    //}

    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string.
     * 返回 模式字符串在文本字符串中第一次出现的位置
     * @param passedTxtStr the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; n if no such match
     */
    public int search(String passedTxtStr) {
        int txtStrLength = passedTxtStr.length();
        if (txtStrLength < patStrLength) return txtStrLength;

        long txtSubStrHash = subStrHashOfRange(passedTxtStr, patStrLength);

        // check for match at offset 0
        if (findAMatchInSpot0(passedTxtStr, txtSubStrHash))
            return 0;

        // 检查hash匹配情况； 如果hash匹配的话，检查字符是否匹配
        // 123456 初级计算公式：(12345 - 1 * 10^4) * 10 + 6 = 23456
        // 🐖 这里使用endSpotCursor作为游标 是一个语义不很清晰的选择 - 原作者的代码未必是最好的代码
        for (int endSpotCursor = patStrLength; endSpotCursor < txtStrLength; endSpotCursor++) {
            txtSubStrHash = calculateNextTxtSubStrsHashBasedOn(txtSubStrHash, passedTxtStr, endSpotCursor);

            // match: 哈希匹配 & 字符匹配
            // 计算出 相对于文本字符串首字符的偏移量
            int offsetFromSpot0 = endSpotCursor - patStrLength + 1;
            if (findAMatchInSpot(passedTxtStr, txtSubStrHash, offsetFromSpot0))
                return offsetFromSpot0;
        }

        // no match：如果不匹配，则： 返回文本字符串的长度
        return txtStrLength;
    }

    // 有些上下文信息（参数的具体上下文）应该由真实的入参提供，这样方法的形参中就可以不包含这些信息，由此来简化方法中的变量名
    // implement_SOP_05:（🐖 在txtHash的计算中，额外加上了一个Q 来 保证所有的数都为正，这样 取余操作 才能够得到预期的结果）
    private long calculateNextTxtSubStrsHashBasedOn(long currentHash, String passedStr, int endSpot) {
        // 移除leading数码，添加trailing数码，并检查匹配
        int startSpot = endSpot - patStrLength;
        char startCharacter = passedStr.charAt(startSpot);
        long startCharactersTrueValue = (startCharacter * weightOfFirstDigit) % largePrime;

        // #1 计算出移除首字符的真实值之后的剩余差值 = 当前哈希结果值 - 首位数字的真实值 🐖 这里 +大素数、%大素数的操作 是为了不溢出吗???
        currentHash = (currentHash + largePrime - startCharactersTrueValue) % largePrime;

        // #2 基于#1中的差值，计算出新的字符串的哈希结果 = 差值 * 进制基数/baseSize + 下一个字符的数值 🐖 这里%大素数的操作 是为了哈希结果能够落在散列表中??
        char endCharacter = passedStr.charAt(endSpot);
        currentHash = (currentHash * characterOptionsAmount + endCharacter * 1) % largePrime;
        return currentHash;
    }

    private boolean findAMatchInSpot(String passedTxtStr, long txtHash, int passedSpot) {
        return (patternStrHash == txtHash) && compareCharactersThenDetermineMatchOrNot(passedTxtStr, passedSpot);
    }

    private boolean findAMatchInSpot0(String passedTxtStr, long txtSubStrHash) {
        return equalsToPatStrHash(txtSubStrHash) && everyCharacterMatchWithPatStr(passedTxtStr);
    }

    private boolean everyCharacterMatchWithPatStr(String passedTxtStr) {
        int offsetFromSpot0InTxtStr = 0;
        return compareCharactersThenDetermineMatchOrNot(passedTxtStr, offsetFromSpot0InTxtStr);
    }

    private boolean equalsToPatStrHash(long txtHash) {
        return patternStrHash == txtHash;
    }


    // a random 31-bit prime
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

        // 把模式字符串 作为构造方法的参数
        RabinKarp searcher = new RabinKarp(patternStr);
        // search()方法接收文本字符串 来 得到匹配所需的偏移量
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
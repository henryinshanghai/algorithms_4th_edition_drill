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
 * <p>
 * This implementation uses the Rabin-Karp algorithm.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/53substring">Section 5.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class RabinKarp {
    private String patternStr;      // the pattern  // needed only for Las Vegas
    private long patternStrHash;    // pattern hash value
    private int patStrLength;           // pattern length

    private long largePrime;          // a large prime, small enough to avoid long overflow
    private int characterOptionsAmount;           // radix
    private long weightOfFirstDigit;         // R^(M-1) % Q ???

    /**
     * Preprocesses the pattern string.
     *
     * @param pattern                the pattern string
     * @param characterOptionsAmount the alphabet size
     */
    public RabinKarp(char[] pattern, int characterOptionsAmount) {
        this.patternStr = String.valueOf(pattern);
        this.characterOptionsAmount = characterOptionsAmount;
        throw new UnsupportedOperationException("Operation not supported yet");
    }

    /**
     * Preprocesses the pattern string.
     *
     * @param patternStr the pattern string
     */
    public RabinKarp(String patternStr) {
        this.patternStr = patternStr;      // save pattern (needed only for Las Vegas)
        characterOptionsAmount = 256;
        patStrLength = patternStr.length();
        largePrime = longRandomPrime();

        // precompute R^(m-1) % q for use in removing leading digit
        // 预计算出 首字符对应的权重
        weightOfFirstDigit = 1;
        for (int currentRound = 1; currentRound <= patStrLength - 1; currentRound++)
            weightOfFirstDigit = (weightOfFirstDigit * characterOptionsAmount) % largePrime;

        patternStrHash = hashOfRange(patternStr, patStrLength);
    }

    // Compute hash for key[0..m-1].
    private long hashOfRange(String passedStr, int rightBoundary) {
        long currentHashValue = 0;

        for (int currentSpot = 0; currentSpot < rightBoundary; currentSpot++) {
            char currentCharacter = passedStr.charAt(currentSpot);
            currentHashValue = (currentHashValue * characterOptionsAmount + currentCharacter * 1) % largePrime;
        }

        return currentHashValue;
    }

    // Las Vegas version: does pat[] match txt[i..i-m+1] ?
    private boolean check(String passedTxt, int passedOffset) {
        for (int currentSpot = 0; currentSpot < patStrLength; currentSpot++)
            // 匹配的定义：对于特定的偏移量offset，文本字符串T[offset, offset + patLength]的子字符串 与 patternStr字符串 完全相同
            if (patternStr.charAt(currentSpot) != passedTxt.charAt(currentSpot + passedOffset))
                return false;
        return true;
    }

    // Monte Carlo version: always return true
    // private boolean check(int i) {
    //    return true;
    //}

    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string.
     *
     * @param passedTxtStr the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; n if no such match
     */
    public int search(String passedTxtStr) {
        int txtStrLength = passedTxtStr.length();
        if (txtStrLength < patStrLength) return txtStrLength;

        long txtHash = hashOfRange(passedTxtStr, patStrLength);

        // check for match at offset 0
        if ((patternStrHash == txtHash) && check(passedTxtStr, 0))
            return 0;

        // check for hash match; if hash match, check for exact match
        // 123456
        // 初级计算公式：(12345 - 1 * 10^4) * 10 + 6 = 23456
        for (int currentCursorSpot = patStrLength; currentCursorSpot < txtStrLength; currentCursorSpot++) {
            // Remove leading digit, add trailing digit, check for match.
            char leadingDigit = passedTxtStr.charAt(currentCursorSpot - patStrLength);
            long leadingDigitsValue = (leadingDigit * weightOfFirstDigit) % largePrime;
            // 差值 = 当前值 - 首位数字的数值（这里为什么会有+素数 以及 %素数的操作呢？）
            txtHash = (txtHash + largePrime - leadingDigitsValue) % largePrime;

            // 差值 * 进制基数 + 下一个字符的数值（这里为什么会有 %素数的操作？）
            char trailingDigit = passedTxtStr.charAt(currentCursorSpot);
            txtHash = (txtHash * characterOptionsAmount + trailingDigit * 1) % largePrime;

            // match: 哈希匹配 & 字符匹配
            // 计算出 相对于文本字符串首字符的偏移量
            int offset = currentCursorSpot - patStrLength + 1;
            if ((patternStrHash == txtHash) && check(passedTxtStr, offset))
                return offset;
        }

        // no match：如果不匹配，则： 返回文本字符串的长度
        return txtStrLength;
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
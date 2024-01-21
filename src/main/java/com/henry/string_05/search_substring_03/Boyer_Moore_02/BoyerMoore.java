package com.henry.string_05.search_substring_03.Boyer_Moore_02;

/******************************************************************************
 *  Compilation:  javac BoyerMoore.java
 *  Execution:    java BoyerMoore pattern text
 *  Dependencies: StdOut.java
 *
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  bad-character rule part of the Boyer-Moore algorithm.
 *  (does not implement the strong good suffix rule)
 *
 *  % java BoyerMoore abracadabra abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:               abracadabra
 *
 *  % java BoyerMoore rab abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:         rab
 *
 *  % java BoyerMoore bcara abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                                   bcara
 *
 *  % java BoyerMoore rabrabracad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad
 *
 *  % java BoyerMoore abacad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code BoyerMoore} class finds the first occurrence of a pattern string
 * in a text string.
 * <p>
 * This implementation uses the Boyer-Moore algorithm (with the bad-character
 * rule, but not the strong good suffix rule).
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/53substring">Section 5.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class BoyerMoore {
    private final int characterOptions;     // the radix
    private int[] characterToItsRightmostSpotInPatStr;     // the bad-character skip array

    private char[] patternCharacterArr;  // store the pattern as a character array
    private String patternStr;      // or as a string

    /**
     * Preprocesses the pattern string.
     *
     * @param patternStr the pattern string
     */
    public BoyerMoore(String patternStr) {
        this.characterOptions = 256;
        this.patternStr = patternStr;

        // position of rightmost occurrence of c in the pattern
        // 构建跳跃表 - 手段：为每个字符，初始化其 xxx数组的值为 其在模式字符串中最右侧的位置
        characterToItsRightmostSpotInPatStr = new int[characterOptions];
        for (int currentCharacterOption = 0; currentCharacterOption < characterOptions; currentCharacterOption++)
            characterToItsRightmostSpotInPatStr[currentCharacterOption] = -1;

        for (int currentCharacterSpot = 0; currentCharacterSpot < patternStr.length(); currentCharacterSpot++) {
            char currentPatCharacter = patternStr.charAt(currentCharacterSpot);
            characterToItsRightmostSpotInPatStr[currentPatCharacter] = currentCharacterSpot;
        }
    }

    /**
     * Preprocesses the pattern string.
     *
     * @param patternCharacterArr the pattern string
     * @param characterOptions       the alphabet size
     */
    public BoyerMoore(char[] patternCharacterArr, int characterOptions) {
        this.characterOptions = characterOptions;
        this.patternCharacterArr = new char[patternCharacterArr.length];

        for (int currentCharacterSpot = 0; currentCharacterSpot < patternCharacterArr.length; currentCharacterSpot++)
            this.patternCharacterArr[currentCharacterSpot] = patternCharacterArr[currentCharacterSpot];

        // position of rightmost occurrence of c in the pattern
        characterToItsRightmostSpotInPatStr = new int[characterOptions];
        for (int currentCharacterOption = 0; currentCharacterOption < characterOptions; currentCharacterOption++)
            characterToItsRightmostSpotInPatStr[currentCharacterOption] = -1;

        for (int currentCharacterSpot = 0; currentCharacterSpot < patternCharacterArr.length; currentCharacterSpot++)
            characterToItsRightmostSpotInPatStr[patternCharacterArr[currentCharacterSpot]] = currentCharacterSpot;
    }

    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string.
     *
     * @param txtStr the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; n if no such match
     */
    public int search(String txtStr) {
        int patStrLength = patternStr.length();
        int txtStrLength = txtStr.length();

        int skipPitch;
        for (int currentTxtCursor = 0; currentTxtCursor <= txtStrLength - patStrLength; currentTxtCursor += skipPitch) {
            skipPitch = 0;
            // 对于当前文本字符 从右向左地逐个比较模式字符串的字符...
            for (int backwardsPatCursor = patStrLength - 1; backwardsPatCursor >= 0; backwardsPatCursor--) {
                char patCharacter = patternStr.charAt(backwardsPatCursor);
                char txtCharacter = txtStr.charAt(currentTxtCursor + backwardsPatCursor);
                // 如果字符不匹配,则：把i增大(j-right['X']) 来 把文本和模式中的“不匹配字符”对其
                if (patCharacter != txtCharacter) {
                    int rightmostSpotOfTxtCharacter = characterToItsRightmostSpotInPatStr[txtCharacter];
                    skipPitch = Math.max(1, backwardsPatCursor - rightmostSpotOfTxtCharacter);
                    break;
                }
            }

            // 如果skipPitch等于0（保持原始值），说明 模式字符串中的每一个字符都匹配成功，则：字符串匹配成功，返回当前文本字符的指针位置
            if (skipPitch == 0) return currentTxtCursor;    // found
        }

        // 如果程序执行到这里，说明 所有文本字符都检查完成后，仍旧没有找到成功的匹配。则：直接返回文本字符串的长度，表示查找失败
        return txtStrLength;                       // not found
    }


    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string.
     *
     * @param text the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; n if no such match
     */
    public int search(char[] text) {
        int m = patternCharacterArr.length;
        int n = text.length;
        int skip;
        for (int i = 0; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m - 1; j >= 0; j--) {
                if (patternCharacterArr[j] != text[i + j]) {
                    skip = Math.max(1, j - characterToItsRightmostSpotInPatStr[text[i + j]]);
                    break;
                }
            }
            if (skip == 0) return i;    // found
        }
        return n;                       // not found
    }


    /**
     * Takes a pattern string and an input string as command-line arguments;
     * searches for the pattern string in the text string; and prints
     * the first occurrence of the pattern string in the text string.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String patStr = args[0];
        String txtStr = args[1];
        char[] patternArr = patStr.toCharArray();
        char[] textArr = txtStr.toCharArray();

        BoyerMoore boyermoore1 = new BoyerMoore(patStr);
        BoyerMoore boyermoore2 = new BoyerMoore(patternArr, 256);
        int offset1 = boyermoore1.search(txtStr);
        int offset2 = boyermoore2.search(textArr);

        // print results
        StdOut.println("textArr:    " + txtStr);

        StdOut.print("patternArr: ");
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(patStr);

        StdOut.print("patternArr: ");
        for (int i = 0; i < offset2; i++)
            StdOut.print(" ");
        StdOut.println(patStr);
    }
}
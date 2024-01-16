package com.henry.string_05.search_substring_03;

/******************************************************************************
 *  Compilation:  javac KMP.java
 *  Execution:    java KMP pattern text
 *  Dependencies: StdOut.java
 *
 *  Reads in two strings, the pattern and the input text, and
 *  searches for the pattern in the input text using the
 *  KMP algorithm.
 *
 *  % java KMP abracadabra abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:               abracadabra
 *
 *  % java KMP rab abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:         rab
 *
 *  % java KMP bcara abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                                   bcara
 *
 *  % java KMP rabrabracad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern:                        rabrabracad
 *
 *  % java KMP abacad abacadabrabracabracadabrabrabracad
 *  text:    abacadabrabracabracadabrabrabracad
 *  pattern: abacad
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code KMP} class finds the first occurrence of a pattern string
 * in a text string.
 * <p>
 * This implementation uses a version of the Knuth-Morris-Pratt substring search
 * algorithm. The version takes time proportional to <em>n</em> + <em>m R</em>
 * in the worst case, where <em>n</em> is the length of the text string,
 * <em>m</em> is the length of the pattern, and <em>R</em> is the alphabet size.
 * It uses extra space proportional to <em>m R</em>.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/53substring">Section 5.3</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class KMP {
    private final int characterOptionsAmount;       // the radix??
    private final int patStrLength;       // length of pattern
    // next_cursor_spot = dfa[given_character][current_cursor_spot]
    // 对于当前位置 current_cursor_spot，在遇到 given_character时，从 current_cursor_spot 跳转到/转移到 next_cursor_spot

    // dfa = stateTransformedResultOn = characterSpotOnCondition
    private int[][] nextCursorSpotOnCondition;       // dfa: the KMP automaton spotOfPatCharacterToCompareWithNextTxtCharacter

    /**
     * Preprocesses the pattern string.
     *
     * @param patStr the pattern string
     */
    public KMP(String patStr) {
        this.characterOptionsAmount = 256;
        this.patStrLength = patStr.length();

        // build DFA from pattern - dfa[][]的值 <-> 回答“匹配失败时，下一个状态是什么？”
        nextCursorSpotOnCondition = new int[characterOptionsAmount][patStrLength];
        char firstCharacter = patStr.charAt(0);
        nextCursorSpotOnCondition[firstCharacter][0] = 1;

        // ”用于模拟的状态“ aka 重启状态 作用：状态转移过程从此处重新开始???
        // X = stateToSimulate = restartStateForCurrentSpot = restartSpotForCurrentSpot
        for (int restartSpotForCurrentSpot = 0, currentCursorSpot = 1;
             currentCursorSpot < patStrLength; currentCursorSpot++) {

            // 对于 当前位置上可能遇到的每一个字符...
            for (int currentCharacter = 0; currentCharacter < characterOptionsAmount; currentCharacter++)
            {
                // 使用“用于模拟的状态”的“状态转移结果” 来 模拟“当前状态”的“状态转移结果”
                // 🐖 除非匹配成功，否则 ”状态转移结果“会以如下值保持不变
                int stateToSimulate = restartSpotForCurrentSpot;
                nextCursorSpotOnCondition[currentCharacter][currentCursorSpot] = nextCursorSpotOnCondition[currentCharacter][stateToSimulate];
            }

            // 如果当前位置上，遇到了“模式字符串中当前位置上的字符”，则：dfa[][]的值为下一个位置
            // 🐖 手段：当前位置 + 1
            char currentPatternCharacter = patStr.charAt(currentCursorSpot);
            nextCursorSpotOnCondition[currentPatternCharacter][currentCursorSpot] = currentCursorSpot + 1;

            /* “当前位置”处理完成后，计算“下一个位置”的重启状态/位置 */
            // 手段： 下一个位置的重启位置 = 当前位置的重启位置，在遇到”当前模式字符“时的”状态转移结果“
            // 原理：位置i的重启状态 就是 由状态0 从pat[1]一直匹配到pat[i-1]所得到的状态转移结果
            // 所以X(i)的求值是一个迭代的过程: X(i) = dfa[pat[i-1]][X(i-1)]
            restartSpotForCurrentSpot = nextCursorSpotOnCondition[currentPatternCharacter][restartSpotForCurrentSpot];
        }
    }

    /**
     * Preprocesses the pattern string.
     *
     * @param pattern                the pattern string
     * @param characterOptionsAmount the alphabet size
     */
    public KMP(char[] pattern, int characterOptionsAmount) {
        this.characterOptionsAmount = characterOptionsAmount;
        this.patStrLength = pattern.length;

        // build DFA from pattern
        int patternStrLength = pattern.length;
        nextCursorSpotOnCondition = new int[characterOptionsAmount][patternStrLength];
        nextCursorSpotOnCondition[pattern[0]][0] = 1;

        for (int stateToSimulateWhenMisMatch = 0, patternCharCursorCurrentSpot = 1;
             patternCharCursorCurrentSpot < patternStrLength; patternCharCursorCurrentSpot++) {

            for (int currentCharacter = 0; currentCharacter < characterOptionsAmount; currentCharacter++)
                nextCursorSpotOnCondition[currentCharacter][patternCharCursorCurrentSpot] = nextCursorSpotOnCondition[currentCharacter][stateToSimulateWhenMisMatch];     // Copy mismatch cases.

            char currentPatternCharacter = pattern[patternCharCursorCurrentSpot];
            nextCursorSpotOnCondition[currentPatternCharacter][patternCharCursorCurrentSpot] = patternCharCursorCurrentSpot + 1;      // Set match case.
            stateToSimulateWhenMisMatch = nextCursorSpotOnCondition[currentPatternCharacter][stateToSimulateWhenMisMatch];        // Update restart state.
        }
    }

    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string.
     *
     * @param passedTxtStr the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; N if no such match
     */
    public int searchWithIn(String passedTxtStr) {

        // simulate operation of DFA on text
        int txtCharacterAmount = passedTxtStr.length();
        int currentTxtCursor, currentPatCursor;
        for (currentTxtCursor = 0, currentPatCursor = 0; currentTxtCursor < txtCharacterAmount && currentPatCursor < patStrLength; currentTxtCursor++) {
            currentPatCursor = nextCursorSpotOnCondition[passedTxtStr.charAt(currentTxtCursor)][currentPatCursor];
        }

        if (currentPatCursor == patStrLength) return currentTxtCursor - patStrLength;    // found
        return txtCharacterAmount;                    // not found
    }

    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string.
     *
     * @param textStr the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; N if no such match
     */
    public int search(char[] textStr) {

        // simulate operation of DFA on text
        int textStrLength = textStr.length;
        int stateToSimulate, patternCharacterCursorCurrentSpot;

        for (stateToSimulate = 0, patternCharacterCursorCurrentSpot = 0;
             stateToSimulate < textStrLength && patternCharacterCursorCurrentSpot < patStrLength;
             stateToSimulate++) {

            char currentTxtCharacter = textStr[stateToSimulate];
            patternCharacterCursorCurrentSpot = nextCursorSpotOnCondition[currentTxtCharacter][patternCharacterCursorCurrentSpot];
        }

        if (patternCharacterCursorCurrentSpot == patStrLength)
            return stateToSimulate - patStrLength;    // found

        return textStrLength;                    // not found
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
        char[] patternCharArr = patStr.toCharArray();
        char[] textCharArr = txtStr.toCharArray();

        KMP kmp1 = new KMP(patStr);
        int offset1 = kmp1.searchWithIn(txtStr);

        KMP kmp2 = new KMP(patternCharArr, 256);
        int offset2 = kmp2.search(textCharArr);

        // print results
        StdOut.println("textCharArr:    " + txtStr);

        StdOut.print("patternCharArr: ");
        for (int currentSpot = 0; currentSpot < offset1; currentSpot++)
            StdOut.print(" ");
        StdOut.println(patStr);

        StdOut.print("patternCharArr: ");
        for (int currentSpot = 0; currentSpot < offset2; currentSpot++)
            StdOut.print(" ");
        StdOut.println(patStr);
    }
}

package com.henry.string_05.search_substring_03.KMP_01;

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

// 验证：KMP的构造函数会 根据模式字符串来创建出一个DFA，并 使用search()方法 来 在给定的文本中查找该模式字符串
// DFA创建的关键词：current_spot、restart_spot、nextSpot[character_option][current_spot]
// 关键判断：字符是不是“模式字符串”当前位置上的字符；
// 关键性质：当字符不是“模式字符”时，当前位置的dfa[][]值就等于 当前位置的重启位置（X(i) < i）的dfa[][]值
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

        int initCursorSpot = 0;
        char characterOnSpot = patStr.charAt(0);
        nextCursorSpotOnCondition[characterOnSpot][initCursorSpot] = 1;

        // ”用于模拟的状态“ aka 重启状态 作用：状态转移过程从此处重新开始???
        // X = stateToSimulate = restartStateForCurrentSpot = restartSpotForCurrentSpot
        // #0 初始化当前位置 与 当前位置的重启位置
        for (int restartSpotOfCurrentSpot = 0, currentCursorSpot = 1;
             currentCursorSpot < patStrLength; currentCursorSpot++) {

            /* 指针应该指向的下一个位置是哪儿？ */
            // #1 初始化 当前位置的dfa值（状态转移后的位置）
            initDFAFor(currentCursorSpot, restartSpotOfCurrentSpot);

            // #2 如果当前位置上，遇到了“模式字符串中当前位置上的字符”，说明是一次成功的匹配，应该转移到下一个位置
            // 则：把dfa[][]的值更新为下一个位置
            char currentPatternCharacter = patStr.charAt(currentCursorSpot);
            updateDFAOn(currentCursorSpot, currentPatternCharacter);

            /* #3 “当前位置”处理完成后，迭代地计算“下一个位置”的重启状态/位置 */
            // 手段： 下一个位置的重启位置 = 当前位置的重启位置，在遇到”当前模式字符“时的”状态转移结果“
            // 原理：位置i的重启状态 就是 由状态0 从pat[1]一直匹配到pat[i-1]所得到的状态转移结果
            // 所以X(i)的求值是一个迭代的过程: X(i) = dfa[pat[i-1]][X(i-1)]
            restartSpotOfCurrentSpot = calculateRestartSpotForNextSpot(restartSpotOfCurrentSpot, currentPatternCharacter);
        }
    }

    private int calculateRestartSpotForNextSpot(int restartSpotForCurrentSpot, char currentPatternCharacter) {
        return nextCursorSpotOnCondition[currentPatternCharacter][restartSpotForCurrentSpot];
    }

    private void updateDFAOn(int currentCursorSpot, char currentPatternCharacter) {
        nextCursorSpotOnCondition[currentPatternCharacter][currentCursorSpot] = currentCursorSpot + 1;
    }

    private void initDFAFor(int currentCursorSpot, int restartSpotForCurrentSpot) {
        // 对于 当前位置上可能遇到的每一个字符...
        for (int currentCharacter = 0; currentCharacter < characterOptionsAmount; currentCharacter++) {
            // 使用“当前位置的重启位置”的dfa值 来 模拟“当前位置”的dfa值（状态转移到的下一个位置）
            // 🐖 当前位置的重启位置X(i) 相比于 当前位置i 一般会更小 - X(i) < i
            int stateToSimulate = restartSpotForCurrentSpot;
            nextCursorSpotOnCondition[currentCharacter][currentCursorSpot] = nextCursorSpotOnCondition[currentCharacter][stateToSimulate];
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
        // 使用DFA[][] 来 模拟 在文本字符串中对模式字符串的匹配过程
        int txtCharacterAmount = passedTxtStr.length();
        int currentTxtCursor, currentPatCursor;

        for (currentTxtCursor = 0, currentPatCursor = 0;
             currentTxtCursor < txtCharacterAmount && currentPatCursor < patStrLength; currentTxtCursor++) {
            // 用“模式字符串的指针” 来 匹配文本字符串的字符
            currentPatCursor = nextCursorSpotOnCondition[passedTxtStr.charAt(currentTxtCursor)][currentPatCursor];
        }

        // 如果匹配成功，则：返回 匹配的左字符
        if (currentPatCursor == patStrLength) return currentTxtCursor - patStrLength;    // found
        // 如果匹配不成功，则：返回文本字符串的长度
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
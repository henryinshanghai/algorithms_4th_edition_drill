package com.henry.string_05.search_substring_03.Boyer_Moore_02.execution;

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
// 验证：可以使用BoyerMoore算法(设置从前往后的文本指针与从后往前的模式指针，当字符不匹配时，把文本指针跳转到 下一个可能匹配的位置) 来 在文本字符串中查找 与模式字符串相匹配的子字符串
// 字符匹配: pat_character = txt_character; 字符串匹配：每一个文本字符 都与模式字符匹配；
// 特征：txt_cursor并不是直接指向 txt_character的，而是 txt_cursor + backward_pat_cursor
public class BoyerMoore {
    private final int characterOptionAmount;     // the radix
    private int[] characterToItsLastOccurrenceSpotInPatStr;     // characterToItsRightmostSpotInPatStr the bad-character skip array

    private char[] patternCharacterArr;  // store the pattern as a character array
    private String patternStr;      // or as a string

    /**
     * Preprocesses the pattern string.
     * 预处理模式字符串,得到模式字符串对应的 characterToItsLastOccurrenceSpotInPatStr[]
     * @param patternStr the pattern string
     */
    public BoyerMoore(String patternStr) {
        this.patternStr = patternStr;
        this.characterOptionAmount = 256;

        // 记录下 字符->它在模式字符串中最后一次出现的位置（使用数组） aka 跳跃表??
        initLastOccurrenceSpotForCharacters();

        renewLastOccurrenceSpotOfCharacterIn(patternStr);
    }

    private void initLastOccurrenceSpotForCharacters() {
        characterToItsLastOccurrenceSpotInPatStr = new int[characterOptionAmount];
        // 把所有字符的 lastOccurrenceSpotInPatStr 都设置为 -1
        for (int currentCharacterOption = 0; currentCharacterOption < characterOptionAmount; currentCharacterOption++)
            characterToItsLastOccurrenceSpotInPatStr[currentCharacterOption] = -1;
    }

    private void renewLastOccurrenceSpotOfCharacterIn(String patternStr) {
        for (int currentCharacterSpot = 0; currentCharacterSpot < patternStr.length(); currentCharacterSpot++) {
            char currentPatCharacter = patternStr.charAt(currentCharacterSpot);
            characterToItsLastOccurrenceSpotInPatStr[currentPatCharacter] = currentCharacterSpot;
        }
        // 没有在模式字符串中出现的字符，它所对应的 lastOccurrenceSpotInPatStr的值为-1
    }

    /**
     * Preprocesses the pattern string.
     * 预处理模式字符串
     * @param patternCharacterArr the pattern string
     * @param characterOptionAmount       the alphabet size
     */
    public BoyerMoore(char[] patternCharacterArr, int characterOptionAmount) {
        this.characterOptionAmount = characterOptionAmount;
        this.patternCharacterArr = new char[patternCharacterArr.length];

        for (int currentCharacterSpot = 0; currentCharacterSpot < patternCharacterArr.length; currentCharacterSpot++)
            this.patternCharacterArr[currentCharacterSpot] = patternCharacterArr[currentCharacterSpot];

        // position of rightmost occurrence of c in the pattern
        characterToItsLastOccurrenceSpotInPatStr = new int[characterOptionAmount];
        for (int currentCharacterOption = 0; currentCharacterOption < characterOptionAmount; currentCharacterOption++)
            characterToItsLastOccurrenceSpotInPatStr[currentCharacterOption] = -1;

        for (int currentCharacterSpot = 0; currentCharacterSpot < patternCharacterArr.length; currentCharacterSpot++)
            characterToItsLastOccurrenceSpotInPatStr[patternCharacterArr[currentCharacterSpot]] = currentCharacterSpot;
    }

    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string.
     * 返回 文本字符串中，模式字符串第一次出现的位置
     * @param txtStr the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; n if no such match 如果没有匹配的话，则返回n
     */
    public int search(String txtStr) {
        int patStrLength = patternStr.length();
        int txtStrLength = txtStr.length();

        int txtCursorNeedJumpDistance;
        // #1 使用文本指针 来 在可能的匹配位置上开始尝试匹配
        // 特征：① 文本指针并不会直接指向“待比较的文本字符”；② 在匹配失败后，文本指针会向后跳动 txtCursorJumpDistance个位置； ③ 跳到新的位置后，会重复同样的过程-对字符进行比较
        for (int currentTxtCursor = 0; currentTxtCursor <= txtStrLength - patStrLength; currentTxtCursor += txtCursorNeedJumpDistance) {
            // #2 对于“当前可能字符串匹配的位置”（当前文本指针与模式指针）
            // #2-〇 初始化“文本指针在匹配失败时应该跳转的距离”为0    🐖 对于每个可能匹配的位置，“跳动距离”都会被重新置零
            txtCursorNeedJumpDistance = 0;

            for (int backwardsPatCursor = patStrLength - 1; backwardsPatCursor >= 0; backwardsPatCursor--) {
                // #2-Ⅰ 得到文本字符与模式字符
                int txtCharacterSpot = currentTxtCursor + backwardsPatCursor;
                char txtCharacter = txtStr.charAt(txtCharacterSpot);
                char patCharacter = patternStr.charAt(backwardsPatCursor);

                // #2-Ⅱ 并进行字符间的比较
                // 如果文本字符与模式字符之间不匹配,则：
                if (txtCharacter != patCharacter) {
                    // 计算出 文本指针应该跳转的距离
                    txtCursorNeedJumpDistance = calculateTxtCursorJumpDistance(txtCharacter, backwardsPatCursor);
                    break;
                }
            }

            // #2-Ⅲ 对于“当前可能字符串匹配的位置”，如果它的txtCursorJumpDistance 保持原始值（等于0），说明 模式字符串中的每一个字符都匹配成功，
            // 则：模式字符串匹配成功，返回“当前文本字符指针“的位置
            if (txtCursorNeedJumpDistance == 0) return currentTxtCursor;    // found
        }

        // #3 如果程序执行到这里，说明 所有“可能字符串匹配的位置”都检查完成后，仍旧没有找到成功的匹配。
        // 则：直接返回文本字符串的长度，表示查找失败
        return txtStrLength;                       // not found
    }

    private int calculateTxtCursorJumpDistance(char txtCharacter, int backwardsPatCursor) {
        // #1 获取到 此文本字符 在模式字符串中最后一次出现的位置
        int txtCharactersLastOccurrenceInPatStr = characterToItsLastOccurrenceSpotInPatStr[txtCharacter];
        // #2 计算出 模式字符串中，“匹配失败的位置”与“会匹配成功的位置”之间的距离
        int distanceBetweenMismatchSpotAndWouldMatchSpot = backwardsPatCursor - txtCharactersLastOccurrenceInPatStr;
        // #3 这个距离 就是“文本指针应该向后跳转的距离” 🐖 为了防止xxx出现负数，这里使用max()来保证文本指针最少往后移动一个位置
        return Math.max(1, distanceBetweenMismatchSpotAndWouldMatchSpot);
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
                    skip = Math.max(1, j - characterToItsLastOccurrenceSpotInPatStr[text[i + j]]);
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

        // #1 根据模式字符串 构造出 它的 “字符->字符在模式字符串中最后一次出现的位置”的数组
        BoyerMoore boyermoore1 = new BoyerMoore(patStr);
        BoyerMoore boyermoore2 = new BoyerMoore(patternArr, 256);

        // #2 调用search()方法，传入 文本字符串 作为参数，得到 模式字符串 在文本字符串中 首次出现的位置
        int offset1 = boyermoore1.search(txtStr);
        int offset2 = boyermoore2.search(textArr);

        // 打印文本字符串
        StdOut.println("textArr:    " + txtStr);

        StdOut.print("patternArr: ");
        // 在打印模式字符串之前，先打印 offset个空格 来 实现模式字符串 与 其在文本字符串中的匹配 打印对齐的效果
        for (int i = 0; i < offset1; i++)
            StdOut.print(" ");
        StdOut.println(patStr);

        StdOut.print("patternArr: ");
        for (int i = 0; i < offset2; i++)
            StdOut.print(" ");
        StdOut.println(patStr);
    }
}
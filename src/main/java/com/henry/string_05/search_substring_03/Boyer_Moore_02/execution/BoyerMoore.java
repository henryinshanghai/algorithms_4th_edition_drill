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
// 验证：可以使用 BoyerMoore算法 来 在 文本字符串 中 查找 与模式字符串相匹配 的子字符串
// BM算法步骤：
// #1 设置 从前往后的 文本指针 与 从后往前的模式指针；
// #2 当 字符不匹配 时，把 文本指针 跳转到 下一个可能匹配的位置.
// 字符匹配: pat_character = txt_character; 字符串匹配：每一个文本字符 都 与模式字符 相匹配；
// 概念：可能发生匹配的位置(current_txt_character)；
// 特征：txt_cursor并不是 直接指向 txt_character的，而是指向 txt_cursor + backward_pat_cursor
public class BoyerMoore {
    private final int characterOptionAmount;     // 字符选项的数量
    private int[] characterToItsLastOccurrenceSpotInPatStr;     // 字符 -> 该字符最后一次在模式字符串中出现的位置

    private char[] patternCharacterArr;  // 把 模式 存储为一个 字符数组
    private String patternStr;      // 或者 存储为一个 字符串

    /**
     * 预处理 模式字符串,得到 模式字符串所对应的 characterToItsLastOccurrenceSpotInPatStr[]
     *
     * @param patternStr 模式字符串
     */
    public BoyerMoore(String patternStr) {
        this.patternStr = patternStr;
        this.characterOptionAmount = 256;

        // 初始化 字符 -> 它在模式字符串中最后一次出现的位置（使用数组）为-1 aka 跳跃表??
        initLastOccurrenceSpotForCharacters();

        // 使用 模式字符串 来 更新这个lastOccurrenceSpot[]数组
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
        // 那些“没有在模式字符串中出现”的字符，它所对应的 lastOccurrenceSpotInPatStr的值 会是 -1
        // 那些 在模式字符串中 多次出现的字符，它所对应的 lastOccurrenceSpotInPatStr的值 会是 它最后一次出现的位置
    }

    /**
     * 预处理模式字符串     重载方法
     *
     * @param patternCharacterArr   模式字符串
     * @param characterOptionAmount 字母表大小
     */
    public BoyerMoore(char[] patternCharacterArr, int characterOptionAmount) {
        this.characterOptionAmount = characterOptionAmount;
        this.patternCharacterArr = new char[patternCharacterArr.length];

        for (int currentCharacterSpot = 0; currentCharacterSpot < patternCharacterArr.length; currentCharacterSpot++)
            this.patternCharacterArr[currentCharacterSpot] = patternCharacterArr[currentCharacterSpot];

        // 得到 指定字符 -> 该字符在模式字符串中 出现的最右位置
        characterToItsLastOccurrenceSpotInPatStr = new int[characterOptionAmount];
        // 初始化元素值
        for (int currentCharacterOption = 0; currentCharacterOption < characterOptionAmount; currentCharacterOption++)
            characterToItsLastOccurrenceSpotInPatStr[currentCharacterOption] = -1;
        // 更新元素值
        for (int currentCharacterSpot = 0; currentCharacterSpot < patternCharacterArr.length; currentCharacterSpot++)
            characterToItsLastOccurrenceSpotInPatStr[patternCharacterArr[currentCharacterSpot]] = currentCharacterSpot;
    }

    // 返回 在文本字符串中，模式字符串 第一次出现的位置。
    // 如果 没有任何匹配 的话，则 返回 文本字符串的长度n
    public int search(String txtStr) {
        int patStrLength = patternStr.length();
        int txtStrLength = txtStr.length();

        int txtCursorNeedJumpDistance;
        // #1 使用 “文本指针” 来 表示“可能发生匹配的位置”，并 开始尝试匹配
        // 特征：① “文本指针”并不是直接指向“待比较的文本字符”，而是指向 当前开始尝试匹配的位置；
        // ② 在匹配失败后，文本指针会 向后跳动 txtCursorJumpDistance个位置；
        // ③ 跳到 新的位置 后，会重复 同样的过程（对字符进行比较）
        for (int currentTxtCursor = 0; currentTxtCursor <= txtStrLength - patStrLength; currentTxtCursor += txtCursorNeedJumpDistance) { // #2-Ⅰ 更新“当前可能发生字符串匹配的位置”

            // #2 对于“当前可能发生字符串匹配的位置”（current_txt_cursor）, 执行“逐个字符匹配/比较”的操作
            txtCursorNeedJumpDistance = 0; // 用于计算 在“当前可能发生匹配的位置” 发生失配时， 文本指针 所需要的跳转距离
            for (int backwardsPatCursor = patStrLength - 1; backwardsPatCursor >= 0; backwardsPatCursor--) { // “模式指针”从后往前
                // 计算出 “文本字符”的位置
                int txtCharacterSpot = currentTxtCursor + backwardsPatCursor;
                char txtCharacter = txtStr.charAt(txtCharacterSpot);
                char patCharacter = patternStr.charAt(backwardsPatCursor);

                // #2-Ⅰ 如果 文本字符 与 模式字符 之间不相同,说明 在“当前可能发生匹配的位置”上 发生了失配，则：
                if (characterMismatch(txtCharacter, patCharacter)) {
                    // 计算出 文本指针“应该向后跳转的距离” - 用于更新“当前可能发生字符串匹配的位置”
                    txtCursorNeedJumpDistance = calculateTxtCursorJumpDistance(txtCharacter, backwardsPatCursor);
                    break;
                }
            }

            // #2-Ⅱ 如果 “当前可能发生字符串匹配的位置(current_txt_cursor)” 发生了匹配，则：返回该位置(current_txt_cursor)
            // 手段：如果 txtCursorJumpDistance局部变量 保持原始值（等于0），说明 模式字符串中的每一个字符都匹配成功。则 “发生了匹配”
            if (allCharacterMatches(txtCursorNeedJumpDistance)) return currentTxtCursor;    // found
        }

        // #3 如果程序执行到这里，说明 所有“可能发生字符串匹配的位置”都检查完成后，仍旧没有找到成功的匹配。
        // 则：直接返回文本字符串的长度，表示查找失败
        return txtStrLength;                       // not found
    }

    private boolean allCharacterMatches(int txtCursorNeedJumpDistance) {
        return txtCursorNeedJumpDistance == 0;
    }

    /**
     * 计算出 当字符发生失配时，文本指针需要 往后跳的距离
     *
     * @param mismatchedTxtCharacter 失配的文本字符
     * @param backwardsPatCursor     失配时 模式指针的位置
     * @return 文本指针需要 往后跳的距离
     */
    private int calculateTxtCursorJumpDistance(char mismatchedTxtCharacter, int backwardsPatCursor) {
        // #1 获取到 此文本字符 在模式字符串中最后一次出现的位置
        int txtCharactersLastOccurrenceInPatStr = characterToItsLastOccurrenceSpotInPatStr[mismatchedTxtCharacter];
        // #2 计算出 模式字符串中，“匹配失败的位置”与“会匹配成功的位置”之间的距离
        int distanceBetweenMismatchSpotAndWouldMatchSpot = backwardsPatCursor - txtCharactersLastOccurrenceInPatStr;
        // #3 这个距离 就是“文本指针应该向后跳转的距离”
        return Math.max(1, // 🐖 为了防止 计算结果 出现负数，这里 使用max() 来 保证文本指针 最少往后移动一个位置
                distanceBetweenMismatchSpotAndWouldMatchSpot);
    }


    /**
     * 返回 模式字符串 在文本字符串中 第一次出现的索引位置
     *
     * @param textCharArr 文本字符串
     * @return 第一次出现的索引位置（如果有匹配存在），如果没有匹配，则：返回 文本字符串的长度N
     */
    public int search(char[] textCharArr) {
        int patternLength = patternCharacterArr.length;
        int textLength = textCharArr.length;
        int needJumpDistance;
        for (int currentAttemptingSpot = 0; currentAttemptingSpot <= textLength - patternLength; currentAttemptingSpot += needJumpDistance) {

            needJumpDistance = 0;
            for (int backwardsPatCursor = patternLength - 1; backwardsPatCursor >= 0; backwardsPatCursor--) {
                char currentPatCharacter = patternCharacterArr[backwardsPatCursor];
                char currentTextCharacter = textCharArr[currentAttemptingSpot + backwardsPatCursor];

                if (characterMismatch(currentPatCharacter, currentTextCharacter)) {
                    int textCharRightmostSpot = characterToItsLastOccurrenceSpotInPatStr[currentTextCharacter];
                    needJumpDistance =
                            Math.max(1, // 保证 至少向后移动一个位置
                                    backwardsPatCursor - textCharRightmostSpot);
                    break;
                }
            }

            if (allCharacterMatches(needJumpDistance)) {
                return currentAttemptingSpot;    // 在 当前尝试匹配的位置上 得到了一个匹配
            }
        }

        return textLength;                       // not found
    }

    // 字符 不匹配 的情况
    private boolean characterMismatch(char currentPatCharacter, char currentTextCharacter) {
        return currentPatCharacter != currentTextCharacter;
    }


    /**
     * 接受一个模式字符串 和 一个输入字符串 作为 命令行参数；
     * 在 文本字符串中 查找 模式字符串；
     * 打印 模式字符串 在 文本字符串中 第一次出现的位置；
     *
     * @param args 命令行参数
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
        for (int currentSpot = 0; currentSpot < offset1; currentSpot++)
            StdOut.print(" ");
        StdOut.println(patStr);

        StdOut.print("patternArr: ");
        for (int currentSpot = 0; currentSpot < offset2; currentSpot++)
            StdOut.print(" ");
        StdOut.println(patStr);
    }
}
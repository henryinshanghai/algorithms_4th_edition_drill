package com.henry.string_05.search_substring_03.KMP_01.execution;

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

// 验证：可以使用KMP算法(#1 根据模式字符串来创建出它的DFA；#2 使用文本字符串中的文本字符 来 驱动DFA运行到模式指针的最终位置) 来 在“给定的文本”中查找“该模式字符串”
// DFA创建的关键词：current_cursor_spot、its_restart_spot、cursorNextSpotOnCondition[receiving_character][current_cursor_spot]
// 关键判断：字符是不是“模式字符串”当前位置上的字符；
// 关键性质：当字符不是“模式字符”时，当前位置的dfa[][]值就等于 当前位置的重启位置（X(i) < i）的dfa[][]值
public class KMP {
    private final int characterOptionsAmount;       // the radix??
    private final int patStrLength;       // length of pattern

    // next_cursor_spot = cursorNextSpotOnCondition[receiving_character][current_cursor_spot]
    private int[][] cursorNextJumpSpotOnCondition;

    /**
     * Preprocesses(预处理) the pattern string.
     * 预处理内容：在构造器中，创建出 模式字符串的 cursorNextSpotOnCondition[receiving_character][current_cursor_spot]数组
     * @param patStr the pattern string
     */
    public KMP(String patStr) {
        this.characterOptionsAmount = 256; // 256
        this.patStrLength = patStr.length();

        // DFA的应用：使用DFA（特征：状态转移）来 描述KMP字符串匹配算法中“模式指针”的跳转（当前位置的字符不匹配时，模式指针会跳来跳去）
        // cursorNextSpotOnCondition[character][spot]的值 <-> 回答“在当前位置上接收到特定字符时，模式指针要跳转到的下一个位置是什么？”
        cursorNextJumpSpotOnCondition = new int[characterOptionsAmount][patStrLength];

        // #1：把 cursorNextSpotOnCondition[character][spot]在spot=0，character=patCharacter出的值预填充为1
        // 具体到模式指针跳转的语境，这表示：在位置0发生匹配时，模式指针就会转移到位置1
        preFillSpot0WhenReceivingPatCharacter(patStr);

        /*
            restartSpotOfCurrentSpot
            作用：如果“当前位置”接收到的字符 与 “模式字符”匹配失败，则 参考“其重启位置”的状态转移结果 来 确定“当前位置的状态转移结果”
         */
        // 对于从spot=1开始的每一个位置...
        for (int restartSpotOfCurrentSpot = 0,
             currentCursorSpot = 1; currentCursorSpot < patStrLength; currentCursorSpot++) {
            // 获取到当前位置上的模式字符
            char currentPatternCharacter = patStr.charAt(currentCursorSpot);

            // #2 填充当前位置的 cursorNextSpotOnCondition[][current_cursor_spot]的值
            // restartSpotOfCurrentSpot - 用于初始化 当前位置对应的所有dfa[character_on_spot][spot]元素
            // currentPatternCharacter - 用于更新 当前位置接收到“模式字符”时的dfa[pat_character][spot]元素值 aka 模式指针跳转到的位置
            fillDFAItemsFor(currentCursorSpot, restartSpotOfCurrentSpot, currentPatternCharacter);

            // #3 “当前位置”的cursorNextSpotOnCondition[][]填充完成后，计算出“当前位置的下一个位置”的“重启位置” - 用于DFA[][next_spot]元素的初始化
            restartSpotOfCurrentSpot = calculateRestartSpotForNextSpot(restartSpotOfCurrentSpot, currentPatternCharacter);
        }
    }

    private void fillDFAItemsFor(int currentCursorSpot, int restartSpotOfCurrentSpot, char currentPatternCharacter) {
        // #1 初始化 当前指针位置的dfa值（指针跳转/状态转移后的位置） - 手段：参考 其重启位置的状态转移情况
        initDFAFor(currentCursorSpot, restartSpotOfCurrentSpot);

        // #2 对于“当前指针位置”上 字符匹配(“接收到的字符” 与 “模式字符”相同)的情况：更新 当前位置的dfa值（指针应该移动/跳转到的位置）为 “它的物理空间上的下一个位置”
        // 具体做法：把dfa[][]的值更新为 currentCursorSpot的下一个位置
        updateDFAFor(currentCursorSpot, currentPatternCharacter);
    }

    // dfa[pat_char_at_spot0][0] = 1
    private void preFillSpot0WhenReceivingPatCharacter(String patStr) {
        // #1 获取 模式字符串 在位置0上的模式字符
        char patCharacterOnSpot0 = patStr.charAt(0);
        // #2 模式指针在位置0，接收到了 位置0上的模式字符，会跳转到位置1
        cursorNextJumpSpotOnCondition[patCharacterOnSpot0][0] = 1;
    }

    // 手段：下一个位置的重启位置 = 当前位置的重启位置，匹配(接收到)”当前模式字符“时的”状态转移结果“
    // 原理：位置i的重启状态 就是 由状态0 从pat[1]一直匹配到pat[i-1]所得到的状态转移结果
    // 所以求取X(i)的值 会是一个迭代的过程(求值时依赖于上一个值) -> X(i) = dfa[pat[i-1]][X(i-1)]
    // 🐖 “下一个位置的重启位置”的计算，不依赖于“当前位置”，只依赖于“当前重启位置” 与 “当前模式字符”
    // 而重启位置X 总是小于 当前位置current_spot的，因此 它的值总是已经确定了的
    private int calculateRestartSpotForNextSpot(int restartSpotForCurrentSpot, char currentPatternCharacter) {
        return cursorNextJumpSpotOnCondition[currentPatternCharacter][restartSpotForCurrentSpot];
    }

    private void updateDFAFor(int currentCursorSpot, char currentPatternCharacter) {
        cursorNextJumpSpotOnCondition[currentPatternCharacter][currentCursorSpot] = currentCursorSpot + 1;
    }

    private void initDFAFor(int currentCursorSpot, int restartSpotForCurrentSpot) {
        // 对于 当前指针位置上 所可能接收到的每一个字符...
        for (int currentCharacterOption = 0; currentCharacterOption < characterOptionsAmount; currentCharacterOption++) {
            // 使用“当前位置的重启位置”的dfa值 来 “模拟”/“填充” 其dfa值（状态应该转移到的“逻辑上的下一个位置”）
            // 🐖 当前位置的重启位置X(i) 相比于 当前位置i 一般会更小 - X(i) < i
            int stateToSimulate = restartSpotForCurrentSpot;
            cursorNextJumpSpotOnCondition[currentCharacterOption][currentCursorSpot] = cursorNextJumpSpotOnCondition[currentCharacterOption][stateToSimulate];
        }
    }

    /**
     * Preprocesses the pattern string.
     * 对 模式字符串 进行预处理
     * @param pattern                the pattern string 模式字符串
     * @param characterOptionsAmount the alphabet size  字母表大小：英文字母表的大小是26，ASCII码表的大小是256
     */
    public KMP(char[] pattern, int characterOptionsAmount) {
        this.characterOptionsAmount = characterOptionsAmount;
        this.patStrLength = pattern.length;

        // #1 创建 dfa[character_option][cursor_spot]的空数组
        int patternStrLength = pattern.length;
        cursorNextJumpSpotOnCondition = new int[characterOptionsAmount][patternStrLength];

        // #2 初始化 dfa[pat_char_on_0][0] 的值为1
        initDFAItemForSpot0(pattern);

        // #3 从spot=1开始，为 dfa[][]的其他元素 填充 正确的值
        for (int XSpotOfCurrentSpot = 0, currentPatCursorSpot = 1;
             currentPatCursorSpot < patternStrLength; currentPatCursorSpot++) {
            // 获取到 当前的模式字符
            char currentPatternCharacter = pattern[currentPatCursorSpot];

            // 初始化 dfa[][current_spot] 所有元素的值
            initDFAItemForCurrentSpot(currentPatCursorSpot, XSpotOfCurrentSpot, characterOptionsAmount);

            // 更新 dfa[pat_char_on_current_spot][current_spot] 元素的值
            updateDFAItemOnMatchCase(currentPatCursorSpot, currentPatternCharacter);

            // 更新“重启位置X” - 用于 dfa[][next_spot] 所有元素的初始化
            XSpotOfCurrentSpot = updateXForNextSpot(XSpotOfCurrentSpot, currentPatternCharacter);
        }
    }

    private int updateXForNextSpot(int stateToSimulateWhenMisMatch, char currentPatternCharacter) {
        return cursorNextJumpSpotOnCondition[Character.getNumericValue(currentPatternCharacter)][stateToSimulateWhenMisMatch];        // Update restart state.
    }

    private void updateDFAItemOnMatchCase(int patternCharCursorCurrentSpot, char currentPatternCharacter) {
        cursorNextJumpSpotOnCondition[Character.getNumericValue(currentPatternCharacter)][patternCharCursorCurrentSpot] = patternCharCursorCurrentSpot + 1;      // Set match case.
    }

    private void initDFAItemForCurrentSpot(int currentSpot, int stateToSimulateWhenMisMatch, int characterOptionsAmount) {
        for (int currentCharacter = 0; currentCharacter < characterOptionsAmount; currentCharacter++)
            cursorNextJumpSpotOnCondition[currentCharacter][currentSpot] =
                    cursorNextJumpSpotOnCondition[currentCharacter][stateToSimulateWhenMisMatch];     // Copy mismatch cases.
    }

    private void initDFAItemForSpot0(char[] pattern) {
        cursorNextJumpSpotOnCondition[Character.getNumericValue(pattern[0])][0] = 1;
    }

    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string.
     * 返回 在文本字符串中，模式字符串第一次出现的索引位置
     * @param passedTxtStr the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; N if no such match
     */
    public int searchWithIn(String passedTxtStr) {
        // 使用模式字符串的DFA[][] 来 模拟 在文本字符串中对模式字符串的匹配过程
        int txtCharacterAmount = passedTxtStr.length();
        int currentTxtCursor, currentPatCursor;

        // #1 逐个使用文本字符 来 驱动模式字符串的DFA
        // 对于“当前文本指针”...
        for (currentTxtCursor = 0, currentPatCursor = 0;
             currentTxtCursor < txtCharacterAmount && currentPatCursor < patStrLength; currentTxtCursor++) {
            // 获取到“文本指针指向的文本字符”，并
            char currentTxtCharacter = passedTxtStr.charAt(currentTxtCursor);
            // 使用它 来 驱动 “模式字符串的DFA（有限状态自动机）”
            // 驱动DFA的手段：使用DFA[txt_character][pat_cursor] 来 不断移动模式指针
            currentPatCursor = cursorNextJumpSpotOnCondition[currentTxtCharacter][currentPatCursor];
        }

        // #2 根据“模式指针的最终位置” 来 判断是否找到了“子字符串匹配”
        // 如果“文本字符串中的字符”能够 把“模式指针”驱动到“模式字符串的DFA结束状态”，
        // 说明 在“文本字符串”中找到了“匹配模式字符串的子字符串”，则：返回 匹配子字符串的左指针位置
        if (currentPatCursor == patStrLength) {
            return currentTxtCursor - patStrLength;    // found
        }
        // 否则，说明没能找到“匹配的子字符串”，则：返回文本字符串的长度
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
        int txtCursor, patCursor;

        // #1 使用文本字符 来 驱动模式字符串的DFA运行下去(模式指针向后移动)
        for (txtCursor = 0, patCursor = 0;
             txtCursor < textStrLength && patCursor < patStrLength; txtCursor++) {
            // 获取到文本字符
            char currentTxtCharacter = textStr[txtCursor];
            // 根据 模式指针的当前位置 与 文本字符 来 驱动模式指针到下一个位置
            patCursor = patCursorNextSpotWhenReceiving(patCursor, currentTxtCharacter);
        }

        // #2 根据模式指针的最终位置 来 判断是否找到了“子字符串的匹配”
        // #2-Ⅰ 如果模式指针 最终指向了 patStrLength的位置，说明DFA驱动成功，文本字符与模式字符逐一匹配，则
        if (patCursor == patStrLength)
            // 匹配成功，打印出 “字符串成功匹配的起始位置”
            return txtCursor - patStrLength;    // found

        // #2-Ⅱ 如果模式指针停在了其他位置，说明某个位置上字符匹配失败，则：返回文本字符串的长度
        return textStrLength;                    // not found
    }

    private int patCursorNextSpotWhenReceiving(int patCursor, char txtCharacter) {
        // 驱动模式字符串的DFA
        // 做法：使用 当前模式指针位置接收到文本字符后，跳转到的新位置 来 更新当前模式指针
        return cursorNextJumpSpotOnCondition[Character.getNumericValue(txtCharacter)][patCursor];
    }


    /**
     * Takes a pattern string and an input string as command-line arguments;
     * searches for the pattern string in the text string; and prints
     * the first occurrence of the pattern string in the text string.
     * 使用一个 模式字符串 与 一个输入的字符串 作为 命令行参数；
     * 在 文本字符串中 搜索 模式字符串；
     * 并打印出 模式字符串在文本字符串中第一次出现的位置
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String patStr = args[0]; // 模式字符串
        String txtStr = args[1]; // 文本字符串

        char[] patternCharArr = patStr.toCharArray();
        char[] textCharArr = txtStr.toCharArray();

        // #1 调用KMP的 字符串参数构造方法，得到 模式字符串的DFA（以二维数组的方式）
        KMP patStrDFA = new KMP(patStr);
        // #2 调用 searchWithIn()方法，获取到 模式字符串在文本字符串中 第一次出现的位置
        int startSpotOfMatchedSubStr = patStrDFA.searchWithIn(txtStr);

        // 打印文本字符串
        StdOut.println("textCharArr:    " + txtStr);

        // 打印模式字符串
        StdOut.print("patternCharArr: ");
        // 在打印模式字符串之前，先打印 offset个空格 来 实现模式字符串 与 其在文本字符串中的匹配 对齐的效果
        int offset = startSpotOfMatchedSubStr;
        for (int currentSpot = 0; currentSpot < offset; currentSpot++)
            StdOut.print(" ");
        StdOut.println(patStr);

        // 字符数组参数构造方法
        KMP kmp2 = new KMP(patternCharArr, 256);
        int offset2 = kmp2.search(textCharArr);

        // 打印模式字符串*2
        StdOut.print("patternCharArr: ");
        for (int currentSpot = 0; currentSpot < offset2; currentSpot++)
            StdOut.print(" ");
        StdOut.println(patStr);
    }
}

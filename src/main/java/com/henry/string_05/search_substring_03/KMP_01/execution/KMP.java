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

// 验证：可以使用 KMP算法来 在“给定的文本”中 查找“该模式字符串”
// KMP算法步骤：
// #1 根据 模式字符串 来 创建出它的DFA；
// #2 使用文本字符串中的文本字符 来 驱动DFA运行到模式指针的最终位置
// DFA（deterministic finite automation 确定性有限自动机）相关概念：
// current_cursor_spot 表示 模式指针的当前位置
// its_restart_spot 表示 ???
// cursorNextSpotOnCondition[receiving_character][current_cursor_spot] 表示 当匹配成功时，模式指针 所跳转到的 下一个位置
// 关键判断：字符 是不是 “模式字符串” 在当前位置上的字符；
// 关键性质：当 字符 不是 “模式字符” 时，当前位置的dfa[][]值 就等于 “当前位置”的“重启位置”（X(i) < i）的dfa[][]值
public class KMP {
    private final int characterOptionsAmount;       // 所有可能的字符选项的数量
    private final int patStrLength;       // 模式字符串的长度

    // DFA的作用：它 根据 输入的字符序列，一步一步地 改变 自己的状态，最终告诉我们 这个输入序列 是否 被它“接受”
    // DFA的应用：使用DFA（用于 描述状态转移）来 描述 KMP字符串匹配算法 中，特定条件下 “模式指针”的跳转位置
    // 跳转规则：next_cursor_spot = cursorNextSpotOnCondition[receiving_character][current_cursor_spot]
    // 概念：这里的特定条件 指的是 (当前位置 + 所接收到的字符)
    // 🐖 出于方便，以下 在代码注释中 把它简写成为dfa[]
    private int[][] cursorNextJumpSpotOnCondition;

    /**
     * 预处理 模式字符串
     * 预处理内容：在构造器中，创建出 模式字符串的 cursorNextSpotOnCondition[receiving_character][current_cursor_spot]数组
     *
     * @param patStr the pattern string
     */
    public KMP(String patStr) {
        // #1 初始化成员变量
        this.characterOptionsAmount = 256; // 256
        this.patStrLength = patStr.length();
        cursorNextJumpSpotOnCondition = new int[characterOptionsAmount][patStrLength];

        // #2：把 dfa[character=patCharacter][spot=0]的值 预填充为 1
        // 具体到 模式指针跳转的语境，这表示：在 位置0 发生匹配 时，模式指针 就会 转移到位置1
        preFillSpot0OnPatChar(patStr);

        // #3 对于 从spot=1开始的 每一个spot，计算 dfa[][current_spot] 列中的所有元素
        for (int restartSpotOfCurrentSpot = 0, // 作用：如果 “当前位置”接收到的字符 不是 “模式字符”，则 参考“其重启位置”的状态转移结果 来 确定“其的状态转移结果”
             currentCursorSpot = 1; currentCursorSpot < patStrLength; currentCursorSpot++) {
            // 获取到 当前位置上的模式字符
            char currentPatternCharacter = patStr.charAt(currentCursorSpot);

            // 填充 当前位置 所对应的 所有 cursorNextSpotOnCondition[][current_cursor_spot]的值
            fillDFAItemsFor(currentCursorSpot, restartSpotOfCurrentSpot, currentPatternCharacter);

            // 计算出 下一个物理位置的重启位置，为 下一轮循环 做准备
            restartSpotOfCurrentSpot = calculateRestartSpotForNextSpot(restartSpotOfCurrentSpot, currentPatternCharacter);
        }
    }

    /**
     * 为 dfa[character][spot]数组 中 指定spot对应的元素 填充 正确的元素值；
     *
     * @param currentCursorSpot        模式指针的位置
     * @param restartSpotOfCurrentSpot 当前位置的重启位置 用于参考得到dfa[][]元素的初始值
     * @param currentPatternCharacter  当前的模式字符 用于驱动模式指针 向后移动
     */
    private void fillDFAItemsFor(int currentCursorSpot,
                                 int restartSpotOfCurrentSpot,
                                 char currentPatternCharacter) {
        // #1 初始化 当前指针位置的dfa值（指针跳转/状态转移后的位置）； - 手段：参考 “其重启位置”的状态转移情况
        initDFAFor(currentCursorSpot, restartSpotOfCurrentSpot);

        // #2 对于 “当前指针位置”上 “接收到的字符” 就是 “模式字符” 的情况，更新 当前位置对应的dfa值（指针应该移动/跳转到的位置）
        updateDFAFor(currentCursorSpot, currentPatternCharacter);
    }

    // dfa[pat_char_at_spot0][0] = 1
    private void preFillSpot0OnPatChar(String patStr) {
        // #1 获取 模式字符串 在位置0上的模式字符
        char patCharacterOnSpot0 = patStr.charAt(0);
        // #2 当前 模式指针 在位置0上，在 接收到了 位置0上的模式字符 后，会跳转到 位置1
        cursorNextJumpSpotOnCondition[patCharacterOnSpot0][0] = 1;
    }

    // 计算公式：X(current_spot+1) = dfa[current_pattern_char][X(current_spot)]; // X(i) 表示 位置i 的重启位置
    // 原理：位置i的重启状态 就是 从状态0开始，“由 pat[1] 一路匹配到 pat[i-1]” 所得到的 状态转移结果。
    // 所以 求取X(i)的值 会是一个 迭代的过程(求值时 依赖于 上一个值) -> X(i) = dfa[pat[i-1]][X(i-1)]
    // 🐖 “下一个位置的重启位置”的计算，不依赖于“当前位置”，而是 依赖于“其重启位置”
    private int calculateRestartSpotForNextSpot(int restartSpotForCurrentSpot, char currentPatternCharacter) {
        // 下一个位置的重启位置 = 当前位置的重启位置 在接收到 当前模式字符 时，所转换到的位置
        return cursorNextJumpSpotOnCondition[currentPatternCharacter][restartSpotForCurrentSpot];
    }

    /**
     * 当 在当前位置上 所接收到的字符 就是 模式字符 时，把 模式指针 跳转到 物理意义上的下一个位置
     *
     * @param currentCursorSpot       模式指针的位置
     * @param currentPatternCharacter 当前的模式字符
     */
    private void updateDFAFor(int currentCursorSpot, char currentPatternCharacter) {
        // 当 接收到的字符 就是 模式指针指向的模式字符 时，说明 模式指针 应该跳转到 物理意义上的下一个位置，则：
        // dfa[pattern_char][current_spot] = current_spot + 1;
        cursorNextJumpSpotOnCondition[currentPatternCharacter][currentCursorSpot] = currentCursorSpot + 1;
    }

    /**
     * 使用 指定位置的重启位置 的dfa[][]元素值 来 初始化 该位置上的 dfa[][]元素值
     *
     * @param currentCursorSpot         指定的模式指针位置
     * @param restartSpotForCurrentSpot 其所对应的重启位置
     */
    private void initDFAFor(int currentCursorSpot, int restartSpotForCurrentSpot) {
        // 对于 当前指针位置上 所可能接收到的每一个字符...
        for (int currentCharacterOption = 0; currentCharacterOption < characterOptionsAmount; currentCharacterOption++) {
            // 使用 “其重启位置”的dfa值 来 “模拟”/“填充” 其dfa值（状态应该转移到的 “逻辑上的下一个位置”）
            // 🐖 当前位置的重启位置X(i) 相比于 当前位置i 一般会更小 - X(i) < i
            int stateToSimulate = restartSpotForCurrentSpot;
            cursorNextJumpSpotOnCondition[currentCharacterOption][currentCursorSpot]
                    = cursorNextJumpSpotOnCondition[currentCharacterOption][stateToSimulate];
        }
    }

    /**
     * 对 模式字符串 进行预处理
     *
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
        preFillSpot0OnPatChar(pattern);

        // #3 从spot=1开始，每轮循环中，为 dfa[][current_spot]的其他元素 填充 正确的值
        for (int XSpotOfCurrentSpot = 0, currentPatCursorSpot = 1;
             currentPatCursorSpot < patternStrLength; currentPatCursorSpot++) {
            // 获取到 当前的模式字符
            char currentPatternCharacter = pattern[currentPatCursorSpot];

            // 初始化 dfa[][current_spot] 所有元素的值
            initDFAItemForCurrentSpot(currentPatCursorSpot, XSpotOfCurrentSpot, characterOptionsAmount);

            // 更新 dfa[pat_char_on_current_spot][current_spot] 元素的值
            updateDFAItemOnMatchCase(currentPatCursorSpot, currentPatternCharacter);

            // 更新“重启位置X” - 用于 dfa[][next_spot] 所有元素的初始化(下一轮循环)
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

    private void preFillSpot0OnPatChar(char[] pattern) {
        cursorNextJumpSpotOnCondition[Character.getNumericValue(pattern[0])][0] = 1;
    }

    /**
     * 获取到 在 指定的文本字符串 中，指定的模式字符串 第一次出现的索引位置
     * 原理：使用 模式字符串的DFA 来 模拟 在 文本字符串 中 对模式字符串的匹配过程
     * 对 文本字符串 来说，我们 用模式字符串 匹配 其中的子字符串；
     * 对 模式字符串 来说，我们 用其DFA 来 逐个接收文本字符，检查 文本字符串 会不会 被它接受！
     * @param passedTxtStr 指定的文本字符串
     * @return 返回 在 指定的文本字符串 中，指定的模式字符串 第一次出现的索引位置； 如果不存在匹配，则 返回 N
     */
    public int searchWithIn(String passedTxtStr) {
        int txtCharacterAmount = passedTxtStr.length();
        int currentTxtCursor, currentPatCursor;

        /* #1 使用当前的文本字符 来 逐步驱动 模式字符串的DFA */
        // 对于“当前文本指针”...
        for (currentTxtCursor = 0, currentPatCursor = 0;
             currentTxtCursor < txtCharacterAmount && currentPatCursor < patStrLength; currentTxtCursor++) {
            // 获取到 当前文本字符，并
            char currentTxtCharacter = passedTxtStr.charAt(currentTxtCursor);
            // 使用它 来 驱动 “模式字符串的DFA（有限状态自动机）”
            // 驱动DFA的手段：使用 构造好的DFA[txt_character][pat_cursor] 来 不断移动模式指针
            currentPatCursor = cursorNextJumpSpotOnCondition[currentTxtCharacter][currentPatCursor];
        }

        /* #2 根据“模式指针的最终位置” 来 判断 是否找到了 “子字符串匹配” */
        //  如果 “文本字符” 能够 把 “模式指针” 驱动到 “模式字符串的DFA结束状态”，说明 在“文本字符串”中 找到了“匹配模式字符串的子字符串”
        if (currentPatCursor == patStrLength) {
            // 则：返回 所匹配子字符串的 左指针位置      手段：文本指针位置 - 模式字符串的长度
            return currentTxtCursor - patStrLength;
        }
        // 否则，说明 没能找到 “匹配模式字符串的子字符串”，则：
        // 返回 原始文本字符串的 长度
        return txtCharacterAmount;
    }

    // 重载方法，参数是 字符数组类型
    // 原理：使用文本字符模拟DFA的操作
    public int search(char[] textStr) {
        int textStrLength = textStr.length;
        int txtCursor, patCursor;

        // #1 使用文本字符 来 驱动 模式字符串的DFA 运行下去(模式指针 会跳来跳去)
        for (txtCursor = 0, patCursor = 0;
             txtCursor < textStrLength && patCursor < patStrLength; txtCursor++) {
            // 获取到 文本字符
            char currentTxtCharacter = textStr[txtCursor];
            // 根据 模式指针的当前位置 与 文本字符 来 驱动模式指针到下一个位置
            patCursor = patCursorNextSpotWhenReceiving(patCursor, currentTxtCharacter);
        }

        // #2 根据 模式指针的最终位置 来 判断 是否找到了 “匹配模式字符串的子字符串”
        // 如果 模式指针 最终指向了 patStrLength的位置，说明 DFA接受了文本字符序列（找到了一个匹配），则：
        if (patCursor == patStrLength)
            // 匹配成功，打印出 “字符串成功匹配的起始位置”
            return txtCursor - patStrLength;

        // 如果 模式指针 停在了 其他位置，说明 某个位置上 字符匹配失败，则：
        // 返回 原始文本字符串的长度
        return textStrLength;
    }

    private int patCursorNextSpotWhenReceiving(int patCursor, char txtCharacter) {
        // 驱动模式字符串的DFA
        // 做法：使用 当前模式指针位置接收到文本字符后，跳转到的新位置 来 更新当前模式指针
        return cursorNextJumpSpotOnCondition[Character.getNumericValue(txtCharacter)][patCursor];
    }


    /**
     * 使用一个 模式字符串 与 一个输入的字符串 作为 命令行参数；
     * 在 文本字符串中 搜索 模式字符串；
     * 并打印出 模式字符串在文本字符串中第一次出现的位置
     *
     * @param args 命令行参数
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

        /* 展示 原始的文本字符串 以及 与模式字符串相匹配的子字符串 */
        // 打印 原始的文本字符串
        StdOut.println("textCharArr:    " + txtStr);
        // 打印 匹配到的模式字符串
        StdOut.print("patternCharArr: ");
        // 在 打印模式字符串 之前，先打印 offset个空格 来 实现 模式字符串 与 其在文本字符串中的匹配 对齐的效果
        int offset = startSpotOfMatchedSubStr;
        for (int currentSpot = 0; currentSpot < offset; currentSpot++)
            StdOut.print(" ");
        StdOut.println(patStr);

        /* 自定义 可选字符选项的数量 */
        System.out.println("== 可选字符的数量为自定义的值（256）👇 ==");
        KMP kmp2 = new KMP(patternCharArr, 256);
        int offset2 = kmp2.search(textCharArr);

        // 打印模式字符串*2
        StdOut.print("patternCharArr: ");
        for (int currentSpot = 0; currentSpot < offset2; currentSpot++)
            StdOut.print(" ");
        StdOut.println(patStr);
    }
}

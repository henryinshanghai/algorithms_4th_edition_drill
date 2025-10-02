package com.henry.string_05.data_compress_05.LZW_compression_07.codes_execution;

/******************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   https://algs4.cs.princeton.edu/55compression/abraLZW.txt
 *                https://algs4.cs.princeton.edu/55compression/ababLZW.txt
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  terminal command(change directory to current path):java LZW.java - < abraLZW.txt | java LZW.java +
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.TST;

/**
 * The {@code LZW} class provides static methods for compressing
 * and expanding a binary input using LZW compression over the 8-bit extended
 * ASCII alphabet with 12-bit codewords.
 * <p>
 * WARNING: Starting with Oracle Java 7u6, the substring method takes time and
 * space linear in the length of the extracted substring (instead of constant
 * time an space as in earlier versions). As a result, compression takes
 * quadratic time. TODO: fix.
 * See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 * for more details.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 验证：使用 LZW算法（使用“定长的码值” 来表示 “动态长度的字符序列”）能够实现 无损压缩模型的过程（#1 对 字节序列/字节流 的压缩； #2 对 字节序列/字节流 的展开）
public class LZW {
    private static final int characterOptions = 256;        // number of input chars 所有可能的字符选项 数量
    private static final int encodedValueOptions = 4096;       // number of codewords = 2^W 所有可能的编码结果 数量
    private static final int bitWidthLength = 12;         // codeword width 编码结果的宽度

    // Do not instantiate.
    private LZW() {
    }

    /**
     * 从 标准输入 中 读取 8比特/字节的序列；
     * 使用 12位宽度的 LZW压缩算法 来 压缩 它们；
     * 把 压缩结果 写入到 标准输出 中
     */
    public static void compress() {
        TST<Integer> keyToItsEncodedValueTable = new TST<Integer>(); // 编码表（用于 对字符进行编码的 编译表）

        // #0 初始化 “单字符键”的“符号表条目” - 🐖 存在有 多少个“字符选项”，就需要 对应地初始化 多少个 “符号表条目”
        initSingleLetterEntries(keyToItsEncodedValueTable);

        // 从 “单字符的最大编码”码值(位置)的 下一个码值(位置) 开始，继续 对 “多字符键”条目 进行编码
        int currentUnassignedCodeValue = characterOptions + 1;  // 把 characterOptions 预留作为 文件结束的口令

        // 从 标准输入 中 读取 “待编码的 输入字符序列” 作为 “尚未被处理的 字符序列”
        String unattendedCharacterSequence = BinaryStdIn.readString();
        // 对于 “未处理的输入” unattendedCharacterSequence...
        while (unattendedCharacterSequence.length() > 0) {
            /* #1 向 标准输出 中 写入 “当前最长匹配前缀键“的码值（作为 其编码结果） */
            // 获取到 “该未处理输入” 存在于编码表中的 “最长匹配前缀”键 -
            // 🐖 最开始 时，只存在有 单字符键条目，所以 最长前缀 也是 单字符的
            String longestPrefixStr = getLongestPrefixExistInEncodedValueTable(unattendedCharacterSequence, keyToItsEncodedValueTable);
            writeEncodedResultToOutput(keyToItsEncodedValueTable, longestPrefixStr);

            /* #2 向 编码表 中 添加 “多字符”条目 */
            int currentPrefixLength = longestPrefixStr.length();
            // 如果 满足特定的条件，则：向 编码表 中 添加 多字符键条目
            if (conditionsAreFavorable(currentPrefixLength, unattendedCharacterSequence, currentUnassignedCodeValue)) {
                // 手段：分别构造 “编码表条目”的 “多字符键” 与 “码值”，并 将 它们 关联起来
                // ① 构造 “编码表条目”的 “字符串键”（由 “当前最长匹配前缀prefix” + “当前输入字符char” 组成） - 手段：截取 “未处理的输入” 的 前（最长匹配前缀长度 + 1）个字符的 子字符串
                String currentMultiCharacterKey = unattendedCharacterSequence.substring(0, currentPrefixLength + 1);
                // ② 构造 “符号表条目”的 “码值” - 手段：直接使用 编码表中的 未被分配key的码值 即可（用完后++）
                keyToItsEncodedValueTable.put(currentMultiCharacterKey, currentUnassignedCodeValue++);
            }

            /* #3 添加完 “多字符条目” 后，更新 “未处理的输入”变量 */
            unattendedCharacterSequence = updateItAsRequired(unattendedCharacterSequence, currentPrefixLength);
        }

        // #4 最后，向 标准输出 中 写入 预留的EOF字符
        BinaryStdOut.write(characterOptions, bitWidthLength);
        // 刷新 并 关闭流
        BinaryStdOut.close();
    }

    /**
     * 把 未处理字符序列 的头字符指针，向右移动到 “未处理的字符”位置
     *
     * @param unattendedCharacterSequence 当前 未处理的字符序列（开头已经被处理过了）
     * @param currentPrefixLength         当前 未处理的字符 的位置
     * @return 返回 真正的 未处理的字符序列
     */
    private static String updateItAsRequired(String unattendedCharacterSequence, int currentPrefixLength) {
        // 手段：从 “当前未处理的字符序列” 中，截取掉/移除掉 开头处 “最长匹配前缀”中的字符（因为 这些字符 已经被处理/输出/添加到编码表中 过了）
        // aka 截取出 其 从 “最长前缀位置” 到 “末尾位置”的 子字符串  originalStr.substring(<start_index>)
        unattendedCharacterSequence = unattendedCharacterSequence.substring(currentPrefixLength);            // Scan past s in inputCharacterSequence.
        return unattendedCharacterSequence;
    }

    /**
     * 判断 是否需要 继续 向编码表中 添加 多字符键的条目
     *
     * @param currentPrefixLength         未处理的字符序列 在编码表中的 最长匹配前缀
     * @param unattendedCharacterSequence 原始字符串中，尚未被处理的字符序列
     * @param currentUnassignedCodeValue  当前 尚未被分配key的 码值
     * @return yes | no
     */
    private static boolean conditionsAreFavorable(int currentPrefixLength,
                                                  String unattendedCharacterSequence,
                                                  int currentUnassignedCodeValue) {
        return  // 条件① 未处理的字符序列的长度 比起 当前”最长匹配前缀“ 要更长，说明???
                prefixStrShorterThanUnattendedInput(unattendedCharacterSequence, currentPrefixLength)
                        &&  // 条件② 当前 未被分配key的码值 在最大码值的范围内，说明 编码表 还没有 被填满
                        withinMaxCode(currentUnassignedCodeValue);
    }

    private static void writeEncodedResultToOutput(TST<Integer> keyToItsEncodedValueTable, String longestPrefixStr) {
        // 从 编码表 中 获取到 该键 所对应的“编码值”
        Integer itsEncodedValue = keyToItsEncodedValueTable.get(longestPrefixStr);
        // 向 标准输出 中 写入 此码值 - 至此，由 “最长前缀键” 所对应的 “字符序列中的所有字符” 就已经 “处理完成”
        BinaryStdOut.write(itsEncodedValue, bitWidthLength);      // Print s's encoding.
    }

    private static void initSingleLetterEntries(TST<Integer> keyToItsEncodedValueTable) {
        for (int currentCharacter = 0; currentCharacter < characterOptions; currentCharacter++) {
            // 单字符键 -> 码值 🐖 这里 “所设定的码值” 就是 字符的int表示
            String singleCharacterKey = "" + (char) currentCharacter;
            int keysEncodedValue = currentCharacter;

            // 按照 “字符 -> 字符的ASCII码编码”的 规则 来 对 单字符键 进行编码
            keyToItsEncodedValueTable.put(singleCharacterKey, keysEncodedValue);
        }
    }

    private static boolean withinMaxCode(int keysEncodedResult) {
        return keysEncodedResult < encodedValueOptions;
    }

    private static boolean prefixStrShorterThanUnattendedInput(String unattendedCharacterSequence, int currentPrefixLength) {
        return currentPrefixLength < unattendedCharacterSequence.length();
    }

    /**
     * 获取到 指定符号表中 所存在的、能够作为指定字符串前缀的 最长前缀键
     *
     * @param unattendedCharacterSequence 指定的字符串
     * @param keyStrToEncodedValue        指定的符号表（由于键是一个字符串，因此 由三向Trie树 来实现）
     * @return
     */
    private static String getLongestPrefixExistInEncodedValueTable(String unattendedCharacterSequence,
                                                                   TST<Integer> keyStrToEncodedValue) {
        // 手段：调用 三向Trie树的API longestPrefixOf(given_str)
        return keyStrToEncodedValue.longestPrefixOf(unattendedCharacterSequence);
    }

    /**
     * 从 标准输入 中 读取 使用12位宽度的 LZW压缩算法 所编码的 比特序列；
     * 扩展 这些比特序列；
     * 把 结果 写入到 标准输出 中。
     */
    public static void expand() {
        // #0 初始化 一个 大小 为 “所有可能编码结果数量” 的 符号表
        String[] codeValueToItsDecodedStr = new String[encodedValueOptions];
        int currentCodeValueOfDecodedTable; // “码值” <=> “编码值”

        // #1 初始化 “解码表”中的 “单字符键”的条目 - 码值 -> 其所编码的字符序列/从它解码出的字符序列
        // 手段：使用 “字符的int表示” 来 作为“码值”，使用 字符本身 来 作为“字符串”
        for (currentCodeValueOfDecodedTable = 0; currentCodeValueOfDecodedTable < characterOptions; currentCodeValueOfDecodedTable++) {
            String itsDecodedStr = "" + (char) currentCodeValueOfDecodedTable;
            codeValueToItsDecodedStr[currentCodeValueOfDecodedTable] = itsDecodedStr;
        }

        // 把 “当前码值” 所对应的“字符串值” 设置为 空字符串 - 把 它 作为 EOF
        codeValueToItsDecodedStr[currentCodeValueOfDecodedTable++] = "";                        // (unused) lookahead for EOF

        // #2-① 读取“输入中的当前码值”
        int currentCodeValueOfInput = BinaryStdIn.readInt(bitWidthLength);
        // 如果 码值 在 “最大的可选字符选项”的刻度 上，说明 展开的信息 是一个 空字符串(到达EOF)，则：直接返回，不再 解码 了
        if (currentCodeValueOfInput == characterOptions) return;           // expanded message is empty string
        // #2-② 解码出 “当前码值” 所对应的 字符串
        String currentDecodedStr = codeValueToItsDecodedStr[currentCodeValueOfInput];

        while (true) {
            // #2-③ 把 当前码值“ 解码所得到的字符串” 写入到 标准输出 中
            BinaryStdOut.write(currentDecodedStr);

            // #3-① 读取 “输入中的下一个码值”
            int nextCodeValueOfInput = BinaryStdIn.readInt(bitWidthLength);
            // 如果 码值 等于 “最大的可选字符选项”，说明 到达EOF，则：解码 结束，跳出 循环
            if (nextCodeValueOfInput == characterOptions) break;
            // #3-② 解码出 “输入中的下一个码值” 所对应的字符串
            String nextDecodedStr = codeValueToItsDecodedStr[nextCodeValueOfInput];

            // #4 向 “解码表” 中 添加条目👇
            // #4-① 先处理 特殊情况：如果 解码表中的 待填充条目的码值 与 输入中的下一个码值 相同，则..
            if (currentCodeValueOfDecodedTable == nextCodeValueOfInput)
                // 按照规则，构造出 “下一个码值” 所对应的 字符序列👇
                // “输入中的下一个码值” 所对应的字符串 就等于 “输入中的当前码值” 所对应的字符串(AB) + “当前码值 所对应的 字符串的首字符”(A)
                nextDecodedStr = currentDecodedStr + currentDecodedStr.charAt(0);
            // #4-② 如果 解码表中的 待填充条目的码值 还是 在 “有效码值”的范围 内，则：
            if (currentCodeValueOfDecodedTable < encodedValueOptions)
                /* 构造 解码表条目的 “码值” 与 “字符串”，将 它们 关联起来 */
                // 构造 码值 - 手段：把 “自然数序列中的当前码值” +1；
                // 构造“字符串” - 手段：“输入中的 当前编码 所对应的字符串” + “输入中的 下一个编码 所对应的字符串”的首字符(前瞻字符)
                codeValueToItsDecodedStr[currentCodeValueOfDecodedTable++] = currentDecodedStr + nextDecodedStr.charAt(0);

            // #5 更新 “当前解码出的字符串”变量 为 “下一个解码出的字符串” 来 为 下一个循环 做准备{1 打印 字符序列； 2 添加 解码表条目}
            currentDecodedStr = nextDecodedStr;
        }

        // 刷新 并 关闭 流
        BinaryStdOut.close();
    }

    /**
     * 命令行参数为-时，执行compress()
     * 命令行参数为+时，执行expand()
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
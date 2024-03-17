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
// 验证：使用LZW算法（使用“定长的码值” 来表示 “动态长度的字符序列”）能够实现 无损压缩模型的过程（#1 对字节序列/字节流的压缩； #2 对字节序列/字节流的展开）
public class LZW {
    private static final int characterOptions = 256;        // number of input chars
    private static final int encodedValueOptions = 4096;       // number of codewords = 2^W
    private static final int bitWidthLength = 12;         // codeword width

    // Do not instantiate.
    private LZW() {
    }

    /**
     * Reads a sequence of 8-bit bytes from standard input; compresses
     * them using LZW compression with 12-bit codewords; and writes the results
     * to standard output.
     * 从标准输入中读取 8比特字节的序列；
     * 使用 12位宽度的LZW压缩算法 来 压缩它们；
     * 把压缩结果写入到标准输出中
     */
    public static void compress() {
        String unattendedCharacterSequence = BinaryStdIn.readString();
        TST<Integer> keyToItsEncodedValueTable = new TST<Integer>(); // 编码表

        // #0 初始化“单字符键”的“符号表条目” - 🐖 存在有多少个“字符选项”，就对应地初始化多少个“符号表条目”
        for (int currentCharacter = 0; currentCharacter < characterOptions; currentCharacter++) {
            // 单字符键 -> 码值 🐖 这里“所设定的码值”就是 字符的int表示
            String singleCharacterKey = "" + (char) currentCharacter;
            int keysEncodedValue = currentCharacter;

            keyToItsEncodedValueTable.put(singleCharacterKey, keysEncodedValue);
        }

        // 从“单字符的最大编码”码值(位置)的下一个码值(位置)开始，继续 对“多字符键”条目进行编码
        int currentUnassignedCodeValue = characterOptions + 1;  // characterOptions预留作为文件结束的口令

        // 对于“未处理的输入” unattendedCharacterSequence...
        while (unattendedCharacterSequence.length() > 0) {
            /* #1 向标准输出中写入“当前最长匹配前缀键的码值” */
            // 获取到“该未处理输入” 存在于编码表中的“最长匹配前缀”键 - 🐖 最开始时，只存在有单字符键条目 所以最长前缀也是单字符的
            String longestPrefixStr = getLongestPrefixExistInEncodedValueTable(unattendedCharacterSequence, keyToItsEncodedValueTable);  // Find max prefix match s.
            // 从编码表中获取到 键所对应的“编码值”
            Integer itsEncodedValue = keyToItsEncodedValueTable.get(longestPrefixStr);
            // 向标准输出中写入此码值 - 至此，由“最长前缀键”所对应的“字符序列中的所有字符” 就已经“处理完成”
            BinaryStdOut.write(itsEncodedValue, bitWidthLength);      // Print s's encoding.

            /* #2 向编码表中添加“多字符”条目 */
            int currentPrefixLength = longestPrefixStr.length();
            // 如果编码表中的“最长匹配前缀”键 比起“未处理的输入”要更短，并且“当前多字符键”还在“有效码值范围”内...
            if (prefixStrShorterThanUnattendedInput(unattendedCharacterSequence, currentPrefixLength) && withinMaxCode(currentUnassignedCodeValue))    // Add s to symbol table.
            {
                // 向编码表中添加“多字符”条目 - 手段：分别构造“编码表条目”的“多字符键”与“码值”，并将它们关联起来
                // ① 构造“编码表条目”的“字符串键”（由“最长匹配前缀prefix” + “当前输入字符char”组成） - 手段：截取“未处理的输入”的 前（最长匹配前缀长度 + 1）个字符的子字符串
                String currentMultiCharacterKey = unattendedCharacterSequence.substring(0, currentPrefixLength + 1);
                // ② 构造“符号表条目”的“码值” - 手段：把编码表中的“上一个条目的码值”+1即可
                keyToItsEncodedValueTable.put(currentMultiCharacterKey, currentUnassignedCodeValue++);
            }

            /* #3 添加完“多字符条目”后，更新“未处理的输入”变量 */
            // 目标：把字符串的头字符指针，向右移动到“未处理的字符”位置
            // 手段：从“当前未处理的字符序列”中，截取掉/移除掉 “最长匹配前缀”中的字符（因为这些字符已经被处理/输出/添加到编码表中 过了）
            // aka 截取出 其“最长前缀位置”到“末尾位置”的子字符串  originalStr.substring(<start_index>)
            unattendedCharacterSequence = unattendedCharacterSequence.substring(currentPrefixLength);            // Scan past s in inputCharacterSequence.
        }

        // #4 最后，向标准输出中写入 预留的EOF字符 并 关闭流
        BinaryStdOut.write(characterOptions, bitWidthLength);
        BinaryStdOut.close();
    }

    private static boolean withinMaxCode(int keysEncodedResult) {
        return keysEncodedResult < encodedValueOptions;
    }

    private static boolean prefixStrShorterThanUnattendedInput(String unattendedCharacterSequence, int currentPrefixLength) {
        return currentPrefixLength < unattendedCharacterSequence.length();
    }

    private static String getLongestPrefixExistInEncodedValueTable(String unattendedCharacterSequence, TST<Integer> keyStrToEncodedValue) {
        return keyStrToEncodedValue.longestPrefixOf(unattendedCharacterSequence);
    }

    /**
     * Reads a sequence of bit encoded using LZW compression with
     * 12-bit codewords from standard input; expands them; and writes
     * the results to standard output.
     * 从标准输入中读取 使用12位宽度的LZW压缩算法 所编码的比特序列；
     * 扩展这些比特序列；
     * 把结果写入到标准输出中。
     */
    public static void expand() {
        // #0 初始化一个“所有可能编码大小”的符号表
        String[] codeValueToItsCharacterSeq = new String[encodedValueOptions];
        int currentCodeValueInDecodedTable; // next available codeword value

        // #1 初始化“符号表”（解码表）中的“单字符键”的条目 - 手段：使用“字符的int表示”来作为“码值”，使用字符本身来作为“字符串”
        for (currentCodeValueInDecodedTable = 0; currentCodeValueInDecodedTable < characterOptions; currentCodeValueInDecodedTable++)
            codeValueToItsCharacterSeq[currentCodeValueInDecodedTable] = "" + (char) currentCodeValueInDecodedTable;

        codeValueToItsCharacterSeq[currentCodeValueInDecodedTable++] = "";                        // (unused) lookahead for EOF

        // #2-① 读取“输入中的当前码值”
        int currentCodeValueOfInput = BinaryStdIn.readInt(bitWidthLength);
        // 如果码值 在“最大的可选字符选项”的刻度上，说明 展开的信息是一个空字符串，则：直接返回，不再解码了
        if (currentCodeValueOfInput == characterOptions) return;           // expanded message is empty string
        // #2-② 解码出 “当前码值”所对应的字符串
        String currentDecodedStr = codeValueToItsCharacterSeq[currentCodeValueOfInput];

        while (true) {
            // #2-③ 把当前码值“解码得到的字符串”写入到标准输出中
            BinaryStdOut.write(currentDecodedStr);

            // #3-① 读取“输入中的下一个码值”
            currentCodeValueOfInput = BinaryStdIn.readInt(bitWidthLength);
            // 如果 码值等于“最大的可选字符选项”，说明???，则：xx结束，跳出循环
            if (currentCodeValueOfInput == characterOptions) break;
            // #3-② 解码出 “输入中的下一个码值”所对应的字符串
            String nextDecodedStr = codeValueToItsCharacterSeq[currentCodeValueOfInput];

            // #4 向“解码表”中添加条目👇
            // #4-① 先处理特殊情况：前瞻过程中得到的字符 与 当前子字符串的开头字符(??) 相同，则..
            if (currentCodeValueInDecodedTable == currentCodeValueOfInput)
                // 按照规则，构造出 “下一个码值”所对应的字符序列👇
                // “输入中的下一个码值”所对应的字符串 就等于 “输入中的当前码值”所对应的字符串 + “当前码值所对应的字符串的首字符”
                nextDecodedStr = currentDecodedStr + currentDecodedStr.charAt(0);
            // #4-② 如果当前码值 还是在“有效码值”的范围内，则：构造符号表条目的“码值” 与 “字符串”，将它们关联起来
            if (currentCodeValueInDecodedTable < encodedValueOptions)
                // 构造码值 - 手段：把“自然数序列中的当前码值”+1；  构造“字符串” - 手段：“输入中的当前编码所对应的字符串” + “输入中的下一个编码所对应的字符串”的首字符
                codeValueToItsCharacterSeq[currentCodeValueInDecodedTable++] = currentDecodedStr + nextDecodedStr.charAt(0);

            // #5 更新 “当前字符串”变量 为 “输入中的下一个字符串” 来 为下一个循环做准备{1 打印字符序列； 2 添加解码表条目}
            currentDecodedStr = nextDecodedStr;
        }
        BinaryStdOut.close();
    }

    /**
     * Sample client that calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
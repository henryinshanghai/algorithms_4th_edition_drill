package com.henry.string_05.data_compress_05.LZW_compression_07;

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
        TST<Integer> keyStrToEncodedValue = new TST<Integer>();

        // since TST is not balanced, it would be better to insert in a different order
        for (int currentCharacter = 0; currentCharacter < characterOptions; currentCharacter++)
            keyStrToEncodedValue.put("" + (char) currentCharacter, currentCharacter);

        int keysEncodedResult = characterOptions + 1;  // R is codeword for EOF

        while (unattendedCharacterSequence.length() > 0) {
            String longestPrefixStr = getLongestPrefixExistInSymbolTable(unattendedCharacterSequence, keyStrToEncodedValue);  // Find max prefix match s.
            Integer itsEncodedValue = keyStrToEncodedValue.get(longestPrefixStr);
            BinaryStdOut.write(itsEncodedValue, bitWidthLength);      // Print s's encoding.

            int currentPrefixLength = longestPrefixStr.length();
            if (prefixStrShorterThanCurrentInput(unattendedCharacterSequence, currentPrefixLength) && withinMaxCode(keysEncodedResult))    // Add s to symbol table.
            {
                // 从指定的编码开始，把编码分配给“从输入中截取的子字符串”（前currentPrefixLength个字符）
                String currentKey = unattendedCharacterSequence.substring(0, currentPrefixLength + 1);
                keyStrToEncodedValue.put(currentKey, keysEncodedResult++);
            }

            // 更新“未处理的当前输入” - 截取“最长前缀位置”到“末尾位置”的子字符串
            unattendedCharacterSequence = unattendedCharacterSequence.substring(currentPrefixLength);            // Scan past s in inputCharacterSequence.
        }

        // 以给定的比特宽度 输出给定的数字
        BinaryStdOut.write(characterOptions, bitWidthLength);
        BinaryStdOut.close();
    }

    private static boolean withinMaxCode(int keysEncodedResult) {
        return keysEncodedResult < encodedValueOptions;
    }

    private static boolean prefixStrShorterThanCurrentInput(String unattendedCharacterSequence, int currentPrefixLength) {
        return currentPrefixLength < unattendedCharacterSequence.length();
    }

    private static String getLongestPrefixExistInSymbolTable(String unattendedCharacterSequence, TST<Integer> keyStrToEncodedValue) {
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
        String[] codeValueToDecodedStr = new String[encodedValueOptions];
        int legitCodeValue; // next available codeword value

        // 初始化符号表中的“单字符键”的条目
        for (legitCodeValue = 0; legitCodeValue < characterOptions; legitCodeValue++)
            codeValueToDecodedStr[legitCodeValue] = "" + (char) legitCodeValue;
        codeValueToDecodedStr[legitCodeValue++] = "";                        // (unused) lookahead for EOF

        int codeValue = BinaryStdIn.readInt(bitWidthLength);
        if (codeValue == characterOptions) return;           // expanded message is empty string
        String currentDecodedStr = codeValueToDecodedStr[codeValue];

        while (true) {
            BinaryStdOut.write(currentDecodedStr);
            codeValue = BinaryStdIn.readInt(bitWidthLength);

            if (codeValue == characterOptions) break;

            String nextDecodedStr = codeValueToDecodedStr[codeValue];
            // 先处理特殊情况：前瞻过程中得到的字符 与 当前子字符串的开头字符 相同👇
            if (legitCodeValue == codeValue)
                nextDecodedStr = currentDecodedStr + currentDecodedStr.charAt(0);
            // 构造反编译表
            if (legitCodeValue < encodedValueOptions)
                // 为下一个码值 绑定 字符串（当前字符串 + 下一个字符串的首字符）
                codeValueToDecodedStr[legitCodeValue++] = currentDecodedStr + nextDecodedStr.charAt(0);

            // 更新“当前字符串” 为下一个循环做准备
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
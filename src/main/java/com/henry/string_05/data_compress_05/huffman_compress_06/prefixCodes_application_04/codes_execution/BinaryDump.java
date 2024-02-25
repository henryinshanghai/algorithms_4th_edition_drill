package com.henry.string_05.data_compress_05.huffman_compress_06.prefixCodes_application_04.codes_execution;
/******************************************************************************
 *  Compilation:  javac BinaryDump.java
 *  Execution:    java BinaryDump n < file
 *  Dependencies: BinaryStdIn.java
 *  Data file:    https://introcs.cs.princeton.edu/stdlib/abra.txt
 *
 *  Reads in a binary file and writes out the bits, n per line.
 *
 *  % more abra.txt
 *  ABRACADABRA!
 *
 *  % java BinaryDump 16 < abra.txt
 *  0100000101000010
 *  0101001001000001
 *  0100001101000001
 *  0100010001000001
 *  0100001001010010
 *  0100000100100001
 *  96 bits
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code BinaryDump} class provides a client for displaying the contents
 * of a binary file in binary.
 * <p>
 * For more full-featured versions, see the Unix utilities
 * {@code od} (octal dump) and {@code hexdump} (hexadecimal dump).
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * <p>
 * See also {@link HexDump} and {@link PictureDump}.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 作用：读取标准输入中的字符序列，并得到其对应的比特序列
public class BinaryDump {

    // Do not instantiate.
    private BinaryDump() {
    }

    /**
     * Reads in a sequence of bytes from standard input and writes
     * them to standard output in binary, k bits per line,
     * where k is given as a command-line integer (defaults
     * to 16 if no integer is specified); also writes the number
     * of bits.
     * 从标准输入读取 字节序列，并以二进制的形式把它们写入到标准输出中（每行k个比特）
     * k由命令行整数指定（默认值是16）
     * 同时写入 比特数量
     *
     * @param args the command-line arguments 命令行参数
     */
    public static void main(String[] args) {
        // 每行的比特数量
        int bitsPerLine = 16; // 默认值16
        if (args.length == 1) {
            // 读取参数传入的值 作为“每行的比特数量”
            bitsPerLine = Integer.parseInt(args[0]);
        }

        int count;
        // 如果标准输入不为空...
        for (count = 0; !BinaryStdIn.isEmpty(); count++) {
            if (bitsPerLine == 0) {
                // 读取标准输入中的下一个比特，作为布尔值
                BinaryStdIn.readBoolean();
                continue;
            } else if (count != 0 && count % bitsPerLine == 0)
                StdOut.println();

            if (BinaryStdIn.readBoolean())
                StdOut.print(1); // 打印1
            else
                StdOut.print(0); // 打印0
        }

        // 打印换行符号
        if (bitsPerLine != 0)
            StdOut.println();
        // 打印 具体的比特数量
        StdOut.println(count + " bits");
    }
}
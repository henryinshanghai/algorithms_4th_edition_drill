package com.henry.string_05.data_compress_05.run_length_encoding_05.codes_execution;

/******************************************************************************
 *  Compilation:  javac HexDump.java
 *  Execution:    java HexDump < file
 *  Dependencies: BinaryStdIn.java StdOut.java
 *  Data file:    https://algs4.cs.princeton.edu/55compression/abra.txt
 *
 *  Reads in a binary file and writes out the bytes in hex, 16 per line.
 *
 *  % more abra.txt
 *  ABRACADABRA!
 *
 *  % java HexDump 16 < abra.txt
 *  41 42 52 41 43 41 44 41 42 52 41 21
 *  96 bits
 *
 *
 *  Remark
 *  --------------------------
 *   - Similar to the Unix utilities od (octal dump) or hexdump (hexadecimal dump).
 *
 *  % od -t x1 < abra.txt
 *  0000000 41 42 52 41 43 41 44 41 42 52 41 21
 *  0000014
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.PictureDump;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code HexDump} class provides a client for displaying the contents
 * of a binary file in hexadecimal.
 * 以 十六进制数字的形式 来 展示二进制文件
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * <p>
 * See also {@link BinaryDump} and {@link PictureDump}.
 * For more full-featured versions, see the Unix utilities
 * {@code od} (octal dump) and {@code hexdump} (hexadecimal dump).
 * <p>
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 作用：读取 标准输入中的 字符序列，并 得到 其对应的十六进制表示
public class HexDump {

    // Do not instantiate.
    private HexDump() {
    }

    /**
     * 从 标准输入中 读取 字节序列，并 使用 十六进制数的形式 把它们写入到 标准输出中。
     * 每行 k个 十六进制数，其中 k是 通过命令行参数指定的整数（如果没有指定，默认为16）
     * 同事写入 比特数量
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        int hexesPerLine = 16;
        if (args.length == 1) {
            hexesPerLine = Integer.parseInt(args[0]);
        }

        int currentByteCursor;
        for (currentByteCursor = 0; !BinaryStdIn.isEmpty(); currentByteCursor++) {
            if (hexesPerLine == 0) {
                BinaryStdIn.readChar();
                continue;
            }
            if (currentByteCursor == 0) StdOut.printf("");
            else if (currentByteCursor % hexesPerLine == 0) StdOut.printf("\n", currentByteCursor);
            else StdOut.print(" ");
            char encodedChar = BinaryStdIn.readChar();
            StdOut.printf("%02x", encodedChar & 0xff);
        }
        if (hexesPerLine != 0) StdOut.println();
        StdOut.println((currentByteCursor * 8) + " bits");
    }
}

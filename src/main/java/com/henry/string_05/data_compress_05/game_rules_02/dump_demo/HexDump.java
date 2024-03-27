package com.henry.string_05.data_compress_05.game_rules_02.dump_demo;

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
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code HexDump} class provides a client for displaying the contents
 * of a binary file in hexadecimal.
 * 以十六进制数字的形式 来 展示二进制文件
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
// 作用：读取标准输入中的字符序列，并得到其对应的十六进制表示
public class HexDump {

    // Do not instantiate.
    private HexDump() {
    }

    /**
     * 从标准输入中读取一个比特序列，
     * 并以16进制的记法 把它们写入到标准输出中，
     * 每行k个hex数字 - k作为一个命令行参数给定(如果整数值没有被指定的话，默认值为16)
     * 同时，也写入 比特的数量
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int byteAmountPerLine = 16;
        if (args.length == 1) {
            byteAmountPerLine = Integer.parseInt(args[0]);
        }

        int byteCounter;

        for (byteCounter = 0; !BinaryStdIn.isEmpty(); byteCounter++) {
            // 如果使用者传入的“每行展示的字节数量”为0，则：程序只能空读输入，而不做任何输出 - 手段：continue
            if (byteAmountPerLine == 0) {
                BinaryStdIn.readChar();
                continue;
            }

            // 在最开始的位置打印空字符串 - why?
            if (byteCounter == 0) StdOut.printf("");
            // 如果字节打印到了行末，则：打印换行符
            else if (isMeetTheEndOfCurrentLine(byteAmountPerLine, byteCounter))
                StdOut.printf("\n", byteCounter);
            // 对于没有到行末的情况：打印空格符 来 分隔两个十六进制数字
            else StdOut.print(" ");

            // 从标准输入中读取一个8个比特 来 得到一个字符
            char characterOfInput = BinaryStdIn.readChar();
            // 以指定的格式 打印这个字符
            StdOut.printf("%02x", characterOfInput & 0xff);
        }

        // 在用户指定的“每行的比特数量”不为0的情况下，就需要 手动地在当前行(最后一行)中添加一个换行符
        if (byteAmountPerLine != 0) StdOut.println();

        // 打印具体的比特数量
        StdOut.println((byteCounter * 8) + " bits");
    }

    // 判断字节计数器是不是到了该换行的位置
    private static boolean isMeetTheEndOfCurrentLine(int byteAmountPerLine, int byteCounter) {
        return byteCounter % byteAmountPerLine == 0;
    }
}
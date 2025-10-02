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
// 作用：读取 标准输入中的字符序列，并 得到 其对应的 十六进制表示
public class HexDump {

    // Do not instantiate.
    private HexDump() {
    }

    /**
     * 从 标准输入 中 读取一个字符序列，
     * 并 以 16进制的记法 把 它们 写入到 标准输出 中，
     * 每行 k个 hex数字 - k 作为 一个命令行参数 指定(如果 整数值 没有被指定 的话，默认值为16)
     * 同时，也写入 比特的数量
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        int hexAmountPerLine = 16;
        // 如果 用户指定了 每行所要显示的 hex数字的数量，
        if (args.length == 1) {
            // 则：以用户指定的数量为准
            hexAmountPerLine = Integer.parseInt(args[0]);
        }

        int byteCounter;

        for (byteCounter = 0; !BinaryStdIn.isEmpty(); byteCounter++) {
            // 如果 用户指定的 “每行展示的字节数量”为0，
            if (hexAmountPerLine == 0) {
                // 则：程序只能空读输入，而不做任何输出 - 手段：continue
                // 🐖 每次读取8个bit(一个byte)  手段：readChar() 因为char在java中就是1个字节的长度
                BinaryStdIn.readChar();
                continue;
            }

            // 如果 字节计数器的值为0，说明 当前 在 最开始的位置，则：
            if (byteCounter == 0) {
                // 打印 空字符串 - why?
                StdOut.printf("");
            } else if (isMeetTheEndOfCurrentLine(hexAmountPerLine, byteCounter)) {// 如果 字节 打印到了 行末，则：
                // 打印 换行符 来 按照预期 每行展示特定数量的hex(字节)
                StdOut.printf("\n", byteCounter);
            } else { // 如果 没有到行末，则：
                // 打印 空格符 来 分隔 两个十六进制数字
                StdOut.print(" ");
            }

            // 从 标准输入 中 读取一个8个比特 来 得到 一个字符
            char characterOfInput = BinaryStdIn.readChar();
            // 以 指定的格式 打印这个字符
            StdOut.printf("%02x", characterOfInput & 0xff);
        }

        // 如果 用户指定的“每行的比特数量” 不为0，则：
        if (hexAmountPerLine != 0) {
            // 手动地在当前行(最后一行)中添加一个换行符
            StdOut.println();
        }

        // 打印 具体的比特数量
        StdOut.println((byteCounter * 8) + " bits");
    }

    // 判断 字节计数器 是不是到了 该换行的位置
    private static boolean isMeetTheEndOfCurrentLine(int byteAmountPerLine, int byteCounter) {
        // 手段：查看 取余结果 是否为0，为0说明是整数倍 该换行了！
        return byteCounter % byteAmountPerLine == 0;
    }
}
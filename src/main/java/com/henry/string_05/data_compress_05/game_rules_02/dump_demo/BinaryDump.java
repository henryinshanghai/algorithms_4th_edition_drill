package com.henry.string_05.data_compress_05.game_rules_02.dump_demo;

/******************************************************************************
 *  Compilation:  javac BinaryDump.java
 *  Execution:    java BinaryDump n < file
 *  Dependencies: BinaryStdIn.java
 *  Data file:    https://introcs.cs.princeton.edu/stdlib/abra.txt
 *
 *  Reads in a binary file and writes out the bits, n per line.
 *  读取一个二进制文件，并输出比特序列（每行输出n个比特）
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
 * 以二进制数字的形式 来 展示二进制文件
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
// 作用：读取标准输入中的字符序列，并得到其对应的二进制表示
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
        // 指定每行所展示的比特数量 - 默认值16
        int bitAmountPerLine = 16;
        if (args.length == 1) { // 如果只存在有1个命令行参数，则...
            // 把 参数传入的值 作为“每行的比特数量”
            bitAmountPerLine = Integer.parseInt(args[0]);
        }

        int bitCounter;
        // 只要标准输入不为空...
        for (bitCounter = 0; !BinaryStdIn.isEmpty(); bitCounter++) {
            if (bitAmountPerLine == 0) {
                // 读取标准输入中的下一个比特值 - 为什么这里只消耗比特，但是不打印任何东西？
                // 因为如果 开发者指定 “每行所打印的比特数量为0”的话，那么 程序就只能 不打印任何比特，而空消耗输入了
                BinaryStdIn.readBoolean();
                continue;
            } else if (isMeetTheEndOfCurrentLine(bitCounter, bitAmountPerLine))
                // 打印换行
                StdOut.println();

            // 读取标准输入中的下一个比特值
            // 规则：如果读取结果为true，则打印1；如果为false，则打印0
            if (BinaryStdIn.readBoolean())
                StdOut.print(1); // 打印1
            else
                StdOut.print(0); // 打印0
        }

        // 在用户指定的“每行的比特数量”不为0的情况下，就需要 手动地在当前行(最后一行)中添加一个换行符
        if (bitAmountPerLine != 0)
            StdOut.println();

        // 打印出 具体的比特数量
        StdOut.println(bitCounter + " bits");
    }

    // 判断比特计数器是不是到了该换行的位置
    private static boolean isMeetTheEndOfCurrentLine(int bitCounter, int bitAmountPerLine) {
        return bitCounter != 0 && bitCounter % bitAmountPerLine == 0;
    }
}
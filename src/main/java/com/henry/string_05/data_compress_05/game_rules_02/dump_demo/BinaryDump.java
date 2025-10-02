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
// 作用：读取 标准输入中的 字符序列，并 得到 其对应的 二进制表示
public class BinaryDump {

    // Do not instantiate.
    private BinaryDump() {
    }

    /**
     * 从 标准输入 读取 字节（字符）序列，并以 二进制的形式 把它们 写入到 标准输出中（每行k个比特）
     * k 由 命令行参数 指定（默认值是16）
     * 同时写入 比特数量
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 指定 每行所要展示的 比特数量 - 默认值16
        int bitAmountPerLine = 16;
        if (args.length == 1) { // 如果 只存在有 1个命令行参数，说明 用户指定了 每行展示的比特数量，
            // 则：把 参数传入的值 作为“每行的比特数量”
            bitAmountPerLine = Integer.parseInt(args[0]);
        }

        int bitCounter;
        // 只要 标准输入 不为空...
        for (bitCounter = 0; !BinaryStdIn.isEmpty(); bitCounter++) {
            // 如果 每行所要展示的 比特数量为0（用户指定），说明 用户不想展示任何比特，则：
            if (bitAmountPerLine == 0) {
                // 只是读取 当前比特值，而不打印任何内容
                BinaryStdIn.readBoolean();
                continue;
            } else if (isMeetTheEndOfCurrentLine(bitCounter, bitAmountPerLine)) // 如果到了需要换行的位置，则：
                // 打印换行
                StdOut.println();

            // 读取 标准输入中的 当前比特值
            // 规则：如果读取结果为true，则打印1；如果为false，则打印0
            if (BinaryStdIn.readBoolean())
                StdOut.print(1); // 打印1
            else
                StdOut.print(0); // 打印0
        }

        // 如果 用户所指定的 “每行的比特数量”不为0，说明??，则：
        if (bitAmountPerLine != 0)
            // 手动地 在最后一行后 添加一个换行符
            StdOut.println();

        // 打印出 具体的比特数量
        StdOut.println(bitCounter + " bits");
    }

    /**
     * 作用：判断 比特计数器 是不是到了 该换行的位置
     * @param bitCounter    比特计数器
     * @param bitAmountPerLine  每行想要展示的比特数量
     * @return 一个boolean值，用于表示 是否到达了 需要换行的位置
     */
    private static boolean isMeetTheEndOfCurrentLine(int bitCounter, int bitAmountPerLine) {
        return bitCounter != 0 && bitCounter % bitAmountPerLine == 0;
    }
}
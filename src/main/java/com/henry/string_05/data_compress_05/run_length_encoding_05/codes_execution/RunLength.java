package com.henry.string_05.data_compress_05.run_length_encoding_05.codes_execution;

/******************************************************************************
 *  Compilation:  javac RunLength.java
 *  Execution:    java RunLength - < input.txt   (compress)
 *  Execution:    java RunLength + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   https://algs4.cs.princeton.edu/55compression/4runs.bin
 *                https://algs4.cs.princeton.edu/55compression/q32x48.bin
 *                https://algs4.cs.princeton.edu/55compression/q64x96.bin
 *
 *  Compress or expand binary input from standard input using
 *  run-length encoding.
 *
 *  % java BinaryDump 40 < 4runs.bin
 *  0000000000000001111111000000011111111111
 *  40 bits
 *
 *  This has runs of 15 0s, 7 1s, 7 0s, and 11 1s.
 *
 *  % java RunLength - < 4runs.bin | java HexDump
 *  0f 07 07 0b
 *  4 bytes
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * The {@code RunLength} class provides static methods for compressing
 * and expanding a binary input using run-length coding with 8-bit
 * run lengths.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */

// 验证：对于比特流中最常见的冗余形式(一连串重复的比特)，可以使用游程的长度(runLengthSize) 来 对游程（一串连续相同的比特字符）进行编码
public class RunLength {
    private static final int runLengthMaxSize = 256;
    private static final int bitsAmountForRecordingRunLength = 8;

    // Do not instantiate.
    private RunLength() {
    }

    /**
     * Reads a sequence of bits from standard input (that are encoded
     * using run-length encoding with 8-bit run lengths); decodes them;
     * and writes the results to standard output.
     * 从标准输入中读取比特序列（使用 8位长度的游程进行编码）；解码它们；
     * 然后把结果写入到标准输出中
     * 🐖 相邻的两个游程中的比特数字 必然是相反的
     */
    public static void expand() {
        boolean defaultBitDigit = false;
        while (!BinaryStdIn.isEmpty()) {
            // 从标准输入中读取8个比特，并返回 用8比特表示的一个int值 - 用于表示 当前游程的长度（比特数量）
            int currentRunLengthSize = BinaryStdIn.readInt(bitsAmountForRecordingRunLength);
            // 打印 当前游程中的比特数字
            for (int currentBitSpot = 0; currentBitSpot < currentRunLengthSize; currentBitSpot++)
                BinaryStdOut.write(defaultBitDigit);
            
            // 打印完当前游程后，翻转“所打印的比特值” - 为打印下一个游程做准备
            defaultBitDigit = flipBitDigitForNextRunLength(defaultBitDigit);
        }
        BinaryStdOut.close();
    }

    private static boolean flipBitDigitForNextRunLength(boolean defaultBitDigit) {
        defaultBitDigit = !defaultBitDigit;
        return defaultBitDigit;
    }

    /**
     * Reads a sequence of bits from standard input; compresses
     * them using run-length coding with 8-bit run lengths; and writes the
     * results to standard output.
     * 从标准输入中读取比特序列；
     * 使用8位的游程来对它们进行压缩；
     * 把压缩结果写入到标准输出中
     */
    public static void compress() {
        // 用于表示 当前游程的长度
        char currentRunLengthSize = 0;
        // 设置变量，用于表示 “前一个比特数值”   默认值为false/0
        boolean previousBitDigit = false;
        while (!BinaryStdIn.isEmpty()) {
            // 从标准输入中读取当前比特数值
            boolean currentBitDigit = BinaryStdIn.readBoolean();
            // 如果当前游程已经结束，则... 手段：当前比特值 与 前一个比特值不相等
            if (currentRunLengthComeToEnd(previousBitDigit, currentBitDigit)) {
                // 打印当前游程
                printOutCurrentRunLength(currentRunLengthSize);
                // 重置下一个游程的计数器  手段：把 游程的长度 重置为1；
                currentRunLengthSize = resetSizeTo1ForNextRunLength();
                // 设置下一个游程的比特数字 手段：翻转“前一个比特”变量的值
                previousBitDigit = flipBitDigitForNextRunLength(previousBitDigit);
            } else { // 如果当前游程还没有结束，则...
                // 先处理特殊的边界情况：游程的比特长度 已经达到 所支持的最大的比特长度，则...
                if (reachToLimitedMax(currentRunLengthSize)) {
                    // 先向标准输出中写入 当前游程的长度
                    BinaryStdOut.write(currentRunLengthSize, bitsAmountForRecordingRunLength);
                    // 再向标准输入中写入 0 - 它不是用于表示游程的长度的，而是标识 游程长度超限，需要把后继比特计入一个新游程
                    currentRunLengthSize = printSize0RunLengthAndResetSizeAs0();

                }
                // 扩展当前游程 - 手段：把游程长度+1
                currentRunLengthSize++;
            }
        }

        // 🐖 对于最后一个游程，不会有新的比特输入来标识它的结束。所以我们需要直接把游程给打印出来
        BinaryStdOut.write(currentRunLengthSize, bitsAmountForRecordingRunLength);

        BinaryStdOut.close();
    }

    private static char printSize0RunLengthAndResetSizeAs0() {
        // 把游程长度的变量 重置为0
        char currentRunLengthSize = 0;
        // 再向标准输入中写入 当前游程的长度0 - 作用：使用“长度为0的游程” 来 打断长度超过256的游程，使之分解成为 多个可处理的小游程
        BinaryStdOut.write(currentRunLengthSize, bitsAmountForRecordingRunLength);

        return currentRunLengthSize;
    }

    private static char resetSizeTo1ForNextRunLength() {
        return 1;
    }

    private static void printOutCurrentRunLength(char currentRunLengthSize) { // 形式参数的上下文是方法本身，所以参数名应该具有通用性
        BinaryStdOut.write(currentRunLengthSize, bitsAmountForRecordingRunLength);
    }


    private static boolean reachToLimitedMax(char currentRunningLength) {
        return currentRunningLength == runLengthMaxSize - 1;
    }

    private static boolean currentRunLengthComeToEnd(boolean previousBitValue, boolean currentBitValue) {
        return currentBitValue != previousBitValue;
    }


    /**
     * Sample client that calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     * 如果命令行参数是-，则调用compress(); 如果命令行参数是+，则调用expand()
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
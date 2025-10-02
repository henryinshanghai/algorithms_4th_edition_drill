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

// 验证：对于 比特流中 最常见的冗余形式 游程(一连串 重复的比特)，可以使用 游程的长度(runLengthSize) 来 对 其 进行编码
public class RunLength {
    private static final int runLengthMaxSize = 256;
    private static final int bitsAmountForRecordingRunLength = 8;

    // Do not instantiate.
    private RunLength() {
    }

    /**
     * 从 标准输入 中 读取 比特序列（使用 8位长度的游程进行编码）；解码它们；
     * 然后 把 结果 写入到 标准输出中
     * 🐖 相邻的 两个游程中的 比特数字 必然是相反的
     */
    public static void expand() {
        boolean currentRunLengthBitDigit = false;
        while (!BinaryStdIn.isEmpty()) {
            // 从 标准输入 中 读取8个比特，并 返回 用8比特表示的 一个int值 - 用于表示 当前游程的长度（比特数量）
            int currentRunLengthSize = BinaryStdIn.readInt(bitsAmountForRecordingRunLength);
            // 打印 当前游程中的比特数字
            for (int currentBitSpot = 0; currentBitSpot < currentRunLengthSize; currentBitSpot++)
                BinaryStdOut.write(currentRunLengthBitDigit);

            // 打印完 当前游程 后，翻转“所打印的比特值” - 为 打印下一个游程 做准备
            currentRunLengthBitDigit = setBitDigitForNextRunLength(currentRunLengthBitDigit);
        }
        BinaryStdOut.close();
    }

    private static boolean setBitDigitForNextRunLength(boolean defaultBitDigit) {
        defaultBitDigit = !defaultBitDigit;
        return defaultBitDigit;
    }

    /**
     * 从 标准输入 中 读取 比特序列；
     * 使用 8位的游程 来 对它们进行压缩；
     * 把 压缩结果 写入到 标准输出中
     */
    public static void compress() {
        // 用于表示 当前游程的长度
        char currentRunLengthSize = 0;
        // 设置变量，用于表示 “前一个比特数值”   默认值为false/0
        boolean previousBitDigit = false;
        while (!BinaryStdIn.isEmpty()) {
            // 从 标准输入 中 读取 当前比特数值
            boolean currentBitDigit = BinaryStdIn.readBoolean();
            // 如果 当前游程 已经结束，说明??，则：
            if (currentRunLengthComeToEnd(previousBitDigit, currentBitDigit)) {
                // 打印 当前游程
                printOutCurrentRunLength(currentRunLengthSize);
                // 重置 下一个游程的 计数器  手段：把 游程的长度 重置为1；
                currentRunLengthSize = resetSizeTo1ForNextRunLength();
                // 设置 下一个游程的 比特数字 手段：翻转“前一个比特”变量的值
                previousBitDigit = setBitDigitForNextRunLength(previousBitDigit);
            } else { // 如果 当前游程 还没有结束，则...
                // 先处理 特殊的边界情况：游程的比特长度 已经达到 所支持的 最大的 比特长度，则...
                if (reachToLimitedMax(currentRunLengthSize)) {
                    // 先 向 标准输出 中 写入 当前游程的长度
                    BinaryStdOut.write(currentRunLengthSize, bitsAmountForRecordingRunLength);
                    // 再 向 标准输入中 写入 0 - 作用：它 不是用于 表示游程的长度的，而是用于 标识 游程长度 超限，需要 把 后继比特 计入 一个新游程
                    currentRunLengthSize = printSize0RunLengthAndResetSizeAs0();

                }
                // 扩展 当前游程 - 手段：把 游程长度 +1
                currentRunLengthSize++;
            }
        }

        // 🐖 对于 最后一个游程，不会有 新的比特输入 来 标识它的结束。所以 我们需要 直接 把 游程 给打印出来
        BinaryStdOut.write(currentRunLengthSize, bitsAmountForRecordingRunLength);

        BinaryStdOut.close();
    }

    private static char printSize0RunLengthAndResetSizeAs0() {
        // 把 游程长度的变量 重置 为 0
        char currentRunLengthSize = 0;
        // 再 向 标准输入中 写入 当前游程的长度0 - 作用：使用 “长度为0的游程” 来 打断 长度超过256的游程，使之 分解成为 多个 可处理的小游程
        BinaryStdOut.write(currentRunLengthSize, bitsAmountForRecordingRunLength);

        return currentRunLengthSize;
    }

    private static char resetSizeTo1ForNextRunLength() {
        return 1;
    }

    /**
     * 打印 当前游程中的所有比特
     * 🐖 形式参数的上下文 是 方法本身，所以 参数名 应该具有 通用性
     *
     * @param currentRunLengthSize 当前游程的长度
     */
    private static void printOutCurrentRunLength(char currentRunLengthSize) {
        BinaryStdOut.write(currentRunLengthSize, bitsAmountForRecordingRunLength);
    }


    private static boolean reachToLimitedMax(char currentRunningLength) {
        return currentRunningLength == runLengthMaxSize - 1;
    }

    /**
     * 判断 当前游程 是否结束
     *
     * @param previousBitValue 前一个比特 的数值
     * @param currentBitValue  当前比特 的数值
     * @return 当前游程 是否结束
     */
    private static boolean currentRunLengthComeToEnd(boolean previousBitValue, boolean currentBitValue) {
        // 手段：如果 当前比特 与 前一个比特 的数值 不相同，说明 当前游程 已经结束
        return currentBitValue != previousBitValue;
    }


    /**
     * 如果命令行参数是-，则调用compress(); 如果命令行参数是+，则调用expand()
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
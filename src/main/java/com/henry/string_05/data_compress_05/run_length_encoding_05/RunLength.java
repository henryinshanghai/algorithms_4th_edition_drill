package com.henry.string_05.data_compress_05.run_length_encoding_05;

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

// 使用8位的游程编码 来 压缩和扩展二进制输入
public class RunLength {
    private static final int runningMaxLength = 256;
    private static final int bitsAmountForRecordingRunningLength = 8;

    // Do not instantiate.
    private RunLength() {
    }

    /**
     * Reads a sequence of bits from standard input (that are encoded
     * using run-length encoding with 8-bit run lengths); decodes them;
     * and writes the results to standard output.
     * 从标准输入中读取比特序列（使用 8位长度的游程进行编码）；解码它们；
     * 然后把结果写入到标准输出中
     */
    public static void expand() {
        boolean defaultBitValue = false;
        while (!BinaryStdIn.isEmpty()) {
            int runningLength = BinaryStdIn.readInt(bitsAmountForRecordingRunningLength);
            for (int currentBit = 0; currentBit < runningLength; currentBit++)
                BinaryStdOut.write(defaultBitValue);
            // 打印完当前游程后，转换“所打印的比特值”
            defaultBitValue = !defaultBitValue;
        }
        BinaryStdOut.close();
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
        char currentRunningLength = 0;
        boolean previousBitValue = false;
        while (!BinaryStdIn.isEmpty()) {
            boolean currentBitValue = BinaryStdIn.readBoolean();
            if (currentBitValue != previousBitValue) {
                BinaryStdOut.write(currentRunningLength, bitsAmountForRecordingRunningLength);
                currentRunningLength = 1;
                previousBitValue = !previousBitValue;
            } else {
                if (currentRunningLength == runningMaxLength - 1) {
                    BinaryStdOut.write(currentRunningLength, bitsAmountForRecordingRunningLength);
                    currentRunningLength = 0;
                    BinaryStdOut.write(currentRunningLength, bitsAmountForRecordingRunningLength);
                }
                currentRunningLength++;
            }
        }
        BinaryStdOut.write(currentRunningLength, bitsAmountForRecordingRunningLength);
        BinaryStdOut.close();
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
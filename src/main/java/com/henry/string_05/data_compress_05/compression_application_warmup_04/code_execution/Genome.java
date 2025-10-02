package com.henry.string_05.data_compress_05.compression_application_warmup_04.code_execution;

/******************************************************************************
 *  Compilation:  javac Genome.java
 *  Execution:    java Genome - < input.txt   (compress)
 *  Execution:    java Genome + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   https://algs4.cs.princeton.edu/55compression/genomeTiny.txt
 *
 *  Compress or expand a genomic sequence using a 2-bit code.
 *
 *  % more genomeTiny.txt
 *  ATAGATGCATAGCGCATAGCTAGATGTGCTAGC
 *
 *  % java Genome - < genomeTiny.txt | java Genome +
 *  ATAGATGCATAGCGCATAGCTAGATGTGCTAGC // todo 输出结果跟这个不一样/(ㄒoㄒ)/~~
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * The {@code Genome} class provides static methods for compressing
 * and expanding a genomic sequence using a 2-bit code.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 验证：可以使用“两位编码”（碱基字符在碱基字母表{A, C, T, G}中的位置） 来 对所有“碱基选项”进行编码，进而 压缩或扩展 一个基因组序列
public class Genome {

    // Do not instantiate.
    private Genome() {
    }

    /**
     * 从标准输入中 读取一个 由8位扩展ASCII字符所构成的序列（字母表/字符选项 {A, C, T, G}）
     * 以“每个字符两位比特”的方式 来 压缩它们
     * 并 把 结果 写出到 标准输出中
     */
    public static void compress() {
        Alphabet baseOptions = Alphabet.DNA; // base 碱基
        // 从 标准输入 中 读取 字节数据，并 以 字符串 返回
        String baseSequence = BinaryStdIn.readString();
        int baseAmount = baseSequence.length();

        // #1 向 标准输出 中 输出 碱基序列中 碱基的数量 - 作用:用于 在解码时，提供解码 所需要的 碱基数量的信息
        BinaryStdOut.write(baseAmount);

        // #2 向 标准输出 中，写入 各个字符的 两位编码
        for (int currentBaseSpot = 0; currentBaseSpot < baseAmount; currentBaseSpot++) {
            // 获取到 当前的碱基字符
            char currentBaseChar = baseSequence.charAt(currentBaseSpot);
            // 对 当前碱基字符 进行编码  手段：使用 该碱基字符 在“碱基字母表”中的位置 来 对 碱基 进行编码
            int currentBaseEncodedResult = baseOptions.toIndex(currentBaseChar);
            // 向 标准输出 中 输出 当前碱基字符 编码的结果（以 两个比特 表示的 int值）
            BinaryStdOut.write(currentBaseEncodedResult, 2);
        }

        // #3 刷新并关闭 标准输出流
        BinaryStdOut.close();
    }

    /**
     * 从 标准输入 中读取一个 二进制序列；
     * 把 每两个bit都 转化成为 一个8bit的 扩展ASCII字符（基于字母表 A,C,T,G）
     * 并且 把 转换结果 写入到 标准输出 中
     * 🐖 解码时，会对 编码结果中的各个部分 依次 解码，并 预期 各个部分 有特定的含义。因此：
     * ① 解码次序 需要 与 编码时的次序 相同；
     * ② 解码的规则 需要 与 编码时的规则 相同；
     * ③ 解码的正确性 依赖于 编码的正确性（各个部分的次序&正确性）
     */
    public static void expand() {
        Alphabet baseOptions = Alphabet.DNA;
        // #1 先从 标准输入 中 读取一个int值 - 读取到的结果 预期是 这段比特序列 解码结果中的 碱基数量
        int expectedBaseAmount = BinaryStdIn.readInt();

        // #2 每次 读取两个比特，并把 这两个比特 解码成为 碱基字符
        for (int currentBaseSpot = 0; currentBaseSpot < expectedBaseAmount; currentBaseSpot++) {
            // 读取到 当前的碱基字符编码结果  手段：从 标准输入 中 读取两个比特
            char currentBaseEncodedResult = BinaryStdIn.readChar(2);
            // 对 该编码结果 进行解码       手段：获取到 该编码结果 在”碱基字母表“中 对应的碱基字符
            char currentBaseChar = baseOptions.toChar(currentBaseEncodedResult);
            // 向 标准输出 中 输出“当前碱基字符”(以 8位比特 所表示的字符)
            BinaryStdOut.write(currentBaseChar, 8);
        }

        // #3 刷新并关闭 标准输出
        BinaryStdOut.close();
    }


    /**
     * 如果 命令行参数 是 -，则：调用 compress()方法 进行压缩/编码；
     * 如果 命令行参数 是 +，则：调用 expand()方法 进行展开/解码；
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            compress();
        } else if (args[0].equals("+")) {
            expand();
        } else {
            throw new IllegalArgumentException("Illegal command line argument");
        }
    }

}
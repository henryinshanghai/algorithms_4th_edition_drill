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
     * Reads a sequence of 8-bit extended ASCII characters over the alphabet
     * { A, C, T, G } from standard input; compresses them using two bits per
     * character; and writes the results to standard output.
     * <p>
     * 从标准输入中 读取一个 由8位扩展ASCII字符所构成的序列（字符选项 A, C, T, G）
     * 以“每个字符两位比特”的方式 来 压缩它们
     * 并把结果写出到 标准输出中
     */
    public static void compress() {
        Alphabet baseOptionAlphabet = Alphabet.DNA;
        String inputBaseSequence = BinaryStdIn.readString();
        int baseAmountOfInput = inputBaseSequence.length();
        // 向标准输出中输出 碱基序列中碱基的数量 - 作用:???
        BinaryStdOut.write(baseAmountOfInput);

        // Write two-bit code for char.
        for (int currentBaseSpot = 0; currentBaseSpot < baseAmountOfInput; currentBaseSpot++) {
            // 获取到当前的碱基字符
            char currentBaseChar = inputBaseSequence.charAt(currentBaseSpot);
            // 对当前碱基字符进行编码  手段：使用碱基字符在“碱基字母表”中的位置 来 对碱基进行编码
            int basesIndexInAlphabet = baseOptionAlphabet.toIndex(currentBaseChar);
            // 向标准输出中输出 当前碱基字符编码后的结果（以两个比特表示的int值）
            BinaryStdOut.write(basesIndexInAlphabet, 2);
        }

        BinaryStdOut.close();
    }

    /**
     * Reads a binary sequence from standard input; converts each two bits
     * to an 8-bit extended ASCII character over the alphabet { A, C, T, G };
     * and writes the results to standard output.
     * 从标准输入中读取一个二进制序列；
     * 把每两个bit都转化成为一个8bit的扩展ASCII字符（基于字母表 A,C,T,G）
     * 并且把转换结果 写入到标准输出中去
     */
    public static void expand() {
        Alphabet baseOptionAlphabet = Alphabet.DNA;
        // 从标准输入中读取一个int值 - 读取到的结果预期是 这段比特序列解码结果中的碱基数量
        int expectedBaseAmount = BinaryStdIn.readInt();
        // Read two bits; write char.
        for (int currentBaseSpot = 0; currentBaseSpot < expectedBaseAmount; currentBaseSpot++) {
            // 从标准输入中读取两个比特 来 得到“预期碱基字符的编码结果” aka 碱基字符在碱基字母表中的位置
            char currentBasesIndexInAlphabet = BinaryStdIn.readChar(2);
            // 在碱基字母表的对应位置上 获取到 当前碱基字符
            char currentBase = baseOptionAlphabet.toChar(currentBasesIndexInAlphabet);
            // 向标准输出中 输出“当前碱基字符”(以8位比特表示的字符)
            BinaryStdOut.write(currentBase, 8);
        }
        BinaryStdOut.close();
    }


    /**
     * Sample client that calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
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
 *  ATAGATGCATAGCGCATAGCTAGATGTGCTAGC
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Alphabet;
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
// 验证：可以使用“两位编码” 来 对所有“碱基选项”进行编码，进而 压缩或扩展 一个基因组序列
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
        Alphabet baseOptions = Alphabet.DNA;
        String inputBaseSequence = BinaryStdIn.readString();
        int baseAmount = inputBaseSequence.length();
        BinaryStdOut.write(baseAmount);

        // Write two-bit code for char.
        for (int currentBaseSpot = 0; currentBaseSpot < baseAmount; currentBaseSpot++) {
            char currentBaseChar = inputBaseSequence.charAt(currentBaseSpot);
            int basesIndex = baseOptions.toIndex(currentBaseChar);

            BinaryStdOut.write(basesIndex, 2);
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
        Alphabet baseOptions = Alphabet.DNA;
        int anIntOfInput = BinaryStdIn.readInt();
        // Read two bits; write char.
        for (int currentCursor = 0; currentCursor < anIntOfInput; currentCursor++) {
            char currentIndex = BinaryStdIn.readChar(2);
            char currentCharacter = baseOptions.toChar(currentIndex);

            BinaryStdOut.write(currentCharacter, 8);
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
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
// 使用两位编码 来 压缩或扩展 一个基因组序列
public class Genome {

    // Do not instantiate.
    private Genome() {
    }

    /**
     * Reads a sequence of 8-bit extended ASCII characters over the alphabet
     * { A, C, T, G } from standard input; compresses them using two bits per
     * character; and writes the results to standard output.
     *
     * 从标准输入中 读取一个 由8位扩展ASCII字符所构成的序列（字符选项 A, C, T, G）
     * 以“每个字符两位比特”的方式 来 压缩它们
     * 并把结果写出到 标准输出中
     */
    public static void compress() {
        Alphabet DNA = Alphabet.DNA;
        String s = BinaryStdIn.readString();
        int n = s.length();
        BinaryStdOut.write(n);

        // Write two-bit code for char.
        for (int i = 0; i < n; i++) {
            int d = DNA.toIndex(s.charAt(i));
            BinaryStdOut.write(d, 2);
        }
        BinaryStdOut.close();
    }

    /**
     * Reads a binary sequence from standard input; converts each two bits
     * to an 8-bit extended ASCII character over the alphabet { A, C, T, G };
     * and writes the results to standard output.
     */
    public static void expand() {
        Alphabet DNA = Alphabet.DNA;
        int n = BinaryStdIn.readInt();
        // Read two bits; write char.
        for (int i = 0; i < n; i++) {
            char c = BinaryStdIn.readChar(2);
            BinaryStdOut.write(DNA.toChar(c), 8);
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
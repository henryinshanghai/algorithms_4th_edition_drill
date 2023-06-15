package com.henry.symbol_table_chapter_03.symbol_table_01.usage.performanceDemo;

import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


/******************************************************************************
 *  Compilation:  javac CountWordFrequencyViaSymbolTable.java
 *  Execution:    java CountWordFrequencyViaSymbolTable L < input.txt
 *  Dependencies: LinkedNodeSymbolTable.java StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/31elementary/tinyTale.txt
 *                https://algs4.cs.princeton.edu/31elementary/tale.txt
 *                https://algs4.cs.princeton.edu/31elementary/leipzig100K.txt
 *                https://algs4.cs.princeton.edu/31elementary/leipzig300K.txt
 *                https://algs4.cs.princeton.edu/31elementary/leipzig1M.txt
 *
 *  Read in a list of words from standard input and print out
 *  the most frequently occurring word that has length greater than
 *  a given threshold.
 *
 *  % java CountWordFrequencyViaSymbolTable 1 < tinyTale.txt
 *  it 10
 *
 *  % java CountWordFrequencyViaSymbolTable 8 < tale.txt
 *  business 122
 *
 *  % java CountWordFrequencyViaSymbolTable 10 < leipzig1M.txt
 *  government 24763
 *
 *
 ******************************************************************************/

/**
 * The {@code CountWordFrequencyViaSymbolTable} class provides a client for
 * reading in a sequence of words and printing a word (exceeding
 * a given length) that occurs most frequently. It is useful as
 * a test client for various symbol table implementations.
 * <p>
 * For additional documentation, see <a href="https://algs4.cs.princeton.edu/31elementary">Section 3.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class CountWordFrequencyViaSymbolTable {

    // Do not instantiate.
    private CountWordFrequencyViaSymbolTable() {
    }

    /**
     * Reads in a command-line integer and sequence of words from
     * standard input and prints out a word (whose length exceeds
     * the threshold) that occurs most frequently to standard output.
     * It also prints out the number of words whose length exceeds
     * the threshold and the number of distinct such words.
     * <p>
     * 从标准输入中读取 一个命令行整数 与 一个单词序列，并把 出现频率最高的单词（长度超过最低要求） 打印到标准输出中
     * 同时也打印出 长度超过最低要求的单词的总数量 & 一共有多少组满足条件的单词
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int wordGroupAmount = 0, // 符号表中 不同单词的总数量（相同的单词视为同一组）
                wordAmount = 0; //  符号表中单词的总数量
        int minLengthThreshold = Integer.parseInt(args[0]);

        // 选用某个符号表的实现 来 创建一个符号表对象 - 进而测试实现的性能
        ST<String, Integer> wordToItsFrequency = new ST<String, Integer>();

        // compute frequency counts
        while (!StdIn.isEmpty()) {
            // 读取文件中的字符串
            String currentWord = StdIn.readString();
            if (currentWord.length() < minLengthThreshold)
                continue; // 只统计满足条件的单词（足够长的单词）

            wordAmount++;

            // 如果 currentWord 在符号表中已经存在，则：更新其出现次数（+1）。如果还没有存在，则：向符号表中添加此单词，单词出现次数为1
            if (wordToItsFrequency.contains(currentWord)) {
                wordToItsFrequency.put(currentWord, wordToItsFrequency.get(currentWord) + 1);
            } else {
                wordToItsFrequency.put(currentWord, 1);
                wordGroupAmount++;
            }
        }

        // 从符号表中，找到 出现频率最高的单词 👇
        // 手段：预设一个 最高频的单词，然后遍历符号表，不断更新它
        String mostFrequentWord = "";
        wordToItsFrequency.put(mostFrequentWord, 0); 
        
        for (String currentWord : wordToItsFrequency.keys()) {
            if (wordToItsFrequency.get(currentWord) > wordToItsFrequency.get(mostFrequentWord))
                mostFrequentWord = currentWord;
        }

        StdOut.println(mostFrequentWord + " " + wordToItsFrequency.get(mostFrequentWord)); // 出现最多的单词 - 出现的次数
        StdOut.println("wordGroupAmount = " + wordGroupAmount); // 不重复的单词数
        StdOut.println("wordAmount    = " + wordAmount); // 总单词数
    }
}
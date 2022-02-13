package com.henry.symbol_table_chapter_03.symbol_table_01.performanceDemo;

import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


/******************************************************************************
 *  Compilation:  javac FrequencyCounterFromWebsite.java
 *  Execution:    java FrequencyCounterFromWebsite L < input.txt
 *  Dependencies: ST.java StdIn.java StdOut.java
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
 *  % java FrequencyCounterFromWebsite 1 < tinyTale.txt
 *  it 10
 *
 *  % java FrequencyCounterFromWebsite 8 < tale.txt
 *  business 122
 *
 *  % java FrequencyCounterFromWebsite 10 < leipzig1M.txt
 *  government 24763
 *
 *
 ******************************************************************************/

/**
 *  The {@code FrequencyCounterFromWebsite} class provides a client for 
 *  reading in a sequence of words and printing a word (exceeding
 *  a given length) that occurs most frequently. It is useful as
 *  a test client for various symbol table implementations.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/31elementary">Section 3.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class FrequencyCounterFromWebsite {

    // Do not instantiate.
    private FrequencyCounterFromWebsite() { }

    /**
     * Reads in a command-line integer and sequence of words from
     * standard input and prints out a word (whose length exceeds
     * the threshold) that occurs most frequently to standard output.
     * It also prints out the number of words whose length exceeds
     * the threshold and the number of distinct such words.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int distinct = 0, words = 0; // distinct：不重复的单词数 words：单词的总数
        int minlen = Integer.parseInt(args[0]);
        ST<String, Integer> st = new ST<String, Integer>();

        // compute frequency counts
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            if (key.length() < minlen) continue; // 只统计满足条件的单词（足够长的单词）
            words++;
            if (st.contains(key)) { // 更新
                st.put(key, st.get(key) + 1); // 统计某个单词出现的次数；   手段： 使用符号表put()的更新功能；
            }
            else { // 添加
                st.put(key, 1);
                distinct++;
            }
        }

        // find a key with the highest frequency count
        // 找到出现次数最多的单词
        String max = "";
        st.put(max, 0); // 预设一个键值对  作用：找到出现次数最多的键
        for (String word : st.keys()) {
            if (st.get(word) > st.get(max))
                max = word; // 更新键
        }

        StdOut.println(max + " " + st.get(max)); // 出现最多的单词 - 出现的次数
        StdOut.println("distinct = " + distinct); // 不重复的单词数
        StdOut.println("words    = " + words); // 总单词数
    }
}
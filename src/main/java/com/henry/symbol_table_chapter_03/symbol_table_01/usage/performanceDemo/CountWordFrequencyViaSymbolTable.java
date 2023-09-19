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

// 验证：可以使用 符号表的实现 处理不同规模的问题 来 评估实现的性能；
// 🐖 这里可以switch 对于ST的实现(默认使用的是JDK提供的TreeMap)，来 测试具体实现的性能。
public class CountWordFrequencyViaSymbolTable {

    // Do not instantiate.
    private CountWordFrequencyViaSymbolTable() {
    }

    /**
     * 从标准输入中读取 一个命令行整数 与 一个单词序列，并把 出现频率最高的单词（长度超过最低要求） 打印到标准输出中
     * 同时也打印出 长度超过最低要求的单词的总数量 & 一共有多少组满足条件的单词
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int uniqueWordsAmount = 0, // 符号表中 不同单词的总数量（相同的单词视为同一组）
                totalWordAmount = 0; //  符号表中单词的总数量

        int wordsLengthThreshold = Integer.parseInt(args[0]);

        // 选用某个符号表的实现 来 创建一个符号表对象 - 进而测试该实现的性能
        ST<String, Integer> wordToItsFrequency = new ST<String, Integer>();

        // 遍历所有的单词，并建立起 有效单词 -> 单词出现频率 的符号表
        while (!StdIn.isEmpty()) {
            // 读取文件中的字符串
            String currentWord = StdIn.readString();
            // 只统计满足条件的单词（足够长的单词）
            if (currentWord.length() < wordsLengthThreshold)
                continue;

            totalWordAmount++;

            // 如果 currentWord 在符号表中已经存在...
            if (wordToItsFrequency.contains(currentWord)) {
                // 则：更新其出现次数（+1）
                wordToItsFrequency.put(currentWord, wordToItsFrequency.get(currentWord) + 1);
            } else { // 如果还没有存在...
                // 则：向符号表中添加此单词，并设置其出现次数为1
                wordToItsFrequency.put(currentWord, 1);
                uniqueWordsAmount++;
            }
        }

        /* 从符号表中，找到 出现频率最高的单词 👇 */
        // 手段：预设一个 最高频的单词，然后遍历符号表，不断更新它
        String mostFrequentWord = "";
        wordToItsFrequency.put(mostFrequentWord, 0); 
        
        for (String currentWord : wordToItsFrequency.keys()) {
            if (wordToItsFrequency.get(currentWord) > wordToItsFrequency.get(mostFrequentWord))
                mostFrequentWord = currentWord;
        }

        StdOut.println(mostFrequentWord + " " + wordToItsFrequency.get(mostFrequentWord)); // 出现最多的单词 - 出现的次数
        StdOut.println("uniqueWordsAmount = " + uniqueWordsAmount); // 不重复的单词数
        StdOut.println("totalWordAmount    = " + totalWordAmount); // 总单词数
    }
}
package com.henry.symbol_table_chapter_03.application_03.index_05;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.io.File;

// 可以使用 key -> SET的符号表 来 表示反向索引(一个键 关联多个值)
// 比如 所有出现过指定单词的文件
public class FileIndex {
    public static void main(String[] args) {
        // 建立 从word -> word出现的files 的映射（符号表）
        ST<String, SET<File>> wordToFilesST = new ST();

        // 命令行参数 是一系列的文件名
        for (String filename : args) {
            // 得到文件的文件流 - 作用：能够方便地对文件逐行读取、对文件判空...
            File file = new File(filename);
            In in = new In(file);

            while (!in.isEmpty()) {
                // 逐个读取文件中的单词字符串
                String word = in.readString();
                // 如果单词还没有被添加到集合中, 则：
                if (!wordToFilesST.contains(word)) {
                    // 为它添加一个集合
                    wordToFilesST.put(word, new SET<File>());
                }

                // 为当前单词的文件列表 添加当前文件
                SET<File> set = wordToFilesST.get(word);
                set.add(file);
            }
        }

        while (!StdIn.isEmpty()) {
            // 使用标准输入中用户的input 作为 关键字
            String passedWord = StdIn.readString();
            // 获取到 与该关键字所关联的所有文件，并打印文件名
            if (wordToFilesST.contains(passedWord)) {
                for (File file : wordToFilesST.get(passedWord)) {
                    StdOut.println(" " + file.getName());
                }
            }
        }

    }
}

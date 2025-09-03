package com.henry.symbol_table_chapter_03.application_03.index_05;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用 key -> SET的符号表 来 表示索引(一个键 会关联到 多个值)
// 命令行参数：<文件名> <键与值的分隔符>
// 手段：添加 键值对 时，先添加 空的集合 作为值，然后再 向集合中添加 所关联的具体值
public class LookupIndex {
    public static void main(String[] args) {
        // 读取文件名 来 得到对应的文件流
        In in = new In(args[0]);
        // 读取第二个参数 作为 键与值的分隔符
        String separator = args[1];
        // 创建一个 String -> Queue的符号表，用作 索引
        ST<String, Queue<String>> keyToValuesST = new ST<>();
        // 创建一个 String -> Queue的符号表，用作 反向索引
        ST<String, Queue<String>> valueToKeyST = new ST<>();

        // 从文件流中读取文件内容 - 手段：使用一个while循环来读取，直到行内容为空
        while (in.hasNextLine()) {
            // 读取文件 当前行的内容，并 把 内容 根据指定的分隔符 来 分割成单词数组
            String[] separateWordArr = in.readLine().split(separator);
            String keyOfCurrentLine = separateWordArr[0]; // 第0th位置上的单词 就是key
            // 剩余位置上的单词都是value
            for (int currentSpot = 1; currentSpot < separateWordArr.length; currentSpot++) {
                String currentValue = separateWordArr[currentSpot];
                // #1 key => valuesQueue
                if (!keyToValuesST.contains(keyOfCurrentLine)) {
                    keyToValuesST.put(keyOfCurrentLine, new Queue<>());
                }

                // value => itsKeysQueue
                if (!valueToKeyST.contains(currentValue)) {
                    valueToKeyST.put(currentValue, new Queue<>());
                }

                // #2 向queue中添加 key所绑定的当前value
                keyToValuesST.get(keyOfCurrentLine).enqueue(currentValue);
                // 向queue中添加 value所关联的key
                valueToKeyST.get(currentValue).enqueue(keyOfCurrentLine);
            }
        }

        // 接受用户 从标准输入中 传入的input
        while (!StdIn.isEmpty()) {
            String userInput = StdIn.readLine();
            // #1 作为key，从符号表中 查询出 key所对应的values
            if (keyToValuesST.contains(userInput)) {
                for (String currentValue : keyToValuesST.get(userInput)) {
                    StdOut.println(" " + currentValue);
                }
            }
            // #2 作为value，从符号表中 查询出 value所关联的keys
            if (valueToKeyST.contains(userInput)) {
                for (String currentKey : valueToKeyST.get(userInput)) {
                    StdOut.println(" " + currentKey);
                }
            }
        }
    }
}

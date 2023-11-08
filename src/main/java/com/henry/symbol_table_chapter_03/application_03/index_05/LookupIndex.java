package com.henry.symbol_table_chapter_03.application_03.index_05;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// 验证：可以使用 key -> SET的符号表 来 表示索引(一个键 关联多个值)
// 命令行参数：<文件名> <键与值的分隔符>
// 手段：添加键值对时，先添加空的集合作为值，然后再向集合中添加所关联的具体值
public class LookupIndex {
    public static void main(String[] args) {
        In in = new In(args[0]); // 读取索引文件的文件流
        String separator = args[1]; // 从命令行参数中，读取第二个参数 作为 键与值的分隔符
        ST<String, Queue<String>> keyToValueST = new ST<>();
        ST<String, Queue<String>> valueToKeyST = new ST<>();

        // 从文件流中读取字符串信息 - 手段：使用一个while循环来读取，直到行内容为空
        while (in.hasNextLine()) {
            // 从当前行中解析出：key & a bunch of values
            String[] strArr = in.readLine().split(separator);
            String key = strArr[0];
            for (int spot = 1; spot < strArr.length; spot++) {
                String value = strArr[spot];
                // #1 为key 添加一个对应的queue
                if (!keyToValueST.contains(key)) {
                    keyToValueST.put(key, new Queue<>());
                }

                if (!valueToKeyST.contains(value)) {
                    valueToKeyST.put(value, new Queue<>());
                }

                // #2 向queue中添加 key所绑定的当前value
                keyToValueST.get(key).enqueue(value);
                // 向queue中添加 value所关联的key
                valueToKeyST.get(value).enqueue(key);
            }
        }

        // 接收用户从标准输入中传入的input
        while (!StdIn.isEmpty()) {
            String userInput = StdIn.readLine();
            // #1 作为key，从符号表中查询 key所对应的values
            if (keyToValueST.contains(userInput)) {
                for (String currentValue : keyToValueST.get(userInput)) {
                    StdOut.println(" " + currentValue);
                }
            }
            // #2 作为value，从符号表中查询 value所关联的keys
            if (valueToKeyST.contains(userInput)) {
                for (String currentKey : valueToKeyST.get(userInput)) {
                    StdOut.println(" " + currentKey);
                }
            }
        }
    }
}

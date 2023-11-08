package com.henry.symbol_table_chapter_03.application_03.dictionary_04;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;

// fixed 在终端执行命令时失败（类的编译问题）
// 绕过这个问题
/*
    命令行参数 <-> CLI arguments for your application
    标准输入的input <-> redirect from a certain file
    自定义文件中字符串 需要单独添加双引号
 */
public class LookupCSV {
    public static void main(String[] args) {
        // 读取并接受 命令行中的参数
        In in = new In(args[0]); // args[0]是一个文件名。根据文件名 得到文件的流对象
        int keyField = Integer.parseInt(args[1]); // args[1]是一个数字   1
        int valueFiled = Integer.parseInt(args[2]); // args[2]也是一个数字    0
        /* 以上，建立起 从1th个字符串 -> 第0th个字符串的映射 */

        // 使用标准输入中的信息 来 构造符号表
        ST<String, String> st = new ST<>();
        while (in.hasNextLine()) { // 判断文件流是否存在下一行
            // 读取当前行的字符串
            String line = in.readLine();
            // 分割字符串，得到字符串数组
            String[] tokens = line.split(",");
            // 从特定的位置，得到需要的元素
            String key = tokens[keyField];
            String value = tokens[valueFiled];

            // 把拾取到的键值 添加到 符号表中
            st.put(key, value);
        }

        // 从构造的符号表中，读取指定key所关联的值
        while (!StdIn.isEmpty()) {
            // 读取标准输入中的input
            String passedKey = StdIn.readString();
            // 如果input已经在符号表中了，则：
            if (st.contains(passedKey)) {
                // 打印它所关联的值
                System.out.println(st.get(passedKey));
            }
        }
    }
}

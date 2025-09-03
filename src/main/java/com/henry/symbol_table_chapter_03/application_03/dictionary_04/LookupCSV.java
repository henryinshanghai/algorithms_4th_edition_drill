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
        // 读取命令行参数(文件名??)，并 以之作为参数来构造一个文件流in对象
        In in = new In(args[0]);
        int keyField = Integer.parseInt(args[1]); // args[1]是一个数字 用作键的位置序号
        int valueFiled = Integer.parseInt(args[2]); // args[2]也是一个数字 用作值的位置序号
        /* 以上，建立起 从1th个字符串 -> 第0th个字符串的映射 */

        // 使用 in对象(文件中的信息) 来 构造符号表（symbol table）
        ST<String, String> st = new ST<>();
        while (in.hasNextLine()) { // 判断文件流 是否存在下一行
            // 读取 当前行的字符串
            String line = in.readLine();
            // 分割 当前字符串，得到 各个字符串所组成的数组
            String[] tokens = line.split(",");
            // 从 特定的位置获取到需要的元素 来 作为符号表的键、值
            String key = tokens[keyField];
            String value = tokens[valueFiled];

            // 把 拾取到的键值 添加到 符号表中
            st.put(key, value);
        }

        // 从 构造的符号表 中，读取 指定key所关联的值
        while (!StdIn.isEmpty()) {
            // 读取 标准输入中的input
            String passedKey = StdIn.readString();
            // 如果 input 已经存在于 符号表中了，则：
            if (st.contains(passedKey)) {
                // 打印 它所关联的值
                System.out.println(st.get(passedKey));
            }
        }
    }
}

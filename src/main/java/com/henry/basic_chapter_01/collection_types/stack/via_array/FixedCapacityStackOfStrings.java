package com.henry.basic_chapter_01.collection_types.stack.via_array;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// 目标：实现栈数据结构
// 数据类型的预期特征：{#1 仅支持String类型; #2 栈的容量是固定的; #3 支持Stack的默认操作}
// 实现手段：使用数组作为底层的数据结构
// 执行手段：redirect input from <tobe.txt>
public class FixedCapacityStackOfStrings {

    // 实例变量 - 用于实现 抽象数据类型 的底层数据结构
    private String[] stringArray;
    private int stringAmount;

    // 构造方法
    public FixedCapacityStackOfStrings(int initCapacity) {
        stringArray = new String[initCapacity];
    }

    // 实例方法
    // 入栈操作
    private void push(String item) {
        stringArray[stringAmount++] = item;
    }

    // 出栈操作
    private String pop() {
        return stringArray[--stringAmount];
    }

    private int size() {
        return stringAmount;
    }

    private boolean isEmpty() {
        return stringAmount == 0;
    }

    // 抽象数据类型 应该 只提供用例所需要的API
    public static void main(String[] args) {
        // 创建自定义类型的对象
        FixedCapacityStackOfStrings stringsStack = new FixedCapacityStackOfStrings(100);

        // 重定向标准输入;
        /*
            作用：指定读取输入的源头（比如从文件读取）
            特征：默认情况下，会从 终端应用程序读取输入；
         */
        while (!StdIn.isEmpty()) {
            // 从文件中读取字符串    手段：readString()
            String item = StdIn.readString();

            // 如果当前字符不是-，则：把当前字符入队
            if (!item.equals("-")) {
                stringsStack.push(item);
            } else if (!stringsStack.isEmpty()) { // 如果是的，则：从队列中出队 并打印元素
                StdOut.print(stringsStack.pop() + " ");
            }
        }

        StdOut.println("(" + stringsStack.size() + " left on stack)");
    }
}

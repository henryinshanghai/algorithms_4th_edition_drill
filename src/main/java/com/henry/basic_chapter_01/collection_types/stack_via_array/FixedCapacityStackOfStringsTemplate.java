package com.henry.basic_chapter_01.collection_types.stack_via_array;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// 实现 固定容量、只能处理String元素的栈
// 下压栈的特征：LIFO
// 实现此数据结构 所使用的底层数据结构：数组
public class FixedCapacityStackOfStringsTemplate {

    // 抽象数据类型 应该 只提供用例所需要的API
    public static void main(String[] args) {
        FixedCapacityStackOfStringsTemplate stringsStack = new FixedCapacityStackOfStringsTemplate(100);

        // 重定向标准输入;
        /*
            作用：指定读取输入的源头（比如从文件读取）
            特征：默认情况下，会从 终端应用程序读取输入；
         */
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();

            if (!item.equals("-")) {
                stringsStack.push(item);
            } else if (!stringsStack.isEmpty()) {
                StdOut.print(stringsStack.pop() + " ");
            }
        }

        StdOut.println("(" + stringsStack.size() + " left on stack)");
    }

    // 实例变量 - 用于实现 抽象数据类型 的底层数据结构
    private String[] stringArray;
    private int stringAmount;

    // 构造方法
    public FixedCapacityStackOfStringsTemplate(int initCapacity) {
        stringArray = new String[initCapacity];
    }

    // 实例方法
    private int size() {
        return stringAmount;
    }

    private String pop() {
        return stringArray[--stringAmount];
    }

    private boolean isEmpty() {
        return stringAmount == 0;
    }

    private void push(String item) {
        stringArray[stringAmount++] = item;
    }
}

package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_use_demo_01;

/******************************************************************************
 *  Compilation:  javac TopM.java
 *  Execution:    java TopM m < input.txt
 *  Dependencies: MinPQ.java Transaction.java StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/24pq/tinyBatch.txt
 *
 *  Given an integer m from the command line and an input stream where
 *  each line contains a String and a long value, this MinPQ client
 *  prints the m lines whose numbers are the highest.
 *
 *  % java TopM 5 < tinyBatch.txt
 *  Thompson    2/27/2000  4747.08
 *  vonNeumann  2/12/1994  4732.35
 *  vonNeumann  1/11/1999  4409.74
 *  Hoare       8/18/1992  4381.21
 *  vonNeumann  3/26/2002  4121.85
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.*;

/**
 *  The {@code TopM} class provides a client that reads a sequence of
 *  transactions from standard input and prints the <em>m</em> largest ones
 *  to standard output. This implementation uses a {@link MinPQ} of size
 *  at most <em>m</em> + 1 to identify the <em>M</em> largest transactions
 *  and a {@link Stack} to output them in the proper order.
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a>
 *  of <i>Algoriths, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
// 验证：使用优先级队列(核心API-#1 向队列中添加元素; #2 从队列中删除最大元素)，能够找出 一个元素集合中，最大的M个元素
public class TopMFromWebsite {

    // This class should not be instantiated.
    private TopMFromWebsite() { }

    /**
     *  Reads a sequence of transactions from standard input; takes a
     *  command-line integer m; prints to standard output the m largest
     *  transactions in descending order.
     * 从标准输入中读取一系列的交易；
     * 获取到一个命令行参数 整数M；
     * 以降序 把交易中最大的M个交易 打印到标准输出中
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // 从命令行参数中 读取M的值
        int M = Integer.parseInt(args[0]);
        // 创建出一个 MinPQ数据结构的实例
        // 🐖 队列中的元素需要是 “可比较的”，这样队列元素才能具有“优先级”
        // 手段：这里Transaction类的compareTo()方法 提供了 元素间比较大小的依据
        MinPQ<Transaction> collectionForBiggestMItems = new MinPQ<Transaction>(M+1); // this is the key point~

        while (StdIn.hasNextLine()) {
            // 读取标准输入的当前行的数据
            String transactionInLine = StdIn.readLine();
            // 从读取的数据中创建出 transaction对象
            Transaction transaction = new Transaction(transactionInLine);
            // 把它添加到 最大元素集合中 - API#1
            collectionForBiggestMItems.insert(transaction);

            // 如果集合中已经添加了超过“M”个元素，则：把其中最小的元素移除(API#2) 来 确保队列中只有M个元素
            if (collectionForBiggestMItems.size() > M)
                collectionForBiggestMItems.delMin();
        }
        // 👆 循环结束后，最大的M个元素就被存放在 collectionForBiggestMItems 中了

        // 从 collectionForBiggestMItems 中按序取出元素打印
        // step#1 先使用一个栈结构 来 从队列中获取元素&存储元素
        Stack<Transaction> transactionStack = new Stack<Transaction>();
        for (Transaction transaction : collectionForBiggestMItems) // collectionForBiggestMItems是 “支持迭代操作的集合”
            // 🐖 数据类型中需要实现自己的迭代器 - 它会决定使用者进行迭代操作时的具体用法
            transactionStack.push(transaction);

        // step#2 遍历栈结构，打印栈中的元素
        for (Transaction transaction : transactionStack)
            StdOut.println(transaction);
    }
}
/*
启示：
    为了完成任务，我们需要的是一个能够提供特定操作的集合；
    特定操作：
        1 向其中插入元素；
        2 删除集合中的最小值；

    程序获取外界输入的方式：
        1 命令行参数； - 手段： args[0]
        2 标准输入流：StdIn.Xxx()
 */
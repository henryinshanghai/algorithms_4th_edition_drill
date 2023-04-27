package com.henry.sort_chapter_02.priority_queue_04.pq_use_demo_01; /******************************************************************************
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
public class TopMFromWebsite {

    // This class should not be instantiated.
    private TopMFromWebsite() { }

    /**
     *  Reads a sequence of transactions from standard input; takes a
     *  command-line integer m; prints to standard output the m largest
     *  transactions in descending order.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // 从命令行参数中 读取M的值
        int biggestMth = Integer.parseInt(args[0]);
        // 创建出一个 MinPQ数据结构的实例
        MinPQ<Transaction> collectionForBiggestMItems = new MinPQ<Transaction>(biggestMth+1); // this is the key point~


        while (StdIn.hasNextLine()) {
            // 读取标准输入的下一行的数据
            String line = StdIn.readLine();
            // 从读取的数据中创建出 transaction对象
            Transaction transaction = new Transaction(line);
            // 把它添加到 最大元素集合中
            collectionForBiggestMItems.insert(transaction);

            // 如果集合中被添加了 “M+1”个元素，则：把其中最小的元素移除
            if (collectionForBiggestMItems.size() > biggestMth)
                collectionForBiggestMItems.delMin();
        }   // 循环结束后，最大的M个元素就被存放在 collectionForBiggestMItems 中了


        // 从 collectionForBiggestMItems 中取出元素打印 - 需要按序取出
        // 手段：使用一个栈结构 来 实现 “取出元素打印”
        Stack<Transaction> stack = new Stack<Transaction>();
        // collectionForBiggestMItems是支持迭代的集合
        // 数据类型中需要实现自己的迭代器 - 它会决定迭代的方式
        for (Transaction transaction : collectionForBiggestMItems)
            stack.push(transaction);

        // 栈（algs4包中所实现的）的迭代方式：从栈顶到栈底？
        for (Transaction transaction : stack)
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
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
// 验证：使用 优先级队列(核心API-#1 向队列中 添加元素; #2 从队列中 删除最大元素)，能够 找出 一个元素集合中，最大的M个元素
public class TopMFromWebsite {

    // 该类不应该被实例化，所以 设置为private.
    private TopMFromWebsite() { }

    /**
     * 从 标准输入 中 读取 一系列的交易；
     * 获取到 一个命令行参数 整数M；
     * 以 降序 把 交易中最大的M个交易 打印到 标准输出中
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 从 命令行参数 中 读取M的值（M是 预期获取到的 最大元素的 数量）
        int M = Integer.parseInt(args[0]);
        // 创建出一个 MinPQ数据结构的 实例，队列的容量大小为 M??
        // 🐖 优先队列中的元素 需要是 “可比较的”，这样 队列元素 才能具有 “优先级”
        // 手段：这里 Transaction类的compareTo()方法 提供了 元素间 比较大小的依据
        MinPQ<Transaction> collectionForBiggestMItems = new MinPQ<Transaction>(M+1);

        /* 在此应用中，优先队列的用法👇 */
        while (StdIn.hasNextLine()) {
            // 读取 标准输入的 当前行的数据
            String transactionInLine = StdIn.readLine();
            // 从 读取的数据 中 创建出 transaction对象
            Transaction transaction = new Transaction(transactionInLine);
            // ① 把 它 添加到 最大元素集合（优先队列） 中 - API#1
            System.out.println("~~~ 向优先级队列中 添加交易对象元素：" + transaction + " ~~~");
            collectionForBiggestMItems.insert(transaction);

            // ② 在 添加元素 后，如果 当前集合中的元素数量 已经超过了 “M”（已经添加了 M个元素），说明 需要 从集合中淘汰掉 当前的最小元素...
            if (collectionForBiggestMItems.size() > M) {
                System.out.println("@@@ 当前优先级队列中的元素数量 大于 预期得到的最大元素的数量 " + M + "，因此删除掉 当前优先级队列中的最小元素 @@@");
                // 则：把 其中最小的元素 移除(API#2) 来 确保 队列中 仅含有M个元素
                collectionForBiggestMItems.delMin();
                System.out.println();
            }
        }
        // 👆 循环结束后，集合的 最大的M个元素 就被存放在 collectionForBiggestMItems 中了

        /* 从 collectionForBiggestMItems 中 按序取出元素 并 打印 */
        System.out.println("### 集合中最大的（以交易金额作为比较依据）" + M + "个元素如下 👇 ###");
        // #1 先使用一个栈结构 来 从队列中获取元素&存储元素
        Stack<Transaction> transactionStack = new Stack<Transaction>();
        for (Transaction transaction : collectionForBiggestMItems) // collectionForBiggestMItems是 “支持迭代操作的集合”
            // 🐖 容器数据类型中 需要实现 自己的迭代器 - 它 会决定 使用者 进行迭代操作时的 具体用法
            transactionStack.push(transaction);

        // #2 遍历 栈结构，依次打印 栈中的元素
        for (Transaction transaction : transactionStack)
            StdOut.println(transaction);
    }
}
/*
启示：
    为了 完成任务，我们 需要的是 一个能够提供特定操作的 集合；
    特定操作：
        1 向其中 插入元素；
        2 删除 集合中的最小值；

    程序 获取外界输入 的方式：
        1 命令行参数； - 手段： args[0]
        2 标准输入流：StdIn.Xxx()
 */
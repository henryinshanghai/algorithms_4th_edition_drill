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
        int m = Integer.parseInt(args[0]);
        MinPQ<Transaction> pq = new MinPQ<Transaction>(m+1); // this is the key point~

        while (StdIn.hasNextLine()) {
            // Create an entry from the next line and put on the PQ.
            // 从下一行创建一个条目并将其放在PQ上。
            String line = StdIn.readLine();
            Transaction transaction = new Transaction(line);
            pq.insert(transaction);

            // remove minimum if m+1 entries on the PQ
            // 如果PQ上有m + 1个条目，则删除最小值
            if (pq.size() > m)
                pq.delMin();
        }   // top m entries are on the PQ 前M个条目位于PQ上

        // print entries on PQ in reverse order
        // 以相反的顺序在PQ上打印条目
        Stack<Transaction> stack = new Stack<Transaction>();
        for (Transaction transaction : pq)
            stack.push(transaction);
        for (Transaction transaction : stack)
            StdOut.println(transaction);
    }
}
/*
启示：
    这是PQ应用的一个具体案例：从N个元素中，找到最大的M个元素；
    这里的PQ是一个使用数字作为键的优先队列；
    说明：1 队列由元素组成； 2 元素可以是一个单一的元素，也可以是键 - 键对应的值组成的复合元素；

    任务：打印tinyBatch.txt文件中的数字最大的M行；
    说明：在N非常大的情况下，这是一件很棘手的事情。优先队列提供了一个很好的解决方案
    原理：
        1 使用一个优先队列来存储最先读取到的M行数据；
        2 当优先队列的大小超过M时，就删除掉队列中最小的元素；
    结果：优先队列中最终存储的是————以增序排列的最大的M行？

    增序排列是怎么做到的？？？

    任务：倒序输出这些行；
    手段：使用栈存储所有元素，再从栈中读取；

    第一次成功运行algorithms4中的代码，避开了命令行参数的坑 感觉很好 哈哈
 */
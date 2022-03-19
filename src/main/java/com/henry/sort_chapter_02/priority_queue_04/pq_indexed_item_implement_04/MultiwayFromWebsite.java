package com.henry.sort_chapter_02.priority_queue_04.pq_indexed_item_implement_04;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac MultiwayFromWebsite.java
 *  Execution:    java MultiwayFromWebsite input1.txt input2.txt input3.txt ...
 *  Dependencies: IndexMinPQ.java In.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/24pq/m1.txt
 *                https://algs4.cs.princeton.edu/24pq/m2.txt
 *                https://algs4.cs.princeton.edu/24pq/m3.txt
 *
 *  Merges together the sorted input stream given as command-line arguments
 *  into a single sorted output stream on standard output.
 *
 *  % more m1.txt 
 *  A B C F G I I Z
 *
 *  % more m2.txt 
 *  B D H P Q Q
 *
 *  % more m3.txt 
 *  A B E F J N
 *
 *  % java MultiwayFromWebsite m1.txt m2.txt m3.txt 
 *  A A B B B C D E F F G H I I J N P Q Q Z 
 *
 ******************************************************************************/

/**
 *  The {@code MultiwayFromWebsite} class provides a client for reading in several
 *  sorted text files and merging them together into a single sorted
 *  text stream.
 *  调用了IndexMinPQ来把“作为命令行输入的多行有序字符串”归并成一行有序的输出
 *  每个输入流的索引都关联着一个元素（输入中的下一个字符串）
 *  This implementation uses a {@link IndexMinPQ} to perform the multiway
 *  merge. 
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a>
 *  of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class MultiwayFromWebsite {

    // This class should not be instantiated.   这个类不应该被实例化
    private MultiwayFromWebsite() { }

    // merge together the sorted input streams and write the sorted result to standard output
    // 归并排序好的输入流并把归并后的结果写入到标准输出中
    private static void merge(In[] streams) {
        int n = streams.length;
        IndexMinPQ<String> pq = new IndexMinPQ<String>(n);
        for (int i = 0; i < n; i++)
            if (!streams[i].isEmpty())
                // 读取 当前文件中的字符串，并插入到 索引优先优先队列(队列中的元素有一个索引)中
                pq.insert(i, streams[i].readString());

        // Extract and print min and read next from its stream.
        // 提取并打印最小值然后从流中读取下一个
        while (!pq.isEmpty()) {
            StdOut.print(pq.minKey() + " ");
            int i = pq.delMin();
            if (!streams[i].isEmpty()) // 读取 最小元素所在的文件流中的下一个元素, 并添加进队列中
                pq.insert(i, streams[i].readString()); // 只有在插入时执行了索引i
        }
        StdOut.println();
    } // 这个用法可能精巧，但是并不简洁


    /**
     *  Reads sorted text files specified as command-line arguments;
     *  merges them together into a sorted output; and writes
     *  the results to standard output.
     *  Note: this client does not check that the input files are sorted.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int n = args.length; // 命令行中的多个参数组成的数组
        In[] streams = new In[n]; // In类型元素组成的数组
        for (int i = 0; i < n; i++)
            streams[i] = new In(args[i]); // 为数组中的每一个元素绑定值
        merge(streams); // 对streams数组中的每一个元素进行merge操作
    }
} // 为什么一定要使用 索引优先队列， 一个基础的优先队列似乎就能完成工作了
/*
alright, let's see what we can do here.
启示：get pass this, or it is your last piece.
 */
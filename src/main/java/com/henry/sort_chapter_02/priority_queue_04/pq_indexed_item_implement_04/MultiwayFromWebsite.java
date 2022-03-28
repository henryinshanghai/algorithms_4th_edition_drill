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
 *  MultiwayFromWebsite 提供了一个client 来 读取多个 其中包含排序文本的文件，并把它们合并起来成为单一的排序文本流
 *  This implementation uses a {@link IndexMinPQ} to perform the multiway
 *  merge.
 *  这里的实现使用一个 IndexMinPQ 来 执行 多向归并的操作
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a>
 *  of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class MultiwayFromWebsite {

    // This class should not be instantiated.
    // 手段：把构造方法私有化
    private MultiwayFromWebsite() { }

    // merge together the sorted input streams and write the sorted result to standard output
    // 归并 有序的输入流 并 把归并后的结果写入到标准输出中
    private static void merge(In[] streams) {
        int streamAmount = streams.length;
        IndexMinPQ<String> pq = new IndexMinPQ<String>(streamAmount);

        /* 向优先队列中插入 每一个流中的起始元素 */
        for (int streamNo = 0; streamNo < streamAmount; streamNo++)
            if (!streams[streamNo].isEmpty())
                // 读取 当前流中的一个字符串，并以 index-> element的方式 将之添加到 索引优先优先队列 中
                pq.insert(streamNo, streams[streamNo].readString());
        // 循环结束后，pq中有3个元素：A, B, A

        /* 提取并打印最小值 然后从流中读取下一个 */
        while (!pq.isEmpty()) {
            // operation01 - 取出集合中的最小元素
            StdOut.print(pq.minKey() + " ");
            /*
                获取到 最小元素的index - 这其实就是最小元素所在的stream的streamNo
                手段： operation2 - 删除集合中的最小元素，并返回 最小元素所关联的index
             */
            int indexOfMinItem = pq.delMin();

            if (!streams[indexOfMinItem].isEmpty())
                // operation03 - 向集合中，添加 流中当前的字符串 作为新的元素
                pq.insert(indexOfMinItem, streams[indexOfMinItem].readString()); // 只有在插入时执行了索引i
        }
        // 循环会一直执行直到 pq集合为空为止
        /*
            为什么这个循环能够保证 3个流中的每一个字符串都被处理到？
            循环体的处理模式：
                - 删除 集合中的最小元素；
                - 获取最小元素所在的流的下一个字符串，作为新的元素插入 集合；
            为什么在集合为空之前，所有 流中的字符串都会已经被添加到 集合中？
            原理：
                1 每次循环都会删除一个元素，但只是有可能会添加一个元素。所以最终集合会变成一个空集合。
                2 最小元素所在的streamNo,会交替出现，所以 所有流中的元素都会被添加到集合中。
                3 当某个流中的元素已经用尽，集合会删除掉当前的最小元素。并从新的流中继续获取元素 —— 直到堆所有的流来说，流中的元素都用尽
         */

        //
        StdOut.println();
    }


    /**
     *  Reads sorted text files specified as command-line arguments;
     *  merges them together into a sorted output; and writes
     *  the results to standard output.
     *  Note: this client does not check that the input files are sorted.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        /* 使用命令行参数获取到 stream[] */
        int argAmount = args.length;
        In[] streams = new In[argAmount];
        for (int streamNo = 0; streamNo < argAmount; streamNo++)
            streams[streamNo] = new In(args[streamNo]);

        /* 对得到的多个有序stream 进行归并操作, 得到单一的有序 stream */
        merge(streams); // 对streams数组中的每一个元素进行merge操作
    }
}
/*
    为什么需要使用 索引优先队列？
    答： 获取到队列中元素的index

    什么地方有用到index?
    答：在获取下一个流中的字符串，选定 streamNo时。

 */
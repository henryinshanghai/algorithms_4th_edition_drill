package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_indexed_item_implement_04.application_and_usage;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac MergeFromMultipleWayTemplate.java
 *  Execution:    java MergeFromMultipleWayTemplate input1.txt input2.txt input3.txt ...
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
 *  % java MergeFromMultipleWayTemplate m1.txt m2.txt m3.txt
 *  A A B B B C D E F F G H I I J N P Q Q Z 
 *
 *  在IDEA中，Edit Configuration，在 CLI arguments字段中，填写所有的 “作为命令行参数的3个文件名”
 ******************************************************************************/

/**
 * The {@code MergeFromMultipleWayTemplate} class provides a client for reading in several
 * sorted text files and merging them together into a single sorted
 * text stream.
 * MergeFromMultipleWayTemplate 提供了一个client 来 读取多个 其中包含排序文本的文件，并把它们合并起来成为单一的排序文本流
 * This implementation uses a {@link IndexMinPQ} to perform the multiway
 * merge.
 * 这里的实现使用一个 IndexMinPQ 来 执行 多向归并的操作
 * <p>
 * For additional documentation, see <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a>
 * of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 验证：可以使用”索引优先队列“这种数据结构 来 实现 把 多个“有序的输入流”(具体表现为“文件中的有序数字集合”） 归并成为 单一的“有序输入流” (aka 多向归并问题)
public class MergeFromMultipleWayTemplate {

    // 不希望 当前类型 被实例化 - 手段：私有的构造方法
    private MergeFromMultipleWayTemplate() {
    }

    // 归并 多个有序的输入流 并 把 归并后的结果 写入到 标准输出 中
    // 🐖 这里 之所以 使用 索引优先队列，是因为 可以 使用索引 来 记录 元素所属的流 - 进而能够 从“对应的流/组”中 获取 其下一个元素
    private static void mergeIntoSingleSortedStream(In[] sortedStreams) {
        // #1 “实例化”一个 对应容量的 索引优先队列
        int streamAmount = sortedStreams.length;
        IndexMinPQ<String> indexMinPQ = new IndexMinPQ<String>(streamAmount); // 队列的初始容量 是 输入流的数量

        // #2 初始化 索引优先队列，
        for (int streamNo = 0; streamNo < streamAmount; streamNo++)
            if (!sortedStreams[streamNo].isEmpty()) { // 对于 每一个输入流
                In currentStream = sortedStreams[streamNo];
                String firstStr = currentStream.readString();
                // 向 优先队列 中 插入 流的编号 -> 流中的起始元素
                indexMinPQ.insert(streamNo, firstStr);
            }
        // 循环结束后，pq中 有3个元素：A, B, A

        // #3 动态更新 索引优先队列中的元素，直到 耗尽 所有输入流中的所有元素
        while (!indexMinPQ.isEmpty()) {
            // #3-1 打印出 当前索引优先队列中的 最小元素
            StdOut.print(indexMinPQ.minKey() + " ");
            // #3-2 移除 已经 打印的字符串，并 消耗 下一个字符串
            removePrintedStrAndConsumeNextStr(sortedStreams, indexMinPQ);
        }

        StdOut.println();
    }

    private static void removePrintedStrAndConsumeNextStr(In[] streams, IndexMinPQ<String> indexMinPQ) {
        // #1 删除 当前索引优先队列中的 最小元素 并 返回 最小元素的index - aka “被删除元素” 所属的流 的编号
        int indexOfMinItem = indexMinPQ.delMin();

        // #2 从 “被删除元素所属于的流/组” 中，再读取一个元素，添加到 索引（这里就是流的序号）优先队列中
        In streamOfDeletedStr = streams[indexOfMinItem];
        if (!streamOfDeletedStr.isEmpty()) {
            String readStr = streamOfDeletedStr.readString();
            indexMinPQ.insert(indexOfMinItem, readStr); // 只有 在插入时 使用了 索引i
        }
    }


    /**
     * 读取 被指定为命令行参数的、排序好的 文本文件
     * 把 它们 合并到 一个有序的输出 中，并 把结果 写入到 标准输出流 中。
     * <p>
     * 🐖 客户端 不会检查 输入文件 是否是 有序的
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) { // 命令行参数args 是 文件名的序列：m1.txt m2.txt m3.txt
        // 使用 命令行参数的数量 来 创建一个 对应容量的 In类型的数组
        int argAmount = args.length;
        // 实例化 streams数组
        In[] streams = new In[argAmount];

        // 初始化 streams数组
        for (int streamNo = 0; streamNo < argAmount; streamNo++) {
            // 读取 命令行参数 所提供的文件名
            String currentStreamFile = args[streamNo];
            // 由 文件名 来 得到 标准输入流，并 绑定到 streams数组元素 上
            streams[streamNo] = new In(currentStreamFile);
        }

        // 对 标准输入流数组 streams 进行 归并操作 - 得到 完整的有序序列
        mergeIntoSingleSortedStream(streams);
    }
}
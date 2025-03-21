package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_indexed_item_implement_04.application_and_usage;

import com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_indexed_item_implement_04.IndexMinPQSimpleDrill;
import edu.princeton.cs.algs4.In;

public class MergeFromMultipleWayDrill {

    private static void mergeIntoSingleSortedStream(In[] sortedStreams) {
        // 实例化 索引优先队列
        int streamAmount = sortedStreams.length;
        IndexMinPQSimpleDrill<String> indexMinPQ = new IndexMinPQSimpleDrill<>(streamAmount);

        // 初始化 索引优先队列
        for (int streamNo = 0; streamNo < sortedStreams.length; streamNo++) {
            if (!sortedStreams[streamNo].isEmpty()) {
                indexMinPQ.insert(streamNo, sortedStreams[streamNo].readString());
            }
        }

        // 更新索引优先队列 来 读取并打印(消耗)剩余的所有元素
        while (!indexMinPQ.isEmpty()) {
            // 获取到当前队列中的最小元素
            System.out.print(indexMinPQ.minItem() + " ");
            int streamNoOfMinItem = indexMinPQ.delMin();

            if (!sortedStreams[streamNoOfMinItem].isEmpty()) {
                indexMinPQ.insert(streamNoOfMinItem, sortedStreams[streamNoOfMinItem].readString());
            }
        }

        System.out.println();

    }

    public static void main(String[] args) { // m1.txt m2.txt m3.txt
        int argAmount = args.length;
        In[] streams = new In[argAmount];

        // 文件名 -> 标准输入流
        for (int currentStreamNo = 0; currentStreamNo < streams.length; currentStreamNo++) {
            String currentStreamFile = args[currentStreamNo];
            streams[currentStreamNo] = new In(currentStreamFile);
        }

        // 传入 标准输入流数组
        mergeIntoSingleSortedStream(streams);
    }
}

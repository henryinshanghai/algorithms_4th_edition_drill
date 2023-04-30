package com.henry.sort_chapter_02.priority_queue_04.pq_indexed_item_implement_04;

import edu.princeton.cs.algs4.In;

public class MergeFromMultipleWayDrill {

    private static void merge(In[] streams) {
        int streamAmount = streams.length;
        IndexMinPQSimpleDrill<String> indexMinPQ = new IndexMinPQSimpleDrill<>(streamAmount);

        // 索引优先队列的初始化
        for (int streamNo = 0; streamNo < streams.length; streamNo++) {
            if (!streams[streamNo].isEmpty()) {
                indexMinPQ.insert(streamNo, streams[streamNo].readString());
            }
        }

        // 处理剩余的所有元素
        while (!indexMinPQ.isEmpty()) {
            // 获取到当前队列中的最小元素
            System.out.print(indexMinPQ.minItem() + " ");
            int streamNoOfMinItem = indexMinPQ.delMin();

            if (!streams[streamNoOfMinItem].isEmpty()) {
                indexMinPQ.insert(streamNoOfMinItem, streams[streamNoOfMinItem].readString());
            }
        }

        System.out.println();

    }

    public static void main(String[] args) { // m1.txt m2.txt m3.txt
        int argAmount = args.length;
        In[] streams = new In[argAmount];

        // 文件名 -> 标准输入流
        for (int i = 0; i < streams.length; i++) {
            streams[i] = new In(args[i]);
        }

        // 传入 标准输入流数组
        merge(streams);
    }
}

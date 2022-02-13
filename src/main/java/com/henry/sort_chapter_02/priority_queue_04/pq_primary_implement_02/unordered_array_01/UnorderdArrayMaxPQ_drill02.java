package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.unordered_array_01;

import edu.princeton.cs.algs4.StdOut;

public class UnorderdArrayMaxPQ_drill02<Key extends Comparable<Key>> {
    // 实例变量
    private Key[] pq;      // elements
    private int n;         // number of elements

    // 构造方法
    public UnorderdArrayMaxPQ_drill02(int capacity) {
        pq = (Key[]) new Comparable[capacity]; // 不能直接创建泛型数组，只能先创建通用类型的数组，然后再进行强制类型转换
        n = 0;
    }

    // APIs
    public boolean isEmpty()   { return n == 0; }
    public int size()          { return n;      }
    public void insert(Key x)  { pq[n++] = x;   }

    /**
     * 从优先队列中删除最大的元素
     */
    public Key delMax() {
        int max = 0;
        for (int i = 1; i < n; i++)
            if (less(max, i)) max = i;
        exch(max, n-1);

        return pq[--n]; // 直接返回最后一个元素的值
    }

    // 这里传入的参数是两个位置索引
    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) < 0;
    }

    /**
     * 交换两个索引位置上的元素
     */
    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    public static void main(String[] args) {
        // 创建一个队列对象
        UnorderdArrayMaxPQ_drill02<String> pq = new UnorderdArrayMaxPQ_drill02<>(10);

        pq.insert("this"); // 写作This会使它成为最小值 Damn~
        pq.insert("is");
        pq.insert("a");
        pq.insert("test");

        // 逐个删除并打印其元素 从最大值到最小值
        while (!pq.isEmpty()) {
            StdOut.println(pq.delMax());
        }

    }
}

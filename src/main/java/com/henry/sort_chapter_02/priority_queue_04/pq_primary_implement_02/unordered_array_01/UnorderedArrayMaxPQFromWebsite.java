package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.unordered_array_01;

import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac UnorderedArrayMaxPQFromWebsite.java
 *  Execution:    java UnorderedArrayMaxPQFromWebsite
 *  Dependencies: StdOut.java 
 *
 *  Priority queue implementation with an unsorted array.
 *
 *  Limitations
 *  -----------
 *   - no array resizing
 *   - does not check for overflow or underflow.
 *
 ******************************************************************************/

public class UnorderedArrayMaxPQFromWebsite<Key extends Comparable<Key>> {
    // 实例变量
    private Key[] pq;      // elements
    private int n;         // number of elements

    // set initial size of heap to hold size elements
    public UnorderedArrayMaxPQFromWebsite(int capacity) {
        pq = (Key[]) new Comparable[capacity]; // 不能直接创建泛型数组，只能先创建通用类型的数组，然后再进行强制类型转换
        n = 0;
    }

    // APIs
    public boolean isEmpty()   { return n == 0; }
    public int size()          { return n;      }
    public void insert(Key x)  { pq[n++] = x;   }

    public Key delMax() {
        int max = 0;
        for (int i = 1; i < n; i++)
            if (less(max, i)) max = i;
        exch(max, n-1);

        return pq[--n]; // 直接返回最后一个元素的值
    }


    /***************************************************************************
     * Helper functions. 辅助函数
     ***************************************************************************/
    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) < 0;
    }

    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }


    /***************************************************************************
     * Test routine. 测试用例
     ***************************************************************************/
    public static void main(String[] args) {
        UnorderedArrayMaxPQFromWebsite<String> pq = new UnorderedArrayMaxPQFromWebsite<String>(10);
        pq.insert("this");
        pq.insert("is");
        pq.insert("a");
        pq.insert("test");
        while (!pq.isEmpty())
            StdOut.println(pq.delMax());
    }

}
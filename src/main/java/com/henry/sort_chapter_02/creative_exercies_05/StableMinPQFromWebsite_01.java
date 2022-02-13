package com.henry.sort_chapter_02.creative_exercies_05;

import edu.princeton.cs.algs4.StdOut;


/******************************************************************************
 *  Compilation:  javac StableMinPQFromWebsite.java
 *  Execution:    java StableMinPQFromWebsite
 *  Dependencies: StdOut.java
 *
 *  Generic min priority queue implementation with a binary heap.
 *  The min() and delMin() operations always return the minimum key
 *  that as inserted least recently when there are two or more.
 *
 *  % java StableMinPQFromWebsite
 *  a
 *  is
 *  test
 *  this
 *
 *  We use a one-based array to simplify parent and child calculations.
 *  作用：稳定的优先队列  aka 存储元素时，对于重复的元素，会存储重复元素的相对顺序
 *  aka 强制稳定
 *
 *  手段：1 使用一个time[]数组来记录元素被插入到队列时的时间标识；
 *  2 在greater()方法中，当两个元素的值相等时，再去比较两个元素插入队列的时间标识；
 *  3 在exch()方法中，交换pq[]中的两个元素后，time[]中元素对应的时间戳也需要交换
 *  4 同理insert()、delMin()也都需要做一些处理
 ******************************************************************************/

public class StableMinPQFromWebsite_01<Key extends Comparable<Key>> {
    // 实例变量
    private Key[]  pq;                   // store element at indices 1 to N  使用索引[1-N]来存储元素
    private long[] time;                 // timestamp 时间戳
    private int n;                       // number of elements on priority queue 优先队列中的元素数
    private long timestamp = 1;          // timestamp for when item was inserted 元素被插入时的时间戳

    // create an empty priority queue with given initial capacity
    public StableMinPQFromWebsite_01(int initCapacity) {
        pq = (Key[]) new Comparable[initCapacity + 1];
        time = new long[initCapacity + 1];
        n = 0;
    }

    // create an empty priority queue
    public StableMinPQFromWebsite_01() {
        this(1);
    }


    // Is the priority queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // Return the number of elements on the priority queue.
    public int size() {
        return n;
    }

    //  Return the smallest key on the priority queue.
    public Key min() {
        if (isEmpty()) throw new RuntimeException("Priority queue underflow");
        return pq[1];
    }

    // helper function to double the size of the heap array
    private void resize(int capacity) {
        assert capacity > n;
        Key[]  tempPQ   = (Key[]) new Comparable[capacity];
        long[] tempTime = new long[capacity];
        for (int i = 1; i <= n; i++)
            tempPQ[i] = pq[i];
        for (int i = 1; i <= n; i++)
            tempTime[i] = time[i];
        pq   = tempPQ;
        time = tempTime;
    }

    // add a new key to the priority queue
    public void insert(Key x) {
        // double size of array if necessary
        if (n == pq.length - 1) resize(2 * pq.length);

        // add x, and percolate it up to maintain heap invariant
        n++;
        pq[n] = x;
        time[n] = ++timestamp;
        swim(n);
        assert isMinHeap();
    }

    // Delete and return the smallest key on the priority queue.
    public Key delMin() {
        if (n == 0) throw new RuntimeException("Priority queue underflow");
        exch(1, n);
        Key min = pq[n--];
        sink(1);
        pq[n+1] = null;         // avoid loitering and help with garbage collection
        time[n+1] = 0;
        if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length  / 2);
        assert isMinHeap();
        return min;
    }


    /***************************************************************************
     * Helper functions to restore the heap invariant.
     ***************************************************************************/

    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    /***************************************************************************
     * Helper functions for compares and swaps.
     ***************************************************************************/
    private boolean greater(int i, int j) {
        int cmp = pq[i].compareTo(pq[j]);
        if (cmp > 0) return true;
        if (cmp < 0) return false;
        return time[i] > time[j];
    }

    private void exch(int i, int j) {
        Key temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
        long tempTime = time[i];
        time[i] = time[j];
        time[j] = tempTime;
    }

    // is pq[1..N] a min heap?
    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    // is subtree of pq[1..n] rooted at k a min heap?
    private boolean isMinHeap(int k) {
        if (k > n) return true;
        int left = 2*k, right = 2*k + 1;
        if (left  <= n && greater(k, left))  return false;
        if (right <= n && greater(k, right)) return false;
        return isMinHeap(left) && isMinHeap(right);
    }


    private static final class Tuple implements Comparable<Tuple> {
        private String name;
        private int id;

        private Tuple(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public int compareTo(Tuple that) {
            return this.name.compareTo(that.name);
        }

        public String toString() {
            return name + " " + id;
        }
    }

    // test client
    public static void main(String[] args) {
        StableMinPQFromWebsite_01<Tuple> pq = new StableMinPQFromWebsite_01<Tuple>();

        // insert a bunch of strings
        String text = "it was the best of times it was the worst of times it was the "
                + "age of wisdom it was the age of foolishness it was the epoch "
                + "belief it was the epoch of incredulity it was the season of light "
                + "it was the season of darkness it was the spring of hope it was the "
                + "winter of despair";
        String[] strings = text.split(" ");
        for (int i = 0; i < strings.length; i++) {
            pq.insert(new Tuple(strings[i], i));
        }


        // delete and print each key
        while (!pq.isEmpty()) {
            StdOut.println(pq.delMin());
        }
        StdOut.println();

    }

}
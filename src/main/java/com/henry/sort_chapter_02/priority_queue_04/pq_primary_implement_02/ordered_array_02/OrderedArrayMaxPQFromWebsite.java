package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.ordered_array_02;

import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac OrderedArrayMaxPQFromWebsite.java
 *  Execution:    java OrderedArrayMaxPQFromWebsite
 *  Dependencies: StdOut.java 
 *
 *  Priority queue implementation with an ordered array.
 *
 *  Limitations
 *  -----------
 *   - no array resizing 
 *   - does not check for overflow or underflow.
 *
 * 特征：在插入元素时就保证最后一个元素始终是最大值；    这样删除最大值时直接删除最后一个元素即可；
 ******************************************************************************/

public class OrderedArrayMaxPQFromWebsite<Key extends Comparable<Key>> {
    private Key[] pq;          // elements
    private int n;             // number of elements

    // set inititial size of heap to hold size elements
    public OrderedArrayMaxPQFromWebsite(int capacity) {
        pq = (Key[]) (new Comparable[capacity]);
        n = 0;
    }


    public boolean isEmpty() { return n == 0;  }
    public int size()        { return n;       }
    public Key delMax()      { return pq[--n]; }

    public void insert(Key key) {
        int i = n-1;
        while (i >= 0 && less(key, pq[i])) {
            pq[i+1] = pq[i];
            i--;
        }
        pq[i+1] = key;
        n++;
    }



    /***************************************************************************
     * Helper functions.
     ***************************************************************************/
    private boolean less(Key v, Key w) {
        return v.compareTo(w) < 0;
    }

    /***************************************************************************
     * Test routine.
     ***************************************************************************/
    public static void main(String[] args) {
        OrderedArrayMaxPQFromWebsite<String> pq = new OrderedArrayMaxPQFromWebsite<String>(10);
        pq.insert("this");
        pq.insert("is");
        pq.insert("a");
        pq.insert("test");
        while (!pq.isEmpty())
            StdOut.println(pq.delMax());
    }

}
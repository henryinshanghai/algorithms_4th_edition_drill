package com.henry.sort_chapter_02.priority_queue_04.pq_sort_05;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


/******************************************************************************
 *  Compilation:  javac HeapFromWebsite.java
 *  Execution:    java HeapFromWebsite < input.txt
 *  Dependencies: StdOut.java StdIn.java
 *  Data files:   https://algs4.cs.princeton.edu/24pq/tiny.txt
 *                https://algs4.cs.princeton.edu/24pq/words3.txt
 *
 *  Sorts a sequence of strings from standard input using heapsort.
 *
 *  % more tiny.txt
 *  S O R T E X A M P L E
 *
 *  % java HeapFromWebsite < tiny.txt
 *  A E E L M O P R S T X                 [ one string per line ]
 *
 *  % more words3.txt
 *  bed bug dad yes zoo ... all bad yet
 *
 *  % java HeapFromWebsite < words3.txt
 *  all bad bed bug dad ... yes yet zoo   [ one string per line ]
 *
 ******************************************************************************/

/**
 *  The {@code HeapFromWebsite} class provides a static method to sort an array
 *  using <em>heapsort</em>.
 *  <p>
 *  This implementation takes &Theta;(<em>n</em> log <em>n</em>) time
 *  to sort any array of length <em>n</em> (assuming comparisons
 *  take constant time). It makes at most 
 *  2 <em>n</em> log<sub>2</sub> <em>n</em> compares.
 *  <p>
 *  This sorting algorithm is not stable.
 *  It uses &Theta;(1) extra memory (not including the input array).
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class HeapFromWebsite {

    // This class should not be instantiated.
    private HeapFromWebsite() { }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param pq the array to be sorted
     */
    public static void sort(Comparable[] pq) {
        int n = pq.length;

        // heapify phase
        for (int k = n/2; k >= 1; k--)
            sink(pq, k, n); // n值不会变化，一直都是pq.length

        // sortdown phase
        int k = n;
        while (k > 1) {
            // 删除顶点元素的操作
            exch(pq, 1, k--); // k-- 用于移动指针
            sink(pq, 1, k); // 这里的k是二叉堆中当前的节点数量
            // 注意：这里并没有物理删除节点值。所以最后会得到一个升序的数组
        }
    }

    /***************************************************************************
     * Helper functions to restore the heap invariant.
     ***************************************************************************/

    private static void sink(Comparable[] pq, int k, int n) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && less(pq, j, j+1)) j++;
            if (!less(pq, k, j)) break;
            exch(pq, k, j);
            k = j;
        }
    }

    /***************************************************************************
     * Helper functions for comparisons and swaps.
     * Indices are "off-by-one" to support 1-based indexing.
     ***************************************************************************/
    private static boolean less(Comparable[] pq, int i, int j) {
        return pq[i-1].compareTo(pq[j-1]) < 0;
    }

    private static void exch(Object[] pq, int i, int j) {
        Object swap = pq[i-1];
        pq[i-1] = pq[j-1];
        pq[j-1] = swap;
    }

    // print array to standard output
    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.println(a[i]);
        }
    }

    /**
     * Reads in a sequence of strings from standard input; heapsorts them; 
     * and prints them to standard output in ascending order. 
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        HeapFromWebsite.sort(a);
        show(a);
    }
}



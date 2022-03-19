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

    // 希望把当前类处理成为一个静态方法库，所以这里把 构造方法私有化 - 不希望Client创建它的实例对象
    private HeapFromWebsite() { }

    // 把传入的数组中的元素，按照升序重新排列 - 升序的规则就是自然顺序
    public static void sort(Comparable[] pq) { // 从输入流中读取的原始数组
        int itemAmount = pq.length;

        // Ⅰ 把原始数组 构建成为一个堆
        /*
            思路：从底往上逐层构建堆 （小的堆 -> 整个大的堆）
            手段：从完全二叉树的倒数第二层的最后一个节点开始，执行 sink(currentSpot)操作
            特征：
                spotInHeap = indexInArray + 1; // 因为数组中元素的下标是从0开始的，而堆中元素的位置是从1开始的
                所以 排定堆中的第一个位置上的元素 <=> 排定数组中的pq[0]
         */
        for (int spotInHeap = itemAmount/2; spotInHeap >= 1; spotInHeap--)
            sink(pq, spotInHeap, itemAmount); // k最终的值是1 n值一直都是pq.length

        // 逐个排定最大元素的阶段
        int cursorToLastSpotInHeap = itemAmount;
        while (cursorToLastSpotInHeap > 1) { // 当堆中只剩下一个元素的时候，它一定就是原始数组的最小值 不再需要任何的交换操作
            // 删除顶点元素的操作
            /*
                手段：把堆中最大的元素(也就是数组中最大的元素) 交换到数组的末尾；
                特征：
                    1 这里并没有物理删除节点值。所以最后会得到一个升序的数组
                    2 交换完成之后，更新 指向最后一个元素的指针的位置 - 以便用剩下的元素 来 重建堆有序的数组
             */
            exch(pq, 1, cursorToLastSpotInHeap--); // cursorToLastSpotInHeap-- 用于移动指针

            // 用剩下的元素 来 重建堆有序的数组
            /*
                手段：把完全二叉树的根节点sink；
                特征：
                    1 这里完全二叉树的末尾节点是 指针所在的位置（lastSpotInHeap）
             */
            sink(pq, 1, cursorToLastSpotInHeap); // 这里的k是二叉堆中当前的节点数量
        }
    }

    /***************************************************************************
     * Helper functions to restore the heap invariant.
     ***************************************************************************/
    // 把位置k上的元素移动到正确的位置上 注： spot = index + 1
    private static void sink(Comparable[] pq, int currentSpot, int lastSpotInHeap) { // k最终的值是1 n值一直都是pq.length
        while (2*currentSpot <= lastSpotInHeap) { // 循环终结条件：当前位置的子节点的位置 已经到了 数组的边界位置 - 在此位置之后，执行交换是没有意义的 因为已经没有可用的子节点了
            int biggerChildSpot = 2*currentSpot;
            if (biggerChildSpot < lastSpotInHeap && less(pq, biggerChildSpot, biggerChildSpot+1)) biggerChildSpot++; // 传入的是spot的值，而比较时需要使用index

            // 如果 已经把当前位置上的元素 交换到了合适的位置，则： break 以停止交换
            if (!less(pq, currentSpot, biggerChildSpot)) break;

            // 否则的话（注：这里不需要使用else,因为就只有两种选择），继续执行交换 - 直到达到预期结果为止
            exch(pq, currentSpot, biggerChildSpot);

            // 更新 currentSpot，以继续循环
            currentSpot = biggerChildSpot;
        }
    }

    /***************************************************************************
     * Helper functions for comparisons and swaps.
     * Indices are "off-by-one" to support 1-based indexing.
     ***************************************************************************/
    // spotInHeap = indexInArray + 1
    private static boolean less(Comparable[] pq, int i, int j) { // parameters are spotInHeap
        // using the indexInArray
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
            StdOut.println(a[i]); // 从位置0开始，逐个打印每个位置上的元素
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



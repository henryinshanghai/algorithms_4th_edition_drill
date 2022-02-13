package com.henry.sort_chapter_02.quick_sort_algorithm_03.quick_sort_for_repeatItem_03;

import edu.princeton.cs.algs4.Quick3way;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;


/******************************************************************************
 *  Compilation:  javac Quick3way.java
 *  Execution:    java Quick3way < input.txt
 *  Dependencies: StdOut.java StdIn.java
 *  Data files:   https://algs4.cs.princeton.edu/23quicksort/tiny.txt
 *                https://algs4.cs.princeton.edu/23quicksort/words3.txt
 *
 *  Sorts a sequence of strings from standard input using 3-way quicksort.
 *
 *  % more tiny.txt
 *  S O R T E X A M P L E
 *
 *  % java Quick3way < tiny.txt
 *  A E E L M O P R S T X                 [ one string per line ]
 *
 *  % more words3.txt
 *  bed bug dad yes zoo ... all bad yet
 *
 *  % java Quick3way < words3.txt
 *  all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 ******************************************************************************/

/**
 *  The {@code Quick3way} class provides static methods for sorting an
 *  array using quicksort with 3-way partitioning.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/23quick">Section 2.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Quick3wayFromWebsite {

    // This class should not be instantiated.
    private Quick3wayFromWebsite() { }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1);
        assert isSorted(a);
    }

    // quicksort the subarray a[lo .. hi] using 3-way partitioning
    private static void sort(Comparable[] a, int lo, int hi) {
        // 1 递归的基本情况：参数hi<=参数lo
        if (hi <= lo) return;

        // 2 三个指针   作用：a[lo...lt-1]为小于区，a[i...i-1]为等于区，a[gt...hi]为大于区
        int lt = lo, gt = hi; // 专用指针：标定区域范围
        int i = lo + 1; // 通用指针  从第二个元素开始执行比较&交换操作

        // 3 切分元素   使用数组的第一个元素作为切分元素    手段：初始化v=a[lo]
        Comparable v = a[lo];

        while (i <= gt) {
            // 比较当前元素与切分元素的值大小
            int cmp = a[i].compareTo(v);
            // 当前元素更小时...   1 交换当前元素与小于区的右边界元素(a[i]&a[lt]) 2 把i++:移动到下一个数组元素 3 把lt++：使指针指向小于区的右边界
            if      (cmp < 0) exch(a, lt++, i++);
            // 当前元素更大时...   1 交换当前元素与大于区的左边界元素(a[i]&a[gt]) 2 把gt--:使指针指向大于区的左边界
            else if (cmp > 0) exch(a, i, gt--);
            // 当前元素=切分元素时   1 i++:扩充等于区
            else              i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        // 就只对小于区和大于区进行排序即可
        sort(a, lo, lt-1);
        sort(a, gt+1, hi);
        assert isSorted(a, lo, hi);
    }



    /***************************************************************************
     *  Helper sorting functions.
     ***************************************************************************/

    // is v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // exchange a[i] and a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }


    /***************************************************************************
     *  Check if array is sorted - useful for debugging.
     ***************************************************************************/
    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++)
            if (less(a[i], a[i-1])) return false;
        return true;
    }



    // print array to standard output
    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.println(a[i]);
        }
    }

    /**
     * Reads in a sequence of strings from standard input; 3-way
     * quicksorts them; and prints them to standard output in ascending order. 
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        Quick3way.sort(a);
        show(a);
    }

}
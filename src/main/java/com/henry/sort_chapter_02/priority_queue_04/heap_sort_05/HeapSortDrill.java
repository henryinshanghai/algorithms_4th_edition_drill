package com.henry.sort_chapter_02.priority_queue_04.heap_sort_05;

import edu.princeton.cs.algs4.StdIn;

// first, make the original array a heap, then heap sort it via arrange the biggest item and re-make the heap.
public class HeapSortDrill {

    public static void sort(Comparable[] a) {
        // build a heap out of the original array
        int itemAmount = a.length;

        for (int currentNodeSpot = itemAmount / 2; currentNodeSpot > 1; currentNodeSpot--) {
            sink(a, currentNodeSpot, itemAmount);
        }

        // heap-sort the array based on the heap
        int cursorToLastNode = itemAmount;
        while (cursorToLastNode > 1) {
            // 排定最大元素
            exch(a, 1, cursorToLastNode--);

            // 重建堆
            sink(a, 1, cursorToLastNode);
        }
    }

    private static void sink(Comparable[] originalArray, int currentNodeSpot, int lastNodeSpot) {
        while (currentNodeSpot * 2 < lastNodeSpot) {
            int biggerChildSpot = currentNodeSpot * 2;
            if (less(originalArray, biggerChildSpot, biggerChildSpot+1)) biggerChildSpot++;

            if (!less(originalArray, currentNodeSpot, biggerChildSpot)) break;

            exch(originalArray, currentNodeSpot, biggerChildSpot);
            currentNodeSpot = biggerChildSpot;
        }
    }

    private static void exch(Comparable[] originalArray, int spotI, int spotJ) {
        Comparable temp = originalArray[spotI - 1];
        originalArray[spotI - 1] = originalArray[spotJ - 1];
        originalArray[spotJ - 1] = temp;
    }

    private static boolean less(Comparable[] originalArray, int spotI, int spotJ) {
        return originalArray[spotI - 1].compareTo(originalArray[spotJ - 1]) < 0;
    }

    public static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {
        String[] words = StdIn.readAllStrings();

        HeapSortDrill.sort(words);

        show(words);
    }
}

package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.heap_sort_05;

import edu.princeton.cs.algs4.StdIn;

public class HeapSortDrill {

    private static void sort(Comparable[] a) {
        int itemAmount = a.length;

        // #1 build a heap out of the array - via sink(currentNodeSpot)
        for (int currentNodeSpot = itemAmount / 2; currentNodeSpot >= 1; currentNodeSpot--) { // 这里是 >=
            sinkNodeOf(a, currentNodeSpot, itemAmount);
        }

        // #2 sort the items in the array - arrange the biggest item, and re-construct the heap.
        int cursorToLastNodeSpot = itemAmount;
        while (cursorToLastNodeSpot > 1) {
            // arrange the maxItem and remove it logically
            exch(a, 1, cursorToLastNodeSpot--);
            // to re-build the max heap based on what's rest.
            sinkNodeOf(a, 1, cursorToLastNodeSpot);
        }
    }

    private static void sinkNodeOf(Comparable[] originalArray, int currentNodeSpot, int lastNodeSpot) {
        while (currentNodeSpot * 2 < lastNodeSpot) {
            int biggerChildSpot = currentNodeSpot * 2;
            if (less(originalArray, biggerChildSpot, biggerChildSpot + 1)) biggerChildSpot++;

            if(!less(originalArray, currentNodeSpot, biggerChildSpot)) break;

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

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();

        HeapSortDrill.sort(a);

        show(a);
    }
}
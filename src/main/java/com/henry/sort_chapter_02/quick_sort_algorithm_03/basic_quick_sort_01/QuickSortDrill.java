package com.henry.sort_chapter_02.quick_sort_algorithm_03.basic_quick_sort_01;

import edu.princeton.cs.algs4.StdRandom;

// #1 arrange the first item via swapping corresponding items into lessZone and greaterZone(using head and tail cursor);
// #2 sort the lessZone, greaterZone;
public class QuickSortDrill {

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);

        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        if (leftBar >= rightBar) return;

        int arrangedPosition = partition(a, leftBar, rightBar);

        sort(a, leftBar, arrangedPosition - 1);
        sort(a, arrangedPosition + 1, rightBar);
    }

    // 排定算法
    private static int partition(Comparable[] a, int leftBar, int rightBar) {
        int leftCursor = leftBar;
        int rightBackwardsCursor = rightBar + 1;

        Comparable pivot = a[leftBar];

        // 分拣算法
        while (true) {
            while(less(a[++leftCursor], pivot)) if (leftCursor == rightBar) break;
            while(less(pivot, a[--rightBackwardsCursor])) if (rightBackwardsCursor == leftBar) break;

            if (leftCursor >= rightBackwardsCursor) break;
            exch(a, leftCursor, rightBackwardsCursor);
        }

        // 排定元素
        exch(a, leftBar, rightBackwardsCursor);

        // 排定位置
        int arrangedPosition = rightBackwardsCursor;
        return arrangedPosition;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {
        String[] a = {"Q", "U", "I", "C", "K", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);

        show(a);
    }
}

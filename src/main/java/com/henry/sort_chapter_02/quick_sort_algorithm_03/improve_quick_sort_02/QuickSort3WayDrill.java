package com.henry.sort_chapter_02.quick_sort_algorithm_03.improve_quick_sort_02;

import edu.princeton.cs.algs4.StdRandom;

// #1 arrange all the items that equals to pivot(equalsZone) via picking current item into its corresponding zone(using barCursors and currentItemCursor);
// #2 sort lessZone and greaterZone;
public class QuickSort3WayDrill {
    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);

        sort(a, 0, a.length - 1);
    }

    public static void sort(Comparable[] a, int leftBar, int rightBar) {
        if(leftBar >= rightBar) return;

        // 排定 与pivot元素相等的元素序列 a[lessZone, greaterZone]
        int lessZoneRightBarCursor = leftBar;
        int greaterZoneLeftBarCursor = rightBar;
        int currentItemCursor = leftBar + 1;

        Comparable pivot = a[leftBar];

        while (currentItemCursor <= greaterZoneLeftBarCursor) {
            int result = a[currentItemCursor].compareTo(pivot);

            if (result < 0) exch(a, currentItemCursor++, lessZoneRightBarCursor++);
            else if(result > 0) exch(a, currentItemCursor, greaterZoneLeftBarCursor--);
            else currentItemCursor++;
        }

        sort(a, leftBar, lessZoneRightBarCursor - 1);
        sort(a, greaterZoneLeftBarCursor + 1, rightBar);
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {
        String[] a = {"Q", "U", "I", "C", "K", "S", "O", "R", "T", "3", "W", "A", "Y", "E", "X", "A", "M", "P", "L", "E"};

        sort(a);
        show(a);
    }
}

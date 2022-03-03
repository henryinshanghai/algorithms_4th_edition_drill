package com.henry.sort_chapter_02.quick_sort_algorithm_03.improve_quick_sort_02;

import edu.princeton.cs.algs4.StdRandom;

public class Quick3way_round01_drill01 {

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);

        sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int leftBar, int rightBar) {
        if(leftBar >= rightBar) return;

        int lessZoneRightBoundary = leftBar;
        int equalZoneRightBoundary = leftBar + 1;
        int greaterZoneLeftBoundary = rightBar;

        Comparable pivot = a[leftBar];

        while (equalZoneRightBoundary <= greaterZoneLeftBoundary) {
            Comparable currentItem = a[equalZoneRightBoundary];
            int compareResult = currentItem.compareTo(pivot);

            if (compareResult < 0) exch(a, equalZoneRightBoundary, lessZoneRightBoundary++); // 这里第一次交换过来的肯定是一个pivot元素
            else if (compareResult > 0) exch(a, equalZoneRightBoundary, greaterZoneLeftBoundary--);
            else equalZoneRightBoundary++;
        }

        sort(a, leftBar, lessZoneRightBoundary - 1); // 这里的区间范围是： a[leftBar, lessZoneRightBoundary - 1]
        sort(a, greaterZoneLeftBoundary + 1, rightBar);
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void printItems(Comparable[] a) {
        int N = a.length;

        for (int cursor = 0; cursor < N; cursor++) {
            System.out.print(a[cursor] + " ");
        }
    }

    public static void main(String[] args) {
        String[] a = new String[]{"R", "B", "W", "W", "R", "W", "B", "R", "R", "W", "B", "R"};

        sort(a);

        printItems(a);
    }
}

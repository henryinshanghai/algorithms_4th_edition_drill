package com.henry.sort_chapter_02.primary_algorithm_01.selection_sort_01;

import edu.princeton.cs.algs4.In;

// for current spot, select minItem from rest items to fill it out.
public class SelectionSortDrill {

    public static void sort(Comparable[] a) {
        int itemAmount = a.length;

        for (int currentCursor = 0; currentCursor < itemAmount; currentCursor++) {
            int cursorToMinItem = currentCursor;

            for (int dynamicCursor = currentCursor+1; dynamicCursor < itemAmount; dynamicCursor++) {
                if (less(a[dynamicCursor], a[cursorToMinItem])) {
                    cursorToMinItem = dynamicCursor;
                }
            }

            exch(a, currentCursor, cursorToMinItem);
        }
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0 ? true : false;
    }

    public static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {
        String[] a = In.readStrings();
//        String[] a = new String[] {"S", "E", "L", "E", "C", "T", "I", "O", "N", "S", "O", "R", "T"};
//        String[] a = new String[] {"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);

        show(a);
    }
}

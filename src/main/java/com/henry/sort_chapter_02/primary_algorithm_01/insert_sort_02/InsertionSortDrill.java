package com.henry.sort_chapter_02.primary_algorithm_01.insert_sort_02;

import edu.princeton.cs.algs4.In;

// for current item, insert it into the 'sorted zone'.
public class InsertionSortDrill {

    public static void sort(Comparable[] a) {
        int itemAmount = a.length;

        for (int cursorOfItemToInsert = 1; cursorOfItemToInsert < itemAmount; cursorOfItemToInsert++) {

            // 检测布尔表达式的值。如果为 true，循环体被执行。如果为false，循环终止
            for (int backwardsCursor = cursorOfItemToInsert;
                 backwardsCursor > 0 && less(a[backwardsCursor], a[backwardsCursor - 1]) ; backwardsCursor--) {
                exch(a, backwardsCursor, backwardsCursor - 1);
            }
        }
    }

    public static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    public static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void show(Comparable[] a) {
        for (int cursor = 0; cursor < a.length; cursor++) {
            System.out.print(a[cursor] + " ");
        }
    }
    public static void main(String[] args) {
        String[] a = In.readStrings();

        sort(a);
        show(a);
    }
}

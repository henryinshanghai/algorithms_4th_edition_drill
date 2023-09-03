package com.henry.sort_chapter_02.primary_algorithm_01.shell_sort_03;

public class ShellSortDrill02 {
    public static void main(String[] args) {
        Comparable[] a = {"S", "H", "E", "L", "L", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E",};

        sort(a);

        printItems(a);
    }

    private static void printItems(Comparable[] a) {
        for (Comparable item : a) {
            System.out.print(item + " ");
        }
    }

    private static void sort(Comparable[] a) {

        int itemAmount = a.length;
        int blockSize = 1;
        while (blockSize < itemAmount / 3) {
            blockSize = blockSize * 3 + 1;
        }

        while (blockSize >= 1) {
            int startPointOfDisorder = blockSize;
            for (int anchorOfItemToInsert = startPointOfDisorder; anchorOfItemToInsert < itemAmount; anchorOfItemToInsert++) {
                for (int backwardCursor = anchorOfItemToInsert; backwardCursor >= blockSize; backwardCursor -= blockSize) {
                    if (less(a[backwardCursor], a[backwardCursor - blockSize])) {
                        exch(a, backwardCursor, backwardCursor - blockSize);
                    }
                }
            }

            blockSize /= 3;
        }

    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }
}

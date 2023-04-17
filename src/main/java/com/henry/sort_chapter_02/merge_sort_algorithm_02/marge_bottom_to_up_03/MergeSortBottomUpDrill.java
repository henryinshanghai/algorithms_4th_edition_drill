package com.henry.sort_chapter_02.merge_sort_algorithm_02.marge_bottom_to_up_03;

public class MergeSortBottomUpDrill {
    private static Comparable[] aux;

    public static void sort(Comparable[] a) {
        int itemAmount = a.length;
        aux = new Comparable[itemAmount];

        // update current Pair to the end
        for (int itemAmountOfSubGroup = 1; itemAmountOfSubGroup < itemAmount; itemAmountOfSubGroup = itemAmountOfSubGroup * 2) {
            // merge current Pair
            for (int leftBarCursor = 0; leftBarCursor < itemAmount - itemAmountOfSubGroup; leftBarCursor += (itemAmountOfSubGroup * 2)) {
                merge(a, leftBarCursor, leftBarCursor + itemAmountOfSubGroup - 1,
                        Math.min(((leftBarCursor + itemAmountOfSubGroup * 2) - 1), itemAmount - 1));
            }
        }
    }

    private static void merge(Comparable[] a, int leftBar, int middle, int rightBar) {
        // 1
        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            aux[cursor] = a[cursor];
        }

        // 2
        int leftHalfCursor = leftBar;
        int rightHalfCursor = middle + 1;

        // 3 🐖 这里比较的是 aux辅助数组中的元素，得到较小元素
        for (int cursor = leftBar; cursor <= rightBar; cursor++) {
            if (leftHalfCursor > middle) a[cursor] = aux[rightHalfCursor++];
            else if (rightHalfCursor > rightBar) a[cursor] = aux[leftHalfCursor++];
            else if (less(aux[leftHalfCursor], aux[rightHalfCursor])) a[cursor] = aux[leftHalfCursor++];
            else a[cursor] = aux[rightHalfCursor++];
        }
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void show(Comparable[] a) {
        for (int cursor = 0; cursor < a.length; cursor++) {
            System.out.print(a[cursor] + " ");
        }
    }

    public static void main(String[] args) {
        String[] a = {"M", "E", "R", "G", "E", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);
        show(a);
    }
}

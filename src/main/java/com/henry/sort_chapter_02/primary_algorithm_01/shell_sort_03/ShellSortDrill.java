package com.henry.sort_chapter_02.primary_algorithm_01.shell_sort_03;

public class ShellSortDrill {

    public static void sort(Comparable[] a) {
        int itemAmount = a.length;

        // 计算一个较大的N值
        int itemAmountOfSubGroup = 1;
        while (itemAmountOfSubGroup < itemAmount / 3) {
            itemAmountOfSubGroup = itemAmountOfSubGroup * 3 + 1;
        }

        // 获取“完全排序的数组”
        while (itemAmountOfSubGroup >= 1) {
            // 获取“分隔有序的元素序列”
            int startPointOfDisorder = itemAmountOfSubGroup;
            for (int cursorOfItemToInsert = startPointOfDisorder; cursorOfItemToInsert < itemAmount; cursorOfItemToInsert++) {
                // 把当前待插入元素 插入到序列中
                for (int backwardsCursor = cursorOfItemToInsert;
                     backwardsCursor >= startPointOfDisorder && less(a[backwardsCursor], a[backwardsCursor - itemAmountOfSubGroup]);
                     backwardsCursor-=itemAmountOfSubGroup) {
                    exch(a, backwardsCursor, backwardsCursor - itemAmountOfSubGroup);
                }
            }

            itemAmountOfSubGroup = itemAmountOfSubGroup / 3;
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
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {
        String[] a = {"S", "H", "E", "L", "L", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);
        show(a);
    }
}

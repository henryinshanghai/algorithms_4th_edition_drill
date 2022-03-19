package com.henry.sort_chapter_02.primary_algorithm_01.selection_sort_01;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/*
    选择排序算法描述：逐一排定数组中的元素 ———— 找到/选择到未排定区间中的最小元素，并排定它
    有意义的变量名：
        定锚指针 - anchorCursor
        游标指针 - dynamicCursor
        指向最小元素的指针 - cursorToMinItem
        数组元素数量 - itemAmount
 */
public class SelectionSort_round2_drill01 {

    public static void sort(Comparable[] a) {
        int itemAmount = a.length;

        for (int anchorCursor = 0; anchorCursor < itemAmount; anchorCursor++) {
            int cursorToMinItem = anchorCursor;

            for (int dynamicCursor = anchorCursor+1; dynamicCursor < itemAmount; dynamicCursor++) {
                if (less(a[dynamicCursor], a[cursorToMinItem])) {
                    cursorToMinItem = dynamicCursor;
                }
            }

            exch(a, anchorCursor, cursorToMinItem);
            assert isSorted(a, 0, anchorCursor);
        }

        assert isSorted(a);
    }

    private static boolean isSorted(Comparable[] a) {
        return isSorted(a, 0, a.length);
    }

    private static boolean isSorted(Comparable[] a, int leftBar, int rightBar) {
        for (int cursor = leftBar+1; cursor < rightBar; cursor++) {
            if (less(a[cursor], a[cursor - 1])) return false;
        }

        return true;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(Comparable v1, Comparable v2) {
        return v1.compareTo(v2) < 0;
    }

    private static void show(Comparable[] a){
        // 在单行中打印数组
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // 从标准输入中读取字符串，然后把它们排序输出
        String[] a = In.readStrings();
//        String[] a = new String[]{"E", "E", "G", "M", "R", "A", "C", "E", "R", "T"}; // 10
//        String[] a = new String[]{"S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"}; // 10

        sort(a);

        show(a);
    }
}

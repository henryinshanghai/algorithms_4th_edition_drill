package com.henry.sort_chapter_02.merge_sort_algorithm_02.marge_down_to_top_03;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class MergeBU {

    private static Comparable[] aux; // 辅助数组

    public static void sort(Comparable[] a){
        // 进行lgN次两两合并
        int N = a.length;
        aux = new Comparable[N];
        for (int sz = 1; sz < N; sz = sz + sz) { // sz 子数组的大小
            for (int lo = 0; lo < N - sz; lo+= sz+sz) { // lo：子数组的索引
                merge(a, lo, lo + sz + 1, Math.min(lo + sz + sz - 1, N - 1));
            }
        }
    }
    // todo beyond my sh*t!
    /**
     * 通过归并实现数组元素排序
     * @param a
     * @param lo
     * @param mid
     * @param hi
     */
    private static void merge(Comparable[] a, int lo, int mid, int hi) {
        int i = lo;
        int j = mid + 1;

        for (int k = 0; k < a.length; k++) {
            aux[k] = a[k];
        }

        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if(less(aux[i], aux[j])){
                a[k] = aux[i++];
            } else {
                a[k] = aux[j++];
            }

        }
    }


    @SuppressWarnings("unchecked")
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /**
     * 交换i、j这两个位置的元素
     * @param a
     * @param i
     * @param j
     */
    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void show(Comparable[] a){
        // 在单行中打印数组
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        System.out.println();
    }

    public static boolean isSorted(Comparable[] a){
        // 测试数组中的元素是否有序
        for (int i = 0; i < a.length; i++) {
            if (less(a[i], a[i - 1])) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // 从标准输入中读取字符串，然后把它们排序输出
        String[] a = In.readStrings();
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}

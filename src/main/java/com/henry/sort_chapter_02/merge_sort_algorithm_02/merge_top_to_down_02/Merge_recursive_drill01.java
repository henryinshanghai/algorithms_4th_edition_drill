package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_top_to_down_02;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Merge_recursive_drill01 {
    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a, int lo, int hi){
        // 1 声明递归调用的基线条件
        if (hi <= lo) {
            return;
        }

        // 2 更新方法的参数
        int mid = lo + (hi - lo)/2; // mid = (a.length -1) / 2

        // 3 调用方法，实现功能：对数组的半边进行排序
        sort(a, lo, mid);
        sort(a, mid + 1, hi);

        // 4 对排序好的子数组进行归并
        merge(a, lo, mid, hi); // ② 调用时，传入的是左边数组的最后一个元素的索引
    }

    /**
     * 对数组中的元素进行排序
     * 手段：1 借助aux辅助数组； 2 分成两半，比较&绑定
     * @param a
     * @param lo
     * @param mid
     * @param hi
     */
    private static void merge(Comparable[] a, int lo, int mid, int hi) {
        int i = lo;
        int j = mid + 1; // ①
        int N = a.length;
        Comparable[] aux = new Comparable[N];
        
        // 1 拷贝所有元素到aux数组中
        for (int k = 0; k < N; k++) {
            aux[k] = a[k];
        }
        
        // 2 为a[]数组的所有位置绑定正确的数据
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else if (less(aux[i], aux[j])) {
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
        String[] a = new String[]{"E", "E", "G", "M", "R", "A", "C", "E", "R", "T"}; // 10
        sort(a, 0, a.length-1);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}

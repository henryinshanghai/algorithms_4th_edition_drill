package com.henry.sort_chapter_02.merge_sort_algorithm_02.marge_down_to_top_03;

import edu.princeton.cs.algs4.StdOut;

public class MergeBU_drill03 {
    private static Comparable[] aux;

    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        int N = a.length;

        for (int unit = 1; unit < N; unit = unit * 2) { // 循环终止条件几乎不会影响到功能，只会多一些无效的循环
            // 内循环的作用：对划分得到的所有子数组进行两两归并
            for (int  lo = 0;  lo < (N-unit); lo += 2*unit) { // 在数组中移动数据区间     循环终止条件： (mid+1) > N  这是什么？
                int mid = lo + unit - 1;
                int hi = Math.min((lo + 2 * unit - 1), N-1);
                merge(a, lo, mid, hi);
            }
        }
    }

    public static void merge(Comparable[] a, int lo, int mid, int hi) { // 没有递归调用方法的话，值不会时时变化，也就不需要作为方法参数了
        int N = a.length;
        aux = new Comparable[N];

        int i = lo;
        int j = mid + 1;

        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

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
//        String[] a = In.readStrings();
        String[] a = new String[]{"E", "E", "G", "M", "R", "A", "C", "E", "R", "T"}; // 10
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}

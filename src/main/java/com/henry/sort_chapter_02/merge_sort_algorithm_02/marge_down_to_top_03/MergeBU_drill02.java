package com.henry.sort_chapter_02.merge_sort_algorithm_02.marge_down_to_top_03;

import edu.princeton.cs.algs4.StdOut;

public class MergeBU_drill02 {
    private static Comparable[] aux;

    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        int N = a.length;
        // 遍历数组
        for (int unit = 1; unit < N; unit = unit*2) { // 1 先确定初始值； 2 再确定步长（用于数组内跳转） 3 最后决定终止条件
            // 内层循环：对数组划分出的子数组顺序地执行合并操作
            // (N-1) = lo + 2*unit -1
            for (int lo = 0; lo < N - 2 * unit; lo += 2*unit) {
                int mid = lo + unit - 1;
                int hi = Math.min((lo + 2 * unit - 1), N-1);

                // 执行merge操作
                merge(a, lo, mid, hi);
            }
        }

    }

    /**
     * 对数组元素进行排序
     * @param a
     * @param lo
     * @param mid
     * @param hi
     */
    private static void merge(Comparable[] a, int lo, int mid, int hi) {
        int i = lo;
        int j = mid + 1;
        int N = a.length;
        aux = new Comparable[N];

        for (int k = 0; k < N; k++) {
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

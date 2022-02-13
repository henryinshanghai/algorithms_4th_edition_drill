package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_01;

import edu.princeton.cs.algs4.StdOut;

public class Merge_drill04 {
    private static Comparable[] aux;

    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        // 参考具体的算法实现
    }

    /**
     * 这里的参数mid其实可以在方法内部进行计算，之所以作为方法参数，可能是为了把风险交给用户
     * @param a
     * @param lo
     * @param hi
     */
    public static void merge(Comparable[] a, int lo,int hi){ // 参数很多  int mid,
        int N = a.length;
        int mid = (N - 1) / 2;
        int i = lo;
        int j = mid + 1;

        aux = new Comparable[N];

        for (int k = 0; k < N; k++) {
            aux[k] = a[k];
        }

        for (int k = 0; k < N; k++) {
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
//        sort(a);
        merge(a, 0, a.length-1); // (a.length-1)/2,


        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}

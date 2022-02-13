package com.henry.sort_chapter_02.merge_sort_algorithm_02.merge_top_to_down_02;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import javax.xml.soap.Node;

public class Merge_recursive_drill02 {
    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a, int lo, int hi){
        if (hi <= lo) {
            return; // 返回值类型为void，也可以使用return
        }

        // 更新方法调用时传入地参数
        int mid = lo + (hi - lo) / 2;

        // 调用sort()方法，完成子数组的排序
        sort(a, lo, mid);
        sort(a, mid + 1, hi);

        // 调用merge()方法，完成有序子数组的合并
        merge(a, lo, mid, hi);
    }

    /**
     * 通过合并的方式对数组进行排序
     * @param a
     * @param lo
     * @param mid
     * @param hi
     */
    private static void merge(Comparable[] a, int lo, int mid, int hi) {
        // 定义指针
        int i = lo;
        int j = mid+1;
        int N = a.length;
        Comparable[] aux = new Comparable[N];

        // 把原始数组拷贝到aux数组
        for (int k = 0; k < N; k++) {
            aux[k] = a[k];
        }

        // 为原始数组的每个位置绑定正确的数据
        for (int k = lo; k <= hi; k++) { // 这里的区间改成[0, N)就会有下标越界的异常 WHY? no clue todo
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
        sort(a, 0, a.length - 1); // 统一使用索引，不会导致下标越界

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}

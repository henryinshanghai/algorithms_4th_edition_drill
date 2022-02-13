package com.henry.sort_chapter_02.primary_algorithm_01.shell_sort_03;

import edu.princeton.cs.algs4.StdOut;

public class ShellSort_03_drill03 {
    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        // 希尔排序
        int N = a.length;
        int h = 1;

        // 1 创建h值序列
        while (h < N / 3) {  // 这里如果h太大的话，就没有把数组进行有效的分组 所以 N/3
            h = 3 * h + 1;
        }

        // 2 对于当前的h - 对 当前数组 进行h排序（aka 从任意元素开始，每h个元素所组成的元素序列要求是有序的）
        // 使用h把原始数组分成h个子数组
        while (h >= 1) {
            /* 使用插入排序 来 对当前h得到的每一个子数组进行排序 */
            // 手段：对于每一个当前指针，维持当前指针左侧的子数组的有序性 - 目的是“h更小时，子数组能够局部有序”
            for (int i = h; i < N; i++) {
                // 这里的代码pattern与插入排序完全相同 但是 比较、交换、更新的step都是h 而不是1
                for (int j = i; (j>=h) && less(a[j], a[j-h]); j -= h) { // 在数组中移动区间
                    exch(a, j, j - h);
                }
            }

             // 循环结束后，当前的数组就已经是 h有序的了

            // 更新h的值，对原始数组进行粒度的划分
            h = h / 3;
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
        String[] a = new String[]{"S", "H", "E", "L", "L", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"}; // 10
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}

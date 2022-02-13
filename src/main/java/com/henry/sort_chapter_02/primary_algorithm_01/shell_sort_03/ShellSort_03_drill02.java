package com.henry.sort_chapter_02.primary_algorithm_01.shell_sort_03;

import edu.princeton.cs.algs4.StdOut;

public class ShellSort_03_drill02 {
    /**
     * 对数组中的元素进行排序
     *
     * @param a
     */
    public static void sort(Comparable[] a) {
        // 希尔排序 一组一组地处理（相邻的下一个是另一组了）
        // 1 生成h值的序列,并更新h到最大值
        int N = a.length;
        int h = 1;
        while (h < N / 3) {
            h = 3 * h + 1;
        }

        while (h >= 1) {
            for (int i = h; i < N; i++) { // 外循环    作用：遍历数组中所有的当前元素[h, N-1]     为什么从h开始？ 因为所有组中的第一个元素是不需要排序的
                // 内循环      作用：对当前元素所在的组进行排序    手段：比较并交换当前子数组中的元素
                for (int j = i; (j >= h) && less(a[j], a[j - h]); j = j - h) {
                    exch(a, j, j - h);
                }
            }

            // 两层循环结束后，所有的子组都已经排序完成了
            // 更细致地分组，然后进入下一次循环继续对子组进行排序
            h = h / 3;
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    /**
     * 交换i、j这两个位置的元素
     *
     * @param a
     * @param i
     * @param j
     */
    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void show(Comparable[] a) {
        // 在单行中打印数组
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        System.out.println();
    }

    public static boolean isSorted(Comparable[] a) {
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
        String[] a = new String[]{"S", "H", "E", "L", "L", "S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E"};
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}

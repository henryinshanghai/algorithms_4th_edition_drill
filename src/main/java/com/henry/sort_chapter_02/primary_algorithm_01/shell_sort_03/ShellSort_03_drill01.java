package com.henry.sort_chapter_02.primary_algorithm_01.shell_sort_03;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class ShellSort_03_drill01 {
    /**
     * 对数组中的元素进行排序
     * @param a
     */
    public static void sort(Comparable[] a){
        // 希尔排序
        int N = a.length;
        int h = 1;
        while (h < N/3) {
            h = 3 * h + 1; // 根据N生成一个h的值序列
        }

        // 在循环中把h的值逐步更新到1
        while (h >= 1) {
            // 把数组分成h个子组    这里并不是每个组逐一操作
            for (int i = h; i < N; i++) { // 外层循环执行（N-h）趟  索引从h开始 为什么？    i++把流程往后移动(操作下一组)
                // 内循环的作用：把当前元素a[j]插入到有序区中      注：随着i的增加，这里会处理不同组的当前元素&有序区
                for (int j = i; j >= h && less(a[j], a[j-h]); j -= h) { // j -= h 把比较&交换的操作向前移动
                    exch(a, j, j-h); // 开始时j的值=i，当i的值越大时，j -= h就可能把比较&交换的操作向前移动     这是合乎常理的，i越大，意味着元素在数组中的位置越靠后
                }
            }

            // 更新h的值 作用：更细粒度地划分数组为子数据序列
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
        String[] a = In.readStrings();
        sort(a);

        // 断言数组元素已经有序了
        assert isSorted(a);
        show(a);
    }
}
// not fully understand
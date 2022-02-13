package com.henry.sort_chapter_02.priority_queue_04.pq_sort_05;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * 使用优先队列来对一个数组进行排序操作
 * 特征：在原始数组上面倒腾就行，不需要额外的空间
 */
public class HeapSort_drill01 {
    // 不需要任何实例变量    说明：一旦自定义类中需要实例变量，这就需要一定的内存空间。所以最好让使用者传入，原地修改

    public static void sort(Comparable[] pq) {
        int n = pq.length;

        /*
            phrase1 构建堆
            手段：使用sink()操作来实现
         */
        for (int i = n/2; i >=1 ; i--) {
            sink(pq, i, n);
        }

        /*
            phase2: 下沉排序
            手段：同样使用sink()操作来实现
         */
        while (n > 1) {
            exch(pq, 1, n--); //方法中对参数进行了--操作，所以这里可以使用n--而不用担心下标越界
            sink(pq, 1, n);
        }
    }

    /**
     * 对指定数组的指定位置上的节点执行“下沉”操作
     * 说明：这里之所以需要n作为参数，是因为没有把它维护成一个实例变量
     * @param pq
     * @param i
     * @param n
     */
    private static void sink(Comparable[] pq, int i, int n) { // 当前节点的值小于子节点中较大者
        while (2*i+1 <= n) { // 2*i+1 <= n
            int j = 2 * i;
            if (less(pq, j, j+1)) j = j + 1;

            if (!less(pq, i, j)) break;

            // 交换
            exch(pq, i, j);

            // 更新
            i = j;
        }
    }

    private static void exch(Comparable[] pq, int i, int j) {
        Comparable temp = pq[i - 1];
        pq[i - 1] = pq[j - 1];
        pq[j - 1] = temp;
    }

    @SuppressWarnings("unchecked")
    private static boolean less(Comparable[] pq, int i, int j) {
        return pq[i - 1].compareTo(pq[j - 1]) < 0;
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        HeapSort_drill01.sort(a);

        show(a);
    }

    private static void show(String[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.println(a[i]);
        }
    }
}
/*
这里使用n = pq.length; 导致了调用时的很多不确定性

1 传入参数时，n可以大一些，因为方法内部会对参数进行-1操作

为什么要进行减一操作？因为直接使用pq.length作为N时，会比之前的N（元素个数）大1————第0个位置我们时不存储元素的
 */

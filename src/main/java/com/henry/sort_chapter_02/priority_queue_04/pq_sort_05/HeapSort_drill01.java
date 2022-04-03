package com.henry.sort_chapter_02.priority_queue_04.pq_sort_05;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * 使用优先队列来对一个数组进行排序操作
 * 特征：在原始数组上面倒腾就行，不需要额外的空间
 */
public class HeapSort_drill01 {
    /* 对Client传入的数组进行原地排序，因此不需要任何实例变量 */

    /* 对client传入的数组进行原地排序 */
    public static void sort(Comparable[] arrayToSort) {
        int itemAmount = arrayToSort.length;

        // phrase1 从底往上地构建堆
        for (int currentRootSpot = itemAmount / 2; currentRootSpot >= 1; currentRootSpot--) {
            sink(arrayToSort, currentRootSpot, itemAmount);
        }

        // phase2: 从最大元素开始，逐个排定元素
        /*
            1 把最大堆的堆顶元素 交换到 末尾 - 排定最大元素；
            2 重新生成最大堆(排除掉既有的最大元素)；
            重复步骤1, 2 直到所有元素被排定
         */
        while (itemAmount > 1) {
            exch(arrayToSort, 1, itemAmount--);
            // sink(1)的操作可以使得堆有序 - 因为交换节点时，交换上来的是较大的子节点
            sink(arrayToSort, 1, itemAmount);
        }
    }

    /**
     * 对当前堆中的指定节点执行 "下沉操作"
     * 说明：这里之所以需要 arrayToSort, n作为参数，是因为没有把它维护成一个实例变量
     * 特征：下沉的次数不确定，这里使用一个while()循环
     * @param arrayToSort
     * @param currentSpotInHeap
     * @param lastSpotInHeap
     */
    private static void sink(Comparable[] arrayToSort, int currentSpotInHeap, int lastSpotInHeap) { // 当前节点的值小于子节点中较大者
        while (2 * currentSpotInHeap + 1 <= lastSpotInHeap) { // 2*currentSpotInHeap+1 <= lastSpotInHeap
            // 1 找到当前节点的较大子节点
            int biggerChildSpot = 2 * currentSpotInHeap;
            if (less(arrayToSort, biggerChildSpot, biggerChildSpot + 1)) biggerChildSpot = biggerChildSpot + 1;

            // 4 在执行交换之前，如果当前节点 在堆中已经被排定 - 跳出循环
            if (!less(arrayToSort, currentSpotInHeap, biggerChildSpot)) break;

            // 2 交换 堆中两个位置上的元素
            exch(arrayToSort, currentSpotInHeap, biggerChildSpot);

            // 3 更新 当前的位置
            currentSpotInHeap = biggerChildSpot;
        }
    }

    // 交换堆中位置i 与 位置j上的堆元素
    private static void exch(Comparable[] arrayToSort, int spotIInHeap, int spotJInHeap) {
        Comparable temp = arrayToSort[spotIInHeap - 1];
        arrayToSort[spotIInHeap - 1] = arrayToSort[spotJInHeap - 1];
        arrayToSort[spotJInHeap - 1] = temp;
    }

    // 比较堆中位置i 与 位置j上的堆元素
    @SuppressWarnings("unchecked")
    private static boolean less(Comparable[] arrayToSort, int spotIInHeap, int spotJInHeap) {
        // array[x] -> heap(x+1)
        // spotInArray = spotInHeap - 1
        return arrayToSort[spotIInHeap - 1].compareTo(arrayToSort[spotJInHeap - 1]) < 0;
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        HeapSort_drill01.sort(a);
        show(a);
    }

    private static void show(String[] a) {
        for (int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
    }
}

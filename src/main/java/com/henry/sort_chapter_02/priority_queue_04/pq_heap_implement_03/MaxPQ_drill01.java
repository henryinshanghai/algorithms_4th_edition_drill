package com.henry.sort_chapter_02.priority_queue_04.pq_heap_implement_03;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * 实现优先队列；
 * 手段：使用堆来实现；
 * 优先队列的最大值即为最大堆的堆顶节点
 *
 * @param <Key>
 */
public class MaxPQ_drill01<Key extends Comparable<Key>> {
    private Key[] pq; // 一个Key类型的数组     作用：用于表示一个堆逻辑结构；
    private int N;

    public MaxPQ_drill01(int capacity) {
        pq = (Key[]) new Comparable[capacity + 1];
        N = 0;
    }

    // API
    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    /**
     * 比较两个位置上的元素大小
     *
     * @param i
     * @param j
     * @return
     */
    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) < 0;
    }

    /**
     * 交换两个位置上的元素
     */
    private void exch(int i, int j) {
        Key temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    // 核心api

    /**
     * 向优先队列中插入元素
     *
     * @param item
     */
    public void insert(Key item) {
        // 1 把元素插入数组尾部————用数组表示一个堆
        pq[++N] = item; // Mark: 这个实现中没有使用数组下标为0的位置
        // 2 上浮以恢复二叉堆的平衡
        swim(N);
    }

    /**
     * 从优先队列中删除最大元素
     * 说明：最大堆的节点元素就是数组的第一个元素
     * 手段SOP:1 把最后一个子节点 与 根节点进行交换； 2 对根节点执行下沉操作（以维护堆的平衡）； 3 返回所删除的最大节点的值
     *
     * @return
     */
    public Key delMax() {
        Key maxItem = pq[1];
        // 调整堆的平衡
        exch(1, N--);
//        N--; // 代码可以合并到上一行
        sink(1);
//        N--; // Mark N--写在这里会导致结果不正确...   WHY？ 因为：1 N--是删除元素的操作 2 删除元素的操作需要在下沉操作之前完成
        return maxItem;
    }

    /**
     * 对指定位置的节点执行下沉操作
     *
     * @param k
     */
    private void sink(int k) {
        // 比较 & 交换
        while (2 * k <= N) { // 极端情况：2k+1 = N 2k+1<=N
            /* 1 比较当前节点 与 当前节点的子节点中的较大者 （当前节点应该更大）*/
            // 1-1 获取到子节点中的较大节点
            int j = 2 * k;
            if (j < N && less(j, j + 1)) {
                j++;
            }

            // ？？？ what is this means? understood
            // 比较当前节点 与 子节点 如果当前节点更大的话，循环交换的过程结束 break
            if (!less(k, j)) {
                break;
            }

            // 1-2 交换当前节点 与 子节点中的较大者
            exch(k, j);

            // 使用子节点来更新当前节点的指针
            k = j;
        }
    }

    /**
     * 对二叉堆中指定位置的节点执行上浮操作
     * 说明：堆是使用数组来实现的，所以可以直接使用公式来表示父节点与子节点之间的关系
     *
     * @param k
     */
    private void swim(int k) {
        // 比较 & 交换
        // 比较当前节点 与 当前节点的父节点
        // 如果父节点竟然更小（注：最大堆中父节点应该更大）
        while (k > 1 && less(k / 2, k)) { // 极端情况：k/2 >= 1  k >= 2
            // 交换父节点与当前节点
            exch(k / 2, k);

            // 更新k 进入下一轮判断
            k = k / 2;
        }
    }

    public static void main(String[] args) {
        MaxPQ_drill01<String> pq = new MaxPQ_drill01<>(10);

//        while (!StdIn.isEmpty()) {
//            String item = StdIn.readString();
//            if (!item.equals("-")) pq.insert(item);
//            else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
//        }
//        StdOut.println("(" + pq.size() + " left on pq)");

        pq.insert("this");
        pq.insert("is");
        pq.insert("a");
        pq.insert("test");

        while (!pq.isEmpty()) {
            System.out.println(pq.delMax());
        }
    }
}
// 启示：Mark N--写在这里会导致结果不正确...   WHY？ 因为：1 N--是删除元素的操作 2 删除元素的操作需要在下沉操作之前完成
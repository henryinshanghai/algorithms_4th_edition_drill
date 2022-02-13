package com.henry.sort_chapter_02.priority_queue_04.pq_heap_implement_03;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class MaxPQFromBook<Key extends Comparable<Key>> {
    private Key[] pq;
    private int N = 0;

    public MaxPQFromBook(int maxN) {
        pq = (Key[]) new Comparable[maxN + 1];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    // 核心APIs

    /**
     * 向优先队列中插入一个指定的元素
     *
     * @param item
     */
    public void insert(Key item) {
        pq[++N] = item; // N=队列中的元素个数 需要的索引是N+1 使用++N能够一步到位
        swim(N); // 参数：元素的索引 aka 数组的最后一个位置
    }

    /**
     * 从优先队列中删除最大的元素
     */
    public Key delMax() {
        Key maxItem = pq[1];

        // 恢复二叉堆的平衡
        // 1 交换顶点位置的元素与最后一个位置的元素
        exch(1, N);
        // 2 对顶点位置的元素执行下沉操作————这会恢复二叉堆的平衡
        sink(1);
        return maxItem;
    }

    /**
     * 对指定位置的元素执行下沉操作
     *
     * @param k
     */
    private void sink(int k) {
        // 比较&交换
        // 编写过程优化
//        int j = 2 * k;
//        if (less(j, j + 1)) {
//            j = j + 1;
//        }

//        while(k<N && less(k, j)){ // 比较合格：父节点的值小于子节点中的较大者
//            // 执行交换
//            exch(k, j);
//
//            // 更新当前节点 计算子节点的步骤也需要同步更新   为了能够同时对“计算子节点”的过程也进行更新————需要把计算过程放到循环中
//            // 但这么做第一次计算j的过程就没有了    怎么办？把less(k,j)放到循环中，放在计算j值步骤的后面
//            k = j;
//        }

        while (2 * k <= N) { // 极端情况：2k=N 所以k<=N/2
            // 计算j/更新j
            int j = 2 * k;
            if (less(j, j + 1)) {
                j++;
            }

            // 编写过程优化 这里的if/else可以优化成if(!xxx)的形式
//            if (less(k, j)) {
//                exch(k, j);
//            } else { // 如果当前节点的值并没有小于它最大的子节点...
//                // 就不需要进行交换 后继过程也不需要了
//                break;
//            }
            if (!less(k, j)) { // 把特殊的情况放在前面写...
                break;
            }
            exch(k, j); // 省略了else的语句

            // 更新k
            k = j;
        }
    }


    /**
     * 对当前节点进行上浮操作；
     * 原因：当前节点大于它的父节点
     *
     * @param k
     */
    private void swim(int k) {
        while (k > 1 && less(k / 2, k)) { // 极端情况：上浮到二叉堆的顶点处
            // 由于数组的第一个位置是不使用的，所以位置最小的元素是a[1] 循环中k不能取到1，否则就会有a[0]的引用出现
//            if (less(k/2, k)) { // 直接把这个条件放在循环条件中，能够避免无效的循环
//                exch(k / 2, k);
//            }
            exch(k / 2, k);
            k = k / 2;
        }
    }

    /**
     * 交换数组中两个位置上的元素
     *
     * @param i
     * @param k
     */
    private void exch(int i, int k) {
        Key temp = pq[i];
        pq[i] = pq[k];
        pq[k] = temp;
    }

    /**
     * 比较数组中两个位置上的元素
     * @param i
     * @param k
     * @return
     */
    private boolean less(int i, int k) {
        return pq[i].compareTo(pq[k]) < 0;
    }

    public static void main(String[] args) {
        MaxPQFromWebsite<String> pq = new MaxPQFromWebsite<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
        }
        StdOut.println("(" + pq.size() + " left on pq)");
    }
}

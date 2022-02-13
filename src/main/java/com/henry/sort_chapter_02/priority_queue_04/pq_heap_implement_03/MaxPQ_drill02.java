package com.henry.sort_chapter_02.priority_queue_04.pq_heap_implement_03;

/**
 * 使用二叉堆逻辑结构来实现优先队列
 * 核心API：1 插入操作； 2 删除最大值；
 * 特征：1 第一个节点总是最大值/最小值； 2 父节点与子节点之间存在明确的大小关系； 3 每次添加一个新的元素，优先队列都能够保持1、2这两点特性
 */
public class MaxPQ_drill02<Key extends Comparable<Key>> {
    private Key[] pq;
    private int N;

    public MaxPQ_drill02(int capacity) {
        pq = (Key[]) new Comparable[capacity + 1]; // 因为pq[0]不会用来存储元素
        N = 0;
    }

    // API
    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) < 0;
    }

    private void exch(int i, int j) {
        Key temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    // 核心API
    public void insert(Key item) {
        pq[++N] = item;
        // 调整二叉堆的平衡
        swim(N);
    }

    /**
     * 对指定位置的节点进行上浮操作
     * 比较 & 交换
     *
     * @param k
     */
    private void swim(int k) {
        while (k >= 2 && less(k / 2, k)) { // 比较当前节点与父节点 极端情况：k/2 = 1   k/2 >= 1 k >=2
            exch(k, k/2);
            k = k / 2; // 更新当前节点
        }
    }

    // 核心API
    /**
     * 删除优先队列中最大的元素：数组中的第一个元素
     * @return
     */
    public Key delMax(){
        Key maxItem = pq[1];
        // 交换数组中的第一个元素与最后一个元素   &  删除最后一个元素
        exch(1, N--);
        // 下沉元素到适当位置
        sink(1);
        return maxItem;
    }

    /**
     * 对指定位置的节点执行下沉操作
     * @param k
     */
    private void sink(int k) {
        // 比较 & 交换
        while ((2*k + 1) <= N) { // 极端情况：2k+1 = N N <= 2k+1
            // 获取到当前节点的较大子节点
            int j = 2 * k;
            if (j<N && less(j, j+1)) { // 极端情况： j+1 = N
                j++;
            }

            // 比较 当前节点与子节点
            if (!less(k, j)) {
                break;
            }

            // 交换
            exch(j, k);
            k = j;
        }
    }


    public static void main(String[] args) {
        MaxPQ_drill02<String> pq = new MaxPQ_drill02<>(10);

        pq.insert("this");
        pq.insert("is");
        pq.insert("a");
        pq.insert("test");

        while (!pq.isEmpty()) {
            System.out.println(pq.delMax());
        }
    }
}

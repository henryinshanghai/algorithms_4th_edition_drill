package com.henry.sort_chapter_02.priority_queue_04.pq_indexed_item_implement_04;

import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * 作用：创建存储“索引-值”的优先队列
 * @param <Key>
 */
public class IndexMinPQ_drill02<Key extends Comparable<Key>>{
    // 实例变量
    private Key[] keys; // 存储索引关联的值
    private int[] pq; // 存储索引
    private int[] qp; // 存储索引在pq[]中的位置
    private int n; // 表示索引-值优先队列中的元素个数
    private int maxN; // 表示索引-值优先队列的容量

    // 构造方法
    public IndexMinPQ_drill02(int maxN) {
        this.maxN = maxN;
        n = 0;
        keys = (Key[])new Comparable[maxN + 1];
        pq = new int[maxN + 1];
        qp = new int[maxN + 1];

        for (int i = 0; i <= maxN; i++) {
            qp[i] = -1; // 表示pq中还没有存储任何索引
        }
    }

    // APIs
    public boolean isEmpty(){
        return n == 0;
    }

    public int size(){
        return n;
    }

    // 核心API

    /**
     * 向索引-值优先队列中插入一个索引-值
     * @param i
     * @param item
     */
    public void insert(int i, Key item) {
        // 判断索引的合法性
        validateIndex(i);

        // 判断优先队列中是不是已经有过这个索引了
        if (contains(i)) throw new IllegalArgumentException("此索引在优先队列中已经存在!");

        // 向pq[]中添加索引
        pq[++n] = i; // Mark:先+1再使用 不使用第一个位置
        // 向qp[]中添加"索引在pq[]中的位置"
        qp[i] = n;
        // 向keys[]中添加"索引关联的值"
        keys[i] = item;

        // 调整pq二叉堆的平衡
        swim(n); // n表示pq[]数组中的位置   二叉堆的所有节点都在pq[1...N-1]
    }

    // 核心APIs
    /**
     * 删除索引优先队列中的最小值，并返回其索引
     */
    public int delMin(){
        // 判断当前队列是不是已经空了
        if (n == 0) throw new NoSuchElementException("队列已经空了！");

        // "索引为1的key[]元素就是最小值" this is wrong. 因为索引是用户指定的
        // 正确表述：Key[]中的最小元素在pq[]数组中的第一个位置
        // 获取最小值所关联的索引
        int minItemsIndex = pq[1];

        // 从pq[]数组中删除这个索引 pq[位置] = 索引值
        exch(1, n--);
        sink(1);

        // 更新各个数组
        keys[minItemsIndex] = null;
        pq[n + 1] = -1;
        qp[minItemsIndex] = -1;
        return minItemsIndex;

    }

    /**
     * 在最小二叉堆中，对指定位置的节点执行“下沉”操作 aka 当前节点大于子节点中的较小者
     * 比较 & 交换 & 更新
     * @param k
     */
    private void sink(int k) {
        while (2*k <= n) { // todo modify this 2k+1 <= n
            int j = 2 * k;
            if(j < n && greater(j , j+1)) j = j + 1;

            if (!greater(k, j)) {
                return;
            }

            exch(k, j);
            k = j;
        }
    }

    /**
     * 在最小二叉堆中,对指定位置的节点执行"上浮"操作 aka 当前节点的值小于父节点的值 aka 父节点更大
     * 核心操作:比较 & 交换 & 更新
     * @param k
     */
    private void swim(int k) {
        while (k >=2 && greater(k / 2, k)) { // 比较的是keys[]数组的元素 k / 2 = 1
            // 交换二叉堆中的两个节点
            exch(k / 2, k);
            // 更新
            k = k / 2;
        }
    }

    /**
     * 交换二叉堆/pq[]优先队列中的两个节点
     * @param i 位置i
     * @param j 位置j
     */
    private void exch(int i, int j) {
        int temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;

        // 交换完成节点后，还需要更新qp[]数组————存储索引在pq[]中的位置i
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    /**
     * 判断keys[]中特定索引所关联的值哪个更大？
     * @param i
     * @param j
     * @return
     */
    private boolean greater(int i, int j) { // 这里的i与j不是索引，而是索引在pq[]中的位置
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    /**
     * 判断队列中是否已经存在指定的索引
     * @param i
     * @return
     */
    private boolean contains(int i) {
        validateIndex(i);
        return qp[i] != -1; // 使用qp[]数组判断
    }

    private void validateIndex(int i) {
        if (i < 0) throw new IllegalArgumentException("索引不能小于0");
        if (i >= maxN) throw new IllegalArgumentException("索引不能大于或等于maxN");
    }

    public static void main(String[] args) {
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

        IndexMinPQ_drill02<String> pq = new IndexMinPQ_drill02<>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // delete and print each key
        while (!pq.isEmpty()) {
            int i = pq.delMin();
            StdOut.println(i + " " + strings[i]);
        }
    }
}
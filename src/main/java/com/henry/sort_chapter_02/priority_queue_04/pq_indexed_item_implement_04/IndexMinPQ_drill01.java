package com.henry.sort_chapter_02.priority_queue_04.pq_indexed_item_implement_04;

import edu.princeton.cs.algs4.StdOut;

import java.util.NoSuchElementException;

/**
 * 作用：存储索引-值的优先队列
 *
 * @param <Key>
 */
public class IndexMinPQ_drill01<Key extends Comparable<Key>> {
    // 实例变量/底层数据结构
    private Key[] keys;
    private int[] pq;
    private int[] qp;
    private int n; // 元素个数
    private int maxN; // 预期容量

    public IndexMinPQ_drill01(int maxN) {
        this.maxN = maxN; // 当方法参数与实例变量名称相同时，使用this.实例变量名称来标识实例变量
        n = 0;

        keys = (Key[]) new Comparable[maxN + 1];
        pq = new int[maxN + 1];
        qp = new int[maxN + 1];

        for (int i = 0; i <= maxN; i++) {
            qp[i] = -1;
        }
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    /**
     * 交换两个位置上的元素
     *
     * @param i
     * @param j
     */
    public void exch(int i, int j) { // 对特定位置（pq[]数组中的位置）上的索引执行交换操作
        int temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;

        // 更新qp[]：存储索引在pq数组中的位置；
        qp[pq[i]] = i; // pq[i]在pq[]中的位置是：1
        qp[pq[j]] = j;
    }

    /**
     * 比较Keys[]数组中两个元素/对象的大小
     *
     * @param i pq[]数组中的元素位置
     * @param j pq[]数组中的元素位置
     * @return
     */
    public boolean greater(int i, int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0; // Mark: 这里把比较符号弄反了
    }

    /**
     * 向包含索引的优先队列中插入索引-值
     */
    public void insert(int i, Key item) {
        // 对索引的校验：合法范围 + 是否已存在 fixme 注：校验很有必要 一会添加
        validateIndex(i);
        if (contains(i)) throw new IllegalArgumentException("index is already in the priority queue");

        // 向索引数组中插入新的索引
        pq[++n] = i;
        // 存储索引在pq中的位置
        qp[i] = n;
        keys[i] = item;

        // 恢复pq[]二叉堆的平衡————方便查找
        swim(n); // 新节点在数组中的位置：数组的末尾
    }

    /**
     * 在顶点最小的二叉堆中，对指定位置的节点执行上游操作————当前节点比起父节点更小
     * 核心：比较 & 交换
     *
     * @param k
     */
    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) { // k/2 = 1  k/2 >=1 k >=2
            exch(k, k / 2);
            k = k / 2;
        }
    }

    // 核心API

    /**
     * 删除包含索引的优先队列中的最小元素
     * 作用：删除Keys[]中的最小值，并返回最小值的索引
     * @return
     */
    public int delMin() {
        if (n == 0) throw new NoSuchElementException("Priority queue underflow");

        int minItemsIndex = pq[1];
        // 更新pq[]数组的二叉堆
        exch(1, n--);
        sink(1);

        assert pq[n + 1] == minItemsIndex; // 相当于在代码中添加了一个断点————如果此处没有符合预期，我就知道前面的代码出错了

        // 更新实例变量
//        pq[n+1] = -1; // pq[索引关联的值排序之后的序号/索引在pq中的位置] = 索引;      索引在pq[]中的位置是通过swim()/sink()来安排的
//        qp[minItemsIndex] = -1; // qp[索引] = 索引在pq中的位置； -1表示pq中没有这个索引
//        keys[minItemsIndex] = null; // keys[索引] = 索引所关联的值

        qp[minItemsIndex] = -1;        // delete
        keys[minItemsIndex] = null;    // to help with garbage collection
        pq[n+1] = -1;        // not needed

        return minItemsIndex;
    }

    /**
     * 对pq[]这个存储索引的最小优先队列中的节点执行下沉操作————当前节点的值大于子结点中的较小者
     * 核心：比较 & 交换 & 更新
     *
     * @param k
     */
    private void sink(int k) {
        while ((2 * k) <= n) { // 2k+1 = n
            // 找出子结点中的较小者
            int j = 2 * k;
            if (j < n && greater(j, j + 1)) j++;

            // 比较当前节点与子结点中的较小者
            if (!greater(k, j)) { // 当前节点如果并没有比其子节点大... 就没有必要进行任何操作 Mark:进行比较的操作都要小心参数的顺序   不然微小的初始错误会导致完全预期之外的结果
                break;
            }

            exch(k, j);
            k = j;
        }
    }

    /**
     * 辅助函数：校验索引值是否合法
     */
    // throw an IllegalArgumentException if i is an invalid index
    private void validateIndex(int i) {
        if (i < 0) throw new IllegalArgumentException("index is negative: " + i); // 索引是可以等于0的
        if (i >= maxN) throw new IllegalArgumentException("index >= capacity: " + i); // 为什么=maxN也会有异常呢？
    }

    /**
     * 确认索引优先队列中是否已经包含指定索引值
     * @param i
     * @return
     */
    public boolean contains(int i) {
        validateIndex(i);
        return qp[i] != -1;
    }

    public static void main(String[] args) {
        // insert a bunch of strings
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

        IndexMinPQ_drill01<String> pq = new IndexMinPQ_drill01<String>(strings.length);
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // delete and print each key
        while (!pq.isEmpty()) {
            int i = pq.delMin();
            StdOut.println(i + " " + strings[i]);
        }
        StdOut.println();

        System.out.println("----------------------");
        // reinsert the same strings
//        for (int i = 0; i < strings.length; i++) {
//            pq.insert(i, strings[i]);
//        }

//        // print each key using the iterator
//        for (int i : pq) {
//            StdOut.println(i + " " + strings[i]);
//        }


//        while (!pq.isEmpty()) {
//            pq.delMin();
//        }

    }
}

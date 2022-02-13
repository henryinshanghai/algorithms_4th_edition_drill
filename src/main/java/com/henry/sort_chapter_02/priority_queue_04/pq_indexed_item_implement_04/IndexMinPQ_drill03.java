package com.henry.sort_chapter_02.priority_queue_04.pq_indexed_item_implement_04;

import java.util.NoSuchElementException;

/**
 * 经验：
 * 1 keys[]数组中最小值的索引存储在pq[]数组的第二个位置
 * aka
 * pq[1]中存储的是xxx的索引————keys[]数组中最小值
 * <p>
 * 2 在insert()时，先把n+1 跳过第一个位置pq[0]
 */
public class IndexMinPQ_drill03<Key extends Comparable<Key>> {
    // 实例变量
    private Key[] keys;
    private int[] pq;
    private int[] qp;
    private int n;
    private int maxN;

    // 构造方法


    public IndexMinPQ_drill03(int maxN) {
        this.maxN = maxN;
        n = 0;
        keys = (Key[]) new Comparable[maxN + 1];
        pq = new int[maxN + 1];
        qp = new int[maxN + 1];

        for (int i = 0; i <= maxN; i++) {
            qp[i] = -1; // pq中还没有任何位置存储索引
        }
    }

    // API
    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    // 核心API
    private void insert(int index, Key item) {
        validateIndex(index);

        if (contains(index)) throw new IllegalArgumentException("此索引在优先队列中已经存在！");

        pq[++n] = index;
        qp[index] = n;
        keys[index] = item;

        swim(n); // pq[]数组中的指定位置n
    }

    public int delMin(){
        if (n == 0) throw new NoSuchElementException("队列已经被你掏空了！");

        // 获取到最小值所关联的索引值
        int minItemsIndex = pq[1];
        // 使用二叉堆的方式来删除元素
        exch(1, n--);
        sink(1);

        // 更新数组
        keys[minItemsIndex] = null;
        pq[n+1] = -1; // 从数组的角度考虑
        qp[minItemsIndex] = -1;

        return minItemsIndex;
    }

    private void sink(int pos) { // 当前节点大于两个子结点中的较小者
        while (2 * pos + 1 <= n) {
            int larger = 2 * pos;
            if(greater(larger, larger+1)) larger = larger + 1;

            // 比较
            if (!greater(pos, larger)) break;

            // 交换
            exch(pos, larger);

            // 更新
            pos = larger;
        }
    }


    private void swim(int pos) { // 父节点大于当前节点
        while (pos >= 2 && greater(pos / 2, pos)) { // pos/2 >= 1
            exch(pos / 2, pos);
            pos = pos / 2;
        }
    }

    /**
     * 交换二叉堆中指定位置的节点
     *
     * @param i
     * @param j
     */
    private void exch(int i, int j) {
        int temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;

        // 更新qp数组   在pq[]数组中i、j位置的元素被交换后，qp[]数组中需要同步更新 qp[索引] = 索引在pq[]数组中的位置
        // 交换操作会导致：索引在pq[]数组中的位置发生变化    所以qp[]数组中，特定位置绑定的值需要更新
        // 更新手段：索引交换之后，索引所在的位置也变成了交换前对方的位置；
        // 示例：
        // 位置   0 1 2 3 4 5 6 7         位置/索引 0 1 2 3 4 5 6 7
        // pq = {0,0,0,5,0,0,3,0}              qp x x x 6 x 3 x x
        // 交换位置3与位置6中的元素: i=3 j=6
        // pq = {0,0,0,3,0,0,5,0}              qp x x x 3 x 6 x x   qp[索引值3] = 3（这个3就是pq[]数组的位置信息 aka i）
        // qp[索引值3] = i 索引值3怎么来？ 3 = pq[i]  综上qp[pq[i]] = i 这条语句就能在当前变量值的情况下实现更新qp[]数组
        qp[pq[i]] = i; // 这是一个恒等式 qp[索引值] = 索引值在pq[]数组中的位置
        qp[pq[j]] = j;
    }

    private boolean greater(int i, int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private boolean contains(int index) {
        return qp[index] != -1;
    }

    private void validateIndex(int index) {
        if (index < 0) throw new IllegalArgumentException("索引值不能小于0");
        if (index >= maxN) throw new IllegalArgumentException("索引值必须在[0, maxN)的区间！");
    }

    public static void main(String[] args) {
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

        IndexMinPQ_drill03<String> pq = new IndexMinPQ_drill03<>(strings.length);

        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        while (!pq.isEmpty()) {
            int minItemsIndex = pq.delMin();
            System.out.println(minItemsIndex + " : "  + strings[minItemsIndex]);
        }
    }
}
/*
启示：
1 在pq[]中的索引交换后，需要对qp[]数组中“索引在qp[]中的位置”进行同步更新；
手段：qp[pq[i]] = i;

2 在对二叉堆建立/恢复平衡时，比较的不是二叉堆节点上的值（索引值），而是索引值关联的值的大小；

3 交换操作，交换的是二叉堆上的节点（索引值）
 */
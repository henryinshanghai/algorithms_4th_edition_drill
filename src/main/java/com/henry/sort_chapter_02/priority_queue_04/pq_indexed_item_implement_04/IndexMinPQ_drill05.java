package com.henry.sort_chapter_02.priority_queue_04.pq_indexed_item_implement_04;

/**
 * 经验：删除最小元素后，对pq[]进行更新时：由于交换了第一个与最后一个位置上的索引值。所以需要更新的是pq[n+1] = -1
 */
public class IndexMinPQ_drill05<Key extends Comparable<Key>> {
    // 实例变量
    private Key[] keys;
    private int[] pq;
    private int[] qp;
    private int n;
    private int maxN;

    // 构造方法
    public IndexMinPQ_drill05(int maxN) {
        this.maxN = maxN;
        n = 0;

        keys = (Key[]) new Comparable[maxN + 1];
        pq = new int[maxN + 1];
        qp = new int[maxN + 1];
        for (int i = 0; i <= maxN; i++) {
            qp[i] = -1;

        }
    }

    // API
    public boolean isEmpty(){
        return n == 0;
    }

    public int size(){
        return n;
    }

    // 核心API
    public void insert(int index, Key item) {
        validateIndex(index);

        if(contains(index)) throw new IllegalArgumentException("此索引值在优先队列中已经存在 请更换索引值");

        pq[++n] = index;
        qp[index] =n;
        keys[index] =item;

        swim(n);
    }

    public int delMin(){
        int minItemsIndex = pq[1];
        exch(1, n--);
        sinK(1);

        keys[minItemsIndex] = null;
        pq[n + 1] = -1;
        qp[minItemsIndex] = -1;


        return minItemsIndex;
    }

    private void sinK(int k) { // 当前节点关联的值比子结点关联的值更大，执行下沉操作
        while(2*k+1 <= n){ //
            // 找到关联较小值的子节点
            int j = 2 * k;
            if(j <= (n-1) && greater(j, j+1)) j = j + 1; // j+1 >= n

            // 比较
            if(!greater(k, j)) break;

            exch(k, j);
            k = j;
        }
    }

    private void swim(int k) { // 父节点关联的值大于当前节点关联的值，执行上浮操作
        while(k >=2 && greater(k/2, k)){ // Mark:注意大于小于号
            // 交换
            exch(k / 2, k);
            // 更新
            k = k / 2;
        }
    }

    private void exch(int i, int k) {
        int temp = pq[i];
        pq[i] = pq[k];
        pq[k] = temp;

        qp[pq[i]] = i;
        qp[pq[k]] = k;
    }

    private boolean greater(int i, int k) {
        return keys[pq[i]].compareTo(keys[pq[k]]) > 0;
    }

    private boolean contains(int index) {
        return qp[index] != -1;
    }

    private void validateIndex(int index) {
        if(index < 0) throw new IllegalArgumentException("索引值不能小于0");
        if(index >= maxN) throw new IllegalArgumentException("索引值不能大于或等于maxN");
    }

    public static void main(String[] args) {
        String[] strings = {"it", "was", "the", "best", "of", "times", "it", "was", "the", "worst"};

        IndexMinPQ_drill05<String> pq = new IndexMinPQ_drill05<>(10);
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
    判断时比较符号弄反了 这耽误了一些时间；对于判断条件，一定要有推导过程
    总用时：21min51s
 */
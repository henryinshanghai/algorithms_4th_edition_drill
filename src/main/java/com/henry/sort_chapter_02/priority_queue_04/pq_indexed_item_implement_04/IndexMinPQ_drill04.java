package com.henry.sort_chapter_02.priority_queue_04.pq_indexed_item_implement_04;

/**
 * 经验：
 * 1 在pq[]中的索引交换后，需要对qp[]数组中“索引在qp[]中的位置”进行同步更新；
 * 手段：qp[pq[i]] = i;
 *
 * 2 在对二叉堆建立/恢复平衡时，比较的不是二叉堆节点上的值（索引值），而是索引值关联的值的大小；
 *
 * 3 交换操作，交换的是二叉堆上的节点（索引值）
 */
public class IndexMinPQ_drill04<Key extends Comparable<Key>> {
    // 实例变量
    private Key[] keys;
    private int[] pq;
    private int[] qp;

    private int n;
    private int maxN;

    public IndexMinPQ_drill04(int maxN) {
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

    public int size() {
        return n;
    }

    // 核心API
    public void insert(int index, Key item) {
        validateIndex(index);

        if(contains(index)) throw new IllegalArgumentException("索引值在优先队列中已存在，请更换索引值");

        pq[++n] = index;
        qp[index] = n;
        keys[index] = item;

        swim(n);
    }

    public int delMin(){
        int minItemsIndex = pq[1];// pq[]中第一个位置存储的索引是最小元素的索引值
        // 使用二叉堆的方式删除最小值
        exch(1, n--);
        sink(1);

        // 更新数组信息
        keys[minItemsIndex] = null;
        pq[n+1] = -1; // 数组的最后一个位置，现在置为-1
        qp[minItemsIndex] = -1;

        return minItemsIndex;
    }

    private void sink(int currentPosition) { // 当前节点所关联的值比起其子节点中的较小者更大
        while(2*currentPosition + 1 <= n){ // 2*currentPosition + 1 <= n
            int child = 2 * currentPosition;
            if (child <= (n-1) && greater(child, child+1)) child = child + 1; // child+1 <= n

            // 比较
            if (!greater(currentPosition, child)) {
                break;
            }

            // 交换
            exch(currentPosition, child);
            // 更新
            currentPosition = child;
        }
    }

    private void swim(int currentPosition) { // 父节点关联的值比当前节点关联的值更大
        while(currentPosition >=2 && greater(currentPosition/2 , currentPosition)){ // currentPosition/2 >= 1
            // 交换
            exch(currentPosition / 2, currentPosition);
            // 更新
            currentPosition = currentPosition / 2;
        }
    }

    private void exch(int i, int j) {
        int temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;

        // 交换后更新qp[]数组
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    private boolean greater(int i, int j) {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    private boolean contains(int index) {
        // 判断索引值是否存在
        return qp[index] != -1;
    }

    private void validateIndex(int index) {
        if (index < 0) throw new IllegalArgumentException("索引值不能小于0");
        if (index >= maxN) throw new IllegalArgumentException("索引值不能大于等于maxN");
    }

    public static void main(String[] args) {
        String[] strings = {"it", "was", "the", "best", "of", "times", "it", "was", "the", "worst"};

        IndexMinPQ_drill04<String> pq = new IndexMinPQ_drill04<>(strings.length);

        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        while (!pq.isEmpty()) {
            int minItemsIndex = pq.delMin();
            System.out.println(minItemsIndex + " : " + strings[minItemsIndex]);
        }
    }
}

/*
启示：
    删除最小元素后，对pq[]进行更新时：由于交换了第一个与最后一个位置上的索引值。所以需要更新的是pq[n+1] = -1

总用时：半小时 make it faster
 */
package com.henry.sort_chapter_02.creative_exercies_05;

import edu.princeton.cs.algs4.StdOut;


/******************************************************************************
 *  Compilation:  javac StableMinPQFromWebsite.java
 *  Execution:    java StableMinPQFromWebsite
 *  Dependencies: StdOut.java
 *
 *  Generic min priority queue implementation with a binary heap.
 *  The min() and delMin() operations always return the minimum key
 *  that as inserted least recently when there are two or more.
 *
 *  % java StableMinPQFromWebsite
 *  a
 *  is
 *  test
 *  this
 *
 *  We use a one-based array to simplify parent and child calculations.
 *  作用：稳定的优先队列  aka 存储元素时，对于重复的元素，会存储重复元素的相对顺序
 *  aka 强制稳定
 *
 *  手段：1 使用一个time[]数组来记录元素被插入到队列时的时间标识；
 *  2 在greater()方法中，当两个元素的值相等时，再去比较两个元素插入队列的时间标识；
 *  3 在exch()方法中，交换pq[]中的两个元素后，timestampArr[]中元素对应的时间戳也需要交换
 *  4 同理insert()、delMin()也都需要做一些处理
 ******************************************************************************/

public class StableMinPQFromWebsite_01<Item extends Comparable<Item>> {
    // 实例变量
    private Item[] itemHeap;                   // 使用索引[1-N]来存储元素
    private long[] timestampArr;                 // 记录 item被插入优先队列中的时刻
    private int itemAmount;
    private long timestamp = 1;          // 元素被插入队列的时间戳

    // client传入初始容量，创建 优先队列对象
    // 特征：数组的大小 = 队列的容量 + 1
    public StableMinPQFromWebsite_01(int initCapacity) {
        itemHeap = (Item[]) new Comparable[initCapacity + 1];
        timestampArr = new long[initCapacity + 1];
        itemAmount = 0;
    }

    // client不需要要传入任何参数，创建 优先队列对象
    public StableMinPQFromWebsite_01() {
        // 委托 当前类中的方法
        this(1);
    }


    // 队列是否已经空了？
    public boolean isEmpty() {
        return itemAmount == 0;
    }

    // 队列中元素的数量
    public int size() {
        return itemAmount;
    }

    //  返回优选队列中的最小元素 - itemHeap[1]
    public Item min() {
        if (isEmpty()) throw new RuntimeException("Priority queue underflow");
        return itemHeap[1];
    }

    // helper function to double the size of the heap array
    private void resize(int capacity) {
        assert capacity > itemAmount;
        Item[] tempPQ = (Item[]) new Comparable[capacity];
        long[] tempTime = new long[capacity];
        for (int i = 1; i <= itemAmount; i++)
            tempPQ[i] = itemHeap[i];
        for (int i = 1; i <= itemAmount; i++)
            tempTime[i] = timestampArr[i];
        itemHeap = tempPQ;
        timestampArr = tempTime;
    }

    // 向优先队列中添加一个新元素
    public void insert(Item newItem) {
        // 需要的话 为优先队列扩容
        if (itemAmount == itemHeap.length - 1) resize(2 * itemHeap.length);

        // add newItem, and percolate it up to maintain heap invariant
        itemAmount++;
        itemHeap[itemAmount] = newItem; // 插入元素
        timestampArr[itemAmount] = ++timestamp;

        swim(itemAmount); // 恢复堆有序
        assert isMinHeap();
    }

    // 删除并返回 队列中的最小元素
    public Item delMin() {
        if (itemAmount == 0) throw new RuntimeException("Priority queue underflow");
        exch(1, itemAmount);
        // 获取到 队列中的最小元素
        Item minItem = itemHeap[itemAmount--];

        // 恢复堆有序
        sink(1);
        itemHeap[itemAmount + 1] = null;         // 防止对象游离 - 帮助垃圾回收
        timestampArr[itemAmount + 1] = 0;

        // 判断是否需要扩容
        if ((itemAmount > 0) && (itemAmount == (itemHeap.length - 1) / 4)) resize(itemHeap.length / 2);
        assert isMinHeap();
        return minItem;
    }


    /***************************************************************************
     * Helper functions to restore the heap invariant.
     ***************************************************************************/

    private void swim(int spotInHeap) {
        while (spotInHeap > 1 && greater(spotInHeap / 2, spotInHeap)) {
            // 交换 当前的堆元素 与其父元素
            exch(spotInHeap, spotInHeap / 2);
            // 更新当前位置
            spotInHeap = spotInHeap / 2;
        }
    }

    private void sink(int spotInHeap) {
        while (2 * spotInHeap <= itemAmount) { // 循环条件： 当前元素的子元素位置仍旧在 数组范围内
            int biggerChildSpot = 2 * spotInHeap;
            if (biggerChildSpot < itemAmount && greater(biggerChildSpot, biggerChildSpot + 1))
                biggerChildSpot++;

            if (!greater(spotInHeap, biggerChildSpot)) break;
            exch(spotInHeap, biggerChildSpot);
            spotInHeap = biggerChildSpot;
        }
    }

    /***************************************************************************
     * Helper functions for compares and swaps.
     ***************************************************************************/
    private boolean greater(int i, int j) {
        int cmp = itemHeap[i].compareTo(itemHeap[j]);
        if (cmp > 0) return true;
        if (cmp < 0) return false;

        // 如果出现 堆元素相等的情况，就比较 item被插入时的时间戳
        return timestampArr[i] > timestampArr[j];
    }

    private void exch(int i, int j) {
        Item temp = itemHeap[i];
        itemHeap[i] = itemHeap[j];
        itemHeap[j] = temp;

        // 维护time[]
        long tempTime = timestampArr[i];
        timestampArr[i] = timestampArr[j];
        timestampArr[j] = tempTime;
    }

    // itemHeap[1..N]是一个最小堆吗?
    private boolean isMinHeap() {
        // 为什么这里 不先对数组元素的特性做出判断？
        // 手段：递归判断 堆的性质
        return isMinHeap(1);
    }

    // itemHeap[1..itemAmount]中以 位置k上的元素作为根节点的子树 是不是一个最小堆?
    private boolean isMinHeap(int spotOfRoot) {
        if (spotOfRoot > itemAmount) return true;
        // 判断当前节点 与 其左右子节点的大小情况
        int leftChildSpot = 2 * spotOfRoot,
            rightChildSpot = 2 * spotOfRoot + 1;
        if (leftChildSpot <= itemAmount && greater(spotOfRoot, leftChildSpot)) return false;
        if (rightChildSpot <= itemAmount && greater(spotOfRoot, rightChildSpot)) return false;

        // 在满足 节点大小规则的情况下，判断：左右子树是不是也是最小堆
        return isMinHeap(leftChildSpot) && isMinHeap(rightChildSpot);
    }


    // 自定义一个元组类型 - 实例：(a, b)
    private static final class Tuple implements Comparable<Tuple> {
        private String name;
        private int id;

        private Tuple(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public int compareTo(Tuple that) {
            return this.name.compareTo(that.name);
        }

        public String toString() {
            return name + " " + id;
        }
    }

    // test client
    public static void main(String[] args) {
        StableMinPQFromWebsite_01<Tuple> pq = new StableMinPQFromWebsite_01<Tuple>();

        // insert a bunch of strings
        String text = "it was the best of times it was the worst of times it was the "
                + "age of wisdom it was the age of foolishness it was the epoch "
                + "belief it was the epoch of incredulity it was the season of light "
                + "it was the season of darkness it was the spring of hope it was the "
                + "winter of despair";
        String[] strings = text.split(" ");
        for (int cursor = 0; cursor < strings.length; cursor++) {
            pq.insert(new Tuple(strings[cursor], cursor));
        }


        // 删除&打印元素 - 从最小元素开始打印元组元素 (item, cursor_of_item)
        while (!pq.isEmpty()) {
            StdOut.println(pq.delMin());
        }
        StdOut.println();

    }

}
package com.henry.sort_chapter_02.creative_exercies_05;

import edu.princeton.cs.algs4.StdOut;


/******************************************************************************
 *  Compilation:  javac StableMinPQFromWebsite.java
 *  Execution:    java StableMinPQFromWebsite
 *  Dependencies: StdOut.java
 *
 *
 *  泛型的最小优先队列（使用二叉堆实现）。
 *  当存在多个元素时，min() 与 delMin() 操作 总会返回 最近插入的最小元素
 *
 *  % java StableMinPQFromWebsite
 *  a
 *  is
 *  test
 *  this
 *
 *  我们使用 基于1的数组 来 简化 父节点和子节点之间的计算
 ******************************************************************************/
/*
 *  作用：“稳定的”优先队列  aka 存储元素时，对于重复的元素，会存储“重复元素的相对顺序”
 *  手段：在添加元素到指定位置时，记录下 添加元素的时间戳（自定义的时间戳即可，用于 唯一标识此元素）
 */

public class StableMinPQTemplate<Item extends Comparable<Item>> {
    // 实例变量
    private Item[] spotToItemArray;                   // 使用索引[1-N]来存储元素
    private long[] spotToTimestampArray;                 // 记录 item被插入优先队列中的时刻
    private int itemAmount;
    private long timestamp = 1;          // 元素被插入队列的时间戳

    public StableMinPQTemplate(int initCapacity) {
        spotToItemArray = (Item[]) new Comparable[initCapacity + 1];
        spotToTimestampArray = new long[initCapacity + 1];
        itemAmount = 0;
    }

    public StableMinPQTemplate() {
        this(1);
    }


    public boolean isEmpty() {
        return itemAmount == 0;
    }

    public int size() {
        return itemAmount;
    }

    public Item min() {
        if (isEmpty()) throw new RuntimeException("Priority queue underflow");
        return spotToItemArray[1];
    }

    // helper function to double the size of the heap array
    private void resize(int capacity) {
        assert capacity > itemAmount;
        /* spotToItemArray & spotToTimestampArray */
        // 容量初始化
        Item[] tempItemArray = (Item[]) new Comparable[capacity];
        long[] tempTimestampArray = new long[capacity];
        // 元素初始化
        for (int i = 1; i <= itemAmount; i++)
            tempItemArray[i] = spotToItemArray[i];
        for (int i = 1; i <= itemAmount; i++)
            tempTimestampArray[i] = spotToTimestampArray[i];
        // 绑定回去原始变量
        spotToItemArray = tempItemArray;
        spotToTimestampArray = tempTimestampArray;
    }

    public void insert(Item newItem) {
        // 需要的话 为优先队列扩容
        if (itemAmount == spotToItemArray.length - 1) resize(2 * spotToItemArray.length);

        // add newItem, and percolate it up to maintain heap invariant
        itemAmount++;
        spotToItemArray[itemAmount] = newItem; // 在堆的末尾添加元素
        spotToTimestampArray[itemAmount] = ++timestamp; // 为此位置绑定唯一的时间戳

        swim(itemAmount); // 恢复堆有序
        assert isMinHeap();
    }

    // 删除并返回 队列中的最小元素
    public Item delMin() {
        if (itemAmount == 0) throw new RuntimeException("Priority queue underflow");
        exch(1, itemAmount);
        // 获取到 队列中的最小元素
        Item minItem = spotToItemArray[itemAmount--];

        // 恢复堆有序
        sink(1);
        spotToItemArray[itemAmount + 1] = null;         // 防止对象游离 - 帮助垃圾回收
        spotToTimestampArray[itemAmount + 1] = 0;

        // 判断是否需要扩容
        if ((itemAmount > 0) && (itemAmount == (spotToItemArray.length - 1) / 4)) resize(spotToItemArray.length / 2);
        assert isMinHeap();
        return minItem;
    }


    /***************************************************************************
     * Helper functions to restore the heap invariant.
     **************************************************************************
     * @param currentNodeSpot*/

    private void swim(int currentNodeSpot) {
        while (currentNodeSpot > 1 && greater(currentNodeSpot / 2, currentNodeSpot)) {
            // 交换 当前的堆元素 与其父元素
            exch(currentNodeSpot, currentNodeSpot / 2);
            // 更新当前位置
            currentNodeSpot = currentNodeSpot / 2;
        }
    }

    private void sink(int currentNodeSpot) {
        while (2 * currentNodeSpot <= itemAmount) { // 循环条件： 当前元素的子元素位置仍旧在 数组范围内
            int smallerChildSpot = 2 * currentNodeSpot;
            if (smallerChildSpot < itemAmount && greater(smallerChildSpot, smallerChildSpot + 1))
                smallerChildSpot++;

            if (!greater(currentNodeSpot, smallerChildSpot)) break;
            exch(currentNodeSpot, smallerChildSpot);
            currentNodeSpot = smallerChildSpot;
        }
    }

    /***************************************************************************
     * Helper functions for compares and swaps.
     **************************************************************************
     * @param spotI
     * @param spotJ*/
    private boolean greater(int spotI, int spotJ) {
        int result = spotToItemArray[spotI].compareTo(spotToItemArray[spotJ]);
        if (result > 0) return true;
        if (result < 0) return false;

        // 如果出现 堆元素相等的情况，就比较 ”item被插入时的时间戳“（其实是一个递增的整数） - 大的表示是后插入的元素
        return spotToTimestampArray[spotI] > spotToTimestampArray[spotJ];
    }

    private void exch(int spotI, int spotJ) {
        Item temp = spotToItemArray[spotI];
        spotToItemArray[spotI] = spotToItemArray[spotJ];
        spotToItemArray[spotJ] = temp;

        // 维护 spotToTimestampArray[]
        long tempTime = spotToTimestampArray[spotI];
        spotToTimestampArray[spotI] = spotToTimestampArray[spotJ];
        spotToTimestampArray[spotJ] = tempTime;
    }

    // spotToItemArray[1..N]是一个最小堆吗?
    private boolean isMinHeap() {
        // 手段：判断 以spot=1位置上的元素 作为根节点的完全二叉树 是不是一个堆
        return isMinHeap(1);
    }

    // spotToItemArray[1..itemAmount]中以 位置k上的元素作为根节点的子树 是不是一个最小堆?
    private boolean isMinHeap(int spotOfRoot) {
        if (spotOfRoot > itemAmount) return true;

        // 数值约束 👇
        int leftChildSpot = 2 * spotOfRoot,
        rightChildSpot = 2 * spotOfRoot + 1;

        if (leftChildSpot <= itemAmount && greater(spotOfRoot, leftChildSpot)) return false;
        if (rightChildSpot <= itemAmount && greater(spotOfRoot, rightChildSpot)) return false;

        // 子树也要求满足相同的约束
        return isMinHeap(leftChildSpot) && isMinHeap(rightChildSpot);
    }


    // 自定义一个元组类型 - 用于 封装多个相干的属性；
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
        // insert a bunch of strings
        String text = "it was the best of times it was the worst of times it was the "
                + "age of wisdom it was the age of foolishness it was the epoch "
                + "belief it was the epoch of incredulity it was the season of light "
                + "it was the season of darkness it was the spring of hope it was the "
                + "winter of despair";
        String[] strings = text.split(" ");

        // 🐖 由于 StableMinPQTemplate中实现了 “稳定性”，因此 当出现相同元素时，队列会以其插入时的顺序返回这些元素
        // 更具体来说，第一个it会先被打印，然后是第二个it...
        // 验证手段：在向队列中插入单词时，添加一个属性 来 标识单词在文段中的位置 - 也就是这里的cursor👇
        StableMinPQTemplate<Tuple> stableMinPQ = new StableMinPQTemplate<Tuple>();

        for (int cursor = 0; cursor < strings.length; cursor++) {
            // 以 当前元素 -> 指针位置 来 生成元组对象，并添加到 最小优先队列中
            stableMinPQ.insert(new Tuple(strings[cursor], cursor));
        }

        // 删除并打印 最小优先队列中的最小元素 - toString()方法 会打印 “当前元素 -> 指针位置”
        while (!stableMinPQ.isEmpty()) {
            StdOut.println(stableMinPQ.delMin());
        }
        StdOut.println();
    }
}
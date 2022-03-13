package com.henry.sort_chapter_02.priority_queue_04.pq_heap_implement_03;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
算法描述：
    使用 堆这种逻辑结构 来实现 优先队列 - insert操作 + delMax操作

    堆这种逻辑结构要怎么实现呢？
        数组 -> 完全二叉树(数值要求) -> 堆有序的完全二叉树

    底层数据结构：
        一个数组 itemArr
        一个int itemAmount


 */
public class MaxPQ_heap_round2_drill01<Key> implements Iterable<Key> {
    private Key[] itemArr;                    // store items at indices 1 to n
    private int itemAmount;                       // number of items on priority queue

    private Comparator<Key> comparator;  // optional comparator

    // 5种不同类型的构造函数 - 给client充分的使用方式的选择

    /**
     * 为队列 设置一个初始化大小的容量
     *
     * @param initCapacity the initial capacity of this priority queue
     */
    public MaxPQ_heap_round2_drill01(int initCapacity) {
        itemArr = (Key[]) new Object[initCapacity + 1];
        itemAmount = 0;
    }

    /**
     * 初始化一个空的队列
     * Initializes an empty priority queue.
     */
    public MaxPQ_heap_round2_drill01() {
        this(1);
    }

    /**
     * 初始化一个空的队列，并 使用指定的比较器 来 指定原始容量
     * Initializes an empty priority queue with the given initial capacity,
     * using the given comparator.
     *
     * @param initCapacity the initial capacity of this priority queue
     * @param comparator   the order in which to compare the keys
     */
    public MaxPQ_heap_round2_drill01(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        itemArr = (Key[]) new Object[initCapacity + 1];
        itemAmount = 0;
    }

    /**
     * 使用一个指定的比较器 来 初始化一个空的队列
     * Initializes an empty priority queue using the given comparator.
     *
     * @param comparator the order in which to compare the keys
     */
    public MaxPQ_heap_round2_drill01(Comparator<Key> comparator) {
        this(1, comparator);
    }

    /**
     * 从一个数组中 来初始化得到 队列
     * Initializes a priority queue from the array of keys.
     * Takes time proportional to the number of keys, using sink-based heap construction.
     *
     * @param keys the array of keys
     */
    public MaxPQ_heap_round2_drill01(Key[] keys) {
        // set up the spots
        itemAmount = keys.length;
        itemArr = (Key[]) new Object[keys.length + 1];

        // copy items from array to inside array
        for (int i = 0; i < itemAmount; i++)
            itemArr[i + 1] = keys[i];

        // make the inside array 'heap-sorted'
        // this is a process of constructing a heap from an array
        // 使用一个 以1结束的序列 在逐步构建堆 - 为什么需要逐步构建呢？
        for (int k = itemAmount / 2; k >= 1; k--)
            sink(k); // 最终会调用sink(1)

        // check if it is heap-sorted
        assert isMaxHeap();
    }


    // 基础 API

    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     * {@code false} otherwise
     */
    public boolean isEmpty() {
        return itemAmount == 0;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int size() {
        return itemAmount;
    }

    /**
     * Returns a largest key on this priority queue.
     *
     * @return a largest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return itemArr[1];
    }

    // helper function to double the size of the heap array
    // Client传入一个新的 capacity 来 重置队列的容量大小
    private void resize(int capacity) {
        assert capacity > itemAmount;
        Key[] tempItemArr = (Key[]) new Object[capacity];
        for (int i = 1; i <= itemAmount; i++) {
            tempItemArr[i] = itemArr[i];
        }
        itemArr = tempItemArr;
    }


    // public APIs
    /*
        insert an item:
        1 add it in the elementArr's last spot;
        2 adjust the array to make it heap-sorted.  via Swim(the_last_spot)
        3 update the itemAmount

        其他：
            1 判断是不是需要扩容
            2 判断 数组是不是被调整到堆有序
     */
    public void insert(Key newItem) {

        // double size of array if necessary
        if (itemAmount == itemArr.length - 1) resize(2 * itemArr.length);

        // add x, and percolate it up to maintain heap invariant
        itemArr[++itemAmount] = newItem;

        // swim the item by spot.
        swim(itemAmount);

        // 断言操作 似乎比起抛异常更好用
        assert isMaxHeap();
    }

    /*
        delMax()

        删除最大元素：
        1 删除堆顶元素 aka 数组的第一个元素
        2 用数组的最后一个位置上的元素 替换到 数组的第一个位置
        3 调整数组 到 堆有序。
        4 把最后一个元素设置为null - 物理删除

        其他：
        1 判断队列是不是已经被删空 - 这是一种预防机制，因为 Client应该在调用delMax之前 先使用isEmpty()判断的。但他可能不这么做
        2 判断队列是不是已经比较空了 如果是的话，进行resize()
        3 断言 数组 是不是 堆有序的
     */
    public Key delMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        Key max = itemArr[1];
        exch(1, itemAmount--);
        sink(1);
        itemArr[itemAmount + 1] = null;     // to avoid loitering and help with garbage collection

        if ((itemAmount > 0) && (itemAmount == (itemArr.length - 1) / 4)) resize(itemArr.length / 2);
        assert isMaxHeap();
        return max;
    }


    /***************************************************************************
     * Helper functions to restore the heap invariant.
     ***************************************************************************/
    // swim the item in current spot into its right spot
    private void swim(int currentSpot) {
        // don't know how much time need to execute, using while
        /*
            #con1: if it is bigger than its father;
            #con2: if it is already on the heap top.

            swim up by exchange the items on these spots.
            updating the current spot
         */
        while (currentSpot > 1 && less(currentSpot / 2, currentSpot)) {
            exch(currentSpot, currentSpot / 2);
            // update currentSpot
            currentSpot = currentSpot / 2;
        }
    }


    // sink the item on current spot to its right spot
    private void sink(int currentSpot) {
        /*
            #con1: the items value is bigger than its 2 child's value / the bigger child's value;
            #con2: the item is on the last spot of the array.

            how to sink?
            - 1 find the bigger child's spot;
            - 2 exchange the item with its bigger child.
            - 3 update current spot, until the condition is not favorable anymore.
         */
        while (2 * currentSpot <= itemAmount) {
            // - 1 find the bigger child's spot;
            int biggerChildSpot = 2 * currentSpot;
            if (biggerChildSpot < itemAmount && less(biggerChildSpot, biggerChildSpot + 1)) biggerChildSpot++;

            // - 2 exchange the item with its bigger child.
            if (!less(currentSpot, biggerChildSpot)) break;

            // - 3 update current spot, until the condition is not favorable anymore.
            exch(currentSpot, biggerChildSpot);
            currentSpot = biggerChildSpot;
        }
    }

    /***************************************************************************
     * Helper functions for compares and swaps.
     ***************************************************************************/
    // why this less function is so much?
    // 添加了一个选择分支： 使用选择器的比较方法 进行比较 - 这是增强功能
    private boolean less(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) itemArr[i]).compareTo(itemArr[j]) < 0;
        } else {
            return comparator.compare(itemArr[i], itemArr[j]) < 0;
        }
    }

    private void exch(int i, int j) {
        Key swap = itemArr[i];
        itemArr[i] = itemArr[j];
        itemArr[j] = swap;
    }

    // 判断当前数组是不是大顶堆？
    // aka is pq[1..n] a max heap?
    /*
        1 null值判断；
            // #con1: 是不是存在null元素
            // #con2： 从队列元素结束的位置到剩下的空间中是不是还有其他的元素
            // #con3： 数组的第一个位置是不是null元素
        2 节点值大小判断；

     */
    private boolean isMaxHeap() {
        // #con1: 是不是存在null元素
        for (int i = 1; i <= itemAmount; i++) {
            if (itemArr[i] == null) return false;
        }

        // #con2： 从队列元素结束的位置到剩下的空间中是不是还有其他的元素
        for (int i = itemAmount + 1; i < itemArr.length; i++) {
            if (itemArr[i] != null) return false;
        }

        // #con3： 数组的第一个位置是不是null元素
        if (itemArr[0] != null) return false;
        return isMaxHeapOrdered(1);
    }

    // is subtree of pq[1..n] rooted at k a max heap?
    // 数组中 以k为根节点的子树是不是一个 大顶堆
    // 手段： 大顶堆的规则
    /*
        如何判断是不是所有的节点都符合 堆的规则？
        由于堆的定义是递归的，所以这里的判断也是递归的

        定义/实现：
            1 对当前的二叉树来说， 根节点的值 > max(左节点的值, 右节点的值)
            2 对于两棵子树来说，子树本身也是堆有序的。

     */
    private boolean isMaxHeapOrdered(int currentSpot) {
        // 为什么传入的节点比 堆中元素更多时，要返回true？ 因为这是一个递归调用 currentSpot的值会随着递归的深度增大而不断变大
        // 当 currentSpot > itemAmount时，就是递归终结的时机
        if (currentSpot > itemAmount) return true;
        // 左、右子节点
        int leftChildSpot = 2 * currentSpot;
        int rightChildSpot = 2 * currentSpot + 1;

        // 当前元素的值 > max(左右子节点的元素值)
        if (leftChildSpot <= itemAmount && less(currentSpot, leftChildSpot)) return false;
        if (rightChildSpot <= itemAmount && less(currentSpot, rightChildSpot)) return false;

        // 左子树是一个 大顶堆 && 右子树是一个 大顶堆
        return isMaxHeapOrdered(leftChildSpot) && isMaxHeapOrdered(rightChildSpot);
    }


    /***************************************************************************
     * Iterator. 为什么会需要实现迭代器？？？
     * 优先队列为什么要支持迭代操作呢？
     ***************************************************************************/

    /**
     * Returns an iterator that iterates over the keys on this priority queue
     * in descending order.
     * The iterator doesn't implement {@code remove()} since it's optional.
     *
     * @return an iterator that iterates over the keys in descending order
     */
    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {

        // create a new pq
        private MaxPQ_heap_round2_drill01<Key> copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            // 容量的初始化
            if (comparator == null) copy = new MaxPQ_heap_round2_drill01<Key>(size());
            else copy = new MaxPQ_heap_round2_drill01<Key>(size(), comparator);
            // 值的初始化
            for (int i = 1; i <= itemAmount; i++)
                copy.insert(itemArr[i]);
        }

        public boolean hasNext() {
            return !copy.isEmpty();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }
    }

    /**
     * Unit tests the {@code MaxPQFromWebsite} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        MaxPQ_heap_round2_drill01<String> pq = new MaxPQ_heap_round2_drill01<String>();

        // 读取输入流 - 为了从文件中读取，需要重定向输入流
        while (!StdIn.isEmpty()) {
            // 获取文件中的当前 字符串
            String item = StdIn.readString();

            // 插入 + 删除最大值： 构建优先队列的过程
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
        }
        StdOut.println("(" + pq.size() + " left on pq)");
    }

}
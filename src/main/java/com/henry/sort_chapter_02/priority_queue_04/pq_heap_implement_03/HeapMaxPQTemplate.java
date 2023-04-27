package com.henry.sort_chapter_02.priority_queue_04.pq_heap_implement_03; /******************************************************************************
 *  Compilation:  javac MaxPQFromWebsite.java
 *  Execution:    java MaxPQFromWebsite < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/24pq/tinyPQ.txt
 *
 * 使用 二叉堆实现的 泛型的最大优先队列
 * 可以使用一个比较器来代替自然排序，但是泛型的key必须要是 可比较的
 *
 *  % java MaxPQFromWebsite < tinyPQ.txt 
 *  Q X P (6 left on itemHeap)
 *
 *  我们使用一个 以1作为基底的数组 来 简化parent与child之间的计算
 *
 *  可以通过 “半交换” 替代 “完全交换” 来 进一步优化
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 这个类表示的是 由泛型key组成的优先队列（逻辑结构）。
 * 手段：使用堆这种逻辑结构 堆 = 满足特定条件的完全二叉树; 条件：堆有序 aka 对于任意节点，它的值都大于等于它的两个子节点。
 * 它支持 常见的 #1 insert操作 与 #2 删除最大值的操作，以及 #3 查看最大的key, #4 测试优先队列是否为空, #5 遍历所有的key的操作
 */

public class HeapMaxPQTemplate<Item> implements Iterable<Item> { // 类本身实现了 Iterables接口
    private Item[] itemHeap;                    // 用[1, itemAmount]的区间 来 存储元素
    private int itemAmount;                       // 优先队列中的元素数量
    private Comparator<Item> comparator;  // 比较器（可选的）

    /**
     * 初始化优先队列时，指定初始化容量
     * @param initCapacity the initial capacity of this priority queue
     */
    public HeapMaxPQTemplate(int initCapacity) {
        itemHeap = (Item[]) new Object[initCapacity + 1];
        itemAmount = 0;
    }

    /**
     * 初始化一个空的优先队列
     */
    public HeapMaxPQTemplate() {
        this(1);
    }

    /**
     * 初始化优先队列时，指定 初始容量与比较器
     */
    public HeapMaxPQTemplate(int initCapacity, Comparator<Item> comparator) {
        this.comparator = comparator;
        itemHeap = (Item[]) new Object[initCapacity + 1];
        itemAmount = 0;
    }

    /**
     * 初始化优先队列时，指定 比较器
     */
    public HeapMaxPQTemplate(Comparator<Item> comparator) {
        this(1, comparator);
    }

    /**
     * 从数组元素中初始化得到一个优先队列
     * 会花费 与元素数量正相关的时间，使用 基于sink操作的堆结构
     *
     * @param items the array of items
     */
    public HeapMaxPQTemplate(Item[] items) {
        itemAmount = items.length;
        itemHeap = (Item[]) new Object[items.length + 1];
        for (int i = 0; i < itemAmount; i++)
            itemHeap[i + 1] = items[i];

        // 构造出一个堆 - 手段：使用sink()方法 排定一半的元素
        // 原理：如果数组中，前面一半的元素都已经满足“堆有序”的话，则：整个数组必然是堆有序的
        // 原因：对某个位置，执行了sink(index)后，则：这个位置上的节点 就一定会大于 它的子节点了。
        // 因此保证前一半的节点被排定后，剩下的节点必然也符合 堆对元素的数值约束了
        for (int k = itemAmount / 2; k >= 1; k--)
            sink(k);
        assert isMaxHeap();
    }


    /**
     * 在优先队列为空时，返回true
     *
     * @return {@code true} if this priority queue is empty;
     * {@code false} otherwise
     */
    public boolean isEmpty() {
        return itemAmount == 0;
    }

    /**
     * 返回优先队列中key的数量
     *
     * @return the number of keys on this priority queue
     */
    public int size() {
        return itemAmount;
    }

    /**
     * 返回优先队列中最大的key
     *
     * @return a largest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Item max() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return itemHeap[1];
    }

    // 为堆数组执行扩容
    private void resize(int capacity) {
        assert capacity > itemAmount;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 1; i <= itemAmount; i++) {
            temp[i] = itemHeap[i];
        }
        itemHeap = temp;
    }


    /**
     * 先优先队列中添加一个新的key
     *
     * @param newItem the new key to add to this priority queue
     */
    public void insert(Item newItem) {

        // double size of array if necessary
        if (itemAmount == itemHeap.length - 1) resize(2 * itemHeap.length);

        // add newItem, and percolate it up to maintain heap invariant
        itemHeap[++itemAmount] = newItem;

        // 在数组末尾插入元素后，通过上浮最后一个位置的元素 来 保持数组的堆有序
        swim(itemAmount);
        assert isMaxHeap();
    }

    /**
     * 移除并返回 优先队列中最大的key
     *
     * @return a largest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Item delMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        Item maxItemInHeap = itemHeap[1];
        // 把 堆中最大的元素(aka itemHeap[1]) 与 数组末尾的元素交换 - 这样最大元素被交换到 堆的最后一个位置
        exch(1, itemAmount--);
        // 下沉堆中第一个位置上的元素， 来 维持数组的堆有序
        sink(1);

        // 删除数组中最后一个位置上的元素(它已经不属于堆) 以防止对象游离
        itemHeap[itemAmount + 1] = null;     // to avoid loitering and help with garbage collection

        // 删除元素后，查看是不是需要调整 数组的容量大小
        if ((itemAmount > 0) && (itemAmount == (itemHeap.length - 1) / 4)) resize(itemHeap.length / 2);
        assert isMaxHeap();
        return maxItemInHeap;
    }


    /***************************************************************************
     * Helper functions to restore the heap invariant（堆的不变性）.
     ***************************************************************************/

    // 把指定位置上的元素（更大的元素） 上浮到 堆中正确的位置 - 作用：恢复完全二叉树的“堆有序”
    private void swim(int currentSpot) {
        while (currentSpot > 1 && less(currentSpot / 2, currentSpot)) {
            exch(currentSpot, currentSpot / 2);
            currentSpot = currentSpot / 2;
        }
    }

    // 把指定位置上的元素（更小的元素） 下沉到 堆中正确的位置  - 作用：恢复完全二叉树的“堆有序”
    private void sink(int currentSpot) {
        while (2 * currentSpot <= itemAmount) {
            int biggerChildSpot = 2 * currentSpot;
            if (biggerChildSpot < itemAmount && less(biggerChildSpot, biggerChildSpot + 1)) biggerChildSpot++;
            if (!less(currentSpot, biggerChildSpot)) break;
            exch(currentSpot, biggerChildSpot);
            currentSpot = biggerChildSpot;
        }
    }

    /***************************************************************************
     * Helper functions for compares and swaps（比较与交换）.
     ***************************************************************************/
    private boolean less(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Item>) itemHeap[i]).compareTo(itemHeap[j]) < 0;
        } else {
            return comparator.compare(itemHeap[i], itemHeap[j]) < 0;
        }
    }

    private void exch(int i, int j) {
        Item swap = itemHeap[i];
        itemHeap[i] = itemHeap[j];
        itemHeap[j] = swap;
    }

    // 判断当前的数组 是否是 一个二叉堆？ 原理：根据二叉堆的特性
    private boolean isMaxHeap() {
        // 堆的性质1 - 完全二叉树 aka 数组中的元素连续且不为null
        for (int cursor = 1; cursor <= itemAmount; cursor++) {
            if (itemHeap[cursor] == null) return false;
        }
        // 堆的性质2 - 使用数组表示的完全二叉树 aka 数组中其他的位置上不能有元素
        for (int i = itemAmount + 1; i < itemHeap.length; i++) {
            if (itemHeap[i] != null) return false;
        }
        // 堆的约定 - 数组的第一个位置不存放任何元素（方便数组下标index 与 元素在二叉树中位置spot之间的转换）
        if (itemHeap[0] != null) return false;
        return isMaxHeapOrdered(1);
    }

    // 以当前spot作为根节点的子树 是不是一个max heap?
    // 手段：
    private boolean isMaxHeapOrdered(int currentSpot) {
        if (currentSpot > itemAmount) return true;
        int leftChildSpot = 2 * currentSpot;
        int rightChildSpot = 2 * currentSpot + 1;
        if (leftChildSpot <= itemAmount && less(currentSpot, leftChildSpot)) return false;
        if (rightChildSpot <= itemAmount && less(currentSpot, rightChildSpot)) return false;

        // 以当前节点的左右子节点作为根节点 的树，也是一个最大堆 - 堆的定义的递归性
        return isMaxHeapOrdered(leftChildSpot) && isMaxHeapOrdered(rightChildSpot);
    }


    /***************************************************************************
     * Iterator. 用于支持迭代语法 - 比如for循环
     ***************************************************************************/

    /**
     * 返回一个迭代器 它会以降序的方式 来 遍历优先队列中的所有key
     * 当前迭代器 没有实现 remove()方法 - 因为这个方法是可选的
     */
    public Iterator<Item> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Item> {

        // 迭代器的操作可能会改变 队列中的元素，所以这里拷贝了 原始对象的一个副本
        private HeapMaxPQTemplate<Item> copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            // 初始化 优先队列对象
            if (comparator == null) copy = new HeapMaxPQTemplate<Item>(size());
            else copy = new HeapMaxPQTemplate<Item>(size(), comparator);
            // 初始化 队列中的元素
            for (int i = 1; i <= itemAmount; i++)
                copy.insert(itemHeap[i]);
        }

        public boolean hasNext() {
            return !copy.isEmpty();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        // 获取到队列中下一个位置上的元素 - 手段：删除掉当前的堆顶元素，并返回
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }
    }

    /**
     * 对自定义类型的单元测试
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        HeapMaxPQTemplate<String> pq = new HeapMaxPQTemplate<String>();

        while (!StdIn.isEmpty()) { // 判断标准输入流是否为空
            // 读取标准输入流中的字符串
            String item = StdIn.readString();
            // 如果当前字符串不是 - 就把它添加到 优先队列中
            if (!item.equals("-")) pq.insert(item);
                // 如果遇到了 - 字符，就删除掉并打印 优先队列中当前的最大元素
            else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
        }

        StdOut.println("(" + pq.size() + " left on itemHeap)");
    }
}
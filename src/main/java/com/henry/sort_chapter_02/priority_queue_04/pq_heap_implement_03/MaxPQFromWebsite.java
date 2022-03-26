package com.henry.sort_chapter_02.priority_queue_04.pq_heap_implement_03; /******************************************************************************
 *  Compilation:  javac MaxPQFromWebsite.java
 *  Execution:    java MaxPQFromWebsite < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/24pq/tinyPQ.txt
 *
 *  Generic max priority queue implementation with a binary heap.
 *  Can be used with a comparator instead of the natural order,
 *  but the generic Key type must still be Comparable.
 *
 *  % java MaxPQFromWebsite < tinyPQ.txt 
 *  Q X P (6 left on itemHeap)
 *
 *  We use a one-based array to simplify parent and child calculations.
 *
 *  Can be optimized by replacing full exchanges with half exchanges
 *  (ala insertion sort).
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  The {@code MaxPQFromWebsite} class represents a priority queue of generic keys.
 *  It supports the usual <em>insert</em> and <em>delete-the-maximum</em>
 *  operations, along with methods for peeking at the maximum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  <p>
 *  This implementation uses a <em>binary heap</em>.
 *  The <em>insert</em> and <em>delete-the-maximum</em> operations take
 *  &Theta;(log <em>itemAmount</em>) amortized time, where <em>itemAmount</em> is the number
 *  of elements in the priority queue. This is an amortized bound 
 *  (and not a worst-case bound) because of array resizing operations.
 *  The <em>min</em>, <em>size</em>, and <em>is-empty</em> operations take 
 *  &Theta;(1) time in the worst case.
 *  Construction takes time proportional to the specified capacity or the
 *  number of items used to initialize the data structure.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *  @param <Item> the generic type of key on this priority queue
 */

public class MaxPQFromWebsite<Item> implements Iterable<Item> {
    private Item[] itemHeap;                    // store items at indices 1 to itemAmount
    private int itemAmount;                       // number of items on priority queue
    private Comparator<Item> comparator;  // optional comparator

    /**
     * Initializes an empty priority queue with the given initial capacity.
     *
     * @param  initCapacity the initial capacity of this priority queue
     */
    public MaxPQFromWebsite(int initCapacity) {
        itemHeap = (Item[]) new Object[initCapacity + 1];
        itemAmount = 0;
    }

    /**
     * Initializes an empty priority queue.
     */
    public MaxPQFromWebsite() {
        this(1);
    }

    /**
     * Initializes an empty priority queue with the given initial capacity,
     * using the given comparator.
     *
     * @param  initCapacity the initial capacity of this priority queue
     * @param  comparator the order in which to compare the keys
     */
    public MaxPQFromWebsite(int initCapacity, Comparator<Item> comparator) {
        this.comparator = comparator;
        itemHeap = (Item[]) new Object[initCapacity + 1];
        itemAmount = 0;
    }

    /**
     * Initializes an empty priority queue using the given comparator.
     *
     * @param  comparator the order in which to compare the keys
     */
    public MaxPQFromWebsite(Comparator<Item> comparator) {
        this(1, comparator);
    }

    /**
     * Initializes a priority queue from the array of items.
     * Takes time proportional to the number of items, using sink-based heap construction.
     *
     * @param  items the array of items
     */
    public MaxPQFromWebsite(Item[] items) {
        itemAmount = items.length;
        itemHeap = (Item[]) new Object[items.length + 1];
        for (int i = 0; i < itemAmount; i++)
            itemHeap[i+1] = items[i];
        for (int k = itemAmount /2; k >= 1; k--)
            sink(k);
        assert isMaxHeap();
    }



    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
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
    public Item max() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return itemHeap[1];
    }

    // helper function to double the size of the heap array
    private void resize(int capacity) {
        assert capacity > itemAmount;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 1; i <= itemAmount; i++) {
            temp[i] = itemHeap[i];
        }
        itemHeap = temp;
    }


    /**
     * Adds a new key to this priority queue.
     *
     * @param  newItem the new key to add to this priority queue
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
     * Removes and returns a largest key on this priority queue.
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
        itemHeap[itemAmount +1] = null;     // to avoid loitering and help with garbage collection

        // 删除元素后，查看是不是需要调整 数组的容量大小
        if ((itemAmount > 0) && (itemAmount == (itemHeap.length - 1) / 4)) resize(itemHeap.length / 2);
        assert isMaxHeap();
        return maxItemInHeap;
    }


    /***************************************************************************
     * Helper functions to restore the heap invariant.
     ***************************************************************************/

    private void swim(int currentSpot) {
        while (currentSpot > 1 && less(currentSpot/2, currentSpot)) {
            exch(currentSpot, currentSpot/2);
            currentSpot = currentSpot/2;
        }
    }

    private void sink(int currentSpot) {
        while (2*currentSpot <= itemAmount) {
            int biggerChildSpot = 2*currentSpot;
            if (biggerChildSpot < itemAmount && less(biggerChildSpot, biggerChildSpot+1)) biggerChildSpot++;
            if (!less(currentSpot, biggerChildSpot)) break;
            exch(currentSpot, biggerChildSpot);
            currentSpot = biggerChildSpot;
        }
    }

    /***************************************************************************
     * Helper functions for compares and swaps.
     ***************************************************************************/
    private boolean less(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Item>) itemHeap[i]).compareTo(itemHeap[j]) < 0;
        }
        else {
            return comparator.compare(itemHeap[i], itemHeap[j]) < 0;
        }
    }

    private void exch(int i, int j) {
        Item swap = itemHeap[i];
        itemHeap[i] = itemHeap[j];
        itemHeap[j] = swap;
    }

    // is itemHeap[1..itemAmount] a max heap?
    private boolean isMaxHeap() {
        // 堆的性质1 - 完全二叉树 aka 数组中的元素连续且不为null
        for (int cursor = 1; cursor <= itemAmount; cursor++) {
            if (itemHeap[cursor] == null) return false;
        }
        // 堆的性质2 - 使用数组表示的完全二叉树 aka 数组中其他的位置上不能有元素
        for (int i = itemAmount +1; i < itemHeap.length; i++) {
            if (itemHeap[i] != null) return false;
        }
        // 堆的约定 - 数组的第一个位置不存放任何元素（方便数组下标index 与 元素在二叉树中位置spot之间的转换）
        if (itemHeap[0] != null) return false;
        return isMaxHeapOrdered(1);
    }

    // is subtree of itemHeap[1..itemAmount] rooted at currentSpot a max heap?
    private boolean isMaxHeapOrdered(int currentSpot) {
        if (currentSpot > itemAmount) return true;
        int leftChildSpot = 2*currentSpot;
        int rightChildSpot = 2*currentSpot + 1;
        if (leftChildSpot  <= itemAmount && less(currentSpot, leftChildSpot))  return false;
        if (rightChildSpot <= itemAmount && less(currentSpot, rightChildSpot)) return false;

        // 以当前节点的左右子节点作为根节点 的树，也是一个最大堆 - 堆的定义的递归性
        return isMaxHeapOrdered(leftChildSpot) && isMaxHeapOrdered(rightChildSpot);
    }


    /***************************************************************************
     * Iterator. 支持迭代 - for循环
     ***************************************************************************/

    /**
     * Returns an iterator that iterates over the keys on this priority queue
     * in descending order.
     * The iterator doesn't implement {@code remove()} since it's optional.
     *
     * @return an iterator that iterates over the keys in descending order
     */
    public Iterator<Item> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Item> {

        // 迭代器的操作可能会改变 队列中的元素，所以这里拷贝了 原始对象的一个副本
        private MaxPQFromWebsite<Item> copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            // 初始化 优先队列对象
            if (comparator == null) copy = new MaxPQFromWebsite<Item>(size());
            else                    copy = new MaxPQFromWebsite<Item>(size(), comparator);
            // 初始化 队列中的元素
            for (int i = 1; i <= itemAmount; i++)
                copy.insert(itemHeap[i]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        // 获取到队列中下一个位置上的元素 - 手段：删除掉当前的堆顶元素，并返回
        public Item next() {
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
        MaxPQFromWebsite<String> pq = new MaxPQFromWebsite<String>();

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
package com.henry.sort_chapter_02.priority_queue_04.pq_heap_implement_03;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MaxPQFromBook<Item extends Comparable<Item>> implements Iterable<Item>{
    private Item[] itemHeap;
    private int itemAmount;

    // 自定义的比较器
    private Comparator customComparator;


    // 五种不同的构造方法
    public MaxPQFromBook() {
        this(1);
    }

    public MaxPQFromBook(int capacity) {
        itemHeap = (Item[]) new Comparable[capacity + 1];
        itemAmount = 0;
    }

    public MaxPQFromBook(int initCapacity, Comparator<Item> comparator) {
        itemHeap = (Item[])new Comparable[initCapacity + 1];
        this.customComparator = comparator;
        itemAmount = 0;
    }
    public MaxPQFromBook(Comparator<Item> comparator) {
        this(1, comparator);
    }

    public MaxPQFromBook(Item[] itemArray) {
        itemAmount = itemArray.length;

        itemHeap = (Item[])new Comparable[itemArray.length + 1]; // Object[]
        for (int i = 0; i < itemAmount; i++) {
            itemHeap[i + 1] = itemArray[i];
        }

        // 初始化堆
        for (int currentSpot = itemAmount / 2; currentSpot >= 1 ; currentSpot--) {
            sink(currentSpot);
        }

        // 断言：我们已经得到了一个 堆有序的数组
        assert isMaxHeap();
    }

    // 判断 当前的itemHeap 是不是一个最大堆
    /*
        手段： 二叉树的结构要求 + 堆的数值要求
     */
    private boolean isMaxHeap() {
        // 堆区间的元素
        for (int cursor = 1; cursor <= itemAmount; cursor++) {
            if(itemHeap[cursor] == null) return false;
        }

        // 堆区间外的数组元素
        for (int cursor = itemAmount + 1; cursor < itemHeap.length; cursor++) {
            if (itemHeap[cursor] != null) return false;
        }

        // 第一个数组元素
        if (itemHeap[0] != null) return false;

        return isMaxHeapSorted(1);
    }

    // 判断堆 是否是 最大堆有序的状态
    private boolean isMaxHeapSorted(int currentRootNodeSpot) {
        if (currentRootNodeSpot > itemAmount) return true;

        int leftChildSpot = currentRootNodeSpot * 2;
        int rightChildSpot = currentRootNodeSpot * 2 + 1;

        if (leftChildSpot <= itemAmount && less(currentRootNodeSpot, leftChildSpot)) return false;
        if (rightChildSpot <= itemAmount && less(currentRootNodeSpot, rightChildSpot)) return false;

        return isMaxHeapSorted(leftChildSpot) && isMaxHeapSorted(rightChildSpot);
    }

    public boolean isEmpty() {
        return itemAmount == 0;
    }

    public int size() {
        return itemAmount;
    }

    // 核心APIs

    /**
     * 向优先队列中插入一个指定的元素
     *
     * @param newItem
     */
    public void insert(Item newItem) {
        // 在插入元素之前，先查看是否需要对数组进行扩容
        // 当 元素数量 = 数组容量 - 1时，表示堆已经满员了
        if(itemAmount == itemHeap.length - 1) resize(itemHeap.length * 2);

        itemHeap[++itemAmount] = newItem; // itemAmount=队列中的元素个数 需要的索引是N+1 使用++N能够一步到位
        swim(itemAmount); // 参数：元素的索引 aka 数组的最后一个位置

        assert isMaxHeap();
    }

    private void resize(int newCapacity) {
        Item[] newItemHeap = (Item[])new Comparable[newCapacity];

        for (int cursor = 1; cursor <= itemAmount; cursor++) {
            newItemHeap[cursor] = itemHeap[cursor];
        }

        itemHeap = newItemHeap;
    }

    /**
     * 从优先队列中删除最大的元素
     */
    public Item delMax() {
        // 先获取到最大元素
        Item maxItem = itemHeap[1];

        // 恢复二叉堆的平衡
        // 1 交换 顶点位置的元素(最大元素) 与 最后一个位置上的元素
        exch(1, itemAmount--);
        // 物理删除掉最后一个位置上的元素
        itemHeap[itemAmount + 1] = null;
        // 2 对顶点位置的元素 执行下沉操作————这会恢复二叉堆的平衡
        sink(1);

        assert isMaxHeap();
        return maxItem;
    }

    /**
     * 对指定位置的元素执行下沉操作
     *
     * @param currentSpot
     */
    private void sink(int currentSpot) {
        // 执行下沉操作的条件 - 当前节点有子节点 && 当前节点的值 < 它的较大子节点的值
        while (2 * currentSpot <= itemAmount) {
            // 计算j/更新j
            int biggerChildSpot = 2 * currentSpot;
            if (less(biggerChildSpot, biggerChildSpot + 1)) {
                biggerChildSpot++;
            }

            // 编写过程优化 这里的if/else可以优化成if(!xxx)的形式
//            if (less(currentSpot, biggerChildSpot)) {
//                exch(currentSpot, biggerChildSpot);
//            } else { // 如果当前节点的值并没有小于它最大的子节点...
//                // 就不需要进行交换 后继过程也不需要了
//                break;
//            }
            // 比起上面的写法，这种方式👇就少了一个else子句
            if (!less(currentSpot, biggerChildSpot)) {
                break;
            }
            exch(currentSpot, biggerChildSpot); // 省略了else的语句

            // 更新游标位置
            currentSpot = biggerChildSpot;
        }
    }


    /**
     * 对当前节点进行上浮操作；
     * 原因：当前节点大于它的父节点
     *
     * @param currentSpot
     */
    private void swim(int currentSpot) {
        // 把上浮操作的两个条件 && 在一起 - 作为循环进行的条件
        while (currentSpot > 1 && less(currentSpot / 2, currentSpot)) {
            exch(currentSpot / 2, currentSpot);

            // 更新当前指针，继续进行上浮操作
            currentSpot = currentSpot / 2;
        }
    }

    /**
     * 交换数组中两个位置上的元素
     *
     * @param i
     * @param k
     */
    private void exch(int i, int k) {
        Item temp = itemHeap[i];
        itemHeap[i] = itemHeap[k];
        itemHeap[k] = temp;
    }

    /**
     * 比较数组中两个位置上的元素
     * @param i
     * @param k
     * @return
     */
    private boolean less(int i, int k) {
        return itemHeap[i].compareTo(itemHeap[k]) < 0;
    }

    public static void main(String[] args) {
        MaxPQFromWebsite<String> pq = new MaxPQFromWebsite<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
        }
        StdOut.println("(" + pq.size() + " left on itemHeap)");
    }

    // 使当前数据结构(优先队列)中的元素 能够支持迭代操作
    @Override
    public Iterator<Item> iterator() {
        return new MyHeapIterator();
    }

    private class MyHeapIterator implements Iterator<Item> {

        private MaxPQFromBook<Item> copy;

        public MyHeapIterator() {
            // 初始化 优先队列对象   手段：创建新对象 并 绑定到copy上
            if (customComparator == null) copy = new MaxPQFromBook<>(size());
            else copy = new MaxPQFromBook<>(size(), customComparator);

            // 初始化 优先队列元素   手段：把 元素 逐一添加到 队列中
            for (int cursor = 1; cursor <= itemAmount; cursor++) {
                copy.insert(itemHeap[cursor]);
            }
        }

        @Override
        public boolean hasNext() {
            return copy.isEmpty();
        }

        // 具体要怎么进行迭代，需要在next()方法中来实现
        /*
            手段：这里使用有破坏性的delMax() 来 实现next()
            特征：这种方式其实会破坏 原始的数据 - 我们并不想要迭代操作破坏 被迭代的对象
            解决手段： 让当前类 持有一个 待迭代对象的副本
         */
        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }

    }
}

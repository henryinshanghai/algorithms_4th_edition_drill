package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_heap_implement_03; /******************************************************************************
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
 * 手段：使用堆这种逻辑结构 堆 = 满足特定条件的完全二叉树(结构约束：结点从上往下，从左往右逐个排定); 条件：堆有序 aka 对于任意节点，它的值都大于等于它的两个子节点的值。
 * 它支持 常见的 #1 insert新结点的操作 与 #2 删除“值最大的结点”的操作，以及 #3 查看最大的key, #4 测试优先队列是否为空, #5 遍历所有的key的操作
 */
// 结论：可以使用 堆这种逻辑结构 来 实现优先队列(#1 向队列中添加元素； #2 从队列中删除最大元素)；
// 步骤：#1 通过向堆的末尾添加结点并修复breach的方式 来 实现添加队列元素； #2 通过删除堆顶结点并修复breach的方式 来 实现删除最大队列元素；
// 术语：队列元素 <=> 堆结点 | 堆结点 <=> 数组元素、结点在堆中的位置 <=> 数组元素在数组中的位置
public class HeapMaxPQTemplate<Item> implements Iterable<Item> { // 类本身实现了 Iterables接口
    private Item[] arrImplementedHeap;                    // 底层使用“单数组物理结构”来实现“堆逻辑结构” 具体来说，用[1, itemAmount]的区间 来 存储堆结点
    private int itemAmount;                       // 优先队列中的元素数量
    private Comparator<Item> comparator;  // 比较器（可选的）

    /**
     * 初始化优先队列时，指定初始化容量
     * @param initCapacity the initial capacity of this priority queue
     */
    public HeapMaxPQTemplate(int initCapacity) {
        arrImplementedHeap = (Item[]) new Object[initCapacity + 1];
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
        arrImplementedHeap = (Item[]) new Object[initCapacity + 1];
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
        arrImplementedHeap = (Item[]) new Object[items.length + 1];
        for (int i = 0; i < itemAmount; i++)
            arrImplementedHeap[i + 1] = items[i];

        // 构造出一个堆 - 手段：使用sink()方法 排定一半的元素
        // 原理：如果数组中，前面一半的元素都已经满足“堆有序”的话，则：整个数组必然是堆有序的
        // 原因：对某个位置，执行了sink(index)后，则：这个位置上的节点 就一定会大于 它的子节点了。
        // 因此保证前一半的节点被排定后，剩下的节点必然也符合 堆对元素的数值约束了
        for (int k = itemAmount / 2; k >= 1; k--)
            sinkNodeOn(k);
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
    public Item getMaxItem() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return getTopNodeInHeap();
    }

    private Item getTopNodeInHeap() {
        return arrImplementedHeap[1];
    }

    // 为堆数组执行扩容
    private void resize(int capacity) {
        assert capacity > itemAmount;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 1; i <= itemAmount; i++) {
            temp[i] = arrImplementedHeap[i];
        }
        arrImplementedHeap = temp;
    }


    /**
     * 向优先队列中添加一个新的item
     *
     * @param newItem the new item to add to this priority queue
     */
    public void insert(Item newItem) { // 对于优先队列，使用者使用insert()时，只会提供一个item
        // #1 如果数组中元素的数量 与 数组的长度相等，说明堆空间已经满了，则：在插入之前，先把数组空间翻倍
        resizeHeapSizeAsNeededOnInsertion();

        /* #2 把元素作为结点添加到堆中，然后维护堆的约束/不变性 - 对于堆，我们使用“堆结点”的术语 */
        performInsertingNewNodeToHeap(newItem);

        assert isMaxHeap();
    }

    private void performInsertingNewNodeToHeap(Item newItem) {
        // #2-1 把新元素添加到 堆的最后一个叶子节点的下一个位置    手段：把新元素添加到数组末尾；
        addNewNodeAfterLastSpot(newItem);
        // #2-2 添加完新节点后，维护堆的约束(对于任意节点，它的值都大于等于它的两个子节点的值) 手段：利用“数组元素之间的关系”来适当地处理新添加的元素
        fixBreachIntroducedByAdding();
    }

    private void fixBreachIntroducedByAdding() {
        swimNodeOn(itemAmount);
    }

    private void addNewNodeAfterLastSpot(Item newItem) {
        arrImplementedHeap[++itemAmount] = newItem;
    }

    private void resizeHeapSizeAsNeededOnInsertion() {
        if (itemAmount == arrImplementedHeap.length - 1)
            resize(2 * arrImplementedHeap.length);
    }

    /**
     * 移除并返回 优先队列中最大的item
     *
     * @return a largest item on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Item delMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");

        Item maxNodeInHeap = retrieveMaxNodeOfHeap();
        performDeletingHeapsMaxNode();
        postDeletingMaxNode();

        assert isMaxHeap();
        return maxNodeInHeap;
    }

    private Item retrieveMaxNodeOfHeap() {
        Item maxNodeInHeap = arrImplementedHeap[1];
        return maxNodeInHeap;
    }

    private void postDeletingMaxNode() {
        // 物理移除堆中的最后一个结点
        removeLastNodePhysically();
        // 根据删除的情况 来 决定要不要减少堆空间
        resizeHeapAsNeededOnDeletion();
    }

    private void performDeletingHeapsMaxNode() {
        // 把 堆顶结点(aka itemHeap[1]) 与 堆中的最后一个结点 交换
        exchTopNodeWithLastNode();
        // 逻辑上移除堆中的最后一个结点
        removeLastNodeLogically();
        // 修复堆中可能存在的breach
        fixBreachIntroducedByExchanging();
    }

    private void resizeHeapAsNeededOnDeletion() {
        // 删除元素后，查看是不是需要调整 数组的容量大小
        if ((itemAmount > 0) && (itemAmount == (arrImplementedHeap.length - 1) / 4)) resize(arrImplementedHeap.length / 2);
    }

    private void removeLastNodePhysically() {
        // 删除数组中最后一个位置上的元素(它已经不属于堆) 以防止对象游离
        arrImplementedHeap[itemAmount + 1] = null;     // to avoid loitering and help with garbage collection
    }

    private void fixBreachIntroducedByExchanging() {
        // 下沉堆中第一个位置上的元素， 来 维持数组的堆有序
        sinkNodeOn(1);
    }

    private void removeLastNodeLogically() {
        itemAmount--;
    }

    private void exchTopNodeWithLastNode() {
        int heapTopSpot = 1;
        int heapLastSpot = itemAmount;
        exch(heapTopSpot, heapLastSpot);
    }


    /***************************************************************************
     * Helper functions to restore the heap invariant（堆的不变性）.
     ***************************************************************************/

    // 把指定位置上的结点 上浮到 堆中正确的位置 - 作用：恢复完全二叉树的“堆有序”
    private void swimNodeOn(int currentSpot) {
        // #1 如果父节点 小于 当前节点，说明违反了堆的约束，则：
        while (currentSpot > 1 && less(currentSpot / 2, currentSpot)) {
            // 把结点上浮一层
            exch(currentSpot, currentSpot / 2);

            // #2 继续考察 交换到的位置
            currentSpot = currentSpot / 2;
        }
    }

    // 把指定位置上的元素（更小的元素） 下沉到 堆中正确的位置  - 作用：恢复完全二叉树的“堆有序”
    private void sinkNodeOn(int currentSpot) {
        while (2 * currentSpot <= itemAmount) {
            // #1 找出 当前节点的较大的子节点的位置
            int biggerChildSpot = 2 * currentSpot;
            if (biggerChildSpot < itemAmount && less(biggerChildSpot, biggerChildSpot + 1)) biggerChildSpot++;

            // #2 如果 当前节点 比 它较大的子节点 更小，说明违反了堆的约束，则：
            if (!less(currentSpot, biggerChildSpot)) break;
            // 把它与较大的子节点交换
            exch(currentSpot, biggerChildSpot);

            // #3 继续考察 交换到的位置
            currentSpot = biggerChildSpot;
        }
    }

    /***************************************************************************
     * Helper functions for compares and swaps（比较与交换）.
     **************************************************************************
     * @param spotI
     * @param spotJ*/
    // 比较堆中 位置i与位置j上的元素大小
    // 手段：获取到 对应位置上的数组元素，进行比较
    private boolean less(int spotI, int spotJ) {
        Item itemOnSpotJ = arrImplementedHeap[spotJ];

        if (comparator == null) {
            Comparable<Item> itemOnSpotI = (Comparable<Item>) arrImplementedHeap[spotI];
            return itemOnSpotI.compareTo(itemOnSpotJ) < 0;
        } else {
            return comparator.compare(arrImplementedHeap[spotI], itemOnSpotJ) < 0;
        }
    }

    // 交换堆中 位置i与位置j上的结点
    // 手段：获取到 对应位置上的数组元素，进行交换
    private void exch(int spotI, int spotJ) {
        Item swap = arrImplementedHeap[spotI];
        arrImplementedHeap[spotI] = arrImplementedHeap[spotJ];
        arrImplementedHeap[spotJ] = swap;
    }

    // 判断当前的数组 是否是 一个二叉堆？ 原理：根据二叉堆的特性
    private boolean isMaxHeap() {
        // 堆的性质1 - 完全二叉树 aka 数组中的元素连续且不为null
        for (int cursor = 1; cursor <= itemAmount; cursor++) {
            if (arrImplementedHeap[cursor] == null) return false;
        }
        // 堆的性质2 - 使用数组表示的完全二叉树 aka 数组中其他的位置上不能有元素
        for (int i = itemAmount + 1; i < arrImplementedHeap.length; i++) {
            if (arrImplementedHeap[i] != null) return false;
        }
        // 堆的约定 - 数组的第一个位置不存放任何元素（方便数组下标index 与 元素在二叉树中位置spot之间的转换）
        if (arrImplementedHeap[0] != null) return false;
        return isMaxHeapOrdered(1);
    }

    // 以当前spot作为根节点的子树 是不是一个max heap?
    // 手段：#1 + #2
    private boolean isMaxHeapOrdered(int currentSpot) {
        if (currentSpot > itemAmount) return true;
        int leftChildSpot = 2 * currentSpot;
        int rightChildSpot = 2 * currentSpot + 1;
        // #1 数值约束：当前位置上结点的值 > 其对应的左右子结点值；
        if (leftChildSpot <= itemAmount && less(currentSpot, leftChildSpot)) return false;
        if (rightChildSpot <= itemAmount && less(currentSpot, rightChildSpot)) return false;

        // #2 结构约束：以当前节点的左右子节点作为根节点 的“（递归）子树”，也是一个最大堆 - 堆的定义的递归性
        return isMaxHeapOrdered(leftChildSpot) && isMaxHeapOrdered(rightChildSpot);
    }


    /***************************************************************************
     * Iterator. 用于支持迭代语法 - 比如for循环
     ***************************************************************************/

    /**
     * 返回一个迭代器 它会以降序的方式 来 遍历优先队列中的所有item
     * 当前迭代器 没有实现 remove()方法 - 因为这个方法是可选的
     */
    public Iterator<Item> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Item> {

        // 迭代器的操作可能会改变 队列中的元素，所以这里拷贝了 原始对象的一个副本
        private HeapMaxPQTemplate<Item> copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no items move
        public HeapIterator() {
            // 初始化 优先队列对象
            if (comparator == null) copy = new HeapMaxPQTemplate<Item>(size());
            else copy = new HeapMaxPQTemplate<Item>(size(), comparator);
            // 初始化 队列中的元素
            for (int i = 1; i <= itemAmount; i++)
                copy.insert(arrImplementedHeap[i]);
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
        HeapMaxPQTemplate<String> maxPQ = new HeapMaxPQTemplate<String>();

        while (!StdIn.isEmpty()) { // 判断标准输入流是否为空
            // 读取标准输入流中的字符串
            String item = StdIn.readString();
            // 如果当前字符串不是 - 就把它添加到 优先队列中
            if (!item.equals("-")) maxPQ.insert(item);
                // 如果遇到了 - 字符，就删除掉并打印 优先队列中当前的最大元素
            else if (!maxPQ.isEmpty()) StdOut.print(maxPQ.delMax() + " ");
        }

        StdOut.println("(" + maxPQ.size() + " left on itemHeap)");
    }
}
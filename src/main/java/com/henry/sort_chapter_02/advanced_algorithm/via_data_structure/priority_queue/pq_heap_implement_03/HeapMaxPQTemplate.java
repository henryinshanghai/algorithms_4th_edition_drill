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
 * 这个类 表示的是 由泛型key组成的优先队列（逻辑结构）。
 * 手段：使用 堆 这种逻辑结构 堆 = 满足“特定条件”的完全二叉树(结构约束：结点从上往下，从左往右逐个排定);
 * 特定条件：堆有序 aka 对于任意节点，它的值都 大于等于 它的两个子节点的值。
 * 它支持 常见的 ① insert新结点的操作 与 ② 删除“值最大的结点”的操作，以及 ③ 查看最大的key, ④ 测试优先队列是否为空, ⑤ 遍历所有的key的操作
 */
// 结论：可以使用 堆这种逻辑结构 来 实现优先队列(#1 向队列中 添加元素； #2 从队列中 删除最大元素)；
// 步骤：#1 通过 向堆的末尾 添加结点 并 修复breach的方式 来 实现 添加队列元素； #2 通过 删除堆顶结点 并 修复breach的方式 来 实现 删除最大队列元素；
// 术语：队列元素 <=> 堆结点 | 堆结点 <=> 数组元素、结点在堆中的位置 <=> 数组元素在数组中的位置
// 底层的元素数组 使用[1, itemAmount]的区间 来 存储堆中的元素，因此 spot_in_heap（从1开始编号元素） = spot_in_arr（从1开始存储元素）
public class HeapMaxPQTemplate<Item> implements Iterable<Item> { // 类本身 实现了 Iterables接口
    private Item[] arrImplementedHeap;   // 底层 使用“单数组物理结构” 来 实现“堆逻辑结构” 具体来说，用[1, itemAmount]的区间 来 存储堆结点
    private int itemAmount;              // 优先队列中的元素数量
    private Comparator<Item> comparator; // 比较器（可选的）

    /**
     * 初始化 优先队列 时，指定 初始化容量
     *
     * @param initCapacity 优先队列的初始容量
     */
    public HeapMaxPQTemplate(int initCapacity) {
        arrImplementedHeap = (Item[]) new Object[initCapacity + 1];
        itemAmount = 0;
    }

    /**
     * 初始化一个 空的优先队列
     */
    public HeapMaxPQTemplate() {
        this(1);
    }

    /**
     * 初始化 优先队列 时，指定 初始容量 与 比较器
     */
    public HeapMaxPQTemplate(int initCapacity, Comparator<Item> comparator) {
        this.comparator = comparator;
        arrImplementedHeap = (Item[]) new Object[initCapacity + 1];
        itemAmount = 0;
    }

    /**
     * 初始化 优先队列 时，指定 比较器
     */
    public HeapMaxPQTemplate(Comparator<Item> comparator) {
        this(1, comparator);
    }

    /**
     * 从数组元素中 初始化 得到一个优先队列
     * 会花费 与元素数量正相关 的时间，使用 基于sink操作的堆结构
     *
     * @param items 元素数组
     */
    public HeapMaxPQTemplate(Item[] items) {
        itemAmount = items.length;
        arrImplementedHeap = (Item[]) new Object[items.length + 1];

        // 🐖 底层数组是 1-base的，第0个位置不使用
        for (int currentSpot = 0; currentSpot < itemAmount; currentSpot++)
            arrImplementedHeap[currentSpot + 1] = items[currentSpot];

        // 构造出一个堆 - 手段：使用 sink()方法 来 排定一半的元素
        // 原理：如果 数组中，前面一半的元素 都已经满足 “堆有序”的话，则：整个数组 必然是 堆有序的
        // 原因：对 某个位置，执行了 sink(index) 后，则：这个位置上的节点 就一定会大于 它的子节点了。
        // 因此保证 前一半的节点 被排定 后，剩下的节点 必然也符合 堆 对元素的数值约束 了
        for (int currentSpotInHeap = itemAmount / 2; currentSpotInHeap >= 1; currentSpotInHeap--)
            sinkNodeOn(currentSpotInHeap);
        assert isMaxHeap();
    }


    /**
     * 在优先队列为空时，返回true
     *
     * @return {@code true} 如果优先队列为空，则 返回true
     * {@code false} 否则 返回false
     */
    public boolean isEmpty() {
        return itemAmount == 0;
    }

    /**
     * 返回 优先队列中 key的数量
     *
     * @return 优先队列中key的数量
     */
    public int size() {
        return itemAmount;
    }

    /**
     * 返回优先队列中最大的key
     *
     * @return 优先队列中的最大key
     * @throws NoSuchElementException 如果优先队列为空
     */
    public Item getMaxItem() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return getTopNodeInHeap();
    }

    private Item getTopNodeInHeap() {
        return arrImplementedHeap[1];
    }

    // 为 堆数组 执行扩容
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
     * @param newItem 向优先队列中所添加的新元素
     */
    public void insert(Item newItem) { // 对于 优先队列，使用者 使用insert() 时，只会 提供一个item
        // #1 如果 数组中元素的数量 与 数组的长度 相等，说明 堆空间 已经满了，则：在 插入 之前，先 把 数组空间 翻倍
        resizeHeapSizeAsNeededOnInsertion();

        /* #2 把 元素 作为结点 添加到堆中，然后 维护 堆的约束/不变性 - 对于 堆，我们使用 “堆结点”的术语 */
        performInsertingNewNodeToHeap(newItem);

        assert isMaxHeap();
    }

    private void performInsertingNewNodeToHeap(Item newItem) {
        // #2-1 把 新元素 添加到 堆的最后一个叶子节点的 下一个位置    手段：把 新元素 添加到 数组末尾；
        addNewNodeAfterLastSpot(newItem);
        // #2-2 添加完 新节点 后，维护 堆的约束(对于 任意节点，它的值都 大于等于 它的两个子节点的值)
        // 手段：利用“数组元素之间的关系” 来 适当地处理 新添加的元素
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
     * @return 优先队列中的最大元素
     * @throws NoSuchElementException 如果优先队列为空
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
        // 物理移除 堆中的最后一个结点
        removeLastNodePhysically();
        // 根据 删除的情况 来 决定 要不要 减少堆空间
        resizeHeapAsNeededOnDeletion();
    }

    private void performDeletingHeapsMaxNode() {
        // 把 堆顶结点(aka itemHeap[1]) 与 堆中的最后一个结点 交换
        exchTopNodeWithLastNode();
        // 逻辑上 移除 堆中的最后一个结点
        removeLastNodeLogically();
        // 修复 堆中可能存在的 breach
        fixBreachIntroducedByExchanging();
    }

    private void resizeHeapAsNeededOnDeletion() {
        // 删除元素后，查看 是不是需要调整 数组的容量大小
        if ((itemAmount > 0) && (itemAmount == (arrImplementedHeap.length - 1) / 4))
            resize(arrImplementedHeap.length / 2);
    }

    private void removeLastNodePhysically() {
        // 删除 数组中最后一个位置上的元素(它已经不属于堆) 以 防止 对象游离
        arrImplementedHeap[itemAmount + 1] = null;     // to avoid loitering and help with garbage collection
    }

    private void fixBreachIntroducedByExchanging() {
        // 下沉 堆中第一个位置上的元素， 来 维持 数组的堆有序
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
     **************************************************************************
     * @param currentSpotInHeap*/

    /**
     * 把 指定位置上的结点 上浮到 堆中正确的位置
     * 作用：恢复 完全二叉树的 “堆有序”
     * 🐖 堆中元素的位置 是从1开始数的
     * @param currentSpotInHeap 节点在堆中的位置
     */
    private void  swimNodeOn(int currentSpotInHeap) {
        // #1 如果 父节点 小于 当前节点，说明 违反了 堆的约束，则：
        while (currentSpotInHeap > 1 && less(currentSpotInHeap / 2, currentSpotInHeap)) {
            // 把 结点 上浮一层
            exch(currentSpotInHeap, currentSpotInHeap / 2);

            // #2 继续考察 所交换到的位置
            currentSpotInHeap = currentSpotInHeap / 2;
        }
    }

    // 把 指定位置上的元素（更小的元素） 下沉到 堆中正确的位置  - 作用：恢复 完全二叉树的“堆有序”
    // 🐖 堆中元素的位置 是从1开始数的
    private void sinkNodeOn(int currentSpotInHeap) {
        while (2 * currentSpotInHeap <= itemAmount) {
            // #1 找出 当前节点的 较大的子节点的位置
            int biggerChildSpot = 2 * currentSpotInHeap;
            if (biggerChildSpot < itemAmount && less(biggerChildSpot, biggerChildSpot + 1)) biggerChildSpot++;

            // #2 如果 当前节点 不比 它较大的子节点 更小，说明 已经满足了 堆的数值约束，则：
            if (!less(currentSpotInHeap, biggerChildSpot)) {
                // 跳出循环，下沉过程结束
                break;
            }
            // 如果 当前节点 比起 它较大的子节点 更小，说明 违反了 堆的数值约束，则：
            // 把 它 与 较大的子节点 交换
            exch(currentSpotInHeap, biggerChildSpot);

            // #3 继续考察 所交换到的位置
            currentSpotInHeap = biggerChildSpot;
        }
    }

    /***************************************************************************
     * Helper functions for compares and swaps（比较与交换）.
     ***************************************************************************/

    /**
     * 比较堆中 位置i 与 位置j上 的元素大小
     * 手段：获取到 底层元素数组中 对应位置上的 数组元素，进行比较
     * 🐖 堆中元素的位置 = 元素 在底层元素数组中的 位置
     * @param spotIInHeap 堆中 元素的位置i
     * @param spotJInHeap 堆中 元素的位置j
     * @return 元素大小的比较结果
     */
    private boolean less(int spotIInHeap, int spotJInHeap) {
        Item itemOnSpotJ = arrImplementedHeap[spotJInHeap];

        if (comparator == null) {
            Comparable<Item> itemOnSpotI = (Comparable<Item>) arrImplementedHeap[spotIInHeap];
            return itemOnSpotI.compareTo(itemOnSpotJ) < 0;
        } else {
            return comparator.compare(arrImplementedHeap[spotIInHeap], itemOnSpotJ) < 0;
        }
    }

    /**
     * 交换堆中 位置i 与 位置j 上的结点
     * 获取到 底层元素数组中 对应位置上的 数组元素，进行交换
     * @param spotIInHeap   堆中 元素的位置i
     * @param spotJInHeap   堆中 元素的位置j
     */
    private void exch(int spotIInHeap, int spotJInHeap) {
        Item swap = arrImplementedHeap[spotIInHeap];
        arrImplementedHeap[spotIInHeap] = arrImplementedHeap[spotJInHeap];
        arrImplementedHeap[spotJInHeap] = swap;
    }

    // 判断 当前的数组 是否是 一个二叉堆？ 原理：根据 二叉堆的特性
    private boolean isMaxHeap() {
        /* 堆的结构约束 */
        // 堆的性质1 - 完全二叉树 aka 数组中的元素 连续 且 不为null
        for (int currentSpot = 1; currentSpot <= itemAmount; currentSpot++) {
            // 如果 底层的元素数组 在闭区间[1, itemAmount]中 存在有 null元素，说明 违反了 堆的性质1，则：
            if (arrImplementedHeap[currentSpot] == null) {
                // 返回false，表示 不是二叉堆
                return false;
            }
        }
        // 堆的性质2 - 使用数组表示的完全二叉树 aka 数组中 其他的位置上 不能有元素
        for (int currentSpot = itemAmount + 1; currentSpot < arrImplementedHeap.length; currentSpot++) {
            // 如果 底层的元素数组 在区间[itemAmount+1, arrImplementedHeap.length) 中 存在有 非null的元素，说明 违反了 堆的性质2，则：
            if (arrImplementedHeap[currentSpot] != null) {
                // 返回false，表示 不是二叉堆
                return false;
            }
        }
        // 堆的约定3 - 数组的第一个位置 不存放 任何元素（方便 数组下标index 与 元素在二叉树中的位置spot 之间的转换）
        // 如果 底层的元素数组 的 第0个位置上的元素 不是 null元素，说明 违反了 堆的约定3，则:
        if (arrImplementedHeap[0] != null) {
            // 返回false，表示 不是二叉堆
            return false;
        }

        /* 堆的数值约束 */
        return isMaxHeapOrdered(1);
    }

    // 以 当前spot 作为根节点的子树 是不是一个max heap?
    // 手段：#1 + #2
    private boolean isMaxHeapOrdered(int currentSpot) {
        // 如果 当前位置 已经 大于 itemAmount，说明 底层的元素数组 已经通过校验，则：
        if (currentSpot > itemAmount) {
            // 返回 true，表示 是二叉堆
            return true;
        }

        // 计算 当前节点 的 左右子节点的位置
        int leftChildSpot = 2 * currentSpot;
        int rightChildSpot = 2 * currentSpot + 1;

        // #1 当前节点的数值约束：当前位置上 结点的值 > 其对应的左右子结点值；
        if (leftChildSpot <= itemAmount && less(currentSpot, leftChildSpot)) {
            return false;
        }
        if (rightChildSpot <= itemAmount && less(currentSpot, rightChildSpot)) {
            return false;
        }

        // #2 左右子节点的数值约束：以 当前节点的左右子节点 作为根节点 的“（递归）子树”，也是一个最大堆 - 堆的定义的 递归性
        return isMaxHeapOrdered(leftChildSpot) && isMaxHeapOrdered(rightChildSpot);
    }


    /***************************************************************************
     * Iterator. 用于支持 迭代语法 - 比如for循环
     ***************************************************************************/

    /**
     * 返回一个迭代器 它会 以降序的方式 来 遍历 优先队列中的所有item
     * 当前迭代器 没有实现 remove()方法 - 因为这个方法是 可选的
     */
    public Iterator<Item> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Item> {

        // 迭代器的操作 可能会改变 队列中的元素，所以这里 拷贝了 原始对象的一个副本
        private HeapMaxPQTemplate<Item> copy;

        // 把所有的元素 都添加到 堆的拷贝中，由于 元素已经是堆有序了，因此 这里只花费 线性时间
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

        // 获取到 队列中 下一个位置上的元素 - 手段：删除掉 当前的堆顶元素，并 返回
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }
    }

    /**
     * 对 自定义类型的 单元测试
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        HeapMaxPQTemplate<String> maxPQ = new HeapMaxPQTemplate<String>();

        while (!StdIn.isEmpty()) { // 判断 标准输入流 是否为空
            // 读取 标准输入流中 的字符串
            String item = StdIn.readString();
            // 如果 当前字符串 不是 - 就 把它 添加到 优先队列中
            if (!item.equals("-")) maxPQ.insert(item);
                // 如果 遇到了 - 字符，就删除掉并打印 优先队列中当前的最大元素
            else if (!maxPQ.isEmpty()) StdOut.print(maxPQ.delMax() + " ");
        }

        StdOut.println("(" + maxPQ.size() + " left on itemHeap)");
    }
}
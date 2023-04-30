package com.henry.sort_chapter_02.priority_queue_04.pq_indexed_item_implement_04;
/******************************************************************************
 *  Compilation:  javac indexMinPQFromWebsite.java
 *  Execution:    java indexMinPQFromWebsite
 *  Dependencies: StdOut.java
 *
 *  Minimum-oriented indexed PQ implementation using a binary heap.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 当前类 表示一个 能够处理泛型key的索引优先队列。
 * 它支持常见的 insert, delete-the-minimum操作，以及 delete, change-the-key的操作。
 * 为了使客户端能够引用 优先队列中的元素, 每个元素都关联了一个[0, maxN - 1]之间的整数 -> 客户端可以使用这个整数来指定 需要删除或者改变的元素
 * <p>
 * 同时还支持 查看最小元素, 测试优先队列是否为空, 遍历优先队列中的元素
 * <p>
 * 这个实现使用了 一个二叉堆 以及一个数组 来 把元素关联到 指定范围内的整数上。
 * insert, delete-the-minimum, delete, change-key, decrease-key, increase-key 这些操作
 * 都只会花费 logN的时间（最坏情况下） - 其中N使优先队列中的元素数量。
 * <p>
 * 构造所花费的时间 与所指定的容量成正比。
 * <p>
 * <p>
 * 更多文档，请参考：<a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a>
 *
 * @param <Element> the generic type of key on this priority queue
 *                  不是太容易理解 参考：https://blog.csdn.net/weixin_43696529/article/details/104675343 理解一下
 *                  维护 key得到一个优先队列
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class IndexMinPQFromWebsite<Element extends Comparable<Element>> implements Iterable<Integer> {
    private int capacity;        // maximum number of elements on PQ
    private int elementAmount;           // number of elements on PQ

    // 难点：逻辑结构 与 物理结构不再严格对应 - 逻辑结构是一个堆，但是没有任何单一个数组是堆
    // 对于使用者而言，会使用 index -> element的方式把 元素以指定索引插入堆中。
    // 对于底层存储的数据结构，会使用 spot -> index -> element的方式来存储“index 与 element”信息
    // 相比于 简单优先队列的信息存储方式 spot -> element, 这里添加了 index
    // 用来记录 spot -> index的关联信息  f(spot_in_heap/array) = index
    private int[] spotToIndexArray; // 🐖 只有spot才具有连续性，但spotToIndexArray本身并不是一个“堆”

    // 用来记录 index -> element的关联信息 f(index/priority) = element_value
    private Element[] indexToElementArray;

    // 用来记录 index -> spot的关联信息  f(index) = spot_in_heap/array
    private int[] indexToSpotArray;        // 作用： 辅助数组，用于快速找到 特定index “在逻辑堆中的位置spot”


    /**
     * 以索引范围 [0, capacity - 1] 来 初始化一个空的索引有限队列
     * 作用：对Client添加索引时的约束 - Client只能使用 [0, capacity-1]这个区间内的值 作为索引值，来初始化一个空的 索引优先队列
     *
     * @param capacity 声明 优先队列中的元素 所能添加索引的范围是 [0, capacity - 1]
     * @throws IllegalArgumentException if {@code capacity < 0}
     */
    public IndexMinPQFromWebsite(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        elementAmount = 0;

        // 初始化各个数组对象 - 特征：下标为0的位置不使用
        spotToIndexArray = new int[capacity + 1];
        indexToElementArray = (Element[]) new Comparable[capacity + 1];    // make this of length maxN??
        indexToSpotArray = new int[capacity + 1];                   // make this of length maxN??

        // 初始化数组元素为-1 - 用于方便地判断 特定的index是不是已经存在了
        for (int i = 0; i <= capacity; i++)
            indexToSpotArray[i] = -1;
    }

    /**
     * 当前优先队列是否为空？
     *
     * @return {@code true} if this priority queue is empty;
     * {@code false} otherwise
     */
    public boolean isEmpty() {
        return elementAmount == 0;
    }

    /**
     * 在优先队列中 是不是包含 特定的索引值？ - 由于 允许Client使用的索引值从0开始，所以这里区间的右边界为 capacity-1
     *
     * @param index an index
     * @return {@code true} if {@code i} is an index on this priority queue;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     */
    public boolean contains(int index) {
        validateIndex(index);
        return indexToSpotArray[index] != -1;
    }

    /**
     * 返回优先队列中 元素的数量
     *
     * @return the number of keys on this priority queue
     */
    public int size() {
        return elementAmount;
    }

    /**
     * 向优先队列中 添加一个元素 并 为之关联指定索引
     *
     * @param index   an index
     * @param element the element to associate with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if there already is an item associated
     *                                  with index {@code i}
     */
    public void insert(int index, Element element) {
        // 判断Client传入的index是否合法
        validateIndex(index);
        // 判断 Client传入的index 是不是已经被使用过了
        if (contains(index)) throw new IllegalArgumentException("index is already in the priority queue");

        elementAmount++;

        /* 把 index 与 element 正确地添加到 各个数组中 */
        // #1 把当前 index 添加到 elementAmount这个spot上面
        spotToIndexArray[elementAmount] = index;
        // #2 同时把 elementAmount这个spot 添加到 index上面
        indexToSpotArray[index] = elementAmount;
        // #3 把element 添加到 indexToElementArray的index上面
        indexToElementArray[index] = element; // 这个数组是对client传入的信息的忠实记录

        /* 添加完element之后， 维护 spotToIndexArray 与 indexToSpotArray */
        swim(elementAmount);
    }

    /**
     * 返回 最小元素 所关联的索引
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int minIndex() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");
        // 表示优先队列的逻辑堆中 spot=1的元素 就是最小元素，对应的索引 = spotToIndexArray[1]
        return spotToIndexArray[1];
    }

    /**
     * 返回 优先队列中的最小元素
     *
     * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Element minElement() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");
        // 最小元素的spot = 1 -> 最小元素的index = spotToIndexArray[1] -> 最小元素的值 = indexToElementArray[spotToIndexArray[1]]
        return indexToElementArray[spotToIndexArray[1]];
    }

    /**
     * 删除 优先队列中的最小元素 并 返回与之关联的index
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int delMin() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");

        // 获取到 堆中最小元素的索引
        int indexOfMinElement = spotToIndexArray[1];

        /* 删除最小元素 */
        // 1 把最小元素 交换到堆的末尾
        // 🐖 这里的 elementAmount-- 使得 交换到末尾的最小元素不会参与“重建堆”的过程
        exch(1, elementAmount--);
        // 2 重建 最小堆 手段：sink(1)
        sink(1);
        // 断言：最小堆重建完成后，原始堆中的最小元素的索引 会 在“当前堆的最后一个spot”的下一个位置上
        assert indexOfMinElement == spotToIndexArray[elementAmount + 1];
        // 3 删除 数据后，为了防止对象游离 为它们绑定null值
        // 🐖exch() 与 sink()的操作 都不会影响到 elementValueArr数组
        indexToElementArray[indexOfMinElement] = null;    // to help with garbage collection

        /* 处理其他辅助数组 */
        // 删除 对最小元素索引值的记录
        spotToIndexArray[elementAmount + 1] = -1;        // 不再需要对此位置(elementAmount+1)元素的索引 - 将之置为-1

        // 删除 对最小元素索引值的记录
        indexToSpotArray[indexOfMinElement] = -1;        // 索引已经不存在了，索引对应堆元素的位置 也要跟着删除

        // 返回 原始堆中最小元素的索引值
        return indexOfMinElement;
    }

    /**
     * 返回 指定索引 所关联的优先队列元素
     *
     * @param index the index of the key to return
     * @return the key associated with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException   no key is associated with index {@code i}
     */
    public Element ElementOf(int index) {
        // 索引是否有效 & 索引是否存在
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        else return indexToElementArray[index];
    }

    /**
     * 修改优先队列中 指定的索引 所关联的元素值
     *
     * @param index   the index of the key to change Client所传入的索引
     * @param element change the key associated with index {@code i} to this key Client想要修改到的元素值
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException   no key is associated with index {@code i}
     */
    public void changeElement(int index, Element element) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        // 直接修改 indexToElementArray 即可
        indexToElementArray[index] = element;

        // 修改 indexToElementArray后， 维护 spotToIndexArray 与 theSpotOfElementInHeap 有序 - 参数是：元素在堆中的spot
        // 特征：由于这里Client传入的index可能位于 原始堆（逻辑概念）的中间位置，所以需要 上浮 + 下沉 一起进行。
        swim(indexToSpotArray[index]);
        sink(indexToSpotArray[index]);
    }

    /**
     * 修改优先队列中 指定索引 所关联的元素值
     * 注： 这个方法的作用与上面一个完全相同，但是它过时了
     * 过时的API可能已经被Client使用了，怎么办？ 保留过时的API， 并使用 新的API来实现它。然后鼓励 Client使用新的API
     * 特征：对Client来说，新的API的名字与旧的不一样 更能够 见名知意
     *
     * @param i       the index of the key to change
     * @param element change the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @deprecated Replaced by {@code changeKey(int, Key)}.
     */
    @Deprecated
    public void change(int i, Element element) { // 过时的API
        changeElement(i, element);
    }

    /**
     * 减小 优先队列中 指定索引所关联的元素值 到 特定的值
     *
     * @param index   the index of the key to decrease
     * @param element decrease the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key >= keyOf(i)}
     * @throws NoSuchElementException   no key is associated with index {@code i}
     */
    public void decreaseElement(int index, Element element) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        if (indexToElementArray[index].compareTo(element) == 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
        if (indexToElementArray[index].compareTo(element) < 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");


        indexToElementArray[index] = element;
        // 由于 这里元素值是减小的，所以 只需要执行上浮操作 - 因为元素只可能上浮
        swim(indexToSpotArray[index]);
    }

    /**
     * 增大 优先队列中 指定索引所关联的元素值 到特定的值
     *
     * @param index   the index of the key to increase
     * @param element increase the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key <= keyOf(i)}
     * @throws NoSuchElementException   no key is associated with index {@code i}
     */
    public void increaseElement(int index, Element element) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        if (indexToElementArray[index].compareTo(element) == 0)
            throw new IllegalArgumentException("Calling increaseKey() with a key equal to the key in the priority queue");
        if (indexToElementArray[index].compareTo(element) > 0)
            throw new IllegalArgumentException("Calling increaseKey() with a key strictly less than the key in the priority queue");

        indexToElementArray[index] = element;
        // 由于 元素值是增大的， 所以这里就只需要 下沉操作 - 因为元素只可能会下沉
        sink(indexToSpotArray[index]);
    }

    /**
     * 删除 优先队列中 指定索引所关联的元素
     *
     * @param index the index of the key to remove
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException   no key is associated with index {@code i}
     */
    public void delete(int index) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");

        int spotOfIndex = indexToSpotArray[index];
        exch(spotOfIndex, elementAmount--);

        // 删除操作后，恢复堆的性质
        // 既可能会上浮，也可能会下沉
        swim(spotOfIndex);
        sink(spotOfIndex);

        // 清除对此index的记录
        indexToElementArray[index] = null;
        indexToSpotArray[index] = -1;
        spotToIndexArray[elementAmount] = -1;
    }

    // throw an IllegalArgumentException if i is an invalid index [0, maxN - 1]
    private void validateIndex(int i) {
        if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
        if (i >= capacity) throw new IllegalArgumentException("index >= capacity: " + i);
    }

    /***************************************************************************
     * General helper functions.
     **************************************************************************
     * @param spotI
     * @param spotJ*/
    // 比较堆结构（逻辑概念）中，两个spot上的元素
    private boolean greater(int spotI, int spotJ) {
        // 这里比较的是 indexToElementArray[] 中的元素， 因为这个数组中记录的才是 堆节点(逻辑概念)中的元素
        // 传入的i, j其实是 “堆节点在堆中的位置”（逻辑概念），也就是 spotToIndexArray中的spot
        return indexToElementArray[spotToIndexArray[spotI]].compareTo(indexToElementArray[spotToIndexArray[spotJ]]) > 0;
    }

    // 交换堆结构（逻辑概念）中，两个spot上的元素
    private void exch(int spotI, int spotJ) {
        // 这里的exch 需要维护两个数组：spotToIndexArray、indexToSpotArray
        int temp = spotToIndexArray[spotI];
        spotToIndexArray[spotI] = spotToIndexArray[spotJ];
        spotToIndexArray[spotJ] = temp;

        // 获取交换过后，spotToIndex中的index值
        int indexOfSpotI = spotToIndexArray[spotI];
        // 更新 index所对应的 indexToSpot中的spot值
        indexToSpotArray[indexOfSpotI] = spotI;
        int indexOfSpotJ = spotToIndexArray[spotJ];
        indexToSpotArray[indexOfSpotJ] = spotJ;
    }


    /***************************************************************************
     * Heap helper functions.
     **************************************************************************
     * @param currentNodeSpot*/
    private void swim(int currentNodeSpot) {
        // 如果当前spot有效，并且 当前位置节点的父节点(逻辑概念)元素 比起 当前位置的节点(逻辑概念)元素 更大，则：交换这两个位置上的节点
        while (currentNodeSpot > 1 && greater(currentNodeSpot / 2, currentNodeSpot)) {
            exch(currentNodeSpot, currentNodeSpot / 2);
            currentNodeSpot = currentNodeSpot / 2;
        }
    }

    private void sink(int currentNodeSpot) {
        while (2 * currentNodeSpot <= elementAmount) {
            int biggerChildSpot = 2 * currentNodeSpot;
            if (biggerChildSpot < elementAmount && greater(biggerChildSpot, biggerChildSpot + 1)) biggerChildSpot++;


            if (!greater(currentNodeSpot, biggerChildSpot)) break;
            exch(currentNodeSpot, biggerChildSpot);
            currentNodeSpot = biggerChildSpot;
        }
    }


    /***************************************************************************
     * Iterators.
     ***************************************************************************/

    /**
     * Returns an iterator that iterates over the keys on the
     * priority queue in ascending order.
     * The iterator doesn't implement {@code remove()} since it's optional.
     *
     * @return an iterator that iterates over the keys in ascending order
     */
    public Iterator<Integer> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Integer> {
        // create a new pq
        private IndexMinPQFromWebsite<Element> copy;

        // add all elements to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            copy = new IndexMinPQFromWebsite<Element>(spotToIndexArray.length - 1);
            for (int i = 1; i <= elementAmount; i++)
                copy.insert(spotToIndexArray[i], indexToElementArray[spotToIndexArray[i]]);
        }

        public boolean hasNext() {
            return !copy.isEmpty();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }


    /**
     * Unit tests the {@code indexMinPQFromWebsite} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // insert a bunch of strings
        String[] strings = {"it", "was", "the", "best", "of", "times", "it", "was", "the", "worst"};

        // 初始化索引优先队列
        IndexMinPQFromWebsite<String> pq = new IndexMinPQFromWebsite<String>(strings.length); // 10

        // 遍历字符串数组，并逐个插入数组元素到 索引优先队列中
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // 删除并打印每一个值
        while (!pq.isEmpty()) {
            int indexOfMinItem = pq.delMin();
            StdOut.println(indexOfMinItem + " " + strings[indexOfMinItem]);
        }
        StdOut.println();

        System.out.println("----------------------");

    }
}
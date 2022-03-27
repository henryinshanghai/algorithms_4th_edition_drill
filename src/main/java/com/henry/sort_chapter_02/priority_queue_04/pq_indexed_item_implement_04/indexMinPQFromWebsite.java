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
 *  The {@code indexMinPQFromWebsite} class represents an indexed priority queue of generic keys.
 *  It supports the usual <em>insert</em> and <em>delete-the-minimum</em>
 *  operations, along with <em>delete</em> and <em>change-the-key</em>
 *  methods. In order to let the client refer to keys on the priority queue,
 *  an integer between {@code 0} and {@code maxN - 1}
 *  is associated with each key—the client uses this integer to specify
 *  which key to delete or change.
 *  It also supports methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  <p>
 *  This implementation uses a binary heap along with an array to associate
 *  keys with integers in the given range.
 *  The <em>insert</em>, <em>delete-the-minimum</em>, <em>delete</em>,
 *  <em>change-key</em>, <em>decrease-key</em>, and <em>increase-key</em>
 *  operations take &Theta;(log <em>n</em>) time in the worst case,
 *  where <em>n</em> is the number of elements in the priority queue.
 *  Construction takes time proportional to the specified capacity.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *  @param <Element> the generic type of key on this priority queue
 *  不是太容易理解 参考：https://blog.csdn.net/weixin_43696529/article/details/104675343 理解一下
 *
    维护 key得到一个优先队列
 */
public class indexMinPQFromWebsite<Element extends Comparable<Element>> implements Iterable<Integer> {
    private int capacity;        // maximum number of elements on PQ
    private int elementAmount;           // number of elements on PQ

    // 用来存放 堆元素的值 的数组    下标：堆元素的索引   数组元素的含义：堆元素的值 - f(index/priority) = element_value
    private Element[] elementValueArr;

    /*
        为什么 这里要使用堆元素的索引 来作为堆呢？而 简单堆实现中 就可以直接使用 堆元素的值 来作为堆呢？
        分析：使用数组来实现堆时， 需要数组元素能够连续排列；
        对于 简单的堆，因为 堆元素的位置 总是连续排列的(1, 2, 3...)。所以 可以用spot 作为数组下标，堆元素的值 作为数组元素；
        对于 索引优先队列：
            我们需要计入 堆元素的索引信息；
            这时有两种方案可以选择：
            - 1 spot -> index + index -> item
            - 2 spot -> item + item -> index

            对于Client来说，他更想要的API是： 使用一个index 来 获取到元素。所以这里选择使用方案1（index -> item）
            在选定了方案1的情况下，堆的实现就必须是： spot -> index了
     */
    // 用来存放 堆元素的索引 的数组/堆   下标：堆元素在堆中的位置； 数组元素的含义：元素的索引； - f(spot_in_heap/array) = index
    private int[] elementsIndexHeap;
    // 用来存放 堆元素的位置 的数组   下标：堆元素的索引； 数组元素的含义： 堆元素在堆中的位置;  - f(index) = spot_in_heap/array
    private int[] theSpotOfIndexInHeap;        // 作用： 快速找到 特定index 在堆中的位置spot


    /**
     * Initializes an empty indexed priority queue with indices between {@code 0}
     * and {@code capacity - 1}.
     * 用法：对Client添加索引时的约束 - 使用 [0, capacity-1]这个区间内的值 作为索引值，来初始化一个空的 索引优先队列
     * @param  capacity 声明 优先队列中的元素 所能添加索引的范围是 [0, capacity - 1]
     * @throws IllegalArgumentException if {@code capacity < 0}
     */
    public indexMinPQFromWebsite(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        elementAmount = 0;

        // 初始化各个数组对象
        elementValueArr = (Element[]) new Comparable[capacity + 1];    // make this of length maxN??
        elementsIndexHeap = new int[capacity + 1];
        theSpotOfIndexInHeap = new int[capacity + 1];                   // make this of length maxN??

        // 初始化数组元素 - 为了能够方便地判断 特定的index是不是已经存在了
        for (int i = 0; i <= capacity; i++)
            theSpotOfIndexInHeap[i] = -1;
    }

    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return elementAmount == 0;
    }

    /**
     * Is {@code i} an index on this priority queue?
     * 在优先队列中 是不是包含 特定的索引值？ - 由于 允许Client使用的索引值从0开始，所以这里区间的右边界为 capacity-1
     * @param  index an index
     * @return {@code true} if {@code i} is an index on this priority queue;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     */
    public boolean contains(int index) {
        validateIndex(index);
        return theSpotOfIndexInHeap[index] != -1;
    }

    /**
     * Returns the number of keys on this priority queue.
     * 返回优先队列中 元素的总数量
     * @return the number of keys on this priority queue
     */
    public int size() {
        return elementAmount;
    }

    /**
     * Associates key with index {@code i}.
     * 向优先队列中 添加一个元素： 关联的index -> element
     * @param  index an index
     * @param  element the element to associate with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if there already is an item associated
     *         with index {@code i}
     */
    public void insert(int index, Element element) {
        // 判断Client传入的index是否合法
        validateIndex(index);
        // 判断 Client传入的index 是不是已经被使用过了
        if (contains(index)) throw new IllegalArgumentException("index is already in the priority queue");

        /* 把 index 与 element 正确地添加到 各个数组中 */
        // 处理 index
        elementAmount++;
        elementsIndexHeap[elementAmount] = index;
        theSpotOfIndexInHeap[index] = elementAmount;

        // 处理 element
        elementValueArr[index] = element;

        /* 添加完element之后， 维护 elementsIndexHeap 与 theSpotOfIndexInHeap */
        swim(elementAmount);
    }

    /**
     * Returns an index associated with a minimum key.
     * 返回 最小元素 所关联的索引
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int minIndex() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");
        // 表示优先队列的逻辑堆中 spot=1的元素 就是最小元素，对应的索引 = elementsIndexHeap[1]
        return elementsIndexHeap[1];
    }

    /**
     * Returns a minimum key.
     * 返回 优先队列中的最小元素
     * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Element minElement() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");
        // 最小元素的spot = 1 -> 最小元素的index = elementsIndexHeap[1] -> 最小元素的值 = elementValueArr[elementsIndexHeap[1]]
        return elementValueArr[elementsIndexHeap[1]];
    }

    /**
     * Removes a minimum key and returns its associated index.
     * 删除 优先队列中的最小元素 并 返回与之关联的index
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int delMin() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");

        /* 返回 最小元素的索引 - 最小元素的spot是1 -> 最小元素的索引：elementsIndexHeap[1] */
        int indexOfMinElement = elementsIndexHeap[1];

        /* 删除最小元素 */
        /*
            手段：
                1 把最小元素 交换到堆的末尾
                2 重建 index堆 手段：sink(1)
                3 删除 数据后，为了防止对象游离 为它们绑定null值
         */
        exch(1, elementAmount--);
        sink(1);
        // 断言：在重建完index堆之后，最小元素的index 被移动到了 elementsIndexHeap的末尾
        assert indexOfMinElement == elementsIndexHeap[elementAmount +1];

        /* 3 删除 数据后，为了防止对象游离 为它们绑定null值 */
        // 删除 elementValueArr中 索引对应的元素  注：exch() 与 sink()的操作 都不会影响到 elementValueArr数组
        elementValueArr[indexOfMinElement] = null;    // to help with garbage collection

        // 删除 indexOfElementArr中的索引值
        elementsIndexHeap[elementAmount + 1] = -1;        // 不再需要对此位置(elementAmount+1)元素的索引 - 将之置为-1

        // 删除 theSpotOfIndexInHeap中的spot值
        theSpotOfIndexInHeap[indexOfMinElement] = -1;        // 索引已经不存在了，索引对应堆元素的位置 也要跟着删除


        // 返回 indexOfMinElement
        return indexOfMinElement;
    }

    /**
     * Returns the key associated with index {@code i}.
     * 返回 指定索引 所关联的优先队列元素
     * @param  index the index of the key to return
     * @return the key associated with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public Element ElementOf(int index) {
        // 索引是否有效 & 索引是否存在
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        else return elementValueArr[index];
    }

    /**
     * Change the key associated with index {@code i} to the specified value.
     * 修改优先队列中 指定的索引 所关联的元素值
     * @param  index the index of the key to change Client所传入的索引
     * @param  element change the key associated with index {@code i} to this key Client想要修改到的元素值
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void changeElement(int index, Element element) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        // 直接修改 elementValueArr 即可
        elementValueArr[index] = element;

        // 修改 elementValueArr后， 维护 elementsIndexHeap 与 theSpotOfElementInHeap 有序 - 参数是：元素在堆中的spot
        /*
            特征：由于这里Client传入的index可能位于 优先队列堆的中间位置，所以需要 上浮 + 下沉 一起进行。
         */
        swim(theSpotOfIndexInHeap[index]);
        sink(theSpotOfIndexInHeap[index]);
    }

    /**
     * Change the key associated with index {@code i} to the specified value.
     * 修改优先队列中 指定索引 所关联的元素值
     * 注： 这个方法的作用与上面一个完全相同，但是它过时了
     * 过时的API可能已经被Client使用了，怎么办？ 保留过时的API， 并使用 新的API来实现它。然后鼓励 Client使用新的API
     * 特征：对Client来说，新的API的名字与旧的不一样 更能够 见名知意
     * @param  i the index of the key to change
     * @param  element change the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @deprecated Replaced by {@code changeKey(int, Key)}.
     */
    @Deprecated
    public void change(int i, Element element) { // 过时的API
        changeElement(i, element);
    }

    /**
     * Decrease the key associated with index {@code i} to the specified value.
     * 减小 优先队列中 指定索引所关联的元素值
     * @param  index the index of the key to decrease
     * @param  element decrease the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key >= keyOf(i)}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void decreaseElement(int index, Element element) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        if (elementValueArr[index].compareTo(element) == 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
        if (elementValueArr[index].compareTo(element) < 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");


        elementValueArr[index] = element;
        // 由于 这里元素值是减小的，所以 只需要执行上浮操作 - 因为元素只可能上浮
        swim(theSpotOfIndexInHeap[index]);
    }

    /**
     * Increase the key associated with index {@code i} to the specified value.
     * 增大 优先队列中 指定索引所关联的元素值
     * @param  index the index of the key to increase
     * @param  element increase the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key <= keyOf(i)}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void increaseElement(int index, Element element) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        if (elementValueArr[index].compareTo(element) == 0)
            throw new IllegalArgumentException("Calling increaseKey() with a key equal to the key in the priority queue");
        if (elementValueArr[index].compareTo(element) > 0)
            throw new IllegalArgumentException("Calling increaseKey() with a key strictly less than the key in the priority queue");

        elementValueArr[index] = element;
        // 由于 元素值是增大的， 所以这里就只需要 下沉操作 - 因为元素只可能会下沉
        sink(theSpotOfIndexInHeap[index]);
    }

    /**
     * Remove the key associated with index {@code i}.
     * 删除 优先队列中 指定索引所关联的元素
     * @param  index the index of the key to remove
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void delete(int index) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");

        int spotOfIndex = theSpotOfIndexInHeap[index];
        exch(spotOfIndex, elementAmount--);

        // 删除操作后，既可能会上浮，也可能会下沉
        swim(spotOfIndex);
        sink(spotOfIndex);

        // 清除对此index的记录 - 为什么没有对 elementsIndexHeap的操作？ elementsIndexHeap[elementAmount] = -1
        elementValueArr[index] = null;
        theSpotOfIndexInHeap[index] = -1;
    }

    // throw an IllegalArgumentException if i is an invalid index [0, maxN - 1]
    private void validateIndex(int i) {
        if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
        if (i >= capacity) throw new IllegalArgumentException("index >= capacity: " + i);
    }

    /***************************************************************************
     * General helper functions.
     ***************************************************************************/
    private boolean greater(int i, int j) {
        return elementValueArr[elementsIndexHeap[i]].compareTo(elementValueArr[elementsIndexHeap[j]]) > 0;
    }

    // 这里的exch 需要维护两个数组：elementsIndexHeap、theSpotOfIndexInHeap
    private void exch(int i, int j) {
        int temp = elementsIndexHeap[i];
        elementsIndexHeap[i] = elementsIndexHeap[j];
        elementsIndexHeap[j] = temp;

        // 维护 theSpotOfIndex数组 这是一个恒等式
        int indexOfSpotI = elementsIndexHeap[i];
        theSpotOfIndexInHeap[indexOfSpotI] = i;
        int indexOfSpotJ = elementsIndexHeap[j];
        theSpotOfIndexInHeap[indexOfSpotJ] = j;
    }


    /***************************************************************************
     * Heap helper functions.
     ***************************************************************************/
    private void swim(int currentSpot) {
        while (currentSpot > 1 && greater(currentSpot/2, currentSpot)) {
            exch(currentSpot, currentSpot/2);
            currentSpot = currentSpot/2;
        }
    }

    private void sink(int currentSpot) {
        while (2*currentSpot <= elementAmount) {
            int biggerChildSpot = 2*currentSpot;
            if (biggerChildSpot < elementAmount && greater(biggerChildSpot, biggerChildSpot+1)) biggerChildSpot++;


            if (!greater(currentSpot, biggerChildSpot)) break;
            exch(currentSpot, biggerChildSpot);
            currentSpot = biggerChildSpot;
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
    public Iterator<Integer> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Integer> {
        // create a new pq
        private indexMinPQFromWebsite<Element> copy;

        // add all elements to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            copy = new indexMinPQFromWebsite<Element>(elementsIndexHeap.length - 1);
            for (int i = 1; i <= elementAmount; i++)
                copy.insert(elementsIndexHeap[i], elementValueArr[elementsIndexHeap[i]]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

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
        String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

        // 初始化索引优先队列
        indexMinPQFromWebsite<String> pq = new indexMinPQFromWebsite<String>(strings.length); // 10

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
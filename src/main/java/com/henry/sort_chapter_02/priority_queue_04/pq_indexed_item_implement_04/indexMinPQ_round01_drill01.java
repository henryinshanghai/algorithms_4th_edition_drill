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

/*
    算法描述：
        一个能够直接引用其中元素的优先队列 - aka 索引优先队列

        手段：使用两个数组
        数组 elementValueArr 用来存储 element；
        数组 indexOfElementArr 用来存储 index;
        数组 theSpotOfIndexInArr 用来存储 索引在堆中的位置
 */
public class indexMinPQ_round01_drill01<Element extends Comparable<Element>> implements Iterable<Integer> {
    private int capacity;        // maximum number of elements on PQ
    private int elementAmount;           // number of elements on PQ

    // f(index/priority) = element  用于存放 元素
    private Element[] elementValueArr;
    // f(spot_in_heap/array) = index    用于存放 index  特征：排序规则是elementValue
    private int[] indexOfElementArr;
    // f(index) = spot_in_heap/array    用于快速找到 指定的
    private int[] theSpotOfIndexInArr;


    /**
     * Initializes an empty indexed priority queue with indices between {@code 0}
     * and {@code maxN - 1}.
     * 初始化一个空的 索引优先队列 - 允许使用的索引范围：[0, maxN -1]
     * @param  capacity 索引优先队列中的元素 的索引范围： [0, maxN -1]
     * @throws IllegalArgumentException if {@code maxN < 0}
     */
    public indexMinPQ_round01_drill01(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        elementAmount = 0;

        // 初始化实例变量
        // 三个数组的初始容量 都是 capacity + 1
        elementValueArr = (Element[]) new Comparable[capacity + 1];
        indexOfElementArr = new int[capacity + 1];
        theSpotOfIndexInArr = new int[capacity + 1];                   // make this of length maxN??

        // 对 theSpotOfIndexInHeap数组中的元素进行初始化 - 用来支持 index不存在时, 查询index的返回
        for (int i = 0; i <= capacity; i++)
            theSpotOfIndexInArr[i] = -1;
    }

    /**
     * Returns true if this priority queue is empty.
     * 优先队列是否为空？ aka 队列中没有任何元素
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return elementAmount == 0;
    }

    /**
     * Is {@code i} an index on this priority queue?
     * 优先队列中 是否存在 索引为index的元素？
     * @param  index an index
     * @return {@code true} if {@code i} is an index on this priority queue;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     */
    public boolean contains(int index) {
        validateIndex(index);
        // 如果插入了 索引为index的元素, 那么 theSpotOfIndexInArr 就不会为-1（这是初始化给数组元素的值）
        return theSpotOfIndexInArr[index] != -1;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int size() {
        return elementAmount;
    }

    /**
     * Associates key with index {@code i}.
     * 向优先队列中插入一个 索引为index的元素element
     * @param  index an index
     * @param  element the key to associate with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if there already is an item associated
     *         with index {@code i}
     */
    public void insert(int index, Element element) {
        // Client输入是否合法
        validateIndex(index);
        // Client输入的index 在优先队列中是否已经存在
        if (contains(index)) throw new IllegalArgumentException("index is already in the priority queue");

        /* 向优先队列中添加元素 */
        // 1 把 element添加到 elementArr的index位置上去
        elementValueArr[index] = element;

        // 2 维护剩下的两个数组
        /*
            存放index的数组：把index 添加到数组的末尾，并对数组进行 堆有序操作
            存放spot的数组：新添加的index 总是会被放在 elementIndexHeap的末尾(aka itemAmount的位置)
         */
        indexOfElementArr[++elementAmount] = index;
        theSpotOfIndexInArr[index] = elementAmount;

        // 对 indexOfElementHeap进行 堆有序的操作 & 维护 theSpotOfIndexHeap的数据 与堆有序的indexOfElementHeap一致
        swim(elementAmount);
    }

    /**
     * Returns an index associated with a minimum key.
     * 返回 索引优先队列中 与最小元素相关联的index
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int minIndex() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");
        // indexOfElementArr本身并不是一个优先队列，因为 比较操作的依据并不是它自己的元素，而是ElementArr中的元素
        // indexOfElementArr[1]中存储的就是 最小元素的index
        return indexOfElementArr[1];
    }

    /**
     * Returns a minimum key.
     *
     * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Element minElement() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");
        // 最小元素 elementValueArr[最小元素的index]
        return elementValueArr[indexOfElementArr[1]];
    }

    /**
     * Removes a minimum key and returns its associated index.
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int delMin() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");

        /* 1 获取最小元素的index 并 返回它 */
        int indexOfMinElement = indexOfElementArr[1];

        /* 2 删除最小元素 & 维护数组的性质(以支持当前的优先队列) */
        // 把 最小元素的index 交换到 indexOfElementArr的末尾
        exch(1, elementAmount--);
        sink(1);
        // 断言：最小元素的index 被移动到了数组的末尾
        assert indexOfMinElement == indexOfElementArr[elementAmount +1];

        /* 3 删除多余的数组元素 */
        // 删除 element
        elementValueArr[indexOfMinElement] = null;    // to help with garbage collection
        // 删除 index
        indexOfElementArr[elementAmount + 1] = -1;        // not needed
        // 删除 theSpotOfIndex
        theSpotOfIndexInArr[indexOfMinElement] = -1;        // delete

        // 返回 indexOfMinElement - 这个值在整个过程中没有发生任何变化
        return indexOfMinElement;
    }

    /**
     * Returns the element associated with index {@code i}.
     * 返回 与指定的index相关联的元素
     * @param  index the index of the element to return
     * @return the element associated with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no element is associated with index {@code i}
     */
    public Element ElementOf(int index) {
        // 校验 Client传入的index
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        // 直接从 elementValueArr中使用index就能够获取到 element
        else return elementValueArr[index];
    }

    /**
     * Change the element associated with index {@code i} to the specified value.
     * 修改优先队列中 指定的索引 所关联的元素值
     * @param  index the index of the element to change
     * @param  element change the element associated with index {@code i} to this element
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no element is associated with index {@code i}
     */
    public void changeElement(int index, Element element) {
        // 校验 Client所传入的index
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");

        // 修改 elementValueArr数组中 index所关联到的element
        elementValueArr[index] = element;

        // 由于 indexOfElementArr中元素的排序规则是based on elementValueArr的
        // 所以 变更element之后，需要重新对 indexOfElementArr进行排序
        /*
            步骤：
                1 获取到Client指定的index 在 indexOfElementArr中的位置；
                2 以此作为参数来 对indexOfElementArr进行重新排序
         */
        swim(theSpotOfIndexInArr[index]);
        sink(theSpotOfIndexInArr[index]);
    }

    /**
     * Change the key associated with index {@code i} to the specified value.
     * 修改优先队列中 指定的索引 所关联的元素值
     * @param  i the index of the key to change
     * @param  element change the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @deprecated Replaced by {@code changeKey(int, Key)}.
     */
    @Deprecated
    public void change(int i, Element element) {
        // 过时的API
        /*
            原因：可能是因为旧的API的签名不够见名知意，所以重新添加了一个 作用完全相同的API。
            特征：
                1 新的API 与 旧的API 就只有方法名称不同；
                2 旧的API不能删除，因为已经有Client使用旧的API编写了代码 - 如果删除，Client代码就会编译失败
            做法：
                1 实现新的API；
                2 在旧的API中，把实现委托给新的API；
            用法：
                鼓励Client使用新的API来编写代码
         */
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
        // 校验index
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        if (elementValueArr[index].compareTo(element) == 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
        if (elementValueArr[index].compareTo(element) < 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");

        // 更新element
        elementValueArr[index] = element;

        // 对 indexOfElementArr进行重新排序
        swim(theSpotOfIndexInArr[index]);
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
        // 校验 index
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        if (elementValueArr[index].compareTo(element) == 0)
            throw new IllegalArgumentException("Calling increaseKey() with a key equal to the key in the priority queue");
        if (elementValueArr[index].compareTo(element) > 0)
            throw new IllegalArgumentException("Calling increaseKey() with a key strictly less than the key in the priority queue");

        // 更新 element
        elementValueArr[index] = element;
        // 对 indexOfElementArr进行排序
        sink(theSpotOfIndexInArr[index]);
    }

    /**
     * Remove the element associated with index {@code i}.
     * 删除 优先队列中 指定index所关联的element
     * @param  index the index of the element to remove
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no element is associated with index {@code i}
     */
    public void delete(int index) {
        // 校验index
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");

        // 找到index的位置 & 把它交换到 indexOfElementArr的末尾
        int spotOfIndex = theSpotOfIndexInArr[index];
        exch(spotOfIndex, elementAmount--);

        // 对 indexOfElementArr进行重新排序
        swim(spotOfIndex);
        sink(spotOfIndex); // 下沉操作才会与 elementAmount有关

        // 清除对此index的记录
        elementValueArr[index] = null;
        indexOfElementArr[theSpotOfIndexInArr[index]] = -1;
        theSpotOfIndexInArr[index] = -1;
    }

    // throw an IllegalArgumentException if i is an invalid index
    private void validateIndex(int i) {
        if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
        if (i >= capacity) throw new IllegalArgumentException("index >= capacity: " + i); // 为什么=maxN也会有异常呢？
    }

    /***************************************************************************
     * General helper functions.
     ***************************************************************************/
    private boolean greater(int i, int j) {
        // 🐖：这里比较的并不是 indexOfElementArr中的元素， 而是 element数组中的元素
        return elementValueArr[indexOfElementArr[i]].compareTo(elementValueArr[indexOfElementArr[j]]) > 0;
    }

    private void exch(int i, int j) {
        int temp = indexOfElementArr[i];
        indexOfElementArr[i] = indexOfElementArr[j];
        indexOfElementArr[j] = temp;

        // 维护 theSpotOfIndex数组
        int indexOfSpotI = indexOfElementArr[i];
        theSpotOfIndexInArr[indexOfSpotI] = i;
        int indexOfSpotJ = indexOfElementArr[j];
        theSpotOfIndexInArr[indexOfSpotJ] = j;
    }


    /***************************************************************************
     * Heap helper functions.
     ***************************************************************************/
    private void swim(int currentSpot) {
        while (currentSpot > 1 && greater(currentSpot /2, currentSpot)) {
            exch(currentSpot, currentSpot /2);
            currentSpot = currentSpot /2;
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
        private indexMinPQ_round01_drill01<Element> copy;

        // add all elements to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            copy = new indexMinPQ_round01_drill01<Element>(indexOfElementArr.length - 1);
            for (int i = 1; i <= elementAmount; i++)
                copy.insert(indexOfElementArr[i], elementValueArr[indexOfElementArr[i]]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        // 这种获取到下一个元素的方式有破坏性 —— 所以我们需要准备一个原始队列对象的副本 来绑定到类的成员变量上，以供操作
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
        indexMinPQ_round01_drill01<String> pq = new indexMinPQ_round01_drill01<String>(strings.length);

        // 遍历字符串数组，并逐个插入到 索引优先队列中
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // 删除并打印每一个值
        while (!pq.isEmpty()) {
            int i = pq.delMin();
            StdOut.println(i + " " + strings[i]);
        }
        StdOut.println();

        System.out.println("----------------------");
    }
}
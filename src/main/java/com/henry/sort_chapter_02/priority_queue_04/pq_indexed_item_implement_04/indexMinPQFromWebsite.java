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
    private int[] indexOfElementArr;        // binary heap using 1-based indexing 以1作为索引开头的二叉堆
    private int[] theSpotOfIndex;        // 作用： 快速找到 index在优先队列数组中的位置
    private Element[] elementArr;      // keys[i] = priority of i

    /**
     * Initializes an empty indexed priority queue with indices between {@code 0}
     * and {@code maxN - 1}.
     * @param  capacity the keys on this priority queue are index from {@code 0}
     *         {@code maxN - 1}
     * @throws IllegalArgumentException if {@code maxN < 0}
     */
    public indexMinPQFromWebsite(int capacity) {
        if (capacity < 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        elementAmount = 0;

        elementArr = (Element[]) new Comparable[capacity + 1];    // make this of length maxN??
        indexOfElementArr = new int[capacity + 1];
        theSpotOfIndex = new int[capacity + 1];                   // make this of length maxN??

        // initialize each item with value
        for (int i = 0; i <= capacity; i++)
            theSpotOfIndex[i] = -1;
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
     *
     * @param  index an index
     * @return {@code true} if {@code i} is an index on this priority queue;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     */
    public boolean contains(int index) {
        validateIndex(index);
        return theSpotOfIndex[index] != -1;
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
     *
     * @param  index an index
     * @param  element the key to associate with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if there already is an item associated
     *         with index {@code i}
     */
    public void insert(int index, Element element) {
        validateIndex(index);
        if (contains(index)) throw new IllegalArgumentException("index is already in the priority queue");

        elementAmount++;
        // 把index插入到数组的最后一个位置
        theSpotOfIndex[index] = elementAmount;
        indexOfElementArr[elementAmount] = index;

        // 把 element添加到 elementArr的index位置上去
        elementArr[index] = element;

        // restore the indexOfElementArr -> 得到堆有序的数组（堆/优先队列）
        swim(elementAmount);
    }

    /**
     * Returns an index associated with a minimum key.
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int minIndex() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");
        // indexOfElementArr是一个优先队列，数组中的元素是：连续而且有序的
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
        return elementArr[indexOfElementArr[1]];
    }

    /**
     * Removes a minimum key and returns its associated index.
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    public int delMin() {
        if (elementAmount == 0) throw new NoSuchElementException("Priority queue underflow");

        int indexOfMinElement = indexOfElementArr[1];
        // 交换元素 + 重建堆有序
        exch(1, elementAmount--);
        sink(1);
        // 最小元素的index 被移动到了数组的末尾
        assert indexOfMinElement == indexOfElementArr[elementAmount +1];

        // 删除 theSpotOfIndex中的索引
        theSpotOfIndex[indexOfMinElement] = -1;        // delete
        // 删除 elementArr中 索引对应的元素
        elementArr[indexOfMinElement] = null;    // to help with garbage collection
        // 删除 indexOfElementArr中的索引值
        indexOfElementArr[elementAmount +1] = -1;        // not needed

        // 返回 indexOfMinElement
        return indexOfMinElement;
    }

    /**
     * Returns the key associated with index {@code i}.
     *
     * @param  index the index of the key to return
     * @return the key associated with index {@code i}
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public Element ElementOf(int index) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        else return elementArr[index];
    }

    /**
     * Change the key associated with index {@code i} to the specified value.
     *
     * @param  index the index of the key to change
     * @param  element change the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void changeElement(int index, Element element) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        elementArr[index] = element;

        // 维护 indexOfElementArr堆有序
        swim(theSpotOfIndex[index]);
        sink(theSpotOfIndex[index]);
    }

    /**
     * Change the key associated with index {@code i} to the specified value.
     *
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
     *
     * @param  index the index of the key to decrease
     * @param  element decrease the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key >= keyOf(i)}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void decreaseElement(int index, Element element) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        if (elementArr[index].compareTo(element) == 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
        if (elementArr[index].compareTo(element) < 0)
            throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");


        elementArr[index] = element;
        swim(theSpotOfIndex[index]);
    }

    /**
     * Increase the key associated with index {@code i} to the specified value.
     *
     * @param  index the index of the key to increase
     * @param  element increase the key associated with index {@code i} to this key
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws IllegalArgumentException if {@code key <= keyOf(i)}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void increaseElement(int index, Element element) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");
        if (elementArr[index].compareTo(element) == 0)
            throw new IllegalArgumentException("Calling increaseKey() with a key equal to the key in the priority queue");
        if (elementArr[index].compareTo(element) > 0)
            throw new IllegalArgumentException("Calling increaseKey() with a key strictly less than the key in the priority queue");

        elementArr[index] = element;
        sink(theSpotOfIndex[index]);
    }

    /**
     * Remove the key associated with index {@code i}.
     *
     * @param  index the index of the key to remove
     * @throws IllegalArgumentException unless {@code 0 <= i < maxN}
     * @throws NoSuchElementException no key is associated with index {@code i}
     */
    public void delete(int index) {
        validateIndex(index);
        if (!contains(index)) throw new NoSuchElementException("index is not in the priority queue");

        int spotOfIndex = theSpotOfIndex[index];
        exch(spotOfIndex, elementAmount--);

        // 删除操作后，既可能会上浮，也可能会下沉
        swim(spotOfIndex);
        sink(spotOfIndex);

        // 清除对此index的记录
        elementArr[index] = null;
        theSpotOfIndex[index] = -1;
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
        return elementArr[indexOfElementArr[i]].compareTo(elementArr[indexOfElementArr[j]]) > 0;
    }

    private void exch(int i, int j) {
        int swap = indexOfElementArr[i];
        indexOfElementArr[i] = indexOfElementArr[j];
        indexOfElementArr[j] = swap;

        // 维护 theSpotOfIndex数组 这是一个恒等式
        theSpotOfIndex[indexOfElementArr[i]] = i;
        theSpotOfIndex[indexOfElementArr[j]] = j;
    }


    /***************************************************************************
     * Heap helper functions.
     ***************************************************************************/
    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= elementAmount) {
            int j = 2*k;
            if (j < elementAmount && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
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
            copy = new indexMinPQFromWebsite<Element>(indexOfElementArr.length - 1);
            for (int i = 1; i <= elementAmount; i++)
                copy.insert(indexOfElementArr[i], elementArr[indexOfElementArr[i]]);
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
        indexMinPQFromWebsite<String> pq = new indexMinPQFromWebsite<String>(strings.length);

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
        // reinsert the same strings
//        for (int i = 0; i < strings.length; i++) {
//            pq.insert(i, strings[i]);
//        }
//
//        // print each key using the iterator
//        for (int i : pq) {
//            StdOut.println(i + " " + strings[i]);
//        }
//        while (!pq.isEmpty()) {
//            pq.delMin();
//        }

    }
}
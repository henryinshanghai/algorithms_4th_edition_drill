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

    % java MaxPQFromWebsite < tinyPQ.txt
    Q X P (6 left on pq)

术语统一：
    1 堆有序的数组 = 堆；
    2 堆的容量 = capacity;
    3 堆的元素个数 = itemAmount;

enhancements:
    - 1 自定义的比较器，而不是使用泛型参数本身的 compareTo()方法
        特征：
            1 具体的比较器需要Client 传入，数据结构中会用一个 comparator字段进行接收
            2 一旦使用了自定义的比较器，这里的泛型参数就不需要再 extends from Comparable了
            3 自定义比较器只是一个备用操作（以防Client想要传入自己的比较器），所以只会影响到 初始化方法 + less()
    - 2 支持迭代的操作
        手段：实现 Iterable<Key>的接口

    ------ new for this file ---
    - 3 从一个数组集合构建出 优先队列；
    - 4 检查当前的数组是不是 堆有序的；
    - 5 不需要Client指定初始容量的 构造方法； + 在合适的时候(insert, delMax)对数组扩容的方法
 */
public class MaxPQ_heap_round2_drill04_enhancement02<Key> implements Iterable<Key>{

    private Key[] itemArr;
    private int itemAmount;

    private Comparator customComparator;


    // 不需要Client指定 容量大小的构造方法
    public MaxPQ_heap_round2_drill04_enhancement02() {
        // 手段：提供一个默认的容量值
        this(1);
    }

    public MaxPQ_heap_round2_drill04_enhancement02(int capacity) {
        itemArr = (Key[])new Comparable[capacity];
        itemAmount = 0;
    }

    public MaxPQ_heap_round2_drill04_enhancement02(int capacity, Comparator comparator) {
        itemArr = (Key[])new Comparable[capacity];
        itemAmount = 0;
        customComparator = comparator;
    }

    public MaxPQ_heap_round2_drill04_enhancement02(Key[] passInArr) {
        /*
            1 根据传入的数组 初始化 itemArr的容量；
            2 根据传入的数组 初始化 itemArr的元素值；
            3 使用sink(1)的方式 使itemArr堆有序；
            4 判断 itemArr是不是堆有序了
         */
        itemArr = (Key[])new Object[passInArr.length+1];
        itemAmount = passInArr.length;

        for (int cursor = 0; cursor < passInArr.length; cursor++) {
            itemArr[cursor + 1] = passInArr[cursor]; 
        }

        // 自下而上地构建堆
        for (int spotInDescendingSequence = itemAmount / 2;
             spotInDescendingSequence <= 1; spotInDescendingSequence--) {
            sink(spotInDescendingSequence);
        }

        // 验证 itemArr是不是一个堆 - 手段： assert 一个返回boolean类型变量的方法
        assert isMaxHeap();
    }

    /*
        1 null值判断；
            // #con1: 是不是存在null元素
            // #con2： 从队列元素结束的位置到剩下的空间中是不是还有其他的元素
            // #con3： 数组的第一个位置是不是null元素
        2 节点值大小判断；
     */
    private boolean isMaxHeap() {
        for (int cursor = 1; cursor <= itemAmount; cursor++) {
            if (itemArr[cursor] == null) return false;
        }

        for (int cursor = itemAmount+1; cursor < itemArr.length; cursor++) {
            if (itemArr[cursor] != null) return false;
        }

        if(itemArr[0] != null) return false;

        return isMaxHeapSorted(1);
    }

    /*
        // is subtree of pq[1..n] rooted at k a max heap?
        定义/实现：
            1 对当前的二叉树来说， 根节点的值 > max(左节点的值, 右节点的值)
            2 对于两棵子树来说，子树本身也是堆有序的。
     */
    private boolean isMaxHeapSorted(int currentSpot) {
        // 递归退出条件
        if (currentSpot > itemAmount) return true;

        int leftChildSpot = currentSpot * 2;
        int rightChildSpot = currentSpot * 2 + 1;

        // 左右子节点都要小于父节点 - 需要添加限制条件，否则可能数组下标越界
        if (leftChildSpot < itemAmount && less(currentSpot, leftChildSpot) ) {
            return false;
        }

        if (leftChildSpot < itemAmount && less(currentSpot, rightChildSpot)) {
            return false;
        }

        return isMaxHeapSorted(leftChildSpot) && isMaxHeapSorted(rightChildSpot);
    }

    public void insert(Key newItem) {
        /*
            1 add the new item in the last Spot of array;
            2 update the items amount
            3 restore the heap
         */
        // before insert, check if need to resize
        if(itemAmount == itemArr.length - 1) resize(itemArr.length * 2);

        itemArr[++itemAmount] = newItem;
        swim(itemAmount);

        // verify if the array is heap-sorted.
        assert isMaxHeap();
    }

    private void resize(int capacity) {
        // assert的用法: assert [boolean expression]   需要先开启JVM参数
        assert capacity > itemAmount;

        // create a new array
        Key[] newItemArr = (Key[])new Object[capacity];

        // copy the item from the old array
        for (int cursor = 1; cursor <= itemAmount; cursor++) { // 这里拷贝的是非null元素
            newItemArr[cursor] = itemArr[cursor];
        }

        itemArr = newItemArr;
    }

    private void swim(int currentSpot) {
        /*
            1 exchange the items if the condition are favorable, stop if it is not.
            2 conditions:
                - the current spot is the first element in the array;
                - the item in the spot is less than its father

            note: be very aware who compare to whom.
         */
        while (currentSpot > 1 && !less(currentSpot, currentSpot / 2)) {
            exch(currentSpot, currentSpot / 2);

            currentSpot = currentSpot / 2;
        }
    }

    private void exch(int i, int j) {
        Key temp = itemArr[i];
        itemArr[i] = itemArr[j];
        itemArr[j] = temp;
    }

    private boolean less(int i, int j) {
        if (customComparator == null) {
            // 先强制转换，再调用
            return ((Comparable<Key>)itemArr[i]).compareTo(itemArr[j]) < 0;
        } else {
            return customComparator.compare(itemArr[i], itemArr[j]) < 0;
        }
    }

    public Key delMax() {
        /*
            1 get the biggest item in the array to return;
            2 exchange it with the item in the last spot;
            3 update the item amount；
            4 restore the sorted-heap
         */
        // 预防措施1：队列已经为空
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");

        Key maxItem = itemArr[1];

        exch(1, itemAmount--);

        sink(1);

        itemArr[itemAmount + 1] = null;

        // 升级措施2： 一旦数组中的元素比较少，就resize()
        // 如果已经没有元素了，也就不需要再做resize的操作了
        if (itemAmount > 0 && itemAmount <= (itemArr.length - 1) / 4) resize(itemArr.length * 1 / 2);

        assert isMaxHeap();

        return maxItem;
    }

    private void sink(int currentSpot) {
        /*
            exchange the items if conditions are favorable, stop if it's not.
            conditions:
                1 when it has somewhere to sink;
                2 when its bigger child is bigger than itself;
         */
        while (currentSpot * 2 <= itemAmount) {
            // find the bigger child
            int biggerChildSpot = currentSpot * 2;
            if(biggerChildSpot < itemAmount && less(biggerChildSpot, biggerChildSpot + 1)) {
                biggerChildSpot = biggerChildSpot + 1;
            }

            if (!less(currentSpot, biggerChildSpot)) break;

            // exchange the items
            exch(currentSpot, biggerChildSpot);

            currentSpot = biggerChildSpot;
        }
    }


    @Override
    public Iterator<Key> iterator() {
        // 实现一个自己的迭代器
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {

        // 准备一个 优先队列/集合的副本，这样迭代操作不会影响到原始数组
//        private Key[] copy;
        private MaxPQ_heap_round2_drill04_enhancement02<Key> copy;

        // 使用构造器 初始化成员变量
        public HeapIterator() {
            if (customComparator == null) {
                copy = new MaxPQ_heap_round2_drill04_enhancement02(itemAmount());
            } else {
                copy = new MaxPQ_heap_round2_drill04_enhancement02(itemAmount(), customComparator);
            }
        }

        // 迭代器中是否存在下一个元素
        public boolean hasNext() {
            return !copy.isEmpty();
        }

        // 获取到迭代器中的下一个元素
        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }

        // 移除迭代器中的元素 - 一般不支持在迭代时修改迭代器，这会导致读写不一致的问题
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // 用例代码 aka 单元测试代码
    public static void main(String[] args) {

        // Client使用不需要指定 capacity的构造器
        MaxPQ_heap_round2_drill04_enhancement02<String> maxPQ = new MaxPQ_heap_round2_drill04_enhancement02<>();

        System.out.println("====== delete the maxItem of PQ when run into - in input stream =======");

        // 获取到 标准输入流
        while (!StdIn.isEmpty()) {
            // 读取标准输入流中的内容
            String item = StdIn.readString();

            if (!item.equals("-")) maxPQ.insert(item);
            else if (!maxPQ.isEmpty()) {
                StdOut.println("current maxItem in PQ is: " + maxPQ.delMax());
            }
        }

        System.out.println();

        System.out.println("after read through whole input, the itemAmount in PQ is: " + maxPQ.itemAmount());
    }

    private int itemAmount() {
        return itemAmount;
    }

    private boolean isEmpty() {
        return itemAmount == 0;
    }

}
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
    2 堆的容量 = capcity;
    3 堆的元素个数 = itemAmount;

enhancements:
    - 1 自定义的比较器，而不是使用泛型参数本身的 compareTo()方法
        特征：
            1 具体的比较器需要Client 传入，数据结构中会用一个 comparator字段进行接收
            2 一旦使用了自定义的比较器，这里的泛型参数就不需要再 extends from Comparable了
            3 自定义比较器只是一个备用操作（以防Client想要传入自己的比较器），所以只会影响到 初始化方法 + less()
    - 2 支持迭代的操作
        手段：实现 Iterable<Key>的接口
 */
public class MaxPQ_heap_round2_drill04_enhancement01<Key> implements Iterable<Key>{

    private Key[] itemArr;
    private int itemAmount;

    private Comparator customComparator;


    public MaxPQ_heap_round2_drill04_enhancement01(int capacity) {
        itemArr = (Key[])new Comparable[capacity];
        itemAmount = 0;
    }

    public MaxPQ_heap_round2_drill04_enhancement01(int capacity, Comparator comparator) {
        itemArr = (Key[])new Comparable[capacity];
        itemAmount = 0;
        customComparator = comparator;
    }

    public void insert(Key newItem) {
        /*
            1 add the new item in the last Spot of array;
            2 update the items amount
            3 restore the heap
         */
        itemArr[++itemAmount] = newItem;

        swim(itemAmount);
    }

    private void swim(int currentSpot) {
        /*
            1 exchange the items if the condition are favorable, stop if it is don't.
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
        Key maxItem = itemArr[1];

        exch(1, itemAmount--);

        sink(1);

        itemArr[itemAmount + 1] = null;

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


    // 用例代码 aka 单元测试代码
    public static void main(String[] args) {

        MaxPQ_heap_round2_drill04_enhancement01<String> maxPQ = new MaxPQ_heap_round2_drill04_enhancement01<>(10);

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

    @Override
    public Iterator<Key> iterator() {
        // 实现一个自己的迭代器
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {

        // 准备一个 优先队列/集合的副本，这样迭代操作不会影响到原始数组
//        private Key[] copy;
        private MaxPQ_heap_round2_drill04_enhancement01<Key> copy;

        // 使用构造器 初始化成员变量
        public HeapIterator() {
            if (customComparator == null) {
                copy = new MaxPQ_heap_round2_drill04_enhancement01(itemAmount());
            } else {
                copy = new MaxPQ_heap_round2_drill04_enhancement01(itemAmount(), customComparator);
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

}
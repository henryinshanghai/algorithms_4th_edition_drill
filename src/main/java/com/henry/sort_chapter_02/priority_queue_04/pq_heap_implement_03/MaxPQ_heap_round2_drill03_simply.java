package com.henry.sort_chapter_02.priority_queue_04.pq_heap_implement_03;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/*
算法描述：
    使用 堆这种逻辑结构 来实现 优先队列 - insert操作 + delMax操作

    堆这种逻辑结构要怎么实现呢？
        数组 -> 完全二叉树(数值要求) -> 堆有序的完全二叉树

    底层数据结构：
        一个数组 itemHeap
        一个int itemAmount

    % java MaxPQFromWebsite < tinyPQ.txt
    Q X P (6 left on pq)

 */
public class MaxPQ_heap_round2_drill03_simply<Key extends Comparable<Key>> {

    private Key[] itemHeap;
    private int itemAmount;


    public MaxPQ_heap_round2_drill03_simply(int capacity) {
        itemHeap = (Key[])new Comparable[capacity];
        itemAmount = 0;
    }

    public void insert(Key newItem) {
        /*
            1 add the new item in the last Spot of array;
            2 update the items amount
            3 restore the heap
         */
        itemHeap[++itemAmount] = newItem;

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
        Key temp = itemHeap[i];
        itemHeap[i] = itemHeap[j];
        itemHeap[j] = temp;
    }

    private boolean less(int i, int j) {
        return itemHeap[i].compareTo(itemHeap[j]) < 0;
    }

    public Key delMax() {
        /*
            1 get the biggest item in the array to return;
            2 exchange it with the item in the last spot;
            3 update the item amount；
            4 restore the sorted-heap
         */
        Key maxItem = itemHeap[1];

        exch(1, itemAmount--);

        sink(1);

        itemHeap[itemAmount + 1] = null;

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

        MaxPQ_heap_round2_drill03_simply<String> maxPQ = new MaxPQ_heap_round2_drill03_simply<>(10);

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

        System.out.println("after read through whole input, the itemAmount in PQ is: " + maxPQ.size());
    }

    private int size() {
        return itemAmount;
    }

    private boolean isEmpty() {
        return itemAmount == 0;
    }
}
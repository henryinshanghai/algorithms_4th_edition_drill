package com.henry.sort_chapter_02.priority_queue_04.pq_heap_implement_03;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

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

 */
public class MaxPQ_heap_round2_drill02_simply<Key extends Comparable<Key>> {

    private Key[] itemArr; // 1-based array
    private int itemAmount;

    // 构造器
    public MaxPQ_heap_round2_drill02_simply(int capacity) {
        itemArr = (Key[]) new Comparable[capacity];
        itemAmount = 0;
    }

    // public APIs
    /*
        1 put the new Item at the last spot of the itemArr
        2 update the itemAmount
        3 make the array to 'heap-sorted'
     */
    public void insert(Key newItem) {
        // 由于数组是从1开始的，所以这里 itemArr[itemAmount]中已经有值了 需要用下一个位置
        itemArr[++itemAmount] = newItem;
        swim(itemAmount);
    }

    // 把当前位置上的元素 移动到合适的位置上去
    private void swim(int currentSpot) {

        while (currentSpot > 1 && less(currentSpot / 2, currentSpot)) {
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
        return itemArr[i].compareTo(itemArr[j]) < 0;
    }

    /*
        1 逻辑上删除最大元素 - 数组中的第一个位置上的元素
        2 把最后一个元素 与 第一个元素 交换位置；
        3 交换位置后，通过下沉第一个元素 让数组达到 堆有序的状态
        4 更新 堆中的元素个数
     */
    public Key delMax() {
        Key maxItem = itemArr[1];

        // 这里需要及时地维护itemAmount的值， 否则紧跟着的sink操作会出现问题(依赖于 itemAmount的正确性)
        exch(1, itemAmount--);

        sink(1);

        itemArr[itemAmount + 1] = null;

//        itemAmount--;

        return maxItem;
    }

    /*
        反复下沉，直到:
        1 当前位置上元素的值 > max(左右子节点上元素的值)
        2 元素已经到了数组的最后一个位置 aka 完全二叉树的最后一个位置上 aka 二叉堆的底部
     */
    private void sink(int currentSpot) {
        // 循环的条件 需要谨慎地考察循环体中的内容
        // 这里的case是： 还需要执行交换操作吗？  + 防止数组越界
        // 如果当前节点刚好处在倒数第二层的最后一个位置，而最后一个节点是它的左子节点 && 左子节点更大 则这时仍旧需要下沉/交换
        while (currentSpot * 2 <= itemAmount) {
            int biggerChildSpot = currentSpot * 2;
            // 数组中的有效spot的区间为： [1, itemAmount] 所以只有在 条件Ⅰ（biggerChildSpot < itemAmount）为true的情况下  后面的条件才有意义
            if (biggerChildSpot < itemAmount && less(biggerChildSpot, currentSpot * 2 + 1)) biggerChildSpot = currentSpot * 2 + 1;

            if (!less(currentSpot, biggerChildSpot)) break;

            exch(currentSpot, biggerChildSpot);
            currentSpot = biggerChildSpot;
        }
    }

    public static void main(String[] args) {
        MaxPQ_heap_round2_drill02_simply<String> maxPQ = new MaxPQ_heap_round2_drill02_simply<>(10);

        // read from input stream in terminal
        while (!StdIn.isEmpty()) { // to redirect the input stream from a txt file, here need to use <
            // read string in the input file
            String item = StdIn.readString();

            // operation against the strings to build the heap
            if (!item.equals("-")) maxPQ.insert(item);
            else if (!maxPQ.isEmpty()) {
                StdOut.print(maxPQ.delMax() + " ");
            }
        }

        System.out.println("(" + maxPQ.size() + " left on maxPQ.)");
    }

    private int size() {
        return itemAmount;
    }

    private boolean isEmpty() {
        return itemAmount == 0;
    }
}
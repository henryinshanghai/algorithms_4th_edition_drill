package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.ordered_array_02;

/*
    算法描述：
        使用一个元素有序的数组 来实现 优先队列 - 核心操作 insert(item) + delMax()

    底层数据结构：
        elements array

    insert操作：
        为了保持 有序数组在插入元素后仍旧有序，这里的insert操作要求会复杂一些

    delMax 操作：
        既然数组已经是 元素有序的状态， 那么直接删除掉数组的最后一个元素就可以了

    less操作：
        不需要传入 数组本身 作为参数
        比较的是两个item

    exch操作：
        没有用到

    泛型的作用： 代码支持处理通用的类型
        对泛型的期待：支持比较操作

    测试数据：
        maxPQ.insert("Ada");
        maxPQ.insert("Alicia");
        maxPQ.insert("Henry");
        maxPQ.insert("Ben");
        maxPQ.insert("Quinta");
        maxPQ.insert("Kelly");
        maxPQ.insert("Annie");
 */
public class MaxPQ_orderedArray_round2_drill02 <Key extends Comparable<Key>>{

    private Key[] elementArr;
    private int itemAmount;


    // 构造方法 - 用来初始化成员变量
    public MaxPQ_orderedArray_round2_drill02(int capacity) {
        elementArr = (Key[])new Comparable[capacity];
        itemAmount = 0;
    }

    // insert
    public void insert(Key item) {
        int backwardsCursor = itemAmount - 1;

        while (backwardsCursor >= 0 && less(item, elementArr[backwardsCursor])) {
            elementArr[backwardsCursor + 1] = elementArr[backwardsCursor];
            backwardsCursor--;
        }

        elementArr[backwardsCursor + 1] = item;
        itemAmount++; // 别忘了更新元素数量
    }

    private boolean less(Key v, Key w) {
        return v.compareTo(w) < 0;
    }

    /*
        Key maxItem = elementArr[itemAmount - 1];
        itemAmount--;

        return maxItem;
     */
    public Key delMax() {
        return elementArr[--itemAmount];
    }

    private boolean isEmpty() {
        return itemAmount == 0;
    }

    public static void main(String[] args) {
        MaxPQ_orderedArray_round2_drill02<String> maxPQ = new MaxPQ_orderedArray_round2_drill02<>(10);

        maxPQ.insert("Ada");
        maxPQ.insert("Alicia");
        maxPQ.insert("Henry");
        maxPQ.insert("Ben");
        maxPQ.insert("Quinta");
        maxPQ.insert("Kelly");
        maxPQ.insert("Annie");

        while (!maxPQ.isEmpty()) {
            System.out.println("current Max Item: " + maxPQ.delMax());
        }

    }
}
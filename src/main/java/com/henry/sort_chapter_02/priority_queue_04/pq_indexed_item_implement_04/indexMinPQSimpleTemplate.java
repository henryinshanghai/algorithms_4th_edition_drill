package com.henry.sort_chapter_02.priority_queue_04.pq_indexed_item_implement_04;

import edu.princeton.cs.algs4.StdOut;

public class indexMinPQSimpleTemplate<Element extends Comparable<Element>> {

    private Element[] indexToElementArray;
    private int[] spotToIndexArray;
    private int[] indexToSpotArray;
    private int elementAmount;
    private int capacity;

    public indexMinPQSimpleTemplate(int initCapacity) {
        indexToElementArray = (Element[]) new Comparable[initCapacity + 1];
        spotToIndexArray = new int[initCapacity + 1];
        indexToSpotArray = new int[initCapacity + 1];

        for (int index = 0; index < indexToSpotArray.length; index++) {
            indexToSpotArray[index] = -1;
        }
    }

    // 向堆中插入一个元素
    public void insert(int index, Element element) {
        elementAmount++;

        // 插入元素后，先维护三个数组
        indexToElementArray[index] = element;
        spotToIndexArray[elementAmount] = index;
        indexToSpotArray[index] = elementAmount;

        // 再执行上浮动作，恢复堆有序
        // 上浮动作本身，会维护 {spotToIndexArray, indexToSpotArray}两个数组
        swim(elementAmount);
    }

    private void swim(int currentNodeSpot) {
        while (currentNodeSpot > 1 && greater(currentNodeSpot / 2, currentNodeSpot)) {
            exch(currentNodeSpot / 2, currentNodeSpot);

            currentNodeSpot = currentNodeSpot / 2;
        }
    }

    // 交换堆中 spotI位置上的元素，与 spotJ位置上的元素
    private void exch(int spotI, int spotJ) {
        // 交换 spot->index
        int temp = spotToIndexArray[spotI];
        spotToIndexArray[spotI]= spotToIndexArray[spotJ];
        spotToIndexArray[spotJ] = temp;

        // 维护index -> spot
        int indexOfSpotI = spotToIndexArray[spotI];
        indexToSpotArray[indexOfSpotI] = spotI;
        int indexOfSpotJ = spotToIndexArray[spotJ];
        indexToSpotArray[indexOfSpotJ] = spotJ;
    }

    // 比较堆中 spotI位置上的元素，是否大于 spotJ位置上的元素
    private boolean greater(int spotI, int spotJ) {
        int indexOfSpotI = spotToIndexArray[spotI];
        int indexOfSpotJ = spotToIndexArray[spotJ];

        int result = indexToElementArray[indexOfSpotI].compareTo(indexToElementArray[indexOfSpotJ]);
        return result > 0;
    }


    // 删除堆中的最小元素， 并返回其所关联的索引
    public int delMin() {

        // 找到当前堆中的最小元素
        int indexOfMinElement = spotToIndexArray[1];
        Element minElement = indexToElementArray[indexOfMinElement];

        // 删除最小元素，并恢复堆
        indexToElementArray[indexOfMinElement] = null;
        exch(1, elementAmount--);
        sink(1);

        return indexOfMinElement;
    }

    // 下沉堆中指定位置上的元素
    private void sink(int currentNodeSpot) {
        while (currentNodeSpot * 2 < elementAmount) {
            int smallerChildSpot = currentNodeSpot * 2;
            if (greater(smallerChildSpot, smallerChildSpot + 1)) smallerChildSpot++;

            if (greater(currentNodeSpot, smallerChildSpot)) {
                exch(currentNodeSpot, smallerChildSpot);
            }

            currentNodeSpot = smallerChildSpot;
        }
    }

    public static void main(String[] args) {
        // insert a bunch of strings
        String[] strings = {"it", "was", "the", "best", "of", "times", "it", "was", "the", "worst"};

        // 初始化索引优先队列
        indexMinPQSimpleTemplate<String> pq = new indexMinPQSimpleTemplate<String>(strings.length); // 10

        // 遍历字符串数组，并逐个插入数组元素到 索引优先队列中
        for (int i = 0; i < strings.length; i++) {
            pq.insert(i, strings[i]);
        }

        // 删除并打印 索引优先队列中的最小元素 及它的索引值
        while (!pq.isEmpty()) {
            int indexOfMinItem = pq.delMin();
            StdOut.println(indexOfMinItem + " " + strings[indexOfMinItem]);
        }
        StdOut.println();

        System.out.println("----------------------");

    }

    private boolean isEmpty() {
        return elementAmount == 0;
    }
}

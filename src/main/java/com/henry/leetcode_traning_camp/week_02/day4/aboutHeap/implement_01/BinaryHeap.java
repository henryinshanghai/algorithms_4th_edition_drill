package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap.implement_01;// Java

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * 手动实现二叉堆数据结构
 */
public class BinaryHeap {
    // 底层数据结构 & 需要的局部变量
    private static final int d = 2; // N叉树的N
    private int[] heap; // 底层数据结构
    private int heapSize; // 二叉堆中的节点个数


    /**
     * This will initialize our heap with default size.
     * 这会使用默认size来初始化我们的堆
     */
    public BinaryHeap(int capacity) {
        heapSize = 0;
        heap = new int[capacity + 1];
        Arrays.fill(heap, -1);
    }


    /* 一些个辅助方法：判空、判满、求父节点的索引、求孩子节点的索引 */
    public boolean isEmpty() {
        return heapSize == 0;
    }


    public boolean isFull() {
        return heapSize == heap.length;
    }


    private int parent(int i) {
        return (i - 1) / d;
    }


    private int kthChild(int i, int k) {
        return d * i + k;
    }


    /* 核心APIs */
    /**
     * 向堆中插入一个新的元素
     * Inserts new element in to heap
     * Complexity: O(log N)
     * As worst case scenario, we need to traverse till the root
     */
    public void insert(int x) {
        if (isFull()) {
            throw new NoSuchElementException("Heap is full, No space to insert new element");
        }
        /* 1 把新元素添加到二叉树的尾部
            手段：把元素添加到数组的最后一个位置
            特征：插入完成后，更新表示堆中节点数量的heapSize
         */
        heap[heapSize] = x;
        heapSize ++;

        /* 2 调整二叉树的结构，以恢复二叉堆的特性2 */
        heapifyUp(heapSize - 1); // 因为刚刚调整了heapSize的值，所以这里的参数要-1
    }


    /**
     * 核心操作：删除索引为x的节点
     * Deletes element at index x
     * Complexity: O(log N)
     */
    public int delete(int x) {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty, No element to delete");
        }
        // 1 记录堆中的最大节点
        int maxElement = heap[x];
        // 2 把堆尾部的元素绑定到堆顶位置
        heap[x] = heap[heapSize - 1];
        // 3 更新heapSize的值
        heapSize--;

        // 4 调整树中的节点，以满足二叉堆的特性2
        heapifyDown(x);

        // 5 返回删除的堆顶节点的值
        return maxElement;
    }


    /**
     * Maintains the heap property while inserting an element.
     * 在插入元素时维护堆的性质
     */
    private void heapifyUp(int i) {
        // 1 记录下刚刚插入的元素值
        int insertValue = heap[i];
        // 2 准备一个循环     因为需要不断地比较 & 覆盖操作，而次数又不确定 所以使用while循环
        /*
            任务：把新插入的节点调整到正确的位置处；
            手段：
                1 比较插入的节点的值 与 其父节点的值大小
                2 如果插入的节点的值更大，说明当前树中的节点违反了性质2.则：上浮新插入的节点
                    手段1：交换新插入的节点 与 它的父节点；
                    手段2：直接把父节点覆盖到新插入节点的位置————这样能够省掉多次不必要的交换
                3 如果插入的节点的值更小，说明当前树中的节点仍旧满足性质2.则：不需要做任何操作
         */
        while (i > 0 && insertValue > heap[parent(i)]) {
            // 2-1 使用父节点来覆盖子节点
            heap[i] = heap[parent(i)];
            // 2-2 更新当前节点的索引值i
            i = parent(i);
        }

        // 2-3 等到其他所有节点的元素值都已经正确时，直接把插入的值绑定到正确的位置即可 aka i
        heap[i] = insertValue;
    }


    /**
     * Maintains the heap property while deleting an element.
     * 在删除一个元素时维护堆的性质
     */
    private void heapifyDown(int i) { // 下沉操作
        int child;
        int temp = heap[i];

        // 只要当前节点的孩子节点的索引值 < heapSize
        while (kthChild(i, 1) < heapSize) {
            // 1 求出当前节点的较大孩子节点
            child = maxChild(i);
            // 2 比较当前节点的值 与 当前节点的较大孩子节点的值
            // 如果当前节点更大，说明没有违反二叉堆的特性2.则：什么也不用做 break it,then we are done
            if (temp >= heap[child]) {
                break;
            }
            // 否则，把当前节点下沉   手段：与上浮操作的手段一摸一样
            heap[i] = heap[child];
            i = child;
        }
        heap[i] = temp;
    }


    /**
     * 获取到索引为i的节点的左右孩子中的较大孩子节点的索引位置
     * @param i
     * @return
     */
    private int maxChild(int i) {
        // 1 求出当前节点的左右孩子的索引
        int leftChild = kthChild(i, 1);
        int rightChild = kthChild(i, 2);
        // 2 比较两个孩子中，哪个孩子的值更大   返回更大的那个孩子的索引
        return heap[leftChild] > heap[rightChild] ? leftChild : rightChild;
    }


    /**
     * Prints all elements of the heap
     * 打印堆中的所有元素 其实就是数组啦
     */
    public void printHeap() {
        System.out.print("nHeap = ");
        for (int i = 0; i < heapSize; i++)
            System.out.print(heap[i] + " ");
        System.out.println();
    }


    /**
     * This method returns the max element of the heap.
     * complexity: O(1)
     */
    public int findMax() {
        if (isEmpty())
            throw new NoSuchElementException("Heap is empty.");
        return heap[0];
    }


    public static void main(String[] args) {
        // 创建一个最大堆对象
        BinaryHeap maxHeap = new BinaryHeap(10);
        maxHeap.insert(10);
        maxHeap.insert(4);
        maxHeap.insert(9);
        maxHeap.insert(1);
        maxHeap.insert(7);
        maxHeap.insert(5);
        maxHeap.insert(3);


        maxHeap.printHeap();
        maxHeap.delete(5);
        maxHeap.printHeap();
        maxHeap.delete(2);
        maxHeap.printHeap();
    }
} // 测试用例的局限：无法可视化地看到树地变化；
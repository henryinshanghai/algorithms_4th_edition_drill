package com.henry.basic_chapter_01.collection_types.queue.via_linked_node;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

// 目标：实现队列数据结构
// 数据类型的预期特征：{#1 支持泛型; #2 支持有序迭代; #3 支持Queue默认的入队与出队操作}
// 实现手段：使用链表作为底层的数据结构
// 执行手段： redirect input from <tobe.txt>
public class Queue<Item> implements Iterable<Item> {

    // 链表的头尾指针
    private Node<Item> headNode;
    private Node<Item> tailNode;
    private int itemAmount;

    private class Node<Item> {
        private Item item;
        private Node nextNode;
    }


    // 出队操作 aka 从队首移除元素
    private Item dequeue() {
        Item item = headNode.item;

        // 分类讨论 👇
        // #1 移除一般情况下的元素(只需要更新 headNode)
        headNode = headNode.nextNode;
        itemAmount--;

        // #2 移除最后一个元素(还会需要更新 tailNode);
        // 作用：这样操作后，就不会再有 任何引用 指向”被移除的节点“。避免 对象游离（保存了一个 指向 ”不再需要的对象“的引用）促进垃圾回收
        if (isEmpty()) tailNode = null;

        return item;
    }

    // 入队操作 aka 向队尾添加元素
    private void enqueue(Item item) {
        Node previousLastNode = tailNode;

        tailNode = new Node();
        tailNode.item = item;
        tailNode.nextNode = null;

        // 分类讨论 👇
        // #1 如果入队的是队列的第一个元素（需要初始化 startNode与lastNode）;
        if (isEmpty()) headNode = tailNode;
        // #2 如果是入队后继的元素（只需要更新lastNode）；
        else previousLastNode.nextNode = tailNode;

        itemAmount++;
    }

    // 支持迭代操作
    @Override
    public Iterator<Item> iterator() {
        return new MyQueueIterator();
    }

    // 作为 队列的迭代器
    private class MyQueueIterator implements Iterator<Item> {

        Node<Item> currentNode = headNode;

        // 是否存在下一个元素
        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        // 把迭代指针移动到下一个元素上去
        @Override
        public Item next() {
            if (isEmpty()) throw new NoSuchElementException();

            Item item = currentNode.item;
            currentNode = currentNode.nextNode;
            // 返回当前节点的元素
            return item;
        }
    }

    // 辅助小操作
    private int size() {
        return itemAmount;
    }

    private boolean isEmpty() {
        return itemAmount == 0;
    }

    // 针对数据结构行为的单元测试
    public static void main(String[] args) {
        Queue<String> myQueue = new Queue<>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();

            // 如果当前字符不是-，则：把当前字符入队
            if (!item.equals("-")) {
                myQueue.enqueue(item);
            } else if (!myQueue.isEmpty()) { // 如果是的，则：从队列中出队 并打印元素
                StdOut.print(myQueue.dequeue() + " ");
            }
        }

        StdOut.println("(" + myQueue.size() + " left on queue)");
    }
}

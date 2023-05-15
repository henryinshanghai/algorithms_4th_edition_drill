package com.henry.basic_chapter_01.collection_types.queue_via_linked_node;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

// 以 链表为底层数据结构 来 实现 泛型、可迭代的先进先出队列数据结构
// 执行手段： redirect input from <tobe.txt>
public class QueueViaNodeTemplate<Item> implements Iterable<Item> {

    public static void main(String[] args) {
        QueueViaNodeTemplate<String> myQueue = new QueueViaNodeTemplate<>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                myQueue.enqueue(item);
            } else if (!myQueue.isEmpty()) {
                StdOut.print(myQueue.dequeue() + " ");
            }
        }

        StdOut.println("(" + myQueue.size() + " left on queue)");
    }

    private Node headNode;
    private Node tailNode;
    private int itemAmount;

    private int size() {
        return itemAmount;
    }

    // 从队首移除元素
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


    @Override
    public Iterator<Item> iterator() {
        return new MyQueueIterator();
    }

    private class Node {
        private Item item;
        private Node nextNode;
    }

    private void enqueue(Item item) {
        Node previousLastNode = tailNode;

        tailNode = new Node();
        tailNode.item = item;
        tailNode.nextNode = null;

        // 分类：#1 入队第一个元素（需要初始化 startNode与lastNode）; #2 入队更多的元素（只需要更新lastNode）；
        if (isEmpty()) headNode = tailNode;
        else previousLastNode.nextNode = tailNode;

        itemAmount++;
    }

    private boolean isEmpty() {
        return itemAmount == 0;
    }

    // 作为 队列的迭代器
    private class MyQueueIterator implements Iterator<Item> {

        Node currentNode = headNode;

        // 是否存在下一个元素
        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        // 把迭代指针移动到下一个元素上去
        @Override
        public Item next() {
            Item item = currentNode.item;
            currentNode = currentNode.nextNode;
            return item;
        }
    }
}

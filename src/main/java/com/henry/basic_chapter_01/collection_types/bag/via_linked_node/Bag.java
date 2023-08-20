package com.henry.basic_chapter_01.collection_types.bag.via_linked_node;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

// 目标：实现背包数据结构
// 数据类型的预期特征：{#1 支持泛型; #2 支持迭代; #3 支持Bag默认的CRU操作}
// 实现手段：使用链表作为底层的数据结构
// 执行手段： redirect input from <tobe.txt>
public class Bag<Item> implements Iterable<Item> { // #1 在类声明中，使用<类型参数> 来 支持泛型  #2 实现 Iterable接口 来 支持迭代

    // 底层使用链表作为存储结（作为递归的数据结构，节点即链表）
    private Node startNode; // 指向起始节点的指针
    private int itemAmount;

    private class Node {
        Item item; // 当前节点中的元素
        Node nextNode; // 链表中 当前节点所指向的下一个节点
    }

    // 手段：把新添加的节点 作为起始节点
    public void add(Item item) {
        Node previousStartNode = startNode;

        // 对于 Bag数据结构来说，只需要考虑startNode即可
        startNode = new Node();
        startNode.item = item;
        startNode.nextNode = previousStartNode;

        itemAmount++;
    }

    @Override
    public Iterator<Item> iterator() {
        return new MyBagIterator(); // 自定义字节的迭代器
    }

    // 手段：实现Iterator接口
    private class MyBagIterator implements Iterator<Item> {

        // 为了避免干扰 ”链表中已经存在的指针“
        // 手段：这里添加一个新的指针 作为实例变量， 并将之初始化为起始指针
        // 作用：为遍历集合提供了一个新的起始指针；
        private Node currentNode = startNode;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();

            Item item = currentNode.item;
            // 把指针移动到下一个节点上
            currentNode = currentNode.nextNode;

            // 返回当前节点中的元素
            return item;
        }
    }

    public int size() {
        return itemAmount;
    }

    public static void main(String[] args) {
        Bag<String> bag = new Bag<String>();

        // 从输入流中读取字符串，并添加到bag对象中
        // 手段：#1 StdIn.readString(); #2 bag.add()
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            bag.add(item);
        }

        StdOut.println("size of bag = " + bag.size());

        // 遍历bag，并打印其中的元素
        for (String item : bag) {
            StdOut.println(item);
        }
    }
}

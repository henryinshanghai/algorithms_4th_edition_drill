package com.henry.basic_chapter_01.collection_types.bag_via_linked_node;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

// 以 链表为底层数据结构 来 实现 泛型、可迭代的背包数据结构（元素存储顺序 与 添加到集合中的顺序 不相关）
// 执行手段： redirect input from <tobe.txt>
public class BagViaNodeTemplate <Item> implements Iterable<Item> {

    private Node startNode; // 指向起始节点的指针
    private int itemAmount;

    @Override
    public Iterator<Item> iterator() {
        return new MyBagIterator();
    }

    private class Node {
        Item item;
        Node nextNode;
    }

    public void add(Item item) {
        Node previousStartNode = startNode;

        // 对于 Bag数据结构来说，只需要考虑startNode即可
        startNode = new Node();
        startNode.item = item;
        startNode.nextNode = previousStartNode;

        itemAmount++;
    }


    private class MyBagIterator implements Iterator<Item> {

        // 为了避免干扰 ”链表中已经存在的指针“，这里添加一个新的指针 作为实例变量
        // 更新 等于号 左边的变量
        private Node currentNode = startNode;

        @Override
        public boolean hasNext() {
            return currentNode == null;
        }

        @Override
        public Item next() {
            Item item = currentNode.item;
            currentNode = currentNode.nextNode;
            return item;
        }
    }

    public static void main(String[] args) {
        BagViaNodeTemplate<String> bag = new BagViaNodeTemplate<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            bag.add(item);
        }

        StdOut.println("size of bag = " + bag.size());
        for (String s : bag) {
            StdOut.println(s);
        }
    }

    private int size() {
        return itemAmount;
    }
}

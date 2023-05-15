package com.henry.basic_chapter_01.collection_types.stack_via_linked_node;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

// 以 链表作为底层数据结构 来 实现 ”泛型“、”可迭代“的下压栈数据结构
// 执行手段：redirect input from <tobe.txt>
public class StackViaNodeTemplate<Item> implements Iterable<Item> {

    public static void main(String[] args) {
        StackViaNodeTemplate<String> myStackViaNodeTemplate = new StackViaNodeTemplate<>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();

            if (!item.equals("-")) {
                myStackViaNodeTemplate.push(item);
            } else if (!myStackViaNodeTemplate.isEmpty()) {
                StdOut.print(myStackViaNodeTemplate.pop() + " ");
            }
        }

        StdOut.println("(" + myStackViaNodeTemplate.size() + " left on stack)");
    }

    // 底层使用的数据结构 - 链表
    private Node topNode;
    private int itemAmount;


    private class Node {
        private Item item;
        Node nextNode;
    }

    public boolean isEmpty() {
        return topNode == null;
    }

    public int size() {
        return itemAmount;
    }

    public void push(Item item) {
        // 记录当前链表中的起始节点
        Node previousTopNode = topNode;

        // 创建新的结点，并使 “链表的起始节点 topNode”指向它
        topNode = new Node();
        topNode.item = item;
        topNode.nextNode = previousTopNode;

        itemAmount++;
    }

    public Item pop() {
        Item item = topNode.item;

        // 使 “链表的起始节点 topNode” 指向它的下一个节点
        topNode = topNode.nextNode;
        itemAmount--;

        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new MyStackIterator();
    }

    private class MyStackIterator implements Iterator<Item> {

        // 为了使 迭代的操作 不会干扰 链表上的原始指针，这里添加一个 原始链表的副本 来 作为实例变量
        private Node currentNode = topNode;

        @Override
        public boolean hasNext() { // 是否存在下一个节点： 手段：当前节点是否为null
            return currentNode != null;
        }

        @Override
        public Item next() { // 获取下一个节点； 手段：获取当前节点的值，然后移动指针到下一个节点
            Item item = currentNode.item;
            currentNode = currentNode.nextNode;
            return item;
        }
    }
}

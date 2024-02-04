package com.henry.basic_chapter_01.collection_types.stack.implementation.via_linked_node;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

// 目标：实现 动态容量、能够处理泛型参数、支持迭代元素操作的栈数据结构
// 数据类型的预期特征：{#1 支持泛型类型; #2 栈的容量是能够动态变化的; #3 支持对集合中元素的迭代操作}
// 实现手段：使用链表作为底层的数据结构
// 执行手段：redirect input from <tobe.txt>
public class Stack<Item> implements Iterable<Item> {

    // 底层使用的数据结构 - 链表
    private Node topNode;
    private int itemAmount;

    // 用于表示链表/链表中节点的数据类型 - 这里出于方便目的，把它定义成为内部类
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

    // 入栈操作 - 向链表中添加一个新的节点，并将之作为新的起始节点
    public void push(Item item) {
        // 记录当前链表中的起始节点
        Node previousTopNode = topNode;

        // 创建新的结点，并使 “链表的起始节点 topNode”指向它
        topNode = new Node();
        topNode.item = item;
        topNode.nextNode = previousTopNode;

        itemAmount++;
    }

    // 出栈操作 - 移除链表中的第一个节点，并返回节点中的元素
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
        // 原因：与数组不同，在链表中获取下一个节点，只能通过“当前节点”的下一个指针
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

    public static void main(String[] args) {
        Stack<String> myStackViaNodeTemplate = new Stack<>();

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
}

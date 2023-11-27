package com.henry.graph_chapter_04.no_direction_graph_01;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 一个用来存储某种类型元素（泛型）的容器
 * 容器操作：#1 不支持从中删除; #2 支持add; #3 支持遍历所有元素的操作
 * 容器特征：进行迭代时，迭代的顺序不确定 -> 应用：client可以使用它 来 声明元素的处理顺序不重要。
 */
/*
验证： 为了使自定义的容器类型，能够支持”迭代其中元素“的操作 - 手段：实现Iterable接口
验证： 为了使自定义的容器能够支持任意类型的元素 - 手段：使用泛型参数 <Item>
 */
public class Bag<Item> implements Iterable<Item> { // 为了使Bag支持 foreach语法 - 实现自 Iterable<Item>
    private Node<Item> firstNode; // 使用头插法来扩展链表
    private int itemAmount; // bag容器中的元素个数

    // 用于辅助的 链表类型/节点类型
    // 概念的递归定义 / 概念对应的 递归数据结构
    /*
        链表是一种递归的数据结构。它：
        #1 或者为空（null）; #2 或者是 “指向一个结点（node）的引用” - 结点中含有 一个泛型的元素 + 一个“指向另一条链表的引用”

        术语：
        #1 在 Bag类中定义的Node类，有时候会被叫做成 “记录”；
        #2 “链接”：表示 对结点的引用；

        practice：在方法中直接使用 实例变量；
     */
    private static class Node<Item> {
        private Item item;
        private Node<Item> nextNode;
    }

    public Bag() {
        firstNode = null;
        itemAmount = 0;
    }

    public boolean isEmpty() {
        return firstNode == null;
    }

    public int size() {
        return itemAmount;
    }

    // 向Bag中添加元素
    public void add(Item item) {
        // 使用一个变量 来 记录链表当前的头节点
        Node<Item> oldFirst = firstNode;
        // 创建并初始化 新的节点 作为“当前的头节点”
        firstNode = new Node<Item>();
        firstNode.item = item;
        firstNode.nextNode = oldFirst;

        itemAmount++;
    }

    // 实现 Iterable接口中的抽象方法 - iterator()方法
    // 特征：该方法会返回一个 Iterator对象 - 手段：自定义Iterator类型
    public Iterator<Item> iterator() {
        return new LinkedIterator(firstNode);
    }

    // 自定义Iterator类型 - 手段：实现 Iterator接口（来自于JDK）
    private class LinkedIterator implements Iterator<Item> {

        // 声明一个节点 - 用来支持迭代器的操作
        private Node<Item> currentNode;

        // 初始化成员变量 - 使用构造器参数
        public LinkedIterator(Node<Item> passInNode) {
            this.currentNode = passInNode;
        }

        // 是否有下一个节点？    手段：当前引用是否为null - 只要不为null，说明就会有下一个节点（null是最后一个节点）
        public boolean hasNext() {
            return currentNode != null;
        }

        // 获取当前节点的值，并把指针移动到下一个节点
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            // 获取当前节点的值
            Item item = currentNode.item;
            // 把指针移动到下一个节点
            currentNode = currentNode.nextNode;
            // 返回当前节点的值
            return item;
        }

        // 暂时不支持remove操作
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Bag<String> bag = new Bag<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            bag.add(item);
        }

        // 读取bag中的元素
        StdOut.println("bag's size is: " + bag.size());
        for (String s : bag) {
            StdOut.println(s);
        }
    }
}

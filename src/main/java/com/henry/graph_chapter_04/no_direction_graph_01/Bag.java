package com.henry.graph_chapter_04.no_direction_graph_01;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 一个用来存储某种类型元素（泛型）的容器
 * 这与链表有什么区别呢？
 */
public class Bag<Item> implements Iterable<Item> { // 为了使Bag支持 foreach语法 - 实现自 Iterable<Item>
    private Node<Item> first; // 使用头插法来扩展链表
    private int n; // bag容器中的元素个数

    // 用于辅助的 链表类型/节点类型
    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
    }

    // 初始化操作
    public Bag() {
        first = null;
        n = 0;
    }

    // 对外提供的APIs
    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    // 向Bag中添加元素
    public void add(Item item) {
        Node<Item> oldFirst = first; // Ⅰ 添加一个新的引用，指向现有节点
        first = new Node<Item>(); // Ⅱ 创建新的节点, 并把first引用指向它
        first.item = item; // Ⅲ 向新节点中添加数据item
        first.next = oldFirst; // Ⅳ 建立新节点 与 旧节点之间的连接关系
        n++;
    }

    // 获取集合的迭代器对象
    public Iterator<Item> iterator() {
        return new LinkedIterator(first);
    }

    // 迭代器需要哪些APis
    private class LinkedIterator implements Iterator<Item> { // Iterator来自于JDK

        // 声明一个节点 - 用来支持迭代器的操作
        private Node<Item> current;

        // 初始化成员变量 - 使用构造器参数
        public LinkedIterator(Node<Item> first) {
            this.current = first;
        }

        // 是否有下一个节点？    手段：当前引用是否为null - 只要不为null，说明就会有下一个节点（null是最后一个节点）
        public boolean hasNext() {
            return current != null;
        }

        // 获取当前节点的值，并把指针移动到下一个节点
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            // 获取当前节点的值
            Item item = current.item;
            // 把指针移动到下一个节点
            current = current.next;
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

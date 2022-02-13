package com.henry.symbol_table_chapter_03.symbol_table_01.usageDemo;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个基本功能的符号表类型
 */
public class SequentialSearchST_drill03<Key, Value> {
    // 实例变量
    private Node first;
    private int n;

    class Node{
        private Key key;
        private Value value;
        private Node next;

        public Node(Key key, Value value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    // 快捷API
    public boolean isEmpty(){
        return n == 0;
    }

    public int size(){
        return n;
    }

    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("符号表中不存在以null作为键的键值对");
        return get(key) != null;
    }

    // 核心API
    /**
     * 根据指定的key从符号表中获取键值对
     * @param key
     * @return
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("符号表中不存在以null作为键的键值对");

        for (Node x = first; x != null; x = x.next) {
            if (x.key.equals(key)) {
                return x.value;
            }
        }

        return null;
    }

    public void put(Key key, Value value) {
        if (key == null) throw new IllegalArgumentException("符号表中不存在以null作为键的键值对");

        // 使用put来实现删除操作
        if (value == null) {
            delete(key);
            return; // Mark:这里操作完成后需要return; 否则下面的代码可能会继续执行
        }

        // 使用put来实现更新操作
        for (Node x = first; x != null; x = x.next) {
            if (x.key.equals(key)) {
                x.value = value;
                return;
            }
        }

        // 使用put来实现插入操作     手段：创建新的节点来更新first指针
        first = new Node(key, value, first);

    }

    private void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("符号表中不存在以null作为键的键值对");

        first = delete(first, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;

        if (x.key.equals(key)) {
            n--;
            return x.next;
        }

        // 1 把A视为 x + B； 2 从B中删除指定节点； 3 把得到的新链表拼接到x上
        x.next = delete(x.next, key);
        return x;
    }

    public Iterable<Key> keys(){
//        List<Key> keys = new ArrayList<Key>();
        Queue<Key> keys = new Queue<>();
        for (Node x = first; x != null; x = x.next) {
//            keys.add(x.key);
            keys.enqueue(x.key);
        }

        return keys;
    }


    public static void main(String[] args) {
        SequentialSearchST_drill03<String, Integer> st =
                new SequentialSearchST_drill03<>();

        for (int i = 0; !StdIn.isEmpty(); i++) {
            st.put(StdIn.readString(), i);
        }

        for (String key : st.keys()) {
            System.out.println(key + " : " + st.get(key));
        }
    }

}
/*
结果：E:12 被多次打印
找原因：应该是put()方法的问题
 */
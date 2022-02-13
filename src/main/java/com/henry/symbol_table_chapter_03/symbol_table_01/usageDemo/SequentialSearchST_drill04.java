package com.henry.symbol_table_chapter_03.symbol_table_01.usageDemo;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;

/**
 * 经验：// Mark:put()中，这里操作完成后需要return; 否则下面的代码可能会继续执行
 * @param <Key>
 * @param <Value>
 */
public class SequentialSearchST_drill04<Key, Value>{
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
        if (key == null) throw new IllegalArgumentException("指定的键为null");
        return get(key) != null;
    }


    // 核心API
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("指定的键为null");

        for (Node x = first; x != null; x = x.next) {
            if (x.key.equals(key)) {
                return x.value;
            }
        }

        return null;
    }

    public void put(Key key, Value value) {
        if (key == null) throw new IllegalArgumentException("指定的键为null");

        if (value == null) {
            delete(key);
            return;
        }

        for (Node x = first; x != null; x = x.next) {
            if (x.key.equals(key)) {
                x.value = value;
                return;
            }
        }

        // 插入操作
        first = new Node(key, value, first);
        n++;
    }

    private void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("指定的键为null");
        // 删除指定节点后，把新的链表绑定回到first链表
        first = delete(first, key);
    }

    /**
     * 从指定的链表中删除指定的节点，并返回得到的新链表
     * @param x
     * @param key
     * @return
     */
    private Node delete(Node x, Key key) {
        // 终结情况
        if (x == null) return null;

        if (x.key.equals(key)) {
            n--;
            return x.next; // 1 删除指定节点；  手段：移动first指针 2 返回值：新链表
        }

        // 做什么
        x.next = delete(x.next, key);
        return x;
    }

    public Iterable<Key> keys(){
        Queue<Key> keys = new Queue<>();
        for (Node x = first; x != null; x = x.next) {
            keys.enqueue(x.key);
        }

        return keys;
    }

    public static void main(String[] args) {
        SequentialSearchST_drill04<String, Integer> st =
                new SequentialSearchST_drill04<>();

        for (int i = 0; !StdIn.isEmpty(); i++) {
            st.put(StdIn.readString(), i); // 插入&更新
        }

        for (String key : st.keys()) {
            System.out.println(key + " :" + st.get(key));
        }
    }
}

package com.henry.symbol_table_chapter_03.symbol_table_01.usageDemo;


import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;

/**
 * 符号表的实现
 * 特征：符号表中的键值对没有排序
 * @param <Key>
 * @param <Value>
 */
public class SequentialSearchST_drill01<Key, Value> {
    // 实例变量
    private int n;
    private Node first;

    class Node{
        // 实例变量
        private Key key;
        private Value value;
        private Node next;

        public Node(Key key, Value value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    /**
     * Initializes an empty symbol table. 符号表
     */
    public SequentialSearchST_drill01() {
    }


    // API
    public boolean isEmpty(){
        return n == 0;
    }

    public int size(){
        return n;
    }

    public boolean contains(Key key) {
        if(key == null) throw new IllegalArgumentException("符号表中不包含以null作为键的键值对");
        return get(key) != null;
    }

    // 核心API
    public void put(Key key, Value value) {
        if(key == null) throw new IllegalArgumentException("键值对的键不能为null");

        // 通过put()执行删除操作
        if (value == null) { // 表示使用者想要删除键为key的键值对
            delete(key);
            return;
        }

        // 通过put()执行更新操作
        for (Node x=first; x!=null; x = x.next) {
            if (x.key.equals(key)) {
                x.value = value;
                return;
            }
        }

        // 通过put()执行添加操作
        first = new Node(key, value, first); // Mark2:这里直接用新建的节点来更新first指针
        n++;
    }

    /**
     * 获取指定key对应的值
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

    /**
     * 删除符号表中指定的key所对应的键值对
     * 从链表中删除元素
     * @param key
     */
    private void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("符号表中不存在键为null的键值对");
        first = delete(first, key);
    }

    /**
     * 从指定链表中删除掉指定key对应的键值对
     * @param x 链表的第一个节点
     * @param key 指定的键
     * @return  递归的新方式
     */
    private Node delete(Node x, Key key) { // Mark1:这是一种挺奇特的实现方式
        if (key == null) throw new IllegalArgumentException("符号表中不存在键为null的键值对");

        if (x.key.equals(key)) {
            n--;
            return x.next; // 链表的新节点
        }

        x.next = delete(x.next, key);
        return x;
    }

    public Iterable<Key> keys(){
        Queue<Key> keys = new Queue<Key>(); // 这里的Queue是引用的jar包中的Queue，所以不是一个抽象类

        for (Node x = first; x != null ; x = x.next) {
            keys.enqueue(x.key);
        }

        return keys;
    }

    public static void main(String[] args) {
        SequentialSearchST_drill01<String, Integer> st =
                new SequentialSearchST_drill01<>();

        // 从输入流中接收字符串，构建键值对并添加到符号表中
        for (int i = 0; !StdIn.isEmpty() ; i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        for (String key :
                st.keys()) {
            System.out.println(key + " ： " + st.get(key));
        }
    }
}

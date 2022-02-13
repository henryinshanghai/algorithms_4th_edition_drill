package com.henry.symbol_table_chapter_03.symbol_table_01.usageDemo;

import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.List;

/**
 * 2020/07/28 henry
 * it hasn't been easy, but we make it anyway~
 * @param <Key>
 * @param <Value>
 */
public class SequentialSearchST_drill02<Key, Value> {
    // 实例变量
    private int n;
    private Node first; // 用一个链表作为底层数据结构

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

    // 辅助API
    public boolean isEmpty(){
        return n == 0;
    }

    public int size(){
        return n;
    }

    public boolean contains(Key key) {
        if(key == null) throw new IllegalArgumentException("符号表中不存在以null为键的键值对");

//        return get(key) != null;
        return true; // fixme later
    }

    // 核心API
    /**
     * 向符号表中插入指定的键值对
     * @param key 键值对的键
     * @param value 键值对的值
     */
    public void put(Key key, Value value) {
        if (key == null) throw new IllegalArgumentException("符号表中键值对的键不允许为null");

        // 通过put执行删除操作
        if (value == null) { // 防御性编程
            if (contains(key)){
                delete(key);
                n--;
            }
        }

        // 通过put执行更新操作
        if (contains(key)) {
            for (Node x = first; x != null; x = x.next) {
                if (x.key.equals(key)) {
                    x.value = value;
                }
            }
        }

        // 通过put执行插入操作————把新节点作为链表的头节点
        first = new Node(key, value, first);
        n++;

    }

    // 核心API
    public Value get(Key key){
        if (key == null) throw new IllegalArgumentException("符号表中不能存在null作为键的键值对");

        for (Node x = first; x != null; x = x.next) {
            if (x.key.equals(key)) {
                return x.value;
            }
        }

        return null;
    }

    /**
     * 从符号表中删除指定key的键值对
     * @param key
     */
    public void delete(Key key) {
        if(key == null) throw new IllegalArgumentException("符号表中不存在键为null的键值对");
        first = delete(first, key);
    }

    /**
     * 〇 递归方法的作用：从指定的链表中删除指定的节点，并返回删除节点后的新链表
     * @param x 链表的头节点
     * @param key 节点的key
     * @return
     */
    private Node delete(Node x, Key key) {
        // Ⅰ 递归终止条件
        // 1 链表为空
        if(x == null) return null;

        // 2 链表的首节点就是需要删除的节点
        if (x.key.equals(key)) {
            n--;
            return x.next; // 新链表的首节点
        }

        // Ⅱ 方法应该返回给上一级什么？ 注：这个其实在方法的作用中就已经明确了  一个新的链表
        // Ⅲ 本机递归应该做些什么？
        // 1 把原始链表分成两个部分：原始链表A = 首节点x + 剩余链表B 2 在B上调用delete()方法； 3 把delete()方法返回的新链表连接到首节点x上
        x.next = delete(x.next, key);

        // 4 返回给调用方，删除指定节点后的新链表
        return x;
    }

    /**
     * 获取到符号表中所有key的集合
     * 注：返回值使用一个可迭代的对象；     作用：方便用例代码中直接使用foreach语法
     * 手段：Iterable<Key>
     */
    public Iterable<Key> keys(){
//        // 创建一个可迭代的容器对象
//        List<Key> keys = new ArrayList<Key>();
//
//        // 向容器中添加key
//        for (Node x = first; x != null; x = x.next) {
//            keys.add(x.key);
//        }
//
//        // 返回满载的容器对象
//        return keys;

        Queue<Key> queue = new Queue<Key>();
        for (Node x = first; x != null; x = x.next)
            queue.enqueue(x.key);
        return queue;
    }

    // 核心API完成后，接下来是编写用例代码
    public static void main(String[] args) {
        // 创建一个符号表
        SequentialSearchST_drill02<String, Integer> st =
                new SequentialSearchST_drill02<>();

        // 向符号表中插入一些键值对
        st.put("henry", 26);
        st.put("quinta", 30);
        st.put("jane", 26);
        st.put("allyme", 27);

        // 遍历符号表中的所有键值对
        for (String key : st.keys()) {
            System.out.println(key + "：" + st.get(key));
        }
    }

}

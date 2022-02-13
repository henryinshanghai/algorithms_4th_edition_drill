package com.henry.symbol_table_chapter_03.symbol_table_01.usageDemo;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac SequentialSearchSTFromWebsite.java
 *  Execution:    java SequentialSearchSTFromWebsite
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/31elementary/tinyST.txt
 *
 *  Symbol table implementation with sequential search in an
 *  unordered linked list of key-value pairs.
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *
 *  % java SequentialSearchSTFromWebsite < tinyST.txt
 *  L 11
 *  P 10
 *  M 9
 *  X 7
 *  H 5
 *  C 4
 *  R 3
 *  A 8
 *  E 12
 *  S 0
 *
 ******************************************************************************/

/**
 *  The {@code SequentialSearchSTFromWebsite} class represents an (unordered)
 *  symbol table of generic key-value pairs.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides a <em>keys</em> method for iterating over all of the keys.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  The class also uses the convention that values cannot be {@code null}. Setting the
 *  value associated with a key to {@code null} is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  It relies on the {@code equals()} method to test whether two keys
 *  are equal. It does not call either the {@code compareTo()} or
 *  {@code hashCode()} method.
 *  <p>
 *  This implementation uses a <em>singly linked list</em> and
 *  <em>sequential search</em>.
 *  The <em>put</em> and <em>delete</em> operations take &Theta;(<em>n</em>).
 *  The <em>get</em> and <em>contains</em> operations takes &Theta;(<em>n</em>)
 *  time in the worst case.
 *  The <em>size</em>, and <em>is-empty</em> operations take &Theta;(1) time.
 *  Construction takes &Theta;(1) time.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://algs4.cs.princeton.edu/31elementary">Section 3.1</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class SequentialSearchSTFromWebsite<Key, Value> {
    // 实例变量
    private int n;           // number of key-value pairs 键值对的数量
    private Node first;      // the linked list of key-value pairs 键值对组成的链表

    // a helper linked list data type 辅助的链表类型
    private class Node {
        private Key key;
        private Value val;
        private Node next; // 指向的下一个节点

        public Node(Key key, Value val, Node next)  {
            this.key  = key;
            this.val  = val;
            this.next = next;
        }
    }

    /**
     * Initializes an empty symbol table. 符号表
     */
    public SequentialSearchSTFromWebsite() {
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return n;
    }

    /**
     * Returns true if this symbol table is empty.
     *
     * @return {@code true} if this symbol table is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns true if this symbol table contains the specified key.
     *
     * @param  key the key
     * @return {@code true} if this symbol table contains {@code key};
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key in this symbol table.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key))
                return x.val;
        }
        return null;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) { // 通过给key绑定null来删除键值对
            delete(key);
            return;
        }

        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        first = new Node(key, val, first); // 更新first节点————在头部添加新的节点 WHY？这样就只需要维护链表的一端吗？
        n++;
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        first = delete(first, key);
    }

    /**
     * TODO I don't understand this
     * 有返回值的递归方法
     * 作用：删除指定节点，并返回新的链表
     * @param x
     * @param key
     * @return
     */
    // delete key in linked list beginning at Node x
    // warning: function call stack too large if table is large
    private Node delete(Node x, Key key) {
        // delete操作的终止条件：1 链表已经空了（aka x == null） 2 找到指定key对应的节点
        if (x == null) return null;

        if (key.equals(x.key)) {
            n--;
            return x.next; // 新链表的首节点
        }

        x.next = delete(x.next, key);

        // 3 返回什么给上一级？ 返回已经处理好的链表（使用首节点来表示链表）
        return x;
    }
    // 2 找到需要返回的值：应该返回给上一级什么信息？删除此节点后的新链表
    // 3 本级递归应该做什么：从剩下的链表中删除掉指定的节点，并返回删除掉节点之后的链表
    // delete()触底时，会返回被删除节点的下一个节点: expectedDelNode.next 所以 x = expectedDelNode
    // 原始链表 = 首节点x + 剩下的链表B
    // 把删除掉节点之后的新链表视为C
    // 返回的新链表直接连接到首节点x上 x.next = delete(x.next, key);


    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table
     */
    public Iterable<Key> keys()  {
        Queue<Key> queue = new Queue<Key>();
        // 因为是通过移动first来扩展链表的，所以第一个first是：L-11
        for (Node x = first; x != null; x = x.next)
            queue.enqueue(x.key);
        return queue;
    }


    /**
     * Unit tests the {@code SequentialSearchSTFromWebsite} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // 符号表实例
        SequentialSearchSTFromWebsite<String, Integer> st = new SequentialSearchSTFromWebsite<String, Integer>();
        // 接收标准输入中的字符串
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            // 添加字符串 - 字符串序号
            st.put(key, i);
        }

        // 遍历打印符号表中的键值对     手段：1 获取到键的集合； 2 通过get(key)的方式获取到值
        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }
}
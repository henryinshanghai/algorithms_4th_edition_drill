package com.henry.symbol_table_chapter_03.implementation.primary;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac LinkedNodeSymbolTable.java
 *  Execution:    java LinkedNodeSymbolTable
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/31elementary/tinyST.txt
 *
 *  Symbol table implementation with sequential search in an
 *  unordered linked list of key-value pairs.
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *
 *  % java LinkedNodeSymbolTable < tinyST.txt
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

// 验证：可以使用链表 来 实现符号表
// 手段：在链表的节点中，定义 key -> value的映射关系
public class LinkedNodeSymbolTable<Key, Value> {
    private int pairAmount;           // 符号表中 键值对的数量
    private Node firstNode;      // 实现符号表的底层数据结构：链表 - 作为递归结构，链表的头节点即可代表链表本身

    // 自定义的节点类型
    private class Node {
        private Key key; // 键值对中的键
        private Value value; // 键值对中的值
        private Node nextNode; // 当前节点所指向的下一个节点

        public Node(Key key, Value value, Node nextNode) {
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }
    }

    /**
     * 初始化一个空的符号表
     */
    public LinkedNodeSymbolTable() {
    }

    /**
     * 返回符号表中的键值对数量
     */
    public int size() {
        return pairAmount;
    }

    /**
     * 符号表是否为空
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 符号表是否包含指定的key
     * 如果key是null，则：抛出 异常
     */
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return getAssociatedValueOf(key) != null;
    }

    /**
     * 获取符号表中 与指定key相关联的value
     * <p>
     * 如果key不在符号表中，则：返回null
     * 如果key是null，则：抛出异常
     *
     * @param passedKey
     */
    public Value getAssociatedValueOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to get() is null");
        for (Node currentNode = firstNode; currentNode != null; currentNode = currentNode.nextNode) {
            if (passedKey.equals(currentNode.key))
                return currentNode.value; // 命中
        }
        return null; // 未命中
    }

    /**
     * 向符号表中插入 指定的键值对
     * 如果符号表中已经存在有指定的key的话，则：使用新的值 来 覆写旧的值
     * 如果传入的值是null的话，则：从符号表中删除指定的key（以及它关联的值）
     * <p>
     * 如果key是null的话，则：抛出异常
     *  @param passedKey
     * @param associatedValue
     */
    public void putInPairOf(Key passedKey, Value associatedValue) {
        if (passedKey == null) throw new IllegalArgumentException("firstNode argument to put() is null");
        // #1 删除键值对的操作
        if (associatedValue == null) {
            deletePairOf(passedKey);
            return;
        }

        // #2 命中，则：更新键对应的值
        for (Node currentNode = firstNode; currentNode != null; currentNode = currentNode.nextNode) {
            if (passedKey.equals(currentNode.key)) { // 命中
                currentNode.value = associatedValue;
                return;
            }
        }

        // #3 未命中，则：为传入的键值对 创建新的节点 - 手段：创建新的节点，并将之作为链表的头节点
        firstNode = new Node(passedKey, associatedValue, firstNode);
        pairAmount++;
    }

    /**
     * 如果指定的key存在于符号表中，则：从符号表中移除 指定的key和它所关联的value
     * 如果 key是null的话，则：抛出异常
     *
     * @param passedKey
     */
    public void deletePairOf(Key passedKey) {
        if (passedKey == null) throw new IllegalArgumentException("argument to delete() is null");
        firstNode = deletePairFrom(firstNode, passedKey);
    }

    /**
     * 在以节点x 为起点的链表中，删除key
     * 警告：如果符号表很大的话，函数调用栈也会太大
     *
     * @param passedNode
     * @param passedKey
     * @return
     */
    private Node deletePairFrom(Node passedNode, Key passedKey) {
        // delete操作的终止条件：1 链表已经空了（aka x == null） 2 找到指定key对应的节点
        if (passedNode == null) return null;

        // 如果要删除的key 是链表的头节点，则：直接返回 当前头节点的下一个节点
        if (passedKey.equals(passedNode.key)) {
            pairAmount--;
            return passedNode.nextNode; // 新链表的首节点
        }

        // 在剩下的链表中，继续 “查找&删除”指定key的节点（递归操作）
        passedNode.nextNode = deletePairFrom(passedNode.nextNode, passedKey);

        // 递归调用结束后（表示任务完成），返回 “删除了指定节点的链表”
        return passedNode;
    }

    /**
     * 以一个 Iterable对象 来 返回符号表中的所有key
     * 如果想要 遍历符号表中所有的key，可以使用foreach语法
     * <p>
     * 返回符号表中所有的key
     */
    public Iterable<Key> getIterableKeys() {
        Queue<Key> keysQueue = new Queue<Key>();

        for (Node currentNode = firstNode; currentNode != null; currentNode = currentNode.nextNode)
            keysQueue.enqueue(currentNode.key);
        return keysQueue;
    }


    /**
     * LinkedNodeSymbolTable 数据类型的单元测试
     * <p>
     * 参数：命令行参数
     */
    public static void main(String[] args) {
        // 创建符号表对象
        LinkedNodeSymbolTable<String, Integer> linkedNodeSymbolTable = new LinkedNodeSymbolTable<String, Integer>();

        // 读取 标准输入中的字符串 - 手段：读取文件内容作为输入
        // 具体方法：#1 redirect input from file; #2 使用 StdIn工具类读取文件内容
        for (int currentSpot = 0; !StdIn.isEmpty(); currentSpot++) {
            String keyOnCurrentSpot = StdIn.readString();
            // 向符号表中添加 键（字符串）-> 值（字符串的位置）对
            linkedNodeSymbolTable.putInPairOf(keyOnCurrentSpot, currentSpot);
        }

        // 打印符号表中的键值对    手段：1 获取到键的集合； 2 通过get(key)的方式获取到值
        // 特征：键值对 会按照它们被插入到符号表中的顺序打印
        for (String currentKey : linkedNodeSymbolTable.getIterableKeys())
            StdOut.println(currentKey + " " + linkedNodeSymbolTable.getAssociatedValueOf(currentKey));
    }
}
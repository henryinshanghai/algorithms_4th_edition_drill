package com.henry.symbol_table_chapter_03.implementation_02.advanced.via_hash_table.via_chaining.implementation_02;


import com.henry.symbol_table_chapter_03.implementation_02.primary.LinkedNodeSymbolTable;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;

import java.util.ArrayList;
import java.util.List;

// 验证：可以使用 拉链法的散列表（数组 + 链表） 来 实现符号表（aka 可以存储与获取 键值对的数据结构）
// 验证：SeparateChainingSymbolTable的符号表实现 相比于 LinkedNodeSymbolTable符号表实现 效率要高出997倍（as set）。
public class SeparateChainingSymbolTable<Key, Value> {

    private int pairAmount; // 符号表中键值对的总数量
    private int tableSize; // 底层数组的容量大小 aka 链接数量

    // 符号表的底层实现：由链表 作为基本元素，所组成的数组
    private LinkedNodeSymbolTable<Key, Value>[] linkedListArray;

    public SeparateChainingSymbolTable() {
        this(997);
    }

    public SeparateChainingSymbolTable(int tableSize) {
        // 使用 传入的参数 作为 底层数组的容量大小
        this.tableSize = tableSize;

        // 初始化 散列表的容量 为 指定大小
        linkedListArray = (LinkedNodeSymbolTable<Key, Value>[]) new LinkedNodeSymbolTable[tableSize];

        // 初始化 散列表的每个元素 为 空链表
        for (int currentSpot = 0; currentSpot < tableSize; currentSpot++) {
            // 把 空链表 绑定到 当前元素上
            linkedListArray[currentSpot] = new LinkedNodeSymbolTable<>();
        }
    }

    /**
     * 计算 指定的key 的hash值
     * 手段：① 先 调用Object的hashCode()得到 哈希值；
     * ② 再 和 一个特定常数 按位与；
     * ③ 最后 把得到的结果 对 数组大小取余。
     *
     * @param passedKey 传入的key
     * @return 返回 该key的hash值
     */
    private int hash(Key passedKey) {
        return (passedKey.hashCode() & 0x7fffffff) % tableSize;
    }

    /**
     * 获取到 符号表中的 指定条目中的value
     * 手段：① 先计算出 指定key的hash值；
     * ② 根据 计算得到的hash值 索引到 其在散列表中的链表元素；
     * ③ 调用 链表的查询元素的API 来 查找指定的节点
     *
     * @param passedKey 指定的键
     * @return 该键所对应的value
     */
    public Value getAssociatedValueOf(Key passedKey) {
        return linkedListArray[hash(passedKey)].getAssociatedValueOf(passedKey);
    }

    /**
     * 向符号表中 添加指定的条目
     * 手段：① 先计算 指定的key的hash值；
     * ② 根据 计算出的hash值 来 索引到 散列表中的链表元素（其实也是一个符号表）；
     * ③ 调用 链表的 添加元素的方法 来 添加条目
     *
     * @param passedKey       指定条目的键
     * @param associatedValue 键所关联的值
     */
    public void putInto(Key passedKey, Value associatedValue) {
        linkedListArray[hash(passedKey)].putInPairOf(passedKey, associatedValue);
    }

    /**
     * 从符号表中 删除指定的键 及 其所关联的值（如果该key存在于符号表中）
     * @param key 指定的key
     * @throws IllegalArgumentException 如果传入的key是一个null
     */
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");

        int hashedSpot = hash(key);
        if (linkedListArray[hashedSpot].contains(key)) pairAmount--;
        linkedListArray[hashedSpot].deletePairOf(key);

        // halve table size if average length of list <= 2
        if (tableSize > INIT_CAPACITY && pairAmount <= 2 * tableSize) {
            resize(tableSize / 2);
        }
    }

    private void resize(int newCapacity) {
        SeparateChainingSymbolTable<Key, Value> temp = new SeparateChainingSymbolTable<>();

        /* 把当前符号表中的每个条目 都重新添加到 temp符号表中 */
        for (int currentSpot = 0; currentSpot < tableSize; currentSpot++) {
            for (Key currentKey : linkedListArray[currentSpot].getIterableKeys()) {
                temp.putInto(currentKey, linkedListArray[currentSpot].getAssociatedValueOf(currentKey));
            }
        }

        /* 把 当前对象的成员变量 指向temp的成员变量 */
        this.pairAmount = temp.pairAmount;
        this.tableSize = temp.tableSize;
        this.linkedListArray = temp.linkedListArray;
    }

    private static final int INIT_CAPACITY = 4;

    public Iterable<Key> keys() {
        Queue<Key> iterableKeys = new Queue<>();
        for (int currentSpot = 0; currentSpot < linkedListArray.length; currentSpot++) {
            for (Key currentKey : linkedListArray[currentSpot].getIterableKeys()) {
                iterableKeys.enqueue(currentKey);
            }
        }

        return iterableKeys;
    }

    // 针对此数据结构功能的单元测试
    public static void main(String[] args) {
        SeparateChainingSymbolTable<String, Integer> symbolTable = new SeparateChainingSymbolTable<>();

        for (int currentSpot = 0; !StdIn.isEmpty(); currentSpot++) {
            String key = StdIn.readString();
            symbolTable.putInto(key, currentSpot);
        }

        // 打印符号表中所有的键
        for (String currentKey : symbolTable.keys()) {
            System.out.println(currentKey + " -> " + symbolTable.getAssociatedValueOf(currentKey));
        }
    }
}

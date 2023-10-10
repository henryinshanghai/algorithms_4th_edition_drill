package com.henry.symbol_table_chapter_03.implementation_02.advanced.via_hash_table.via_chaining.implementation_02;


import com.henry.symbol_table_chapter_03.implementation_02.primary.LinkedNodeSymbolTable;

// 验证：可以使用 拉链法的散列表（数组 + 链表） 来 实现符号表（aka 可以存储与获取 键值对的数据结构）
// 验证：SeparateChainingSymbolTable的符号表实现 相比于 LinkedNodeSymbolTable符号表实现 效率要高出997倍（as set）。
public class SeparateChainingSymbolTable<Key, Value> {

    private int pairAmount;
    private int tableSize;
    // 由链表组成的数组
    private LinkedNodeSymbolTable<Key, Value>[] linkedListArray;

    public SeparateChainingSymbolTable() {
        this(997);
    }

    public SeparateChainingSymbolTable(int tableSize) {
        // 创建M条链接
        this.tableSize = tableSize;

        // 初始化散列表的容量
        linkedListArray = (LinkedNodeSymbolTable<Key, Value>[]) new LinkedNodeSymbolTable[tableSize];

        // 初始化散列表中每个位置上的链表
        for (int currentSpot = 0; currentSpot < tableSize; currentSpot++) {
            linkedListArray[currentSpot] = new LinkedNodeSymbolTable<>();
        }
    }

    private int hash(Key passedKey) {
        return (passedKey.hashCode() & 0x7fffffff) % tableSize;
    }

    public Value getAssociatedValueOf(Key passedKey) {
        return (Value) linkedListArray[hash(passedKey)].getAssociatedValueOf(passedKey);
    }

    public void putInto(Key passedKey, Value associatedValue) {
        linkedListArray[hash(passedKey)].putInPairOf(passedKey, associatedValue);
    }

    public Iterable<Key> keys() {
        return null;
    }
}

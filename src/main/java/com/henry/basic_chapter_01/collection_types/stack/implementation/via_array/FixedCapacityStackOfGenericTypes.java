package com.henry.basic_chapter_01.collection_types.stack.implementation.via_array;

// 目标：实现 固定容量、能够处理泛型参数的栈数据结构
// 数据类型的预期特征：{#1 支持泛型类型; #2 栈的容量是固定的; #3 支持Stack的默认操作}
// 手段：Ⅰ 在类的声明后，添加<Item> 表示 “当前类用于处理泛型参数”; Ⅱ 把原始的String 替换成 Item
public class FixedCapacityStackOfGenericTypes<Item> { // ①

    private Item[] itemArray; // Ⅱ-①
    private int itemAmount;

    // 新增的构造方法  特征：允许用户指定初始容量 initCapacity
    public FixedCapacityStackOfGenericTypes(int initCapacity) {
        itemArray = (Item[]) new Object[initCapacity];
        itemAmount = 0;
    }

    /* 实例方法 */
    // Ⅱ-②
    public void push(Item item) {
        itemArray[itemAmount++] = item;
    }

    // Ⅱ-③
    public Item pop() {
        return itemArray[--itemAmount];
    }

    // helper functions
    public boolean isEmpty() {
        return itemAmount == 0;
    }

    public int getItemAmount() {
        return itemAmount;
    }
}

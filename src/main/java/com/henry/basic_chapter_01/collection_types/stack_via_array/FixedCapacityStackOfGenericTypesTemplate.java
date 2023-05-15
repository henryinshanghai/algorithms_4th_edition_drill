package com.henry.basic_chapter_01.collection_types.stack_via_array;

// 实现 固定容量、能够处理泛型参数的栈
// 手段：#1 在类的声明后，添加<Item> 表示 “当前类用于处理泛型参数”; #2 把原始的String 替换成 Item
public class FixedCapacityStackOfGenericTypesTemplate<Item> {

    private Item[] itemArray;
    private int itemAmount;

    public FixedCapacityStackOfGenericTypesTemplate(int initCapacity) {
        itemArray = (Item[]) new Object[initCapacity];
        itemAmount = 0;
    }

    public boolean isEmpty() {
        return itemAmount == 0;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void push(Item item) {
        itemArray[itemAmount++] = item;
    }

    public Item pop() {
        return itemArray[--itemAmount];
    }
}

package com.henry.basic_chapter_01.collection_types.stack_via_array;

import java.util.Iterator;

// 实现 动态容量、能够处理泛型参数、支持迭代元素操作的栈
// 手段：
public class DynamicCapacityStackOfGenericTypesSupportIterationTemplate<Item> implements Iterable<Item>{

    private Item[] itemArray;
    private int itemAmount;

    public DynamicCapacityStackOfGenericTypesSupportIterationTemplate(int initCapacity) {
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
        if (itemAmount == itemArray.length - 1) resize(itemArray.length * 2);

        itemArray[itemAmount++] = item;
    }

    public Item pop() {
        // alert: 使用除法时，最终结果可能为0. 所以 需要考虑为0时，是否合法
        if(itemAmount > 0 && itemAmount == itemArray.length / 4) resize(itemArray.length / 2);
        return itemArray[--itemAmount];
    }

    // 调整数组的大小
    private void resize(int newCapacity) {
        Item[] tempItemArray = (Item[]) new Object[newCapacity];

        for (int currentSpot = 0; currentSpot < itemArray.length; currentSpot++) {
            tempItemArray[currentSpot] = itemArray[currentSpot];
        }

        itemArray = tempItemArray;
    }

    @Override
    public Iterator<Item> iterator() {
        // 返回自定义的迭代器对象
        return new MyStackIterator();
    }

    // 当前栈数据结构的迭代器
    private class MyStackIterator implements Iterator<Item> {

        // 这里需要一个内部类自己的成员变量吗？ Not necessarily

        @Override
        public boolean hasNext() {
            return itemAmount > 0;
        }

        @Override
        public Item next() {
            return itemArray[--itemAmount];
        }
    }

    public static void main(String[] args) {
        DynamicCapacityStackOfGenericTypesSupportIterationTemplate<String> myStringStack = new DynamicCapacityStackOfGenericTypesSupportIterationTemplate<>(10);

        myStringStack.push("Henry");
        myStringStack.push("want");
        myStringStack.push("a");
        myStringStack.push("dog");

        for (String currentItem : myStringStack) {
            System.out.println("currentItem: " + currentItem);
        }
    }
}

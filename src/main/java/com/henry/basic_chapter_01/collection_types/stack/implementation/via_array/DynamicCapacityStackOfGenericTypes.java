package com.henry.basic_chapter_01.collection_types.stack.implementation.via_array;

// 实目标：现 动态容量、能够处理泛型参数的栈数据结构
// 数据类型的预期特征：{#1 支持泛型类型; #2 栈的容量是能够动态变化的; #3 支持Stack的默认操作}
// 手段：在那些个“会影响数组元素数量的操作”中，判断并使用resize()操作 来 动态改变数组容量
public class DynamicCapacityStackOfGenericTypes<Item> {

    private Item[] itemArray;
    private int itemAmount;

    public DynamicCapacityStackOfGenericTypes(int initCapacity) {
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
        // 判断栈的当前容量 与 栈中元素数量的大小，如果已经满员，则：调整栈底层数据结构的大小
        // 手段：把底层的数组扩容到 原始容量的2倍
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
        // #1 创建新容量的数组 作为底层数据结构
        Item[] tempItemArray = (Item[]) new Object[newCapacity];

        // #2 把原始的底层数组中的元素 注意拷贝到 新容量的数组中
        for (int currentSpot = 0; currentSpot < itemArray.length; currentSpot++) {
            tempItemArray[currentSpot] = itemArray[currentSpot];
        }

        // #3 把表示底层数组的实例变量 指向新容量的数组
        itemArray = tempItemArray;
    }
}

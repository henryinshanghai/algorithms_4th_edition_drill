package com.henry.basic_chapter_01.collection_types.stack_via_array;

// 实现 动态容量、能够处理泛型参数的栈
// 手段：在那些个会影响数组元素数量的操作中，判断并使用resize()操作 来 动态改变数组容量
public class DynamicCapacityStackOfGenericTypesTemplate<Item> {

    private Item[] itemArray;
    private int itemAmount;

    public DynamicCapacityStackOfGenericTypesTemplate(int initCapacity) {
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
}

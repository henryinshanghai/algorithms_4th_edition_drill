package com.henry.sort_chapter_02.priority_queue_04.pq_primary_implement_02.unordered_array_01;

import edu.princeton.cs.algs4.StdOut;

/*
    算法描述：
        使用无序数组 来实现 一个优先队列；（能够插入元素 + 删除队列中的最大值）

    底层数据结构：数组
    泛型： 继承自Comparable的Key
        泛型应该要什么时候添加呢？
    区分： 队列的容量 与 队列中的元素个数
    支持的API：
        - 向此 集合类型中添加元素
        - 从此 集合类型中删除集合中的最大元素
    特征：
        由于要找到最大元素，所以要求集合中的元素能够支持比较操作 -
        手段1：元素本身是 Comparable类型的；
        手段2：一个继承自Comparable的泛型类型 - 有什么作用?
            不使用泛型的话，元素之间的比较就会报错 Why？
            因为同样实现了 Comparable接口的元素类型，它们各自定义的compareTo()可能并不一致
 */
public class UnorderedArrayMaxPQ_drill01<Key extends Comparable<Key>> { // 类型参数

    private Key[] itemArray;
    private int itemAmount;

    // 构造方法 - 用例创建实例的方式
    public UnorderedArrayMaxPQ_drill01(int Max) {
        itemArray = (Key[])new Comparable[Max];
        // 不要少了 实例变量的初始化步骤
        itemAmount = 0;
    }

    // APIs
    public int size() {
        return itemAmount;
    }

    public boolean isEmpty(){
        return itemAmount == 0;
    }

    // 两个核心的APIs
    public void insert(Key item) {
        itemArray[itemAmount++] = item;
    }

    /**
     * 删除最大元素
     * @return
     */
    public Comparable delMax() {
        int cursorToMaxItem = 0;
        for (int dynamicCursorr = 1; dynamicCursorr < itemAmount; dynamicCursorr++) {
            // 如果出现了比max还要大的元素，就更新max的值
            if (less(cursorToMaxItem, dynamicCursorr)) {
                cursorToMaxItem = dynamicCursorr;
            }
        }

        exch(cursorToMaxItem, itemAmount -1);

        // 从数组末尾获取最大元素 & 同时调整元素的个数
        Key maxItem = itemArray[--itemAmount];

        // 物理删除 最大元素？
        itemArray[itemAmount + 1] = null;

        return maxItem;
    }

    // 两个辅助函数
    public boolean less(int i, int j) {
        return itemArray[i].compareTo(itemArray[j]) < 0;
    }

    public void exch(int i, int j) { // Comparable[] pq, 可以作为参数 也可以作为实例变量
        Key temp = itemArray[i];
        itemArray[i] = itemArray[j];
        itemArray[j] = temp;
    }

    /***************************************************************************
     * Test routine. 测试用例
     ***************************************************************************/
    public static void main(String[] args) {
        UnorderedArrayMaxPQ_drill01<String> pq = new UnorderedArrayMaxPQ_drill01<String>(10);

        pq.insert("Do");
        pq.insert("Or");
        pq.insert("Do Not");
        pq.insert("There's");
        pq.insert("No");
        pq.insert("Try");

        System.out.println(pq.size());

        while (!pq.isEmpty())
            StdOut.println(pq.delMax());
    }
}

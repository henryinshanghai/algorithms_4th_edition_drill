package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_primary_implement_02.unordered_array_01;

import edu.princeton.cs.algs4.StdOut;

/*
    算法描述：
        使用无序数组 来实现 一个优先队列；（#1 能够插入元素； #2 删除队列中的最大值）

    底层数据结构：数组
    泛型： 继承自Comparable的Key
        泛型应该要什么时候添加呢？
    区分： 队列的容量 与 队列中的元素个数
    支持的API：
        - 向此 集合类型中添加元素
        - 从此 集合类型中删除集合中的最大元素
    特征：
        由于要找到最大元素，所以要求集合中的元素能够 “支持比较操作”；
        手段1：元素本身是 Comparable类型的；
        手段2：一个继承自Comparable的泛型类型 - 有什么作用?
            不使用泛型的话，元素之间的比较就会报错 Why？
            因为同样实现了 Comparable接口的元素类型，它们各自定义的compareTo()可能并不一致

    类型参数 - 作为“某种具体类型的象征性占位符号”
    特征：实际的参数必须是引用类型；
    应用： Java会使用类型参数 来 检查类型不匹配的错误；
 */
// 结论：可以使用无序数组来实现优先队列(#1 添加元素; #2 从中删除最大元素)；
// 步骤：#1 添加元素时，直接把元素添加到数组末尾； #2 删除最大元素时，从数组中先找到最大元素，再进行删除（挖空&补齐）；
public class UnorderedArrayMaxPQTemplate<Key extends Comparable<Key>> { // 类型参数 - 作为“某种具体类型的象征性占位符号”

    private Key[] itemArray;
    private int itemAmount;

    // 构造方法 - 用例创建实例的方式
    public UnorderedArrayMaxPQTemplate(int Max) {
        // 需要进行强制类型转换 - 因为Java中不允许直接创建泛型数组
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
        itemArray[itemAmount] = null;

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
        UnorderedArrayMaxPQTemplate<String> maxPQ = new UnorderedArrayMaxPQTemplate<String>(10);

        maxPQ.insert("Alicia");
        maxPQ.insert("Ben");
        maxPQ.insert("David");
        maxPQ.insert("Eva");
        maxPQ.insert("Floria");
        maxPQ.insert("Grace");
        maxPQ.insert("Joker");
        maxPQ.insert("Leo");
        maxPQ.insert("Monica");


        System.out.println(maxPQ.size());

        while (!maxPQ.isEmpty())
            StdOut.println(maxPQ.delMax());
    }
}

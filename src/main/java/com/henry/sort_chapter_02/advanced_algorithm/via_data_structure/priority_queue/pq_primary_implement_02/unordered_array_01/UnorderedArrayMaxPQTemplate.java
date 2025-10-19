package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_primary_implement_02.unordered_array_01;

import edu.princeton.cs.algs4.StdOut;

/*
    算法描述：
        使用无序数组 来实现 一个优先队列；（#1 能够插入元素； #2 删除队列中的最大值）

    底层数据结构：数组
    泛型： 继承自Comparable的Key
        泛型 应该要 什么时候添加 呢？
    区分： 队列的容量 与 队列中的元素个数
    支持的API：
        - 向此 集合类型中 添加元素
        - 从此 集合类型中 删除 集合中的最大元素
    特征：
        由于 要找到 最大元素，所以要求 集合中的元素 能够 “支持比较操作”；
        手段1：元素 本身是 Comparable类型的；
        手段2：一个 继承自Comparable的泛型类型 - 有什么作用?
            不使用泛型的话，元素之间的比较 就会报错 Why？
            因为 同样实现了 Comparable接口的元素类型，它们 各自定义的 compareTo() 可能 并不一致

    类型参数 - 作为“某种具体类型的 象征性占位符号”
    特征：实际的参数 必须是 引用类型；
    应用： Java 会使用 类型参数 来 检查 类型不匹配 的错误；
 */
// 结论：可以 使用 无序数组 来 实现 优先队列(#1 添加 元素; #2 从中删除 最大元素)；
// 步骤：#1 添加元素 时，直接 把元素添加到 数组末尾； #2 删除最大元素 时，从数组中 先找到 最大元素，再 进行删除（挖空&补齐）；
public class UnorderedArrayMaxPQTemplate<Key extends Comparable<Key>> { // 类型参数 - 作为“某种具体类型的象征性占位符号”

    private Key[] itemArray;
    private int itemAmount;

    // 构造方法 - 用例 创建实例的方式
    public UnorderedArrayMaxPQTemplate(int Max) {
        // 需要进行 强制类型转换 - 因为Java中 不允许 直接创建 泛型数组
        itemArray = (Key[]) new Comparable[Max];
        // 不要少了 实例变量的初始化步骤
        itemAmount = 0;
    }

    // APIs
    public int size() {
        return itemAmount;
    }

    public boolean isEmpty() {
        return itemAmount == 0;
    }

    // 两个核心的APIs
    public void insert(Key item) {
        itemArray[itemAmount++] = item;
    }

    /**
     * 删除最大元素
     *
     * @return
     */
    public Comparable delMax() {
        int cursorToMaxItem = 0;
        for (int dynamicCursorr = 1; dynamicCursorr < itemAmount; dynamicCursorr++) {
            // 如果 出现了 比max还要大的元素，就 更新max变量的值
            if (less(cursorToMaxItem, dynamicCursorr)) {
                cursorToMaxItem = dynamicCursorr;
            }
        }

        exch(cursorToMaxItem, itemAmount - 1);

        // 从 数组末尾 获取最大元素 & 同时 调整元素的个数
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
     * 测试用例
     ***************************************************************************/
    public static void main(String[] args) {
        // 初始化 自定义的数据类型（优先队列） 类的实例
        UnorderedArrayMaxPQTemplate<String> maxPQ = new UnorderedArrayMaxPQTemplate<String>(10);

        // 向 优先队列 中 添加元素
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

        // 从 优先队列 中，删除 当前的最小元素。直到队列为空
        while (!maxPQ.isEmpty())
            StdOut.println(maxPQ.delMax());
    }
}

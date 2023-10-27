package com.henry.sort_chapter_02.priority_queue_04.pq_heap_implement_03;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.NoSuchElementException;

/*
相关术语： spot in heap;
有意义的变量名：由元素组成的堆 spotToItemArray;
泛型参数：一个可比较的类型
特征：暂时不支持迭代的操作

核心API算法：

插入item的算法：
    #1 在插入元素之前，先查看是否需要对数组进行扩容
    #2 把元素添加到堆数组的末尾
    #3 维护堆数组的“堆有序”特性

上浮算法：
    #1 比较当前位置上的元素 与 其父节点元素的大小；
    #2 如果当前元素更大，则：交换当前元素 与 其父节点元素；
    #3 更新当前位置，重复步骤#1，直到 当前元素 小于等于 其父节点元素；

删除最大item的算法：
    #1 记录下maxItem；
    #2 把 最大元素 与 节点末尾的元素 进行交换；
    #3 维护堆数组的“堆有序”特性

下沉算法：
    #1 找出当前位置元素的 比较大的子元素；
    #2 比较当前位置的元素 与 #1中比较大的子元素，如果当前元素更小，则：执行交换；
    #3 更新当前位置，重复#1, #2，直到当前元素 大于等于 其较大子节点元素；

判断堆数组是否是“最大堆”的？
    #1 最大堆的结构约束 - 堆区间元素不为null，其他区域元素为null
    #2 最大堆的数值约束 - 当前位置元素的数值约束、子节点位置的数值约束
 */
public class HeapMaxPQSimpleTemplate<Item extends Comparable<Item>> {
    private Item[] spotToItemArray; // 堆 - 使用泛型数组来实现
    private int itemAmount; // 堆中元素的数量
    private Comparator customComparator;  // 自定义的比较器 - 用于支持更加灵活的构造方法


    // 五种不同的构造方法
    // 空的队列
    public HeapMaxPQSimpleTemplate() {
        this(1);
    }

    // 指定容量的队列
    public HeapMaxPQSimpleTemplate(int capacity) {
        spotToItemArray = (Item[]) new Comparable[capacity + 1];
        itemAmount = 0;
    }

    public HeapMaxPQSimpleTemplate(int initCapacity, Comparator<Item> comparator) {
        spotToItemArray = (Item[])new Comparable[initCapacity + 1];
        this.customComparator = comparator;
        itemAmount = 0;
    }

    public HeapMaxPQSimpleTemplate(Comparator<Item> comparator) {
        this(1, comparator);
    }

    // 从数组元素中，初始化得到一个优先队列
    public HeapMaxPQSimpleTemplate(Item[] itemArray) {
        itemAmount = itemArray.length;

        spotToItemArray = (Item[])new Comparable[itemArray.length + 1]; // Object[]
        for (int i = 0; i < itemAmount; i++) {
            spotToItemArray[i + 1] = itemArray[i];
        }

        // 构造出一个堆 - 手段：使用sink()方法 排定一半的元素
        // 原理：如果数组中，前面一半的元素都已经满足“堆有序”的话，则：整个数组必然是堆有序的
        // 原因：对某个位置，执行了sink(index)后，则：这个位置上的节点 就一定会大于 它的子节点了。
        // 因此保证前一半的节点被排定后，剩下的节点必然也符合 堆对元素的数值约束了
        for (int currentSpot = itemAmount / 2; currentSpot >= 1 ; currentSpot--) {
            sinkItemToCorrectSpot(currentSpot);
        }

        // 断言：我们已经得到了一个 堆有序的数组
        assert isMaxHeap();
    }

    // 判断 当前的itemHeap 是不是一个最大堆
    // 手段：堆对树节点的结构要求（完全二叉树） + 对节点的数值要求（父节点 大于 任意子节点）
    // 应用：用于调试代码
    private boolean isMaxHeap() {
        // #1 结构约束 - 完全二叉树 对应到数组的约束：堆区间内不能为null, 堆区间外必须为null
        // 堆区间的元素
        for (int cursor = 1; cursor <= itemAmount; cursor++) {
            if(spotToItemArray[cursor] == null) return false;
        }

        // 堆区间外的数组元素
        for (int cursor = itemAmount + 1; cursor < spotToItemArray.length; cursor++) {
            if (spotToItemArray[cursor] != null) return false;
        }

        // 第一个数组元素
        if (spotToItemArray[0] != null) return false;

        // #2 堆的数值约束：判断以index=1的位置上的元素作为根节点的树 是不是一个堆
        return isMaxHeapSorted(1);
    }

    // 判断最大堆 是否是 “堆有序”的状态 - 数值约束：要求父节点的元素值 >= 子节点的元素值
    private boolean isMaxHeapSorted(int currentRootNodeSpot) {
        // 如果指针位置能够超出 堆元素的边界，则：说明数组必然是堆有序的
        if (currentRootNodeSpot > itemAmount) return true;

        // #1 数值约束
        // 左右子节点位置的公式：2*k, 2*k + 1
        int leftChildSpot = currentRootNodeSpot * 2;
        int rightChildSpot = currentRootNodeSpot * 2 + 1;

        // 🐖 需要保证 指针的有效性，否则很容易出现NPE
        // 如果违反了 数值约束，则：不是堆有序的
        if (leftChildSpot <= itemAmount && less(currentRootNodeSpot, leftChildSpot)) return false;
        if (rightChildSpot <= itemAmount && less(currentRootNodeSpot, rightChildSpot)) return false;

        // #2 结构约束
        return isMaxHeapSorted(leftChildSpot) && isMaxHeapSorted(rightChildSpot);
    }

    public boolean isEmpty() {
        return itemAmount == 0;
    }

    public int size() {
        return itemAmount;
    }

    // 向优先队列中插入一个指定的元素
    public void insert(Item newItem) {
        // #1 在插入元素之前，先查看是否需要对数组进行扩容
        // 如果 元素数量 = 数组容量 - 1，说明堆已经满员了，则：需要扩容
        if(itemAmount == spotToItemArray.length - 1) resize(spotToItemArray.length * 2);

        // #2 向堆中添加新结点 - 手段：把元素添加到堆数组的末尾
        // 🐖 ++itemAmount 意味着 第一个元素添加在了 数组中下标为1的位置上
        spotToItemArray[++itemAmount] = newItem; // itemAmount=队列中的元素个数 需要的索引是N+1 使用++N能够一步到位

        // #3 维护堆的数值约束
        // 手段：对 堆中最后一个位置上的元素执行上浮操作 - 具体做法：操作对应的数组元素
        swimItemToCorrectSpot(itemAmount);

        assert isMaxHeap();
    }

    private void resize(int newCapacity) {
        // #1 使用参数传入的大小 来 初始化新的数组
        Item[] newItemHeap = (Item[])new Comparable[newCapacity];

        // #2 把原始数组中的元素 拷贝到新数组的对应位置上
        for (int cursor = 1; cursor <= itemAmount; cursor++) {
            newItemHeap[cursor] = spotToItemArray[cursor];
        }

        // #3 把 spotToItemArray 指向新的数组
        spotToItemArray = newItemHeap;
    }

    // 从优先队列中删除最大的元素
    public Item delMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");

        /* 从堆中删除最大元素 */
        // #1 先获取到最大元素
        Item maxItem = spotToItemArray[1];
        // #2 （交换）把最大元素交换到 堆数组的末尾
        // 手段：交换 位置为1的元素 与 最后一个位置上的元素
        exch(1, itemAmount--);
        // #3 （物理删除）物理删除掉最后一个位置上的元素
        spotToItemArray[itemAmount + 1] = null;

        // 删除元素后，维护堆的数值约束
        // 手段：对顶点位置的元素 执行下沉操作 来 恢复数组的“堆有序”
        sinkItemToCorrectSpot(1);

        assert isMaxHeap(); // 局部断言
        return maxItem;
    }

    // 把指定位置上的元素（更小的元素） 下沉到 堆中正确的位置  - 作用：恢复完全二叉树的“堆有序”
    private void sinkItemToCorrectSpot(int currentNodeSpot) {
        // 如果当前位置属于数组的前半段，则：可以执行下沉操作
        while (2 * currentNodeSpot <= itemAmount) {
            // #1 找到较大的子节点 所对应的位置
            int biggerChildSpot = 2 * currentNodeSpot;
            // 🐖 对于swim 与 sink操作，需要保证 指针的有效性，否则在循环中很容易出现NPE
            if (biggerChildSpot < itemAmount && less(biggerChildSpot, biggerChildSpot + 1)) {
                biggerChildSpot++;
            }

            // #2 比较当前节点的值 与 较大的子节点的值
            if (!less(currentNodeSpot, biggerChildSpot)) {
                break;
            }

            // #3 如果当前节点的值更小，则：把它与较大的子节点元素进行交换位置
            exch(currentNodeSpot, biggerChildSpot);

            // #4 更新游标位置
            currentNodeSpot = biggerChildSpot;
        }
    }


    // 把指定位置上的元素（更大的元素） 上浮到 堆中正确的位置 - 作用：恢复完全二叉树的“堆有序”
    private void swimItemToCorrectSpot(int currentNodeSpot) {
        // 如果当前位置不是堆的根节点位置，则：可以执行上浮操作
        // 🐖 对于swim 与 sink操作，需要保证 指针的有效性，否则在循环中很容易出现NPE
        // #1 判断当前节点 是否大于 根节点
        while (currentNodeSpot > 1 && less(currentNodeSpot / 2, currentNodeSpot)) {
            // #2 如果满足，则：交换父节点 与 当前节点
            exch(currentNodeSpot, currentNodeSpot / 2);

            // #3 更新当前指针，继续进行上浮操作（如果需要的话）
            currentNodeSpot = currentNodeSpot / 2;
        }
    }

    /**
     * 交换数组中两个指定位置上的元素
     *
     * @param i
     * @param k
     */
    private void exch(int i, int k) {
        Item temp = spotToItemArray[i];
        spotToItemArray[i] = spotToItemArray[k];
        spotToItemArray[k] = temp;
    }

    /**
     * 比较数组中两个指定位置上的元素的大小
     * @param i
     * @param k
     * @return
     */
    private boolean less(int i, int k) {
        return spotToItemArray[i].compareTo(spotToItemArray[k]) < 0;
    }

    public static void main(String[] args) {
        HeapMaxPQSimpleTemplate<String> pq = new HeapMaxPQSimpleTemplate<>();

        while (!StdIn.isEmpty()) {
            // #1 从标准输入流中读取字符串
            String item = StdIn.readString();
            // #2 如果当前字符不是-， 则：插入到优先队列中
            if (!item.equals("-")) pq.insert(item);
                // #3 如果 当前字符是- 并且优先队列不为空，则：删除优先队列的最大元素(最大key)
            else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
        }
        StdOut.println("(" + pq.size() + " left on spotToItemArray)");
    }
}

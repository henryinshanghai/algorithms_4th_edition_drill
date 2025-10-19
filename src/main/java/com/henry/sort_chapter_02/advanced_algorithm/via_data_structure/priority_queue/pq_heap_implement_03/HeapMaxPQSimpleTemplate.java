package com.henry.sort_chapter_02.advanced_algorithm.via_data_structure.priority_queue.pq_heap_implement_03;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.NoSuchElementException;

/*
概念：
    ① spot in heap; 元素在堆中的位置（从1开始数）
    ② spot in array; 元素 在底层数组中的位置（从1开始存储元素）
有意义的变量名：由元素组成的 堆 spotToItemArray;
泛型参数：一个可比较的类型
特征：暂时不支持 迭代的操作

核心API算法：

插入item的算法：
    #1 在 插入元素 之前，先查看 是否需要 对 数组 进行扩容
    #2 把 元素 添加到 堆数组的末尾
    #3 维护 堆数组的 “堆有序”特性

上浮算法：
    #1 比较 当前位置上的元素 与 其父节点元素 的大小；
    #2 如果 当前元素 更大，则：交换 当前元素 与 其父节点元素；
    #3 更新 当前位置，重复 步骤#1，直到 当前元素 小于等于 其父节点元素；

删除最大item的算法：
    #1 记录下maxItem；
    #2 把 最大元素 与 节点末尾的元素 进行交换；
    #3 维护 堆数组的 “堆有序”特性

下沉算法：
    #1 找出 当前位置元素的 比较大的子元素；
    #2 比较 当前位置的元素 与 #1中比较大的子元素，如果 当前元素 更小，则：执行交换；
    #3 更新 当前位置，重复#1, #2，直到 当前元素 大于等于 其较大子节点元素；

判断堆数组 是否是 “最大堆”的？
    #1 最大堆的 结构约束 - 堆区间元素 不为null，其他区域元素 为null
    #2 最大堆的 数值约束 - 当前位置元素的 数值约束、子节点位置的 数值约束
 */
public class HeapMaxPQSimpleTemplate<Item extends Comparable<Item>> {
    private Item[] spotToItemArray; // 堆 - 使用 泛型数组 来 实现
    private int itemAmount; // 堆中 元素的数量
    private Comparator customComparator;  // 自定义的比较器 - 用于支持 更加灵活的构造方法


    /* 五种不同的构造方法 */
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
        spotToItemArray = (Item[]) new Comparable[initCapacity + 1];
        this.customComparator = comparator;
        itemAmount = 0;
    }

    public HeapMaxPQSimpleTemplate(Comparator<Item> comparator) {
        this(1, comparator);
    }

    // 从 数组元素 中，初始化得到 一个优先队列
    public HeapMaxPQSimpleTemplate(Item[] itemArray) {
        itemAmount = itemArray.length;

        spotToItemArray = (Item[]) new Comparable[itemArray.length + 1]; // Object[]
        for (int currentItemSpot = 0; currentItemSpot < itemAmount; currentItemSpot++) {
            // 把 当前元素 存储在 底层数组中对应的位置 上
            spotToItemArray[currentItemSpot + 1] = itemArray[currentItemSpot];
        }

        // 构造出 一个堆 - 手段：使用 sink()方法 排定 一半的元素
        // 原理：如果数组中，前面一半的元素 都已经满足 “堆有序”的话，则：整个数组 必然是 堆有序的
        // 原因：对某个位置，执行了 sink(index) 后，则：这个位置上的节点 就一定会大于 它的子节点了。
        // 因此 保证 前一半的节点 被排定 后，剩下的节点 必然也符合 堆 对元素的数值约束 了
        for (int currentSpot = itemAmount / 2; currentSpot >= 1; currentSpot--) {
            sinkItemToCorrectSpot(currentSpot);
        }

        // 断言：我们 已经得到了 一个堆有序的数组
        assert isMaxHeap();
    }

    // 判断 当前的itemHeap 是不是 一个最大堆
    // 手段：堆 对树节点的 结构要求（完全二叉树） + 对节点的数值要求（父节点 大于 任意子节点）
    // 应用：用于 调试代码
    private boolean isMaxHeap() {
        // #1 结构约束 - 完全二叉树 对应到数组的约束：堆区间内 不能为null, 堆区间外 必须为null
        // 堆区间内的 数组元素
        for (int cursor = 1; cursor <= itemAmount; cursor++) {
            if (spotToItemArray[cursor] == null) return false;
        }

        // 堆区间外的 数组元素
        for (int cursor = itemAmount + 1; cursor < spotToItemArray.length; cursor++) {
            if (spotToItemArray[cursor] != null) return false;
        }

        // 第一个数组元素
        if (spotToItemArray[0] != null) return false;

        // #2 堆的数值约束：判断 以 index=1的位置上的元素 作为 根节点的树 是不是 一个堆
        return isMaxHeapSorted(1);
    }

    // 判断 最大堆 是否是 “堆有序”的状态 - 数值约束：要求 父节点的元素值 >= 子节点的元素值
    private boolean isMaxHeapSorted(int currentRootNodeSpot) {
        // 如果 指针位置 能够超出 堆元素的边界，则：说明 数组 必然是 堆有序的
        if (currentRootNodeSpot > itemAmount) return true;

        // #1 数值约束
        // 左右子节点位置的 计算公式：2*k, 2*k + 1
        int leftChildSpot = currentRootNodeSpot * 2;
        int rightChildSpot = currentRootNodeSpot * 2 + 1;

        // 🐖 需要保证 指针的有效性，否则 很容易出现NPE
        // 如果 违反了 数值约束，则：不是 堆有序的
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

    // 向 优先队列 中 插入 一个指定的元素
    public void insert(Item newItem) {
        // #1 在 插入元素 之前，先查看 是否需要 对数组进行扩容
        // 如果 元素数量 = 数组容量 - 1，说明 堆 已经满员了，需要进行扩容，则：
        if (itemAmount == spotToItemArray.length - 1) {
            // 把 底层数组 扩容为 原始容量的2倍
            resize(spotToItemArray.length * 2);
            System.out.println("~~~ 底层数组 spotToItemArray 容量扩容到：" + spotToItemArray.length + "，当前底层数组为：{" + showInStr(spotToItemArray) + "} ~~~");
        }

        // #2 向堆中 添加新结点 - 手段：把 元素 添加到 堆数组的末尾
        // itemAmount 表示 队列中的元素个数，而 需要的索引(数组末尾) 是 N+1；因此 这里 使用++N 能够 一步到位
        // 🐖 ++itemAmount 意味着 第一个元素 添加在了 数组中 下标为1的位置 上
        spotToItemArray[++itemAmount] = newItem;
        System.out.println("!!! 在底层数组 spotToItemArray 的末尾位置" + itemAmount + "处，添加了 新增元素" + newItem + ", 当前底层数组为： {" + showInStr(spotToItemArray) + "} !!!");

        // #3 维护 堆的数值约束
        // 手段：对 堆中最后一个位置上的元素 执行 上浮操作 - 具体做法：操作 对应的数组元素
        swimItemToCorrectSpot(itemAmount);
        System.out.println("@@@ 新添加的元素" + newItem + " 在底层数组spotToItemArray中 上浮到了 正确的位置，得到 新的有效的堆：{" + showInStr(spotToItemArray) + "} @@@");

        assert isMaxHeap();
    }

    private String showInStr(Item[] spotToItemArray) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Item currentItem : spotToItemArray) {
            stringBuilder.append(currentItem + ", ");
        }

        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    private void resize(int newCapacity) {
        // #1 使用 参数传入的大小 来 初始化 新的数组
        Item[] newItemHeap = (Item[]) new Comparable[newCapacity];

        // #2 把 原始数组中的元素 拷贝到 新数组的对应位置 上
        for (int currentSpot = 1; currentSpot <= itemAmount; currentSpot++) {
            newItemHeap[currentSpot] = spotToItemArray[currentSpot];
        }

        // #3 把 spotToItemArray 指向 新的数组
        spotToItemArray = newItemHeap;
    }

    // 从 优先队列 中 删除最大的元素
    public Item delMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");

        /* 从堆中 删除最大元素 */
        // #1 先 获取到 最大元素
        Item maxItem = spotToItemArray[1];
        System.out.println("$$$ 底层数组 spotToItemArray:{" + showInStr(spotToItemArray) + "} 中的最大元素为：" + maxItem + " $$$");
        // #2 （交换）把 最大元素 交换到 堆数组的末尾
        // 手段：交换 位置为1的元素 与 最后一个位置上的元素
        exch(1, itemAmount--);
        System.out.println("%%% 把 位置1上的元素 与 其末尾位置" + (itemAmount + 1) + " 上的元素 进行交换，交换后的数组为：{" + showInStr(spotToItemArray) + "} %%%");
        // #3 （物理删除）物理删除掉 最后一个位置上的元素
        spotToItemArray[itemAmount + 1] = null;
        System.out.println("^^^ 把 spotToItemArray 末尾位置" + (itemAmount + 1) + " 上的元素(预期删除) 设置为null，删除后的数组为：{" + showInStr(spotToItemArray) + "} ^^^");

        // #4 删除元素后，维护 堆的数值约束
        // 手段：对 顶点位置的元素 执行 下沉操作 来 恢复 数组的“堆有序”
        sinkItemToCorrectSpot(1);

        assert isMaxHeap(); // 局部断言
        return maxItem;
    }

    // 把 指定位置上的元素（更小的元素） 下沉到 堆中正确的位置  - 作用：恢复 完全二叉树的“堆有序”
    private void sinkItemToCorrectSpot(int currentNodeSpot) {
        // 如果 当前位置 属于 数组的前半段，则：可以执行 下沉操作
        while (2 * currentNodeSpot <= itemAmount) {
            // #1 找到 较大的子节点 所对应的位置
            int biggerChildSpot = 2 * currentNodeSpot;
            // 🐖 对于 swim 与 sink操作，需要保证 指针的有效性，否则 在循环中 很容易出现NPE
            if (biggerChildSpot < itemAmount && less(biggerChildSpot, biggerChildSpot + 1)) {
                biggerChildSpot++;
            }
            System.out.println();
            System.out.println("*** 底层数组 spotToItemArray中，当前位置" + currentNodeSpot + " 的较大子节点的位置是："
                    + biggerChildSpot + " ***");

            // 如果 当前节点的元素值 不小于 其较大子节点的元素值，说明 已经不存在breach，
            if (!less(currentNodeSpot, biggerChildSpot)) {
                System.out.println("*** 当前节点的元素值" + spotToItemArray[currentNodeSpot] + " 不小于 其较大子节点的元素值" + spotToItemArray[biggerChildSpot]
                        + "，下沉结束 ***");
                // 则：跳出循环
                break;
            }

            // #3 如果 当前节点的元素值 小于 其较大子节点的元素值，
            // 则：把 它 与 较大的子节点元素 进行交换位置
            System.out.println("*** 当前节点的元素值" + spotToItemArray[currentNodeSpot] + " 小于 其较大子节点的元素值" + spotToItemArray[biggerChildSpot] +
                    "，因此 需要 对位置" + currentNodeSpot + " 与 位置" + biggerChildSpot + "上 的元素 进行交换 ***");
            exch(currentNodeSpot, biggerChildSpot);
            System.out.println("*** 交换后的底层数组为：{" + showInStr(spotToItemArray) + "} ***");

            // #4 更新 游标位置
            currentNodeSpot = biggerChildSpot;
            System.out.println("*** 把 当前位置 更新为：" + currentNodeSpot + " 来 继续检查 其 是否符合大顶堆的数值约束（当前节点的元素值 需要大于 其较大子节点的元素值） ***");
            System.out.println();
        }
    }


    // 把 指定位置上的元素（更大的元素） 上浮到 堆中正确的位置 - 作用：恢复 完全二叉树的“堆有序”
    private void swimItemToCorrectSpot(int currentNodeSpot) {
        // 如果 当前位置 不是 堆的根节点位置，则：可以执行 上浮操作
        // 🐖 对于 swim 与 sink操作，需要保证 指针的有效性，否则 在循环中 很容易出现NPE
        // #1 判断 当前节点 是否大于 根节点
        while (currentNodeSpot > 1 && less(currentNodeSpot / 2, currentNodeSpot)) {
            // #2 如果 满足，则：交换 父节点 与 当前节点
            System.out.println();
            System.out.println("### 父节点元素" + spotToItemArray[currentNodeSpot / 2] + " 比起 当前节点元素" + spotToItemArray[currentNodeSpot] +
                    " 要小，因此 对 位置" + currentNodeSpot + " 与 位置" + (currentNodeSpot / 2) + " 上的元素 进行交换 ###");
            exch(currentNodeSpot, currentNodeSpot / 2);
            System.out.println("### 交换后的底层数组为：" + showInStr(spotToItemArray) + " ###");

            // #3 更新 当前指针，继续进行 上浮操作（如果需要的话）
            currentNodeSpot = currentNodeSpot / 2;
            System.out.println("### 把 当前位置 更新为" + currentNodeSpot + " 来 继续检查 其是否符合 大顶堆的数值约束（父节点的值 需要大于 当前节点的值） ###");
            System.out.println();
        }
    }

    /**
     * 交换数组中 两个 指定位置上的元素
     *
     * @param spotI
     * @param spotK
     */
    private void exch(int spotI, int spotK) {
        Item temp = spotToItemArray[spotI];
        spotToItemArray[spotI] = spotToItemArray[spotK];
        spotToItemArray[spotK] = temp;
    }

    /**
     * 比较 数组中 两个指定位置上的元素的大小
     *
     * @param spotI
     * @param spotK
     * @return
     */
    private boolean less(int spotI, int spotK) {
        return spotToItemArray[spotI].compareTo(spotToItemArray[spotK]) < 0;
    }

    public static void main(String[] args) {
        HeapMaxPQSimpleTemplate<String> pq = new HeapMaxPQSimpleTemplate<>();

        while (!StdIn.isEmpty()) {
            // #1 从 标准输入流 中 读取字符串
            String item = StdIn.readString();

            // #2 如果 当前字符 不是-，说明不是 删除操作
            if (!item.equals("-")) {
                // 则：把它 作为元素 插入到 优先队列中
                pq.insert(item);
                System.out.println();
            } else if (!pq.isEmpty()) { // #3 如果 当前字符 是- 并且 优先队列 不为空，说明 是删除操作 且 可以执行删除操作
                // 则：删除 优先队列的最大元素(最大key)
                pq.delMax();
//                StdOut.print(pq.delMax() + " ");
            }

        }
        StdOut.println("(" + pq.size() + " left on spotToItemArray)");
    }
}

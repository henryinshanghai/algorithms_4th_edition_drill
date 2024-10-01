package com.henry.leetcode_traning_camp.week_02.day4.aboutHeap.implement_01;// Java

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * 手动实现二叉堆(逻辑-偏底层)数据结构
 * 二叉堆：满足特定数值约束的完全二叉树
 */
public class BinaryHeap {
    // 底层数据结构 & 需要的局部变量
    private int[] nodeSpotToItsItemArr; // 使用数组 作为 底层数据结构
    private static final int branchAmount = 2; // N叉树的N
    private int nodesAmount; // 二叉堆中的节点个数


    /**
     * This will initialize our heap with default size.
     * 这会使用默认size来初始化我们的堆
     *
     * @param expectedCapacity
     */
    public BinaryHeap(int expectedCapacity) {
        nodesAmount = 0;
        nodeSpotToItsItemArr = new int[expectedCapacity + 1];
        // 为数组中的每一个元素 都绑定相同的初始值
        Arrays.fill(nodeSpotToItsItemArr, -1);
    }


    /* 一些个辅助方法：判空、判满、求父节点的索引、求孩子节点的索引 */
    public boolean isEmpty() {
        return nodesAmount == 0;
    }

    // 堆是否已经满员?
    public boolean isFull() {
        // 堆中结点的数量 == 底层数组的长度
        return nodesAmount == nodeSpotToItsItemArr.length;
    }

    // 根据当前节点在数组中的位置 得到 其父节点在数组中的位置
    private int parentNodeSpot(int passedNodeNo) {
        return (passedNodeNo - 1) / branchAmount;
    }

    // 根据当前节点在数组中的位置 得到 其指定的子结点在数组中的位置
    private int kthChildNodeSpot(int currentNodeSpot, int kthChild) {
        return branchAmount * currentNodeSpot + kthChild;
    }


    /* 核心APIs */

    /**
     * 向堆中插入一个新的元素
     * Inserts new element in to heap
     * Complexity: O(log N)
     * As worst case scenario, we need to traverse till the root
     *
     * @param newItem
     */
    public void insert(int newItem) {
        // #0 判断堆是否已经满员了
        if (isFull()) {
            throw new NoSuchElementException("Heap is full, No space to insert new element");
        }
        /* #1 把新元素 顺序添加到 堆的下一个位置
            手段：把元素添加到数组的最后一个位置
            特征：插入元素后，得到的数据结构 对堆的特性产生了breach
         */
        nodeSpotToItsItemArr[nodesAmount] = newItem;
        nodesAmount++;

        /* #2 调整二叉树中的结点 来 恢复堆的特性2 */
        swimUpNodeOn(nodesAmount - 1); // 因为刚刚调整了heapSize的值，所以这里的参数要-1
    }

    /**
     * Maintains the heap property while inserting an element.
     * 在插入元素时维护堆的性质
     * 🐖 此方法会在需要时，上浮添加的新结点
     *
     * @param currentNodeSpot
     */
    private void swimUpNodeOn(int currentNodeSpot) {
        // #1 记录下 当前位置上的结点中的元素值
        int itemOfCurrentNode = nodeSpotToItsItemArr[currentNodeSpot];

        // #2 准备一个循环 来 得到“当前结点的value”应该在堆二叉树中出现的位置
        // 手段：不断地 把当前结点的父节点 交换到 其应该在的正确的位置上
        // 特征：因为需要不断地比较 & 覆盖操作，而次数又不确定 所以使用while循环
        while (currentNodeSpot > 0) {
            // 获取到 当前节点的父节点的值
            int itemOfParentNode = nodeSpotToItsItemArr[parentNodeSpot(currentNodeSpot)];
            // 比较 当前位置的结点的值 与 父节点的值，并 根据比较结果 来 决定是否 执行“向下覆盖”的行为
            if (noBreachExistUpwards(itemOfCurrentNode, itemOfParentNode)) break;
            // 否则，使用父节点的value 来 覆盖当前节点的value [沿着路径 从上往下覆盖]
            nodeSpotToItsItemArr[currentNodeSpot] = itemOfParentNode;

            //  更新“当前节点”的位置
            currentNodeSpot = parentNodeSpot(currentNodeSpot);
        }

        // #3 在 其他位置的节点的元素值 都已经正确后，直接把“当前值”(插入的值)绑定到“当前位置(正确的位置)”即可
        nodeSpotToItsItemArr[currentNodeSpot] = itemOfCurrentNode;
    }

    private boolean noBreachExistUpwards(int nodesItem, int parentNodesValue) {
        return nodesItem <= parentNodesValue;
    }


    /**
     * 核心操作：删除索引为x的节点，并返回该结点上的元素值
     * Deletes element at index x
     * Complexity: O(log N)
     *
     * @param givenSpot
     */
    public int deleteItemOn(int givenSpot) {
        // #0 判断堆是否已经空
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty, No element to delete");
        }

        // 记录下 堆中对应位置上的元素
        int elementOnSpot = nodeSpotToItsItemArr[givenSpot];
        // #1 用 堆尾部的元素 覆盖到 待删除的位置上
        nodeSpotToItsItemArr[givenSpot] = nodeSpotToItsItemArr[nodesAmount - 1];
        nodesAmount--;

        // #2 调整树中 当前位置上的节点，以满足二叉堆的特性2
        sinkDownNodeOn(givenSpot);

        // #3 返回 原始树中 传入的位置所对应的元素(被删除的结点的元素)
        return elementOnSpot;
    }

    /**
     * Maintains the heap property while deleting an element.
     * 在删除一个元素时维护堆的性质
     * 🐖 此方法会在需要时，下沉删除时 交换到删除位置上的结点
     *
     * @param currentNodeSpot
     */
    private void sinkDownNodeOn(int currentNodeSpot) { // 下沉操作
        // #1 记录下 当前位置上的结点中的元素值
        int itemOnCurrentSpot = nodeSpotToItsItemArr[currentNodeSpot];
        int childNodeSpot;

        // #2 准备一个循环 来 得到“当前位置的结点元素”应该在堆二叉树中出现的位置
        // 手段：不断地 把当前结点 交换到 其较大子结点的位置上
        // 特征：因为需要不断地比较 & 覆盖操作，而次数又不确定 所以使用while循环
        while (isLegitSpot(currentNodeSpot)) {
            // 求出 当前节点的较大孩子节点 所对应的位置
            childNodeSpot = maxChildSpotOf(currentNodeSpot);
            // 比较 当前位置的结点的值 与 其较大子节点的值，并 根据比较结果(是否存在breach) 来 决定是否 执行“向下覆盖”的行为
            if (noBreachExistDownwards(childNodeSpot, itemOnCurrentSpot)) {
                // 什么也不用做 break it,then we are done
                break;
            }
            // 否则，使用 当前节点的value 来 覆盖其较大子结点的value [沿着路径 从下往上覆盖]
            nodeSpotToItsItemArr[currentNodeSpot] = nodeSpotToItsItemArr[childNodeSpot];

            // 更新“当前节点”的位置
            currentNodeSpot = childNodeSpot;
        }

        // #3 在 其他位置的节点的元素值 都已经正确后，直接把“当前值”(交换来的元素)绑定到“当前位置(正确的位置)”即可
        nodeSpotToItsItemArr[currentNodeSpot] = itemOnCurrentSpot;
    }

    // 判断 二叉树中，从当前节点往下是否存在breach
    private boolean noBreachExistDownwards(int childNodeSpot, int itemOnCurrentSpot) {
        return itemOnCurrentSpot >= nodeSpotToItsItemArr[childNodeSpot];
    }

    // 判断 当前位置 是否是一个 可以继续执行循环的位置
    private boolean isLegitSpot(int currentNodeSpot) {
        // 如果当前位置的结点的第一个子结点的位置 < 最大位置，说明 子结点 仍旧在有效位置上，则：
        // 循环可以继续执行
        return kthChildNodeSpot(currentNodeSpot, 1) < nodesAmount;
    }


    /**
     * 获取到索引为i的节点的 左右孩子中的较大孩子节点 的索引位置
     *
     * @param givenNodeSpot
     * @return
     */
    private int maxChildSpotOf(int givenNodeSpot) {
        // #1 求出当前节点的左右孩子的索引
        int leftChildNodeSpot = kthChildNodeSpot(givenNodeSpot, 1);
        int rightChildNodeSpot = kthChildNodeSpot(givenNodeSpot, 2);
        // #2 比较两个孩子中，哪个孩子的值更大   返回更大的那个孩子的索引
        return nodeSpotToItsItemArr[leftChildNodeSpot] > nodeSpotToItsItemArr[rightChildNodeSpot]
                ? leftChildNodeSpot
                : rightChildNodeSpot;
    }


    /**
     * Prints all elements of the heap
     * 打印堆中的所有元素 其实就是数组啦
     */
    public void printHeap() {
        System.out.print("current Heap = ");
        for (int currentNodeSpot = 0; currentNodeSpot < nodesAmount; currentNodeSpot++)
            System.out.print(nodeSpotToItsItemArr[currentNodeSpot] + " ");

        System.out.println();
    }


    /**
     * This method returns the max element of the heap.
     * complexity: O(1)
     */
    public int findMax() {
        if (isEmpty())
            throw new NoSuchElementException("Heap is empty.");
        return nodeSpotToItsItemArr[0];
    }


    public static void main(String[] args) {
        // 创建一个大顶堆对象 并 完成初始化
        BinaryHeap maxHeap = new BinaryHeap(10);
        maxHeap.insert(10);
        maxHeap.insert(4);
        maxHeap.insert(9);
        maxHeap.insert(1);
        maxHeap.insert(7);
        maxHeap.insert(5);
        maxHeap.insert(3); // insert之后，会自动得到一个 大顶堆

        // 打印当前的大顶堆
        maxHeap.printHeap();

        // 删除堆元素5 & 打印大顶堆
        maxHeap.deleteItemOn(5);
        maxHeap.printHeap();

        // 删除堆元素2 & 打印大顶堆
        maxHeap.deleteItemOn(2);
        maxHeap.printHeap();
    }
} // 测试用例的局限：无法可视化地看到树地变化；
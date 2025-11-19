package com.henry.basic_chapter_01.specific_application.implementation.advanced.quick_union;

import edu.princeton.cs.algs4.StdIn;

/**
 * 作用：对一个数据集合中的 任意两个数据 做连接操作；
 * 手段：用一个id[]数组 来 存储 数据所属的分量；
 *
 * 算法：quick-union
 * 说明：算法不同，决定了 id[]中存储的值 也是不同的。
 * 现在 id[x]中 存储的是 父节点的索引值
 */
/*
目标：优化 union()操作的耗时级别
手段：
    #1 把 相互连通的节点，通过“链接”的方式（pointOutNode -> pointInNode） 来 组装成 一棵树。
    #2 然后 把 树的根节点 作为 “相互连通的节点”的组名
 */
public class QuickUnionTemplate {
    private int[] currentNodeToParentNodeArray; // currentNodeToParentNodeArray
    private int treeAmount;

    public QuickUnionTemplate(int nodeAmount) {
        treeAmount = nodeAmount;

        /* 初始化时，为每个节点 初始化一个指向自己的链接 */
        currentNodeToParentNodeArray = new int[nodeAmount];
        for (int currentNode = 0; currentNode < currentNodeToParentNodeArray.length; currentNode++) {
            // 初始化时，当前节点 指向它自己（形成了自链接）
            currentNodeToParentNodeArray[currentNode] = currentNode;
        }
    }

    // APIs
    /**
     * 对 指定的两个元素 进行 连接操作（连接到 同一分量）
     * 说明：先判断 两个元素 是否 已经属于 同一个分量了
     * 手段：在 根节点A 与 根节点B 之间 添加一个指向关系
     * @param nodeP 节点P
     * @param nodeQ 节点Q
     */
    public void unionToSameComponent(int nodeP, int nodeQ) {
        // 获取到 各个节点的根节点id
        int treeIdOfNodeP = findComponentIdOf(nodeP);
        int treeIdOfNodeQ = findComponentIdOf(nodeQ);

        /* 判断 两个节点 是否属于 同一个分量 */
        // 如果属于同一个分量，说明不需要合并，则：
        if (treeIdOfNodeP == treeIdOfNodeQ) {
            // 直接return。
            return;
        }

        // 如果不属于同一个分量，说明 需要对两个分量中的节点 进行合并，则：执行合并
        // 手段：把 两棵树的根结点 连接起来，得到 一棵树
        // 具体做法：把 节点p的根节点(作为当前节点) 指向 节点q的根节点(作为父结点)
        currentNodeToParentNodeArray[treeIdOfNodeP] = treeIdOfNodeQ;

        // 合并后，森林中的 树的数量 减一
        treeAmount--;
    }

    /**
     * 查询到 某个节点 所属的分量的根节点id
     * 说明：
     *  1 使用 链表?? 来 组织 分量中的所有元素；
     *  2 使用 链表的根节点 来 唯一标识 此分量；
     *  手段：
     *      查询到 指定节点的根节点
     *  原理：一个分量中的所有节点 组成了一棵单向的树
     *  返回值：根节点的内容/索引
     * @param currentNode   指定的节点
     */
    public int findComponentIdOf(int currentNode){
        /* 判断 当前节点 是不是 根节点 */
        while (isNotRootNode(currentNode)) { // 如果不是，说明还没有导航到根节点，
            // 则：使用 当前节点的父节点 来 更新当前节点
            currentNode = parentNodeOf(currentNode);
        }

        // 到达 根结点 - 根结点本身 就是 树的标识符，
        int treeId = currentNode;
        return treeId;
    }

    // 获取到 当前节点的父节点
    // 手段：从 数组中记录的映射关系 中获取即可；
    private int parentNodeOf(int currentNode) {
        int parentNode = currentNodeToParentNodeArray[currentNode];
        return parentNode;
    }

    // 判断一个节点是不是 其所属分量的根节点
    // 手段：检查 该节点的父节点 是不是 其自身
    private boolean isNotRootNode(int currentNode) {
        return currentNode != currentNodeToParentNodeArray[currentNode];
    }

    /**
     * 判断 两个元素 是不是 已经相连了（在同一个分量中）
     * 手段：查看 两个节点的根节点 是不是相同的
     * @param nodeP 节点P
     * @param nodeQ 节点Q
     */
    public boolean isConnectedBetween(int nodeP, int nodeQ) {
        int treeIdOfNodeP = findComponentIdOf(nodeP);
        int treeIdOfNodeQ = findComponentIdOf(nodeQ);

        return treeIdOfNodeP == treeIdOfNodeQ;
    }

    /**
     * 获取到 集合中的分量个数
     * @return
     */
    public int getComponentAmount(){
        return treeAmount;
    }

    /****************************************************
     * 使用 上述的APIs 来 完成预期任务（按照 标准输入所传入的整数对的指导 来 对 离散的节点 进行连通）
     ****************************************************/
    public static void main(String[] args) {
        // 从标准输入中 读取到节点数量
        int nodeAmount = StdIn.readInt();
        // 使用构造器 得到 一个由离散的节点 所组成的森林
        QuickUnionTemplate forest = new QuickUnionTemplate(nodeAmount);

        while (!StdIn.isEmpty()) {
            // 读取整数对 pair
            int nodeP = StdIn.readInt();
            int nodeQ = StdIn.readInt();

            // 判断这对元素 是否已经连通
            // 比如：在union(9, 4)的时候，就会导致 (8, 9)连通 - 因此对于 pair(8, 9) 就不用再做union()了
            if (forest.isConnectedBetween(nodeP, nodeQ)) {
                continue; // 如果 已经连通了，就 什么都不做
            }

            // 把 整数对中的两个元素 连接到 同一个分量中
            forest.unionToSameComponent(nodeP, nodeQ);
            System.out.println("在 " + nodeP + " " + nodeQ + " 之间建立连接");

        }

        // 最终，森林中 会只剩下 几棵 多节点的树
        System.out.println(forest.getComponentAmount() + "分量（子集合）");
    }
}
/*
启示：
    1 在返回值类型为void的方法中，也可以使用return;来终止方法
 */

package com.henry.basic_chapter_01.specific_application.implementation.advanced.quick_union_by_weight;

import edu.princeton.cs.algs4.StdIn;

/**
 * 作用：对 一个数据集合中的 任意两个数据 进行连接操作；
 * 手段：使用 一个id[] 来 记录 数据集合中的分量信息；
 * <p>
 * 算法：加权quick-union aka union-find
 * 原理：在 合并 时，小心地 让树 不要长得太高
 * 手段：先 找到小树，再 把小树 接在大树下（而不是随意连接）
 * <p>
 * 术语/同义词：#1 连通分量、组； #2 节点的根节点、节点的组id；#3 当前节点（指出节点）；#4 父节点（指入节点）
 */
public class WeightedQuickUnionTemplate {
    private int[] currentNodeToParentNodeArray; // 记录 当前节点 -> 其父节点 的映射关系，用于 回溯到分量的根节点
    private int[] treeIdToItsNodeAmount; // 记录分量中的节点数量  🐖：只需要 在根节点中存储 正确的值 即可   非根节点 用完就不用管了
    private int treeAmount;

    // 构造器 用于创建 当前类的实例对象
    public WeightedQuickUnionTemplate(int nodeAmount) {
        treeAmount = nodeAmount;
        // 初始化 每一棵树中的节点 的父节点 为 其本身（aka 有一个指向自己的链接）
        currentNodeToParentNodeArray = new int[nodeAmount];
        for (int currentNode = 0; currentNode < nodeAmount; currentNode++) {
            currentNodeToParentNodeArray[currentNode] = currentNode;
        }

        // 初始化 每一棵树中的节点数量（初始时是一堆独立的节点，因此 每棵树的节点数量都是1）
        treeIdToItsNodeAmount = new int[nodeAmount];
        for (int currentGroup = 0; currentGroup < nodeAmount; currentGroup++) {
            treeIdToItsNodeAmount[currentGroup] = 1; // 初始化时，每个分量中都只有一个元素/节点
        }
    }

    // APIs
    /**
     * 对 两个 指定的元素/节点/节点所属的树 进行连接（到同一个分量）
     * 优化：把小树 连到大树上 而不是 随意连接
     *
     * @param nodeP 指定的节点1
     * @param nodeQ 指定的节点2
     */
    public void unionToSameComponent(int nodeP, int nodeQ) {
        // #1 获取到节点1和节点2 所在的分量的id
        int treeIdOfNodeP = findTreeIdOf(nodeP);
        int treeIdOfNodeQ = findTreeIdOf(nodeQ);

        // #2 如果 它们同属于一个分量的话，说明 无需合并，则：
        if (treeIdOfNodeP == treeIdOfNodeQ) {
            // 直接return，返回调用方
            return;
        }

        // #3 把 “小树的根节点” 连接到 “大树的根节点” 上 minNode -> maxNode
        // 如果 nodeP所在的分量中的节点数量 更少，说明 应该把此分量 连接到 彼分量 上，则：
        if (nodePIsInSmallerTree(treeIdOfNodeP, treeIdOfNodeQ)) {
            // ① 把 小树的根节点 的父节点，设置为大树的根节点
            linkSmallerTreeToBiggerTree(treeIdOfNodeP, treeIdOfNodeQ);
            // ② 更新 “大树” 中的节点数量
            addsNodeAmountToBiggerTree(treeIdOfNodeP, treeIdOfNodeQ);
        } else { // 如果 nodeQ所在的分量中的节点数量 更少，说明 应该把此分量 连接到 彼分量 上，则：
            // 同样的步骤，只是实际参数的顺序不同
            linkSmallerTreeToBiggerTree(treeIdOfNodeQ, treeIdOfNodeP);
            addsNodeAmountToBiggerTree(treeIdOfNodeQ, treeIdOfNodeP);
        }

        // 经过合并后，森林中的树的数量-1
        treeAmount--;
    }

    /**
     * 查询到 指定节点所在的分量id；
     * 手段：找到 其所在分量的根节点————链表的根节点 满足 特征xxx
     * 使用 树的根节点 作为 组id
     *
     * @param currentNode 指定的节点
     */
    public int findTreeIdOf(int currentNode) {
        // 当 当前节点 还不是分量的根节点 时，
        while (isNotRootNode(currentNode)) {
            // 从 当前节点 不断向上回溯到 其父节点，直到 当前节点 为 根节点
            currentNode = parentNodeOf(currentNode);
        }

        // 返回 所找到的 分量的根节点，作为 分量/树的id
        int treeId = currentNode;
        return treeId;
    }

    // 判断一个节点 是不是 其所属分量的根节点
    private boolean isNotRootNode(int currentNode) {
        // 手段：查看 其父节点 是不是 其本身
        return currentNode != currentNodeToParentNodeArray[currentNode];
    }

    // 获取到 当前节点的父节点
    private int parentNodeOf(int currentNode) {
        int parentNode = currentNodeToParentNodeArray[currentNode];
        return parentNode;
    }

    // 把 小树的节点数量 累计到 合并后的树的节点数量 中
    private void addsNodeAmountToBiggerTree(int treeIdOfSmallerTree, int treeIdOfBiggerTree) {
        treeIdToItsNodeAmount[treeIdOfBiggerTree] += treeIdToItsNodeAmount[treeIdOfSmallerTree];
    }

    // 把 小树的根节点 连接到 大树的根节点 上
    private void linkSmallerTreeToBiggerTree(int rootNodeOfSmallerTree, int rootNodeOfBiggerTree) {
        currentNodeToParentNodeArray[rootNodeOfSmallerTree] = rootNodeOfBiggerTree;
    }

    // 判断 nodeP所在的分量的节点数量 是不是 比起 nodeQ所在的分量的节点数量 更少
    private boolean nodePIsInSmallerTree(int treeIdOfNodeP, int treeIdOfNodeQ) {
        // 手段：获取到 其所在分量的节点数量 进行对比
        return treeIdToItsNodeAmount[treeIdOfNodeP] < treeIdToItsNodeAmount[treeIdOfNodeQ];
    }

    /**
     * 判断 给定的两个节点之间 是否相连接
     * 手段：先 分别获取到 节点各自所属的分量id，再检查 分量id 是否相等
     * @param nodeP 给定的节点P
     * @param nodeQ 给定的节点Q
     * @return  如果 两个节点之间 相连通，则 返回true。否则 返回false
     */
    public boolean isConnectedBetween(int nodeP, int nodeQ) {
        // ① 分别获取到 节点各自所属的分量id
        int groupIdOfNodeP = findTreeIdOf(nodeP);
        int groupIdOfNodeQ = findTreeIdOf(nodeQ);

        // ② 检查 分量id 是否相等
        return groupIdOfNodeP == groupIdOfNodeQ;
    }

    /**
     * 获取到 集合中 当前分量的总数量
     */
    public int getTreeAmount() {
        return treeAmount;
    }


    /****************************************************
     * 使用 上述的APIs 来 完成预期任务（按照 标准输入 所传入的整数对的指导 来 对 离散的节点 进行连通）
     ****************************************************/
    public static void main(String[] args) {
        // 从标准输入中 读取到 森林中的节点数量
        int nodeAmount = StdIn.readInt();
        // 实例化 得到 一堆 分散的节点
        WeightedQuickUnionTemplate forest = new WeightedQuickUnionTemplate(nodeAmount);

        while (!StdIn.isEmpty()) {
            // 读取 整数对中的 两个节点
            int nodeP = StdIn.readInt();
            int nodeQ = StdIn.readInt();

            // 如果 这两个节点之间 已经相互连通了，说明 不需要进行合并操作，则：
            // 比如：在union(9, 4)的时候， 就会导致 (8, 9)连通 - 因此对于 pair(8, 9) 就不用再做union()了
            if (forest.isConnectedBetween(nodeP, nodeQ)) {
                // 跳过本轮循环
                continue;
            }

            // 把 两个元素 连接到 同一个分量 中
            forest.unionToSameComponent(nodeP, nodeQ);
            System.out.println("在 " + nodeP + " " + nodeQ + " 之间建立连接");
        }

        System.out.println(forest.getTreeAmount() + "分量（子集合）");
    }

}

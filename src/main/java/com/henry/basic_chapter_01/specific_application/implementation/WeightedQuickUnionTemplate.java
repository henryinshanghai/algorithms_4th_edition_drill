package com.henry.basic_chapter_01.specific_application.implementation;

import edu.princeton.cs.algs4.StdIn;

/**
 * 作用：对一个数据集合中的任意两个数据进行连接操作；
 * 手段：使用一个id[]来记录数据集合中的分量信息；
 *
 * 算法：加权quick-union aka union-find
 * 特征：在合并时，小心地让树不要长得太高
 * 手段：找到小树，并把小树接在大树下
 */
/*
同义词：#1 连通分量、组； #2 节点的根节点、节点的组id；#3 当前节点（指出节点）；
#4 父节点（指入节点）
 */
public class WeightedQuickUnionTemplate {
    private int[] currentNodeToParentNodeArray;
    private int[] treeIdToItsNodeAmount; // 记录分量的大小  注：只要在根节点中存储正确的值即可   非根节点用完就不管了  内一个普通的节点也都曾经是根节点
    private int treeAmount;

    public WeightedQuickUnionTemplate(int nodeAmount) {
        treeAmount = nodeAmount;
        // 初始化每一棵树中的节点
        currentNodeToParentNodeArray = new int[nodeAmount];
        for (int currentNode = 0; currentNode < nodeAmount; currentNode++) {
            currentNodeToParentNodeArray[currentNode] = currentNode;
        }

        // 初始化每一棵树中的节点数量
        treeIdToItsNodeAmount = new int[nodeAmount];
        for (int currentGroup = 0; currentGroup < nodeAmount; currentGroup++) {
            treeIdToItsNodeAmount[currentGroup] = 1; // 初始化时，每个分量中都只有一个元素/节点
        }
    }

    // API
    /**
     * 获取到集合中当前分量的总数量
     */
    public int getTreeAmount(){
        return treeAmount;
    }

    /**
     * 查询到指定元素所在的分量；
     * 手段：找到所在分量的根节点————链表的根节点满足特征xxx
     * 使用树的根节点 作为 组id
     * @param currentNode
     */
    public int findTreeIdOf(int currentNode){
        while (isNotRootNode(currentNode)) { // 如果不是根节点（根节点上存在自环）
            currentNode = parentNodeOf(currentNode);
        }

        int treeId = currentNode;
        return treeId; // 返回 参数节点的根节点 aka 组的id
    }

    private boolean isNotRootNode(int currentNode) {
        return currentNode != currentNodeToParentNodeArray[currentNode];
    }

    private int parentNodeOf(int currentNode) {
        int parentNode = currentNodeToParentNodeArray[currentNode]; // 则：更新到父节点
        return parentNode;
    }

    /**
     * 对两个指定的元素进行连接（到同一个分量）
     * 优化：小树连到大树上 而不是随意连接
     * @param nodeP
     * @param nodeQ
     */
    public void unionToSameComponent(int nodeP, int nodeQ) {
        int treeIdOfNodeP = findTreeIdOf(nodeP);
        int treeIdOfNodeQ = findTreeIdOf(nodeQ);

        if (treeIdOfNodeP == treeIdOfNodeQ) {
            return;
        }

        // 把 “小树的根节点” 连接到 “大树的根节点” 上 minNode -> maxNode
        // 手段：比较当前参与union的两个节点所属组中的节点数量
        if (nodePIsInSmallerTree(treeIdOfNodeP, treeIdOfNodeQ)) {
            // 把 小树的根节点 的父节点，设置为大树的根节点
            linkSmallerTreeToBiggerTree(treeIdOfNodeP, treeIdOfNodeQ);
            // 更新 “大树” 中的节点数量
            addsNodeAmountToBiggerTree(treeIdOfNodeP, treeIdOfNodeQ);
        } else {
            linkSmallerTreeToBiggerTree(treeIdOfNodeQ, treeIdOfNodeP);
            addsNodeAmountToBiggerTree(treeIdOfNodeQ, treeIdOfNodeP);
        }

        treeAmount--;
    }

    private void addsNodeAmountToBiggerTree(int treeIdOfSmallerTree, int treeIdOfBiggerTree) {
        treeIdToItsNodeAmount[treeIdOfBiggerTree] += treeIdToItsNodeAmount[treeIdOfSmallerTree];
    }

    private void linkSmallerTreeToBiggerTree(int rootNodeOfSmallerTree, int rootNodeOfBiggerTree) {
        currentNodeToParentNodeArray[rootNodeOfSmallerTree] = rootNodeOfBiggerTree;
    }

    private boolean nodePIsInSmallerTree(int treeIdOfNodeP, int treeIdOfNodeQ) {
        return treeIdToItsNodeAmount[treeIdOfNodeP] < treeIdToItsNodeAmount[treeIdOfNodeQ];
    }

    public static void main(String[] args) {
        int nodeAmount = StdIn.readInt();
        WeightedQuickUnionTemplate forest = new WeightedQuickUnionTemplate(nodeAmount);

        while (!StdIn.isEmpty()) {
            // 读取整数对 pair
            int nodeP = StdIn.readInt();
            int nodeQ = StdIn.readInt();

            // 判断这对元素是否已经连通
            // 比如：在union(9, 4)的时候， 就会导致 (8, 9)连通 - 因此对于 pair(8, 9) 就不用再做union()了
            if (forest.isConnectedBetween(nodeP, nodeQ)) {
                continue; // 如果已经连通了，就什么都不做
            }

            forest.unionToSameComponent(nodeP, nodeQ); // 把两个元素连接到同一个分量中
            System.out.println("在 " + nodeP + " " + nodeQ + " 之间建立连接");

        }

        System.out.println(forest.getTreeAmount() + "分量（子集合）");
    }


    private boolean isConnectedBetween(int nodeP, int nodeQ) {
        int groupIdOfNodeP = findTreeIdOf(nodeP);
        int groupIdOfNodeQ = findTreeIdOf(nodeQ);

        return groupIdOfNodeP == groupIdOfNodeQ;
    }
}

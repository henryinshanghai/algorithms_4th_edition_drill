package com.henry.basic_chapter_01.specific_application.implementation;

import edu.princeton.cs.algs4.StdIn;

/**
 * 作用：对一个数据集合中的任意两个数据做连接操作；
 * 手段：用一个id[]数组来存储数据所属的分量；
 *
 * 算法：quick-union
 * 说明：算法不同，决定了id[]中存储的值也是不同的。
 * 现在id[x]存储的是父节点的索引值
 */
/*
目标：优化 union()操作的耗时级别
手段：
    #1 把相互连通的节点，通过“链接”的方式（pointOutNode -> pointInNode） 来 组装成 一棵树。
    #2 然后把树的根节点 作为 “相互连通的节点”的组名
 */

public class QuickUnionTemplate {
    private int[] currentNodeToParentNodeArray; // currentNodeToParentNodeArray
    private int treeAmount;

    public QuickUnionTemplate(int nodeAmount) {
        treeAmount = nodeAmount;
        currentNodeToParentNodeArray = new int[nodeAmount];

        for (int currentNode = 0; currentNode < currentNodeToParentNodeArray.length; currentNode++) {
            // 初始化时，当前节点 指向它自己（形成了自环）
            currentNodeToParentNodeArray[currentNode] = currentNode;
        }
    }

    // API
    /**
     * 获取集合中的分量个数
     * @return
     */
    public int getComponentAmount(){
        return treeAmount;
    }

    /**
     * 查询到某个元素所属的分量
     * 说明：
     *  1 使用链表来组织分量中的所有元素；
     *  2 使用链表的根节点来唯一标识此分量；
     *  手段：
     *      查询到指定节点的根节点
     *  返回值：根节点的内容/索引
     * @param currentNode
     */
    // 原理：使用 “相互连通的所有节点” 所组成的树的根节点，来 作为 组的id
    public int findComponentIdOf(int currentNode){
        while (isNotRootNode(currentNode)) { // 判断当前节点是不是根节点...
            // 如果不是，则：更新当前节点
            // 手段：找到当前节点的上一个节点
            currentNode = swimUpTheTree(currentNode);
        }

        // 到达根结点 - 根结点本身就是树的标识符
        int treeId = currentNode;
        return treeId;
    }

    private int swimUpTheTree(int currentNode) {
        int parentNode = currentNodeToParentNodeArray[currentNode];
        return parentNode;
    }

    private boolean isNotRootNode(int currentNode) {
        return currentNode != currentNodeToParentNodeArray[currentNode];
    }

    /**
     * 对指定的两个元素进行连接操作（连接到同一分量）
     * 说明：
     *  1 先判断两个元素是否已经属于同一个分量了
     *  手段：在根节点A与根节点B之间添加一个指向关系
     * @param nodeP
     * @param nodeQ
     */
    public void unionToSameComponent(int nodeP, int nodeQ) {
        int treeIdOfNodeP = findComponentIdOf(nodeP);
        int treeIdOfNodeQ = findComponentIdOf(nodeQ);

        if (treeIdOfNodeP == treeIdOfNodeQ) {
            return;
        }

        // 把 节点p的根节点 指向 节点q的根节点 treeIdOfNodeP -> treeIdOfNodeQ
        currentNodeToParentNodeArray[treeIdOfNodeP] = treeIdOfNodeQ;

        // 合并后，森林中的树的数量减一
        treeAmount--;
    }

    /**
     * 判断两个元素是不是已经相连了（在同一个分量中）
     * @param nodeP
     * @param nodeQ
     */
    public boolean isConnectedBetween(int nodeP, int nodeQ) {
        int treeIdOfNodeP = findComponentIdOf(nodeP);
        int treeIdOfNodeQ = findComponentIdOf(nodeQ);

        return treeIdOfNodeP == treeIdOfNodeQ;
    }

    public static void main(String[] args) {
        int nodeAmount = StdIn.readInt();
        QuickUnionTemplate forest = new QuickUnionTemplate(nodeAmount);

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

        System.out.println(forest.getComponentAmount() + "分量（子集合）");
    }
}
/*
启示：
    1 在返回值类型为void的方法中，也可以使用return;来终止方法
 */

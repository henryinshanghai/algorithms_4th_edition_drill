package com.henry.basic_chapter_01.specific_application.implementation.advanced.quick_union;

import edu.princeton.cs.algs4.StdIn;

public class QuickUnionDrill {
    private int[] currentNodeToParentNodeArray;
    private int treeAmount;

    public QuickUnionDrill(int nodeAmount) {
        treeAmount = nodeAmount;
        currentNodeToParentNodeArray = new int[nodeAmount];

        for (int currentNode = 0; currentNode < currentNodeToParentNodeArray.length; currentNode++) {
            currentNodeToParentNodeArray[currentNode] = currentNode;
        }
    }

    // 把 两个节点 快速合并到 同一个分量中
    public void unionToSameComponent(int nodeP, int nodeQ) {
        int treeIdOfNodeP = findTreeIdOf(nodeP);
        int treeIdOfNodeQ = findTreeIdOf(nodeQ);

        if (treeIdOfNodeP == treeIdOfNodeQ) {
            return; // 如果两个节点已经在同一棵树中，则：跳过处理，无需连接
        }

        currentNodeToParentNodeArray[treeIdOfNodeP] = treeIdOfNodeQ;
        treeAmount--;
    }

    // 获取到 指定节点所属分量的根节点id
    private int findTreeIdOf(int currentNode) {
        while (isNotRootNode(currentNode)) {
            currentNode = parentNodeOf(currentNode);
        }

        int treeId = currentNode;
        return treeId;
    }

    private int parentNodeOf(int currentNode) {
        return currentNodeToParentNodeArray[currentNode];
    }

    private boolean isNotRootNode(int currentNode) {
        return currentNodeToParentNodeArray[currentNode] != currentNode;
    }

    public boolean isConnectedBetween(int nodeP, int nodeQ) {
        return findTreeIdOf(nodeP) == findTreeIdOf(nodeQ);
    }

    public int getComponentAmount() {
        return treeAmount;
    }

    public static void main(String[] args) {
        int vertexAmount = StdIn.readInt();

        QuickUnionDrill forest = new QuickUnionDrill(vertexAmount);

        while (!StdIn.isEmpty()) {
            int nodeP = StdIn.readInt();
            int nodeQ = StdIn.readInt();

            if (forest.isConnectedBetween(nodeP, nodeQ)) {
                System.out.println("--- " + nodeP + " 与 " + nodeQ + " 已经相互连通了" + " ---");
                continue;
            }

            forest.unionToSameComponent(nodeP, nodeQ);
            System.out.println("+++ " + "对 " + nodeP + " 与 " + nodeQ + " 进行连通！+++");
        }

        System.out.println("最终森林中有 " + forest.getComponentAmount() + " 棵树");
    }
}

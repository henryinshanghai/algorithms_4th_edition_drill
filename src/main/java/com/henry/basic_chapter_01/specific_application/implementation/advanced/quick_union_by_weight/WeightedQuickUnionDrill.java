package com.henry.basic_chapter_01.specific_application.implementation.advanced.quick_union_by_weight;

public class WeightedQuickUnionDrill {
    private int[] currentNodeToParentNodeArray;
    private int[] treeIdToItsNodeAmountArray;
    private int treeAmount;

    public WeightedQuickUnionDrill(int maxNumber) {
        treeAmount = maxNumber;

        currentNodeToParentNodeArray = new int[maxNumber];
        for (int currentNode = 0; currentNode < currentNodeToParentNodeArray.length; currentNode++) {
            currentNodeToParentNodeArray[currentNode] = currentNode;
        }

        treeIdToItsNodeAmountArray = new int[maxNumber];
        for (int currentTreeId = 0; currentTreeId < treeIdToItsNodeAmountArray.length; currentTreeId++) {
            treeIdToItsNodeAmountArray[currentTreeId] = 1;
        }
    }

    public void unionToSameGroup(int nodeP, int nodeQ) {
        int treeIdOfNodeP = findTreeIdOf(nodeP);
        int treeIdOfNodeQ = findTreeIdOf(nodeQ);

        if (treeIdOfNodeP == treeIdOfNodeQ) {
            return;
        }

        if (nodePIsSmallerTree(treeIdOfNodeP, treeIdOfNodeQ)) {
            // 把小树的根结点 连接到 大树的根结点上
            linkSmallerTreeToBiggerTree(treeIdOfNodeP, treeIdOfNodeQ);
            // 更新大树的节点数量
            addNodeAmountInBiggerTree(treeIdOfNodeP, treeIdOfNodeQ);
        } else {
            linkSmallerTreeToBiggerTree(treeIdOfNodeQ, treeIdOfNodeP);
            addNodeAmountInBiggerTree(treeIdOfNodeQ, treeIdOfNodeP);
        }

        treeAmount--;
    }

    private void addNodeAmountInBiggerTree(int treeIdOfSmallerTree, int treeIdOfBiggerTree) {
        treeIdToItsNodeAmountArray[treeIdOfBiggerTree] += treeIdToItsNodeAmountArray[treeIdOfSmallerTree];
    }

    private void linkSmallerTreeToBiggerTree(int rootNodeOfSmallerTree, int rootNodeOfBiggerTree) {
        currentNodeToParentNodeArray[rootNodeOfSmallerTree] = rootNodeOfBiggerTree;
    }

    private boolean nodePIsSmallerTree(int treeIdOfNodeP, int treeIdOfNodeQ) {
        return treeIdToItsNodeAmountArray[treeIdOfNodeP] < treeIdToItsNodeAmountArray[treeIdOfNodeQ];
    }

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
}

package com.henry.leetcode_traning_camp.week_03.day01.flip_binary_tree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// 验证：可以使用递归的方式 来 实现对二叉树中结点的镜像翻转
// 使用递归的核心依据：子问题的解 就是 原始问题的解的一部分；
public class Solution_flip_binary_tree_side_to_side {
    public static void main(String[] args) {
        // 构建一个二叉树对象
        TreeNode rootNodeOfTree = constructABinaryTree();

        // 得到 二叉树结点的”层序遍历序列“
        List<List<Integer>> nodeSequenceInLevelOrder = traverseTreeInLevelOrder(rootNodeOfTree);
        printLists(nodeSequenceInLevelOrder);

        // 翻转二叉树对象
        TreeNode rootNodeOfFlippedTree = flipTreeSideToSide(rootNodeOfTree); // 使用递归

        // 获取到反转后的二叉树的层序遍历结果
        List<List<Integer>> nodeSequenceOfFlippedTree = traverseTreeInLevelOrder(rootNodeOfFlippedTree);
        printLists(nodeSequenceOfFlippedTree);
    }

    private static TreeNode constructABinaryTree() {
        TreeNode root = new TreeNode(1);
        TreeNode sub_left = new TreeNode(2);
        TreeNode sub_right = new TreeNode(3);

        root.leftSubNode = sub_left;
        root.rightSubNode = sub_right;

        TreeNode sub_left_left = new TreeNode(4);
        TreeNode sub_left_right = new TreeNode(5);
        sub_left.leftSubNode = sub_left_left;
        sub_left.rightSubNode = sub_left_right;

        TreeNode sub_right_left = new TreeNode(6);
        TreeNode sub_right_right = new TreeNode(7);
        sub_right.leftSubNode = sub_right_left;
        sub_right.rightSubNode = sub_right_right;
        return root;
    }

    /**
     * 对指定的二叉树进行翻转操作
     *
     * @param currentRootNode
     * @return
     */
    private static TreeNode flipTreeSideToSide(TreeNode currentRootNode) {
        // #0 递归终结条件
        // 如果 当前结点为nil结点 或者 当前节点为叶子节点，说明 当前路径上的翻转已经结束，则：
        if (currentRootNode == null || isLeafNode(currentRootNode)) {
            // 把当前节点 返回给上一级
            return currentRootNode;
        }

        // #1 本级递归需要做的事情(假设递归方法已经完全实现)：翻转左子树、翻转右子树
        // Ⅰ 规模更小的子问题：翻转子树中的结点；
        // Ⅱ 规模更小的子问题的解 怎么能帮助解决原始问题? 子树中的结点翻转后，只要把其根结点“正确地连接到”原始树的根结点，就完成了对原始树的翻转
        TreeNode newRightNode = flipTreeSideToSide(currentRootNode.leftSubNode);
        TreeNode newLeftNode = flipTreeSideToSide(currentRootNode.rightSubNode);

        // 然后把翻转后的子树的根结点 正确地连接到 原始的根结点上
        currentRootNode.leftSubNode = newLeftNode;
        currentRootNode.rightSubNode = newRightNode;

        // #2 返回“原始树的根结点”
        return currentRootNode;
    }

    // 判断一个结点是否是叶子节点
    private static boolean isLeafNode(TreeNode currentRootNode) {
        // 结点的左子结点 与 右子节点 同时为null
        return currentRootNode.leftSubNode == null && currentRootNode.rightSubNode == null;
    }

    // 打印“结点列表的序列”
    public static void printLists(List<List<Integer>> nodeListSequence) {
        int levelAmount = nodeListSequence.size();
        for (int currentLevel = 0; currentLevel < levelAmount; currentLevel++) {
            List<Integer> currentLevelNodeList = nodeListSequence.get(currentLevel);

            for (int currentNodeSpot = 0; currentNodeSpot < currentLevelNodeList.size(); currentNodeSpot++) {
                Integer currentNodeValue = currentLevelNodeList.get(currentNodeSpot);
                System.out.print(currentNodeValue + " -> ");
            }

            System.out.println();
        }
    }

    // 🐖 对于二叉树的层序遍历，队列是一个非常适合的数据结构
    public static List<List<Integer>> traverseTreeInLevelOrder(TreeNode rootNode) {
        /* #1 准备变量 */
        // 准备一个队列 用于 动态读入 二叉树当前层中的结点
        Deque<TreeNode> nodeQueueForCurrentLevel = new LinkedList<>();
        // 准备一个列表 用于 存储 每一层的结点值列表（每一层对应一个元素）
        LinkedList<List<Integer>> nodesValueListSequence = new LinkedList<>(); // 🐖 这里其实只需要一个list而已，并不需要一定使用 linkedList

        // 把”当前节点“ 入队
        nodeQueueForCurrentLevel.addLast(rootNode);

        // #2 准备一个循环 来 得到 ”对二叉树进行层序遍历的结果列表序列“
        while (!nodeQueueForCurrentLevel.isEmpty()) {
            // Ⅰ 准备一个列表 来 存储”当前层的结点值“
            List<Integer> currentLevelNodeValList = new LinkedList<>();

            /* Ⅱ 处理 二叉树中的当前层结点 来 得到 ”当前层的结点值列表“ */
            // 得到当前层的结点数量
            int nodeAmountOnCurrentLevel = nodeQueueForCurrentLevel.size();
            for (int currentSpot = 0; currentSpot < nodeAmountOnCurrentLevel; currentSpot++) {
                // ① 获取到 当前节点
                TreeNode currentNode = nodeQueueForCurrentLevel.pollFirst();
                // 对”当前节点“进行判空
                if (currentNode == null) {
                    continue; // 跳过空元素
                }
                // ② 把当前结点的值 添加到 ”当前层所对应的结点值列表“中
                currentLevelNodeValList.add(currentNode.val);

                // ③ 把当前结点的子节点 从左到右地 追加到队列的尾部 来 作为下一层的结点列表
                nodeQueueForCurrentLevel.addLast(currentNode.leftSubNode);
                nodeQueueForCurrentLevel.addLast(currentNode.rightSubNode);
            }

            // Ⅲ 把 ”当前层的结点值列表“ 添加到 最终的”结果列表序列"中
            nodesValueListSequence.add(currentLevelNodeValList);
        }

        // #3 返回最终的“结果列表序列”
        return nodesValueListSequence;
    }
}

// 定义二叉树中的结点
class TreeNode {
    // 结点中所包含的内容
    int val; // 结点的值
    TreeNode leftSubNode; // 对左子节点的引用
    TreeNode rightSubNode; // 对右子节点的引用

    public TreeNode(int val) {
        this.val = val;
    }
}

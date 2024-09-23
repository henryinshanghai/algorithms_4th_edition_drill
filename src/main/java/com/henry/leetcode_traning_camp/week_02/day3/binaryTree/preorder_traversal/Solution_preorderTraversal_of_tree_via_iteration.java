package com.henry.leetcode_traning_camp.week_02.day3.binaryTree.preorder_traversal;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// 验证：借助 栈的先进后出特性 和 单开头特性，可以实现 对二叉树的前序遍历
public class Solution_preorderTraversal_of_tree_via_iteration {
    public static void main(String[] args) {
        // 准备一个二叉树对象
        TreeNode rootNode = constructBinaryTree();

        List<Integer> preorderNodeSequence = preorderTraversal(rootNode);

        print(preorderNodeSequence);

    }

    private static void print(List<Integer> nodeSequence) {
        for (int currentSpot = 0; currentSpot < nodeSequence.size(); currentSpot++) {
            System.out.print(nodeSequence.get(currentSpot) + ",");
        }
    }

    private static TreeNode constructBinaryTree() {
        TreeNode root = new TreeNode(1);
        TreeNode subLeft = new TreeNode(2);
        TreeNode subRight = new TreeNode(3);

        root.left = subLeft;
        root.right = subRight;

        subLeft.left = new TreeNode(4);
        subLeft.right = new TreeNode(5);

        subRight.left = new TreeNode(6);
        subRight.right = new TreeNode(7);

        return root;
    }

    private static List<Integer> preorderTraversal(TreeNode rootNode) {
        // 这里其实是把“链表”用作为一个栈 - 约束：栈口元素 就是 前序遍历序列中的当前元素
        Deque<TreeNode> nodeStack = new LinkedList<>();
        List<Integer> preorderNodeValueSequence = new LinkedList<>();

        // #0 先 入栈 根结点
        nodeStack.addLast(rootNode);
        while (!nodeStack.isEmpty()) {
            // #1 获取并移除 栈口元素
            TreeNode currentNode = nodeStack.pollLast();
            // 把栈口元素 添加到 前序遍历的结果序列中 [处理“栈口元素”]
            preorderNodeValueSequence.add(currentNode.val);

            // #2 根据二叉树的结构 来 继续依次入栈 当前结点的右子节点 & 左子节点
            // 🐖 这里是 先入栈右子节点的，这样 左子节点就会在栈口位置 - 处理顺序才能够满足前序序列的约束
            if (currentNode.right != null) {
                // 入栈 右子节点
                nodeStack.addLast(currentNode.right);
            }

            if (currentNode.left != null) {
                // 入栈 左子结点
                nodeStack.addLast(currentNode.left);
            }
        }

        return preorderNodeValueSequence;
    }
}

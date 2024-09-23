package com.henry.leetcode_traning_camp.week_02.day3.binaryTree.preorder_traversal;

import java.util.ArrayList;
import java.util.List;

// 验证：可以使用递归的手段 来 实现对二叉树中结点的前序遍历
// 前序遍历：对于原始树中的任何子树，其中结点在结果序列中的次序 满足“根-左-右”的顺序；
public class Solution_preorderTraversal_of_tree_via_recursion {
    public static void main(String[] args) {
        // 准备一个二叉树对象
        TreeNode root = constructABinaryTree();

        List<Integer> nodesInPreOrder = preorderTraversal(root);

        print(nodesInPreOrder);
    }

    private static void print(List<Integer> res) {
        for (int i = 0; i < res.size(); i++) {
            System.out.print(res.get(i) + ",");
        }
    }

    private static TreeNode constructABinaryTree() {
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

    private static List<Integer> preorderNodeSequence = new ArrayList<>();

    // // 递归方法的作用：把指定的二叉树中的结点，以前序的规则 添加到列表中
    private static List<Integer> preorderTraversal(TreeNode currentRootNode) {
        // 如果 当前结点是nil结点，说明已经遍历到了二叉树的底部 无法继续深入，则：
        if(currentRootNode == null) {
            // 返回当前的 结点序列 给上一级调用
            return preorderNodeSequence;
        }

        // 向序列中添加 当前结点的value
        preorderNodeSequence.add(currentRootNode.val);
        // 对于左子树，把其中的结点 以前序的规则 添加到列表中
        preorderTraversal(currentRootNode.left);
        // 对于右子树，把其中的结点 以前序的规则 添加到列表中
        preorderTraversal(currentRootNode.right);

        // 返回最终的结点序列
        return preorderNodeSequence;
    }
}

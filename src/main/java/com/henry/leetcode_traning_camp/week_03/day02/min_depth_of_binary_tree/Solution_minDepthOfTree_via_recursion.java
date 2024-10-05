package com.henry.leetcode_traning_camp.week_03.day02.min_depth_of_binary_tree;

import com.henry.leetcode_traning_camp.week_03.day02.TreeNode;

// 验证：可以通过 递归手段 + 对左右子树分类讨论的做法 来 求出一棵二叉树的最小深度
public class Solution_minDepthOfTree_via_recursion {
    public static void main(String[] args) {
        // 创建一个二叉树对象
        TreeNode rootOfBinaryTree = constructABinaryTree();

        int minDepthOfTree = minDepthOf(rootOfBinaryTree);
        System.out.println("当前二叉树的最小深度为： " + minDepthOfTree);
    }

    private static TreeNode constructABinaryTree() {
        TreeNode root = new TreeNode(3);

        TreeNode left_child = new TreeNode(9);
        TreeNode right_child = new TreeNode(20);

        root.left = left_child;
        root.right = right_child;

        TreeNode left_of_right_child = new TreeNode(15);
        TreeNode right_of_right_child = new TreeNode(7);
        right_child.left = left_of_right_child;
        right_child.right = right_of_right_child;

        return root;
    }

    /**
     * 递归方法的作用：计算出 指定的二叉树的最小深度
     *
     * @param currentRootNode
     * @return
     */
    private static int minDepthOf(TreeNode currentRootNode) {
        // #0 递归的终结条件
        // 如果当前结点为nil结点，说明路径执行到了叶子结点，则：
        if (currentRootNode == null) {
            // 返回当前子树的深度0 到上一级调用
            return 0;
        }

        // 本级递归要做的事情：使用子树的最小高度 来 计算出 当前树的最小高度，并返回
        // 🐖 计算子树的最小高度时，需要根据子树的情况 来 分类讨论👇
        // 子问题的解怎么能帮助解决原始问题? 子树的最小高度 + 1 = 原始树的最小高度
        // 特殊情况① 如果左子树为空，说明 二叉树的最小高度由右子树提供，则：
        if (currentRootNode.left == null) {
            // 计算出 二叉树的最小高度，并返回
            return minDepthOf(currentRootNode.right) + 1;
        }

        // 特殊情况② 如果右子树为空，说明 二叉树的最小高度由左子树提供，则：
        if (currentRootNode.right == null) {
            // 计算出 二叉树的最小高度，并返回
            return minDepthOf(currentRootNode.left) + 1;
        }

        // 对于一般情况，计算出二叉树的最小高度 并返回
        return Math.min(minDepthOf(currentRootNode.left), minDepthOf(currentRootNode.right)) + 1;
    }
}

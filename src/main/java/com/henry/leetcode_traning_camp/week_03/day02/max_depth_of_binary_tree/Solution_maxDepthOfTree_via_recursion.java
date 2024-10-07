package com.henry.leetcode_traning_camp.week_03.day02.max_depth_of_binary_tree;


import com.henry.leetcode_traning_camp.week_03.day02.TreeNode;

// 验证：可以使用递归的方式 max(maxDepth(left), maxDepth(right)) + 1 来 求出二叉树的最大深度；
// 🐖 处理二叉树相关的任务时，二叉树中结点的遍历方式 是重要的；
public class Solution_maxDepthOfTree_via_recursion {
    public static void main(String[] args) {
        // 创建一个二叉树对象
        TreeNode rootNodeOfTree = constructABinaryTree();
        int maxDepthOfTree = maxDepthOf(rootNodeOfTree);
        System.out.println("当前二叉树的最大深度为： " + maxDepthOfTree);
    }

    private static TreeNode constructABinaryTree() {
        TreeNode root = new TreeNode(1);

        TreeNode left_child = new TreeNode(2);
        TreeNode right_child = new TreeNode(3);

        root.left = left_child;
        root.right = right_child;

        TreeNode left_of_right_child = new TreeNode(4);
        TreeNode right_of_right_child = new TreeNode(5);

        right_child.left = left_of_right_child;
        right_child.right = right_of_right_child;

        right_of_right_child.right = new TreeNode(6);
        return root;
    }

    /**
     * 递归方法的作用：计算出 指定的二叉树的最大深度
     * 二叉树的遍历方式：后序遍历 - 左-右-根
     * @param currentRootNode
     * @return
     */
    private static int maxDepthOf(TreeNode currentRootNode) {
        // #0 递归终结条件
        // 如果 当前结点为nil结点，说明路径执行到nil结点，则：
        if (currentRootNode == null) {
            // 返回 当前子树的深度0 给上一级调用
            return 0;
        }

        // #1 本级递归需要做的事情(假设方法功能已经完整实现)：取左子树与右子树深度中的较大者，然后加上根结点（深度+1）
        return Math.max(maxDepthOf(currentRootNode.left), maxDepthOf(currentRootNode.right)) + 1;
    }
}

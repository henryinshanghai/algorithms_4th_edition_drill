package com.henry.leetcode_traning_camp.week_03.day02;

public class Solution_maxDepthOfTree_02_recursion {
    public static void main(String[] args) {
        // 创建一个二叉树对象
        TreeNode root = new TreeNode(3);

        TreeNode left_child = new TreeNode(9);
        TreeNode right_child = new TreeNode(20);

        root.left = left_child;
        root.right = right_child;

        TreeNode left_of_right_child = new TreeNode(15);
        TreeNode right_of_right_child = new TreeNode(7);
        right_child.left = left_of_right_child;
        right_child.right = right_of_right_child;

        int depth = maxDepth(root);

        System.out.println("当前二叉树的最大深度为： " + depth);
    }

    private static int maxDepth(TreeNode root) {
        if (root == null) return 0;

        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }
}

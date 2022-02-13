package com.henry.leetcode_traning_camp.week_03.day02;

public class Solution_minDepthOfTree_03_method01_recursion {
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

        int depth = minDepth(root);

        System.out.println("当前二叉树的最小深度为： " + depth);
    }

    private static int minDepth(TreeNode root) {
        if (root == null) return 0;

        if (root.left == null) {
            return minDepth(root.right) + 1;
        }

        if (root.right == null) {
            return minDepth(root.left) + 1;
        }

        return Math.min(minDepth(root.left), minDepth(root.right)) + 1;
    }
}

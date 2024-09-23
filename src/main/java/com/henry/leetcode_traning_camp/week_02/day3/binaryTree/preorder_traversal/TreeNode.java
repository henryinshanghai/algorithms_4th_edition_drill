package com.henry.leetcode_traning_camp.week_02.day3.binaryTree.preorder_traversal;

class TreeNode {
    int val; // 当前结点所包含的value
    TreeNode left; // 左子节点
    TreeNode right; // 右子节点

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

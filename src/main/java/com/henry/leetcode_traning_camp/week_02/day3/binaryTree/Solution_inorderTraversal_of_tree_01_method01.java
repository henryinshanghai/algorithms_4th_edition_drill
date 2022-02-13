package com.henry.leetcode_traning_camp.week_02.day3.binaryTree;

import java.util.ArrayList;
import java.util.List;

public class Solution_inorderTraversal_of_tree_01_method01 {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        TreeNode subRight = new TreeNode(2);

        root.left = null;
        root.right = subRight;

        subRight.left = new TreeNode(3);
        subRight.right = null;

        List<Integer> res = inorderTraversal(root);

        System.out.print("[");
        for (int i = 0; i < res.size(); i++) {
            System.out.print(res.get(i) + ",");
        }
        System.out.print("]");
    }

    public static List<Integer> res = new ArrayList<>();

    private static List<Integer> inorderTraversal(TreeNode root) {
        // 跳出递归的条件
        if (root == null) {
            return res;
        }

        // 本级递归需要做的事情
        inorderTraversal(root.left);
        res.add(root.val);
        inorderTraversal(root.right);

        return res;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}

    TreeNode(int val) { this.val = val; }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

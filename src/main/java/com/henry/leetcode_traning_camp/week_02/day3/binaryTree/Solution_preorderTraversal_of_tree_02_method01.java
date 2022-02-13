package com.henry.leetcode_traning_camp.week_02.day3.binaryTree;

import java.util.ArrayList;
import java.util.List;

public class Solution_preorderTraversal_of_tree_02_method01 {
    public static void main(String[] args) {
        // 准备一个二叉树对象
        TreeNode root = new TreeNode(1);
        TreeNode subRight = new TreeNode(2);

        root.left = null;
        root.right = subRight;

        subRight.left = new TreeNode(3);
        subRight.right = null;

        List<Integer> res = preorderTraversal(root);

        for (int i = 0; i < res.size(); i++) {
            System.out.print(res.get(i) + ",");
        }
    }

    private static List<Integer> res = new ArrayList<>();

    private static List<Integer> preorderTraversal(TreeNode root) {
        if(root == null) return res;

        res.add(root.val);
        preorderTraversal(root.left);
        preorderTraversal(root.right);

        return res;
    }
}

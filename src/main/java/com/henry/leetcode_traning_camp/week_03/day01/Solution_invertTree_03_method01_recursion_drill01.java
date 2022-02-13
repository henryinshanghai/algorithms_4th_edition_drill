package com.henry.leetcode_traning_camp.week_03.day01;

import java.util.List;
import java.util.Stack;

import static com.henry.leetcode_traning_camp.week_03.day01.Solution_invertTree_03_method01_recursion.printLists;
import static com.henry.leetcode_traning_camp.week_03.day01.Solution_invertTree_03_method01_recursion.traverseTreeViaLevelOrder;

public class Solution_invertTree_03_method01_recursion_drill01 {
    public static void main(String[] args) {

        // 构建一个二叉树对象
        TreeNode root = new TreeNode(4);
        TreeNode sub_left = new TreeNode(2);
        TreeNode sub_right = new TreeNode(7);

        root.left = sub_left;
        root.right = sub_right;

        TreeNode sub_left_left = new TreeNode(1);
        TreeNode sub_left_right = new TreeNode(3);
        sub_left.left = sub_left_left;
        sub_left.right = sub_left_right;

        TreeNode sub_right_left = new TreeNode(6);
        TreeNode sub_right_right = new TreeNode(9);
        sub_right.left = sub_right_left;
        sub_right.right = sub_right_right;

        // 层序遍历二叉树对象的当前节点列表
        List<List<Integer>> res = traverseTreeViaLevelOrder(root);

        printLists(res);

        System.out.println("~~~~~~翻转后~~~~~~~");

        // 翻转二叉树对象
        TreeNode revert_root = invertTree(root); // 使用递归

        // 获取到反转后的二叉树的层序遍历结果
        List<List<Integer>> lists_after_revert = traverseTreeViaLevelOrder(revert_root);

        // 反转后的二叉树对象的节点列表
        printLists(lists_after_revert);

        new Stack<>();
    }

    private static TreeNode invertTree(TreeNode root) {
        if(root == null || (root.left == null && root.right == null)) return root;

        TreeNode new_left = invertTree(root.right);
        TreeNode new_right = invertTree(root.left);

        root.left = new_left;
        root.right = new_right;

        return root;
    }
}

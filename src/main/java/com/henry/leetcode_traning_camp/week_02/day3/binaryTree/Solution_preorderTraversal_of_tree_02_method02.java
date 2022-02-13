package com.henry.leetcode_traning_camp.week_02.day3.binaryTree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_preorderTraversal_of_tree_02_method02 {
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

    private static List<Integer> preorderTraversal(TreeNode root) {
        // 这里其实使用的不是一个典型队列，而是双端队列的特性————能够在队尾进行插入与删除操作
        Deque<TreeNode> queue = new LinkedList<>();
        List<Integer> res = new LinkedList<>();

        queue.addLast(root);
        while (!queue.isEmpty()) {
            TreeNode curr = queue.pollLast();
            res.add(curr.val);

            if (curr.right != null) {
                queue.addLast(curr.right);
            }

            if (curr.left != null) {
                queue.addLast(curr.left);
            }
        }

        return res;
    }
}

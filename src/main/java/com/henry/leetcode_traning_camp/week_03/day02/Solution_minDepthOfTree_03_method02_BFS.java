package com.henry.leetcode_traning_camp.week_03.day02;

import java.util.Deque;
import java.util.LinkedList;

public class Solution_minDepthOfTree_03_method02_BFS {
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

    // 在层序遍历中，如果在第x层遇到了根节点，说明这时候当前层数就是树的最小高度。则:返回当前层数即可
    private static int minDepth(TreeNode root) {

        // 1 准备一个队列
        Deque<TreeNode> deque = new LinkedList<>();

        // 2 把当前节点入列
        deque.addLast(root);

        // 3 准备一个循环 在循环中,遍历当前层中的所有节点
        int level = 0;
        while (!deque.isEmpty()) {
            level++;

            // 遍历当前层中的所有节点
            int size = deque.size();
            for (int i = 0; i < size; i++) {
                TreeNode curr_node = deque.removeFirst();

                if (curr_node == null) continue;

                // 如果在当前层中遇到了叶子节点，说明这时已经找到了二叉树的最小深度。则：跳出循环，返回当前层数作为二叉树的深度
                if (curr_node.left == null && curr_node.right == null) {
                    return level;
                }

                // 入队当前节点的子节点
                deque.addLast(curr_node.left);
                deque.addLast(curr_node.right);
            }
        }

        return -1;
    }
}

package com.henry.leetcode_traning_camp.week_04.day02;

import java.util.*;

public class Solution_levelOrderOfTree_01_method01_queue_drill02 {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);

        TreeNode leftChild = new TreeNode(9);
        TreeNode rightChild = new TreeNode(20);

        root.left = leftChild;
        root.right = rightChild;

        rightChild.left = new TreeNode(15);
        rightChild.right = new TreeNode(7);

        List<List<Integer>> res = levelOrder(root); //
        
        System.out.println(res);

    }

    private static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();

        if (root == null) {
            return res;
        }

        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> levelList = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                if (queue.peek().left != null) queue.add(queue.peek().left);
                if (queue.peek().right != null) queue.add(queue.peek().right);

                levelList.add(queue.poll().val);
            }

            res.add(levelList);
        }

        return res;
    }
}

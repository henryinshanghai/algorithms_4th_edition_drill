package com.henry.leetcode_traning_camp.week_04.day03;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution_largestValues_01_method01_bfs {
    public static void main(String[] args) {
        // 创建一个树对象
        TreeNode root = new TreeNode(1);
        TreeNode left_son = new TreeNode(3);
        TreeNode right_son = new TreeNode(2);

        root.left = left_son;
        root.right = right_son;

        TreeNode lefty_of_leftSon = new TreeNode(5);
        TreeNode righty_of_leftSon = new TreeNode(3);

        left_son.left = lefty_of_leftSon;
        left_son.right = righty_of_leftSon;

        TreeNode righty_of_rightSon = new TreeNode(9);
        right_son.right = righty_of_rightSon;

        List<Integer> res = largestValues(root);
        System.out.println(res);
    }

    private static List<Integer> largestValues(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();

        if (root == null) {
            return res;
        }

        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            int maxOfLevel = Integer.MIN_VALUE;

            for (int i = 0; i < levelSize; i++) {
                if (queue.peek().left != null) queue.add(queue.peek().left);
                if (queue.peek().right != null) queue.add(queue.peek().right);

                maxOfLevel = Math.max(maxOfLevel, queue.poll().val);
            }

            res.add(maxOfLevel);
        }

        return res;
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

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
package com.henry.leetcode_traning_camp.week_03.day01;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_invertTree_03_method01_recursion {
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

        // 遍历二叉树对象的当前节点列表
        List<List<Integer>> res = traverseTreeViaLevelOrder(root);

        printLists(res);

        // 翻转二叉树对象
        TreeNode revert_root = invertTree(root); // 使用递归

        // 获取到反转后的二叉树的层序遍历结果
        List<List<Integer>> lists_after_revert = traverseTreeViaLevelOrder(revert_root);

        // 反转后的二叉树对象的节点列表
        printLists(lists_after_revert);
    }

    /**
     * 对指定的二叉树进行翻转操作
     * @param root
     * @return
     */
    private static TreeNode invertTree(TreeNode root) {
        if (root == null || (root.left == null && root.right == null)) return root;

        TreeNode new_left = invertTree(root.right);
        TreeNode new_right = invertTree(root.left);

        root.left = new_left;
        root.right = new_right;

        return root;
    }

    public static void printLists(List<List<Integer>> res) {
        int level = res.size();
        for (int i = 0; i < level; i++) {
            List<Integer> curr_level = res.get(i);

            for (int j = 0; j < curr_level.size(); j++) {
                System.out.print(curr_level.get(j) + " -> ");
            }

            System.out.println();
        }
    }

    public static List<List<Integer>> traverseTreeViaLevelOrder(TreeNode root) {
        // 准备一个队列与一个列表
        Deque<TreeNode> temp = new LinkedList<>();
        LinkedList<List<Integer>> res = new LinkedList<>();

        // 把当前节点入队
        temp.addLast(root);

        // 准备一个循环
        while (!temp.isEmpty()) {
            List<Integer> currLevel = new LinkedList<>();
            int node_num_of_currLevel = temp.size();

            // 把当前层的节点添加到列表中
            for (int i = 0; i < node_num_of_currLevel; i++) {
                // 3-1 获取当前队首元素
                TreeNode curr_node = temp.pollFirst();
                // 3-2 对取出的队首元素进行判空
                if (curr_node == null) {
                    continue; // 跳过空元素
                }

                // 3-3 把当前元素的值添加到列表中
                currLevel.add(curr_node.val);

                // 3-4 把当前元素的子节点从左到右添加到队列的尾部
                temp.addLast(curr_node.left);
                temp.addLast(curr_node.right);
            }

            res.add(currLevel);
        }

        return res;
    }
}

class TreeNode{
    int val;
    TreeNode left;
    TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }
}

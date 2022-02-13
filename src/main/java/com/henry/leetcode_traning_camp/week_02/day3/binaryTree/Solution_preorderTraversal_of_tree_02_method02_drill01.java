package com.henry.leetcode_traning_camp.week_02.day3.binaryTree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_preorderTraversal_of_tree_02_method02_drill01 {
    public static void main(String[] args) {


        // 准备一个二叉树对象
        TreeNode root = new TreeNode(1);
        TreeNode subRight = new TreeNode(2);

        root.left = null;
        root.right = subRight;

        subRight.left = new TreeNode(3);
        subRight.right = null;

        List<Integer> res = preorderTraversal(root);

        for (Integer re : res) {
            System.out.print(re + ",");
        }


    }

    // 以前序遍历规则来遍历树中的节点，并把节点的值存储到列表中
    private static List<Integer> preorderTraversal(TreeNode root) {
        // 手段：使用一个双端队列
        Deque<TreeNode> deque = new LinkedList<>();
        List<Integer> res = new LinkedList<>(); // LinkedList这个类型有点意思 还有多态的用法？

        // 准备一个循环   遍历节点并存储节点
        deque.addLast(root);
        while (!deque.isEmpty()) { // WHY this would work?
            TreeNode currNode = deque.pollLast();
            res.add(currNode.val);

            // 在队列末尾添加右子节点
            if (currNode.right != null) {
                deque.addLast(currNode.right); // 这里添加的是当前节点的右子节点(注意变量的正确性)
            }

            if (currNode.left != null) {
                deque.addLast(currNode.left);
            }
        }

        return res;
    }
}

package com.henry.leetcode_traning_camp.week_02.day3.binaryTree;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_inorderTraversal_of_tree_01_method02 {
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

    private static List<Integer> inorderTraversal(TreeNode root) {
        // 1 准备必要的局部变量
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Deque<TreeNode> stack = new LinkedList<>();

        // 2 准备一个循环 在循环中，以特定次序遍历树中的每一个节点，并将之添加到列表res中
        while (root != null || !stack.isEmpty()) {
            // 2-1 把当前节点的左子节点逐一入栈
            while (root != null) {
                stack.push(root);
                root = root.left;
            }

            // 2-2 当root == null时，出栈栈顶元素，并更新当前节点
            root = stack.pop();
            // 记录当前节点
            res.add(root.val);

            // 2-3 更新当前节点为“当前节点的右子节点”
            root = root.right;
        }

        return res;
    }
}

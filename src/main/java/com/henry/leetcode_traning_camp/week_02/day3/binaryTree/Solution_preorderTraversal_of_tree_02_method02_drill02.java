package com.henry.leetcode_traning_camp.week_02.day3.binaryTree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_preorderTraversal_of_tree_02_method02_drill02 {
    public static void main(String[] args) {
        // 按照Tree的图形来创建一个TreeNode对象
        TreeNode root = new TreeNode(1);
        TreeNode subRight = new TreeNode(2);

        root.left = null;
        root.right = subRight;

        subRight.left = new TreeNode(3);
        subRight.right = null;

        // 调用xxx方法，把树中所有节点添加到列表中
        List<Integer> res = preorderTraversal(root);

        for (Integer re : res) {
            System.out.print(re + ",");
        }
    }

    private static List<Integer> preorderTraversal(TreeNode root) {
        // 准备一个deque    作用：配合循环体，以特定的顺序处理树中的节点
        Deque<TreeNode> deque =  new LinkedList<>();
        List<Integer> res = new LinkedList<>();

        // 把根节点入队
        deque.addLast(root);

        // 准备一个循环   利用deque的API来实现“以特定次序存储树中的元素”
        while (!deque.isEmpty()) {
            TreeNode currNode = deque.pollLast();
            // 由于这里的res变量是List<>类型的，因此这里编写代码时就只能使用List<>类型提供的API————这会使得代码更容易阅读
            res.add(currNode.val);

            if (currNode.right != null) {
                deque.addLast(currNode.right);
            }

            if (currNode.left != null) {
                deque.addLast(currNode.left);
            }
        }

        return res;
    }
}

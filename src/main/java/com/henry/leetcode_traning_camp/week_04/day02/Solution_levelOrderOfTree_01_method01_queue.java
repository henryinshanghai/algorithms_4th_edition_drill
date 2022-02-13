package com.henry.leetcode_traning_camp.week_04.day02;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution_levelOrderOfTree_01_method01_queue {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);

        TreeNode leftChild = new TreeNode(9);
        TreeNode rightChild = new TreeNode(20);

        root.left = leftChild;
        root.right = rightChild;

        rightChild.left = new TreeNode(15);
        rightChild.right = new TreeNode(7);

        List<List<Integer>> res = levelOrder(root);
        System.out.println(res);
    }

    private static List<List<Integer>> levelOrder(TreeNode root) {
        // 准备一个队列   用于分层地处理二叉树中的节点
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        // 准备一个元素为列表的列表     用于分层地存储每一层的节点
        List<List<Integer>> wrapList = new LinkedList<List<Integer>>();

        // 鲁棒性代码
        if(root == null) return wrapList;

        // 1 把根节点入队
        queue.offer(root);

        // 2 准备一个循环     在循环中，逐层处理树中的节点
        while(!queue.isEmpty()){
            // 2-1 获取到当前层的节点个数  aka 队列的size 用于处理当前层的所有节点
            int levelNum = queue.size();

            // 2-2 准备一个列表 用于存储当前层的所有节点
            List<Integer> subList = new LinkedList<Integer>();

            // 2-3 遍历当前层的所有节点   手段：从队列中取出当前层的元素
            for(int i=0; i<levelNum; i++) {
                // 在取出当前层的节点之前，把该节点的左右子节点添加到队列中（如果不为空的话）
                if(queue.peek().left != null) queue.offer(queue.peek().left);
                if(queue.peek().right != null) queue.offer(queue.peek().right);
                subList.add(queue.poll().val);
            }

            // for循环结束后，当前层的节点列表就已经完成了
            // 把它添加的列表的列表中
            wrapList.add(subList);
        }

        // 返回列表的列表
        return wrapList;
    }
}

// 内部类
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

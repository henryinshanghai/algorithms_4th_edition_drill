package com.henry.leetcode_traning_camp.week_03.day02.min_depth_of_binary_tree;

import com.henry.leetcode_traning_camp.week_03.day02.TreeNode;

import java.util.Deque;
import java.util.LinkedList;

// 验证：可以 借助 二叉树的层序遍历操作 + 层序遍历中 遇到“叶子节点”时，即可确定树的最小深度的事实 来 得到二叉树的最小深度
// 🐖 二叉树的层序遍历 使用的是 BFS的模板 - while循环 + 一个标准队列
public class Solution_minDepthOfTree_via_BFS {
    public static void main(String[] args) {
        // 创建一个二叉树对象
        TreeNode rootNodeOfTree = constructABinaryTree();
        int minDepth = minDepthOf(rootNodeOfTree);
        System.out.println("当前二叉树的最小深度为： " + minDepth);
    }

    private static TreeNode constructABinaryTree() {
        TreeNode root = new TreeNode(3);

        TreeNode left_child = new TreeNode(9);
        TreeNode right_child = new TreeNode(20);

        root.left = left_child;
        root.right = right_child;

        TreeNode left_of_right_child = new TreeNode(15);
        TreeNode right_of_right_child = new TreeNode(7);
        right_child.left = left_of_right_child;
        right_child.right = right_of_right_child;

        return root;
    }

    // 在层序遍历中，如果在第x层遇到了“叶子节点”，说明“此时的当前层数”就是树的最小高度。则: 返回当前层数即可
    private static int minDepthOf(TreeNode root) {
        // #1 准备一个元素为结点的双端队列
        Deque<TreeNode> nodeQueueOnCurrentLevel = new LinkedList<>();

        // #2 把 当前根节点 添加到队列的末端
        nodeQueueOnCurrentLevel.addLast(root);

        // #3 准备一个循环 在循环中,逐层遍历 当前层中的所有节点 来 找到 “能够确定树的最小深度”的情形
        int currentLevel = 0;
        while (!nodeQueueOnCurrentLevel.isEmpty()) {
            // 计算当前层 手段：当前层 与 第N轮 的关系
            currentLevel++;

            // 对于当前层：#1 遍历当前层的结点；#2 把当前层结点的下一层的子结点 添加到队列中
            int nodesAmountOnCurrentLevel = nodeQueueOnCurrentLevel.size();
            for (int currentNodeSpot = 0; currentNodeSpot < nodesAmountOnCurrentLevel; currentNodeSpot++) {
                // ① 获取并移除 双端队列的队首元素
                TreeNode curr_node = nodeQueueOnCurrentLevel.removeFirst();
                // 如果当前结点为nil结点，说明 还不能确定 二叉树的最小深度(斜链表的情况)，则：
                if (curr_node == null) {
                    // 跳过本轮循环
                    continue;
                }

                // ② 如果当前结点是一个 叶子节点，说明这时已经能够得到 二叉树的最小深度。则：
                if (isLeafNode(curr_node)) {
                    // 直接返回当前层数 作为二叉树的最小深度
                    return currentLevel;
                }

                // ③ 入队当前节点的左右子节点 来 作为下一层的结点
                nodeQueueOnCurrentLevel.addLast(curr_node.left);
                nodeQueueOnCurrentLevel.addLast(curr_node.right);
            }
        }

        return -1;
    }

    private static boolean isLeafNode(TreeNode curr_node) {
        return curr_node.left == null && curr_node.right == null;
    }
}

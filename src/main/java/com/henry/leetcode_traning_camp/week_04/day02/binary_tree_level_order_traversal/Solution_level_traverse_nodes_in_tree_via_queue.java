package com.henry.leetcode_traning_camp.week_04.day02.binary_tree_level_order_traversal;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// 验证：可以使用 队列的先进先出 特性 + 二叉树结点间的关联特性 来 得到二叉树中结点的层序遍历序列
// 特征：对于 每一层的结点，使用queue.size()来得到 当前层的结点数量
public class Solution_level_traverse_nodes_in_tree_via_queue {
    public static void main(String[] args) {
        TreeNode rootNodeOfTree = constructATree();

        List<List<Integer>> nodesValueSequenceInLevelOrder = levelOrder(rootNodeOfTree);

        System.out.println(nodesValueSequenceInLevelOrder);
    }

    /*
        1
      2   3
    4  5 6  7
     */
    private static TreeNode constructATree() {
        TreeNode root = new TreeNode(1);

        TreeNode leftChild = new TreeNode(2);
        TreeNode rightChild = new TreeNode(3);

        root.left = leftChild;
        root.right = rightChild;

        leftChild.left = new TreeNode(4);
        leftChild.right = new TreeNode(5);

        rightChild.left = new TreeNode(6);
        rightChild.right = new TreeNode(7);

        return root;
    }

    private static List<List<Integer>> levelOrder(TreeNode rootNodeOfTree) {
        // 准备一个队列   用于分层地处理二叉树中的节点
        Queue<TreeNode> nodeSimpleQueue = new LinkedList<TreeNode>();
        // 准备一个元素为列表的列表     用于分层地存储每一层的节点
        List<List<Integer>> levelNodesList = new LinkedList<List<Integer>>();

        // 健壮性代码
        if (rootNodeOfTree == null) return levelNodesList;

        // #1 把根节点入队    🐖 offer()是更好的add()方法
        nodeSimpleQueue.offer(rootNodeOfTree);

        // #2 准备一个循环     在循环中，逐层处理树中的节点
        while (!nodeSimpleQueue.isEmpty()) {
            // #2-1 获取到“当前层中的节点个数”(队列的size),用于处理当前层的所有节点
            int nodesAmountOnCurrentLevel = nodeSimpleQueue.size();

            // #2-2 准备一个列表 用于存储当前层的所有节点
            List<Integer> currentLevelNodes = new LinkedList<Integer>();

            // #2-3 对于当前层的每一个结点...   手段：从队列中逐个取出当前层的元素
            for (int currentNodeCursor = 0; currentNodeCursor < nodesAmountOnCurrentLevel; currentNodeCursor++) {
                // Ⅰ 在取出当前层的节点之前，把该结点的左右子结点 添加到队列中（如果不为空的话） 🐖 peek()是获取而不移除队首元素的方法
                if (nodeSimpleQueue.peek().left != null) nodeSimpleQueue.offer(nodeSimpleQueue.peek().left);
                if (nodeSimpleQueue.peek().right != null) nodeSimpleQueue.offer(nodeSimpleQueue.peek().right);

                // Ⅱ 移除以得到队列的队首元素，并将之添加到 当前层的结点列表 集合中   🐖 poll()是获取并移除队首元素的方法
                currentLevelNodes.add(nodeSimpleQueue.poll().val);
            }

            // for循环结束后，当前层的节点列表就已经完成了。则：
            // 把“当前层的结点列表”添加到 “层结点集合结果集” 中
            levelNodesList.add(currentLevelNodes);
        }

        // 返回最终的结果集合
        return levelNodesList;
    }
}

// 内部类
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}

package com.henry.leetcode_traning_camp.week_04.day03.max_item_in_each_level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// 验证：可以使用 BFS + for循环求最值 的方式 来 求取 二叉树每一层结点的maxValue
// 🐖 简单队列 非常适合用来实现BFS的步骤
// Java中实现简单队列：LinkedList();
// 简单队列的常用API：#1 查看队首元素 peek(); #2 获取并移除队首元素 poll(); #3 追加元素 offer();
public class Solution_max_value_of_each_level_via_bfs {
    public static void main(String[] args) {
        // 创建一个树对象
        TreeNode rootNodeOfTree = constructABinaryTree();
        List<Integer> maxValueList = largestItemOnEachLevelOf(rootNodeOfTree);
        System.out.println(maxValueList);
    }

    /*
        1
      2   3
    4 5  6 7
     */
    private static TreeNode constructABinaryTree() {
        // 根节点
        TreeNode root = new TreeNode(1);
        // 左右子结点
        TreeNode left_son = new TreeNode(2);
        TreeNode right_son = new TreeNode(3);

        root.left = left_son;
        root.right = right_son;

        // 左子节点的左右子节点
        TreeNode lefty_of_leftSon = new TreeNode(4);
        TreeNode righty_of_leftSon = new TreeNode(5);
        left_son.left = lefty_of_leftSon;
        left_son.right = righty_of_leftSon;

        // 右子节点的左右子结点
        TreeNode lefty_of_rightSon = new TreeNode(6);
        TreeNode righty_of_rightSon = new TreeNode(7);
        right_son.right = righty_of_rightSon;
        right_son.left = lefty_of_rightSon;

        return root;
    }

    private static List<Integer> largestItemOnEachLevelOf(TreeNode rootNode) {
        // i 准备一个maxValueList - 用于返回maxValue的结果
        List<Integer> currentLevelToItsMaxItem = new ArrayList<>();
        // 健壮性代码
        if (rootNode == null) {
            return currentLevelToItsMaxItem;
        }

        // ii 准备一个 基本队列  - 用于动态地收集 二叉树中当前层的结点
        Queue<TreeNode> nodesOnCurrentLevel = new LinkedList<>();
        nodesOnCurrentLevel.add(rootNode);

        // iii 得到每一层结点的maxValue，并 把这个maxValue 收集到列表中
        while (!nodesOnCurrentLevel.isEmpty()) {
            // #1 准备一个变量 来 表示当前层 所有结点的maxValue
            int maxNodeOfCurrentLevel = Integer.MIN_VALUE;

            /* #2 准备一个循环，在循环中 ① 把下一层的结点顺序添加到队列中； ② 得到当前层中的maxValue。 */
            // 获取到 队列中 当前结点的数量 aka 二叉树中当前层的结点数量
            int currentLevelNodesAmount = nodesOnCurrentLevel.size();
            for (int currentNodeCursor = 0; currentNodeCursor < currentLevelNodesAmount; currentNodeCursor++) {
                /* ① 先 (先左后右地)到队列中 */
                // 获取队首元素(不移除)把下一层的结点顺序添加
                TreeNode peekedHeadNode = nodesOnCurrentLevel.peek();
                if (peekedHeadNode.left != null)
                    nodesOnCurrentLevel.offer(peekedHeadNode.left);
                if (peekedHeadNode.right != null)
                    nodesOnCurrentLevel.offer(peekedHeadNode.right);

                /* ② 得到当前层中的maxValue */
                // Ⅰ 获取并移除 队列当前的队首元素
                TreeNode removedHeadNode = nodesOnCurrentLevel.poll();
                // Ⅱ 尝试用它 来 更新“当前层的最大结点”变量
                maxNodeOfCurrentLevel = Math.max(maxNodeOfCurrentLevel, removedHeadNode.val);
            }

            // #3 把 当前层中的maxValue 添加到 maxValueList中
            currentLevelToItsMaxItem.add(maxNodeOfCurrentLevel);
        }

        // iv 返回结果列表
        return currentLevelToItsMaxItem;
    }
}

class TreeNode {
    int val; // 结点中的值
    TreeNode left; // 左子节点
    TreeNode right; // 右子节点

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
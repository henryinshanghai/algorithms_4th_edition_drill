package com.henry.leetcode_traning_camp.week_04.day03.max_item_in_each_level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// 验证：可以使用 BFS + for循环求最值 的方式 来 求取 二叉树每一层结点的maxValue
// 🐖 简单队列 非常适合用来实现BFS的步骤
// Java中实现简单队列：LinkedList();    简单队列的常用API：#1 查看队首元素 peek(); #2 获取并移除队首元素 poll(); #3 追加元素 offer();
public class Solution_max_value_of_each_level_via_bfs {
    public static void main(String[] args) {
        // 创建一个树对象
        TreeNode rootNodeOfTree = constructABinaryTree();
        List<Integer> maxValueList = largestValuesOnEachLevelOf(rootNodeOfTree);
        System.out.println(maxValueList);
    }

    private static TreeNode constructABinaryTree() {
        TreeNode root = new TreeNode(1);
        TreeNode left_son = new TreeNode(2);
        TreeNode right_son = new TreeNode(3);

        root.left = left_son;
        root.right = right_son;

        TreeNode lefty_of_leftSon = new TreeNode(4);
        TreeNode righty_of_leftSon = new TreeNode(5);

        left_son.left = lefty_of_leftSon;
        left_son.right = righty_of_leftSon;

        TreeNode lefty_of_rightSon = new TreeNode(6);
        TreeNode righty_of_rightSon = new TreeNode(7);
        right_son.right = righty_of_rightSon;
        right_son.left = lefty_of_rightSon;

        return root;
    }

    private static List<Integer> largestValuesOnEachLevelOf(TreeNode rootNode) {
        // i 准备一个maxValueList - 用于返回maxValue的结果
        List<Integer> maxValueList = new ArrayList<>();
        // 健壮性代码
        if (rootNode == null) {
            return maxValueList;
        }

        // ii 准备一个 基本队列  - 用于动态地收集 二叉树中当前层的结点
        Queue<TreeNode> nodeQueueOfCurrentLevel = new LinkedList<>();
        nodeQueueOfCurrentLevel.add(rootNode);

        // iii 得到每一层结点的maxValue，并 把这个maxValue 收集到列表中
        while (!nodeQueueOfCurrentLevel.isEmpty()) {
            // #1 准备一个变量 来 表示当前层 所有结点的maxValue
            int maxValueOfNodesOnCurrentLevel = Integer.MIN_VALUE;

            /* #2 准备一个循环，在循环中 ① 把下一层的结点顺序添加到队列中； ② 得到当前层中的maxValue。 */
            // 获取到 队列中 当前结点的数量 aka 二叉树中当前层的结点数量
            int currentLevelNodesAmount = nodeQueueOfCurrentLevel.size();
            for (int currentNodeCursor = 0; currentNodeCursor < currentLevelNodesAmount; currentNodeCursor++) {
                /* ① 先 把下一层的结点顺序添加(先左后右地)到队列中 */
                // 获取队首元素(不移除)
                TreeNode peekedHeadNode = nodeQueueOfCurrentLevel.peek();
                if (peekedHeadNode.left != null)
                    nodeQueueOfCurrentLevel.offer(peekedHeadNode.left);
                if (peekedHeadNode.right != null)
                    nodeQueueOfCurrentLevel.offer(peekedHeadNode.right);

                /* ② 得到当前层中的maxValue */
                // Ⅰ 获取并移除 队列当前的队首元素
                TreeNode removedHeadNode = nodeQueueOfCurrentLevel.poll();
                // Ⅱ 尝试用它 来 更新“当前层的最大结点”变量
                maxValueOfNodesOnCurrentLevel = Math.max(maxValueOfNodesOnCurrentLevel, removedHeadNode.val);
            }

            // #3 把 当前层中的maxValue 添加到 maxValueList中
            maxValueList.add(maxValueOfNodesOnCurrentLevel);
        }

        // iv 返回结果列表
        return maxValueList;
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
package com.henry.leetcode_traning_camp.week_02.day3.binaryTree.inorder_traversal;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// 验证：可以借助栈的“先进后出” 与 “单一开口”的特性 来 实现二叉树的层序遍历
// 核心步骤：#1 入栈“当前结点”及“左分支结点”； #2 出栈并处理栈口元素； #3 使用右子节点来更新当前元素
// 因为 结点的访问顺序 与 结点的处理顺序 不完全一致，所以 这里对stack的使用其实挺tricky的
public class Solution_inorder_traverse_nodes_in_tree_via_iteration {
    public static void main(String[] args) {
        TreeNode root = constructTree();

        List<Integer> nodesValueInOrderSequence = inorderTraversal(root);

        printResult(nodesValueInOrderSequence);
    }

    private static void printResult(List<Integer> nodesValueSequence) {
        System.out.print("[");
        for (int currentNodeSpot = 0; currentNodeSpot < nodesValueSequence.size(); currentNodeSpot++) {
            System.out.print(nodesValueSequence.get(currentNodeSpot) + ",");
        }
        System.out.print("]");
    }

    private static TreeNode constructTree() {
        TreeNode root = new TreeNode(1);

        TreeNode subLeft = new TreeNode(2);
        TreeNode subRight = new TreeNode(3);

        root.left = subLeft;
        root.right = subRight;

        subLeft.left = new TreeNode(4);
        subLeft.right = new TreeNode(5);

        subRight.left = new TreeNode(6);
        subRight.right = new TreeNode(7);

        return root;
    }

    private static List<Integer> inorderTraversal(TreeNode currentRootNode) {
        // 1 准备必要的局部变量
        List<Integer> inorderedNodeSequence = new ArrayList<>();

        // 如果当前结点为null，说明传入的是一个nil二叉树，则：直接返回空的结点value列表
        if (currentRootNode == null) {
            return inorderedNodeSequence;
        }

        // 对于树的一般情况
        Deque<TreeNode> nodesStack = new LinkedList<>();
        // #2 准备一个循环 在循环中，以特定次序 来遍历树中的每一个节点，并将之添加到“结点中序遍历结果序列”中
        while (currentRootNode != null || !nodesStack.isEmpty()) {
            // Ⅰ 如果当前结点不是nil结点，说明还没有执行到叶子节点，则：
            // 把 当前节点 及 其左分支上的所有结点 都入栈
            while (currentRootNode != null) {
                // ① 把当前节点入栈
                nodesStack.push(currentRootNode);
                // ② 使用 当前结点的左子节点 来 更新当前结点 [update current node*1]
                currentRootNode = currentRootNode.left; // 左
            }

            // Ⅱ 如果 当前结点==null，说明执行到了nil结点(需要把栈口元素添加到结果序列中)，则：
            // ① 出栈栈顶元素(nil结点的根结点 aka 左节点)，并 ② 用它来更新当前节点 [update current node*2]
            currentRootNode = nodesStack.pop();
            // ③ 把 当前结点的值 添加到 “中序遍历规则的结点序列”中
            inorderedNodeSequence.add(currentRootNode.val); // 中

            // Ⅲ 导航到右子节点，根据右子节点的情况（是否为nil结点） 来 决定是执行Ⅰ 还是 Ⅱ
            // 使用“当前节点的右子节点” 来 更新当前结点 [update current node*3]
            // ① 如果 右子节点为nil结点，说明 当前结点的中序遍历就已经完成了，则：出栈栈口元素(上一层的根结点)，继续处理 ①②③④
            // ② 如果 右子节点不为nil结点，说明 右子节点可能存在有左子树，则：继续向栈中push 该结点及其左分支的结点
            currentRootNode = currentRootNode.right; // 右
        }

        return inorderedNodeSequence;
    }
}

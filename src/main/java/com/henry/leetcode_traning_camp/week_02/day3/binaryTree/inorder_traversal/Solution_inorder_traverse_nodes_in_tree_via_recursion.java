package com.henry.leetcode_traning_camp.week_02.day3.binaryTree.inorder_traversal;

import java.util.ArrayList;
import java.util.List;

// 验证：可以使用递归的手段 来 实现二叉树中结点的中序遍历
// 中序遍历：在结果序列中，对于原始树中的任意子树，其结点在结果序列中的次序 都是“左-根-右”的顺序
public class Solution_inorder_traverse_nodes_in_tree_via_recursion {
    public static void main(String[] args) {
        TreeNode root = constructTree();

        List<Integer> inorderedNodeSequence = inorderTraversal(root);

        printResult(inorderedNodeSequence);
    }

    private static void printResult(List<Integer> nodeSequence) {
        System.out.print("[");
        for (int currentSpot = 0; currentSpot < nodeSequence.size(); currentSpot++) {
            Integer currentNodeValue = nodeSequence.get(currentSpot);
            System.out.print(currentNodeValue + ",");
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

    // 把结果序列 设置为 独立的成员变量
    public static List<Integer> inorderNodeSequence = new ArrayList<>();

    // 递归方法的作用：把指定的二叉树中的结点，以中序的规则 添加到列表中
    private static List<Integer> inorderTraversal(TreeNode currentRootNode) {
        // 递归终结条件 - 如果当前结点是nil结点，说明已经到达原始树的叶子节点，则：
        if (currentRootNode == null) {
            // 把 当前的结果 序列，返回给上一级调用
            return inorderNodeSequence;
        }

        /* 本级递归需要做的事情👇 */
        // 对于左子树，把其中的结点 以中序规则 添加到列表中
        inorderTraversal(currentRootNode.left);
        // 然后 把当前根结点的value 添加到列表中
        inorderNodeSequence.add(currentRootNode.val);
        // 对于右子树，把其中的结点 以中序规则 添加到列表中
        inorderTraversal(currentRootNode.right);

        // 返回 按照中序遍历规则 添加了树结点的列表
        return inorderNodeSequence;
    }
}

class TreeNode {
    int val; // 当前结点所包含的value
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

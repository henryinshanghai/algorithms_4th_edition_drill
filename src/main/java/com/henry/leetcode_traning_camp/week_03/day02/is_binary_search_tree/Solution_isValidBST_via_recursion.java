package com.henry.leetcode_traning_camp.week_03.day02.is_binary_search_tree;

import com.henry.leetcode_traning_camp.week_03.day02.TreeNode;

// 验证：可以使用 递归的方式 + BST前序遍历的结果序列必然是一个升序序列的特性 来 判断一个二叉树是不是BST
// 原理：BST所要求的约束 - #1 根结点与左右子结点之间的大小关系； #2 左右子树也要求是BST；
public class Solution_isValidBST_via_recursion {

    public static void main(String[] args) {
//        TreeNode rootNode = constructANonBST(); // false
//        TreeNode rootNode = constructABalancedBST(); // true
        TreeNode rootNode = constructANonBalancedBST(); // true

        if (isValidBST(rootNode)) {
            System.out.println("this is a valid BST");
        } else {
            System.out.println("this is not a valid BST");
        }
    }

    private static TreeNode constructABalancedBST() {
        TreeNode root = new TreeNode(4);

        TreeNode left_child = new TreeNode(2);
        TreeNode right_child = new TreeNode(6);

        left_child.left = new TreeNode(1);
        left_child.right = new TreeNode(3);

        right_child.left = new TreeNode(5);
        right_child.right = new TreeNode(7);

        root.left = left_child;
        root.right = right_child;

        return root;
    }

    private static TreeNode constructANonBST() {
        TreeNode root = new TreeNode(1);

        TreeNode left_child = new TreeNode(2);
        TreeNode right_child = new TreeNode(3);

        root.left = left_child;
        root.right = right_child;

        TreeNode left_of_right_child = new TreeNode(4);
        TreeNode right_of_right_child = new TreeNode(5);

        right_child.left = left_of_right_child;
        right_child.right = right_of_right_child;

        return root;
    }

    // BST可能是不平衡的 - 路径上只有右分支有结点
    private static TreeNode constructANonBalancedBST() {
        TreeNode root = new TreeNode(1);
        TreeNode right_child = new TreeNode(2);
        TreeNode second_right_child = new TreeNode(3);
        TreeNode third_right_child = new TreeNode(4);

        root.right = right_child;
        right_child.right = second_right_child;
        second_right_child.right = third_right_child;

        return root;
    }

    static long previous_node_value = Long.MIN_VALUE;

    /**
     * 递归方法作用：判断 以当前结点作为根结点的树 是不是 一个BST(二叉搜索树)
     *
     * @param currentRootNode
     * @return
     */
    private static boolean isValidBST(TreeNode currentRootNode) {
        // #0 递归终结条件：
        // 如果递归进行到nil节点，说明 在此之上的所有结点 都已经通过了BST的检查，则：
        if (currentRootNode == null) {
            // 可以断言 原始的树是一个BST
            return true;
        }

        // #1 本级递归要做的事情：判断 当前节点 是不是 满足BST的定义 - 左子节点、右子节点、左子树、右子树
        // 规模更小的问题：判断左子树、右子树是不是一个BST？
        // 规模更小的子问题的解 怎么帮助解决 原始问题的? 答：如果 子树不是一个BST，就可以判定 原始的树不是一个BST
        // Ⅰ 对左子树进行判断
        if (!isValidBST(currentRootNode.left)) {
            return false;
        }

        // Ⅱ 判断 根结点 与 左子节点、右子节点的大小关系[左<根<右] - 按照BST的定义，“BST中序遍历的结果序列”应该是一个升序序列
        // 如果 当前结点的值 比起 序列中上一个结点的值(初始值是一个较小值，因此condition会为false) 更小或相等，说明 当前结点 违反了BST的约束，则：
        if (currentRootNode.val <= previous_node_value) {
            // 直接返回false，表示 原始的树不是一个BST
            return false;
        }
        // 把“当前节点的值” 绑定到 表示“上一个结点的值”的变量上 - 用于 下一次调用时与“当前节点的值”作比较
        previous_node_value = currentRootNode.val;

        // Ⅲ 判断右子树是否满足BST的性质
        return isValidBST(currentRootNode.right);
    }
}

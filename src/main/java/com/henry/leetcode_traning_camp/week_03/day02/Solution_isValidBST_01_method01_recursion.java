package com.henry.leetcode_traning_camp.week_03.day02;

public class Solution_isValidBST_01_method01_recursion {
    static long pre_item = Long.MIN_VALUE;

    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);

        TreeNode left_child = new TreeNode(1);
        TreeNode right_child = new TreeNode(4);

        root.left = left_child;
        root.right = right_child;

        TreeNode left_of_right_child = new TreeNode(3);
        TreeNode right_of_right_child = new TreeNode(6);

        right_child.left = left_of_right_child;
        right_child.right = right_of_right_child;

        if (isValidBST(root)) {
            System.out.println("this is a valid BST");
        } else {
            System.out.println("this is not a valid BST");
        }
    }

    /**
     * 使用递归地中序遍历来判断树是否为BST
     * @param root
     * @return
     */
    private static boolean isValidBST(TreeNode root) {
        // 1 递归触底返回条件：如果节点为null，返回true给上一级调用
        if (root == null) return true;

        // 2 本级递归要做的事情：在中序遍历的过程中判断二叉树是不是BST
        // 2-1 判断左子树是否满足BST的性质
        if (!isValidBST(root.left)) {
            return false;
        }

        // 2-2 判断根节点/当前节点是否满足BST的性质：BST中序遍历的结果是一个升序数组
        // 在更新pre_item变量前，判断当前元素与前一个元素之间的关系
        if (root.val <= pre_item) { // 如果当前元素的值小于等于pre_item元素的值，说明二叉树已经不满足BST的性质了 则:...
            // 直接返回false    注：这个false也是返回给上一级调用的
            return false;
        }

        // 更新表示前一个元素的变量
        pre_item = root.val;

        // 2-3 判断右子树是否满足BST的性质
        return isValidBST(root.right);

    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }
}

package com.henry.leetcode_traning_camp.week_03.day02;

public class Solution_isValidBST_01_method01_recursion_drill01 {
    // 准备一个全局变量 用作实时更新的最小值
    static long previous_item = Long.MIN_VALUE;

    public static void main(String[] args) {

//        TreeNode root = new TreeNode(5);
        TreeNode root = new TreeNode(2);

        TreeNode left_child = new TreeNode(1);
//        TreeNode right_child = new TreeNode(4);
        TreeNode right_child = new TreeNode(3);

        root.left = left_child;
        root.right = right_child;

//        TreeNode left_of_right_child = new TreeNode(3);
//        TreeNode right_of_right_child = new TreeNode(6);
//
//        right_child.left = left_of_right_child;
//        right_child.right = right_of_right_child;

        if (isValidBST(root)) {
            System.out.println("this is a valid BST");
        } else {
            System.out.println("this is not a valid BST");
        }

    }

    /**
     * 在中序遍历的递归实现中判断一棵树是不是BST
     * @param root
     * @return
     */
    private static boolean isValidBST(TreeNode root) {
        if (root == null) return true;

        if (!isValidBST(root.left)) {
            return false;
        }

        if (root.val <= previous_item) {
            return false;
        }
        previous_item = root.val;

        return isValidBST(root.right);
    }
}

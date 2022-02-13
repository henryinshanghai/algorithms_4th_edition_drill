package com.henry.leetcode_traning_camp.week_03.day03;



public class Solution_lowestCommonAncestor_01_method01_recursion {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(3);

        TreeNode left_child = new TreeNode(5);
        TreeNode right_child = new TreeNode(1);

        root.left = left_child;
        root.right = right_child;

        TreeNode left_child_of_five = new TreeNode(6);
        TreeNode right_child_of_five = new TreeNode(2);

        left_child.left = left_child_of_five;
        left_child.right = right_child_of_five;

        TreeNode left_child_of_one = new TreeNode(0);
        TreeNode right_child_of_one = new TreeNode(8);

        right_child.left = left_child_of_one;
        right_child.right = right_child_of_one;

        TreeNode left_child_of_two = new TreeNode(7);
        TreeNode right_child_of_two = new TreeNode(4);

        right_child_of_five.left = left_child_of_two;
        right_child_of_five.right = right_child_of_two;

        // 调用方法返回两个树节点在二叉树中的最低公共祖先节点
        TreeNode lca = lowestCommonAncestor(root, left_child, right_child);
        System.out.println("二叉树中p、q节点的公共祖先节点的值为： " + lca.val);

    }

    private static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) return root;
        if (root == p || root == q) {
            return root;
        }

        TreeNode left_res = lowestCommonAncestor(root.left, p, q);
        TreeNode right_res = lowestCommonAncestor(root.right, p, q);

        if (left_res != null && right_res != null) {
            return root;
        } else if (left_res != null) {
            return left_res;
        } else if (right_res != null) {
            return right_res;
        } else {
            return null;
        }
    }

    private static TreeNode createCertainTree() {
        TreeNode root = new TreeNode(3);

        TreeNode left_child = new TreeNode(5);
        TreeNode right_child = new TreeNode(1);

        root.left = left_child;
        root.right = right_child;

        TreeNode left_child_of_five = new TreeNode(6);
        TreeNode right_child_of_five = new TreeNode(2);

        left_child.left = left_child_of_five;
        left_child.right = right_child_of_five;

        TreeNode left_child_of_one = new TreeNode(0);
        TreeNode right_child_of_one = new TreeNode(8);

        right_child.left = left_child_of_one;
        right_child.right = right_child_of_one;

        TreeNode left_child_of_two = new TreeNode(7);
        TreeNode right_child_of_two = new TreeNode(4);

        right_child_of_five.left = left_child_of_two;
        right_child_of_five.right = right_child_of_two;

        return root;
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

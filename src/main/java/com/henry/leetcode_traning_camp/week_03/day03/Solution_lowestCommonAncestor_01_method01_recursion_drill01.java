package com.henry.leetcode_traning_camp.week_03.day03;

public class Solution_lowestCommonAncestor_01_method01_recursion_drill01 {
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
        // 递归触底返回的情况有两种：
        // 1 遇到叶子节点，而且叶子节点并不是要查找的节点p或者q。此时已经没有节点能够继续向下搜索
        // 所以当前节点为nil节点，返回给叶子节点的也是null
        if (root == null) return null;

        // 2 第二种情况，当前节点刚好是要查找的节点 这时候把匹配的节点直接返回上一级
        if (root == p || root == q) return root;

        // 3 本级递归要做的事情：
        // 3-1 获取到从左节点返回的值
        TreeNode left_res = lowestCommonAncestor(root.left, p, q);
        // 3-2 获取到从右节点返回的值
        TreeNode right_res = lowestCommonAncestor(root.right, p, q);

        // 3-3 根据从左右节点返回的值来决定当前节点的返回值
        // 如果左右节点的返回值都不为nil，说明在左右子树中分别找到了待查找的节点p、q。则：当前节点就是p、q的最低公共祖先
        if (left_res != null && right_res != null) return root;

        // 如果左右节点的返回值都是null，说明当前节点的左右子树中都没有找到p、q节点中的任一个。则：当前节点应该向上一级返回nil
        if (left_res == null && right_res == null) return null;

        // 如果左右节点中有一个返回值不是nil，说明在左子树/右子树中找到了节点p、q中的一个。这时候当前节点应该向上传播找到的节点（不是nil的那个节点）
        return left_res == null ? right_res : left_res;
    }
}

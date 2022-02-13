package com.henry.leetcode_traning_camp.day3;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class TreeNode{
    int val;
}

public class VerifyBST {
    public static void main(String[] args) {
        // 方法接收一个BST对象作为参数，所以需要先创建出一个BST对象
        // 手段???

        // 定义验证BST是否为有效BST的方法
        TreeNode root = new TreeNode();
        boolean res = isValidBST(root);
    }

    private static boolean isValidBST(TreeNode root) {
        if(root == null) return true;

        Stack<TreeNode> stack = new Stack<>();
        List<Integer> res =  new ArrayList<>();

        stack.push(root);
        while(!stack.isEmpty()){
            root = stack.pop();
            res.add(root.val);

//            stack.push(root.right);
            // 记混了，这是验证有效的BST 而不是前序遍历BST
        }

        new Thread();

        return true;
    }
}

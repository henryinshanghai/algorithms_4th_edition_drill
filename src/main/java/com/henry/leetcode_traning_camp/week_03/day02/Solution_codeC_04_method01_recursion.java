package com.henry.leetcode_traning_camp.week_03.day02;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_codeC_04_method01_recursion {

    // 实例方法：序列化 & 反序列化
    // Encodes a tree to a single string.
    /**
     * 把一棵树转化成为一个字符串
     * 手段：以某种方式遍历树中的节点(包括nil节点) 前序遍历的顺序
     * 具体方法：递归地把树地不同部分转化成为一个字符串
     * @param root
     * @return
     */
    public static String serialize(TreeNode root) {
        // 1 递归触底返回的条件：当前节点为nil节点
        if (root == null) {
            return "X,";
        }

        // 2 本机递归需要完成的事情
        // 2-1 使用树的左子树来创建一个字符串
        String left_res = serialize(root.left);
        // 2-2 使用树的右子树来创建一个字符串
        String right_res = serialize(root.right);

        // 把左子树得到的字符串与根节点、右子树得到的字符串结合起来
        return root.val + "," + left_res + right_res;
    }

    // Decodes your encoded data to tree.
    /**
     * 解码你的编码数据得到一棵树    aka     从一棵树的前序遍历结果来恢复这棵树
     * 从字符串数组还原回一棵树（按照编码时的规则/顺序）
     * 具体方法：先把字符串转化成为一个数字字符地队列，再从队列元素上创建一棵树
     * @param data
     * @return
     */
    public static TreeNode deserialize(String data) {
        // 1 从字符串中获取到二叉树中的节点值
        String[] node_arr = data.split(",");
        Deque<String> node_val_list = new LinkedList<>(Arrays.asList(node_arr));

        // 2 使用buildTree方法来从队列中还原得到一棵树
        return buildTree(node_val_list);
    }

    /**
     * 从一个指定的队列中按照特定的顺序，使用队列元素来还原一棵树
     * 手段：递归地创建树——根节点、左子树、右子树；
     * @param res
     * @return
     */
    private static TreeNode buildTree(Deque<String> res) {
        // 1 获取到当前结点    特征：队列在poll()方法调用后结束
        String s = res.poll();

        // 如果当前元素的值等于X，说明当前元素表示的是一个nil节点。则：...
        // 返回一个null树返回给上一级调用
        if (s.equals("X")) return null;

        // 使用当前元素作为节点的值来创建一个节点
        TreeNode node = new TreeNode(Integer.parseInt(s));

        // 这个就有点不讲理了 怎么构建左子树与右子树的调用代码是一样的？
        // 答：两个语句在执行时，实际传入的res参数是不一样的
        node.left = buildTree(res); //构建左子树 这会用掉队列中的一些个元素   把创建的左子树连接到根节点上
        node.right = buildTree(res); //构建右子树    用队列中剩下的元素来创建右子树

        return node;
    }

    public static void main(String[] args) {
        // 创建一个二叉树对象root
        TreeNode root = new TreeNode(1);

        TreeNode left_child = new TreeNode(2);
        TreeNode right_child = new TreeNode(3);

        root.left = left_child;
        root.right = right_child;

        TreeNode lefty_of_right_child = new TreeNode(4);
        TreeNode right_of_right_child = new TreeNode(5);

        right_child.left = lefty_of_right_child;
        right_child.right = right_of_right_child;

        // 把二叉树对象转发成为一个字符串对象 aka 对二叉树对象进行序列化
        // 序列化后字符的顺序是二叉树的前序遍历结果
        String serial_res = serialize(root);

        System.out.println("二叉树序列化的结果为： " + serial_res);

        System.out.println("=======================");

        TreeNode resume_tree = deserialize(serial_res);
        preOrderOfTree(resume_tree);

    }

    public static void preOrderOfTree(TreeNode root) {
        // 根 - 左 - 右
        if(root == null) {
            System.out.print("nil ");
            return;
        }

        System.out.print(root.val + " ");

        preOrderOfTree(root.left);
        preOrderOfTree(root.right);
    }
}

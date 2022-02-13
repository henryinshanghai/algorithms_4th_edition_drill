package com.henry.leetcode_traning_camp.week_02.day3.NthThree;

import java.util.ArrayList;
import java.util.List;

public class Solution_preorder_of_Nth_tree_02_method01 {
    public static void main(String[] args) {
        // 创建N叉树对象  this ain't easy for sure😳
        List<Node> subNodeChildren = new ArrayList<>();
        subNodeChildren.add(new Node(5, new ArrayList<>())); // 这里需要在初始化的时候为children属性绑定一个空数组，否则在递归函数中遍历时会出现NPE
        subNodeChildren.add(new Node(6, new ArrayList<>()));
        Node subNode = new Node(3, subNodeChildren);


        List<Node> children = new ArrayList<>();
        children.add(subNode);
        children.add(new Node(2, new ArrayList<>()));
        children.add(new Node(4, new ArrayList<>()));
        Node root = new Node(1, children);


        // 以后序遍历规则遍历N叉树中的节点，并把遍历到的节点存储到列表中
        List<Integer> res = preOrder(root);

        for (Integer curr :
                res) {
            System.out.print(curr + "->");
        }

    }

    static List<Integer> res;
    // 以前序遍历规则访问树中的节点，并把访问到的节点存储到list中
    private static List<Integer> preOrder(Node root) {
        res = new ArrayList<>();

        preOrderAndStore(root);

        return res;
    }

    private static void preOrderAndStore(Node root) {
        if (root == null) return;

        res.add(root.val);
        for (Node curr :
                root.children) {
            preOrderAndStore(curr);
        }
    }
}
/*
    expr:确保你的下一次练习更加简洁，也更有启发性
 */

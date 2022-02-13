package com.henry.leetcode_traning_camp.week_02.day3.NthThree;

import java.util.ArrayList;
import java.util.List;

public class Solution_postorder_of_Nth_tree_01_method01 {
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
        List<Integer> res = postOrder(root);

        for (Integer curr :
                res) {
            System.out.print(curr + "->");
        }
    }

    static List<Integer> res;

    private static List<Integer> postOrder(Node root) {
        res = new ArrayList<>();

        postOrderAndStore(root);

        return res;
    }

    // 后序遍历：左子树 - 右子树 - 根节点
    private static void postOrderAndStore(Node root) {
        // 跳出递归的条件
        if (root == null) return;

        // 本级递归要做的事情
        for (Node curr :
                root.children) {
            postOrderAndStore(curr);
        }
        res.add(root.val);
    }
}

class Node {
    public int val;
    public List<Node> children;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, List<Node> _children) {
        val = _val;
        children = _children;
    }
}

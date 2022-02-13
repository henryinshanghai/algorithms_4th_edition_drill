package com.henry.leetcode_traning_camp.week_02.day3.NthThree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Solution_levelOrder_of_Nth_tree_03_method02_recursive_drill01 {
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
//        List<List<Integer>> res = levelOrder(root);
        List<List<Integer>> original_res = new LinkedList<>();
        List<List<Integer>> res = dfs(root, original_res, 0);

        for (List<Integer> currLevel : res) {
            for (Integer item : currLevel) {
                System.out.print(item + ",");
            }

            System.out.println();
        }
    }

    // 递归方法的作用：以层序遍历的规则访问指定树中的节点，并把指定层的节点存储到指定索引的列表中
    private static List<List<Integer>> dfs(Node node, List<List<Integer>> res, int level) {
        // 1
        if (node == null) return res; // 应该返回给上一层调用什么东西呢？

        // 2 本级递归要做的事情
        // 2-1 为当前层添加一个用来存储节点的列表
        if (res.size() == level) res.add(new ArrayList<>());

        // 2-2 把当前节点的值添加到表示当前层的列表中
        res.get(level).add(node.val);

        // 2-3 对当前节点的每一个子节点执行相同的操作————以便把每一层的节点添加到对应的列表中去
        for (Node child : node.children) {
            dfs(child, res, level + 1);
        }

        return res;
    }
}

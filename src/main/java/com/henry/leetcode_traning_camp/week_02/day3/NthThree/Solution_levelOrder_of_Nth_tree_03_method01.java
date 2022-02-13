package com.henry.leetcode_traning_camp.week_02.day3.NthThree;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_levelOrder_of_Nth_tree_03_method01 {
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
        List<List<Integer>> res = levelOrder(root);

        for (List<Integer> currLevel : res) {
            for (Integer item : currLevel) {
                System.out.print(item + ",");
            }

            System.out.println();
        }


    }

    /**
     * 以层序遍历的规则访问树中的节点，并且把访问到的节点以层为单位添加到
     * @param root
     * @return
     */
    private static List<List<Integer>> levelOrder(Node root) {
        // 准备一个双端队列
        Deque<Node> temp = new LinkedList<>();
        // 准备一个元素为列表的列表
        LinkedList<List<Integer>> res = new LinkedList<>();

        if (root == null) return res;

        // 2 把根节点入队
        temp.addLast(root);

        // 3 准备一个循环 逐层处理队列中的元素
        while (!temp.isEmpty()) {
            // 3-1 准备一个列表 用于存储当前层的所有节点
            List<Integer> currLevel = new LinkedList<>();
            // 3-2 获取到当前队列中的元素个数    aka 当前层的节点数量
            int size = temp.size();

            // 3-3 准备一个循环
            for (int i = 0; i < size; i++) {
                // Ⅰ 出队队首元素
                Node currNode = temp.pollFirst();
                // Ⅱ 把队首元素的只添加到列表中
                currLevel.add(currNode.val); // 这里会有NPE吗？
//                currLevel.add(temp.peek().val);

                // Ⅲ 把当前节点的所有子节点入队
                temp.addAll(currNode.children);
//                temp.addAll(temp.poll().children);
            } // 循环结束后，当前层的所有节点的值就已经添加到了list中   当前节点的所有子节点也都已经添加到队列中

            // 把当前层的list添加到列表中
            res.add(currLevel);
        }

        return res;
    }
}

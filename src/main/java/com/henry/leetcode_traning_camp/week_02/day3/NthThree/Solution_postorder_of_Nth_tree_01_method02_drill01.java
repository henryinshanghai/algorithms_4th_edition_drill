package com.henry.leetcode_traning_camp.week_02.day3.NthThree;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_postorder_of_Nth_tree_01_method02_drill01 {
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

    private static List<Integer> postOrder(Node root) {
        // 0
        Deque<Node> temp = new LinkedList<>(); // 当作一个尾部增删的队列
        LinkedList<Integer> dest = new LinkedList<>(); // 当作一个栈使用

        if (root == null) return dest;

        // 2
        temp.addLast(root);

        // 3
        while (!temp.isEmpty()) {
            /* 3-1 从队列中取出队尾元素，并添加到准备的栈中 */
            // 从队列中取出队列尾部的节点
            Node currNode = temp.pollLast();
            // 把得到的节点添加到dest栈中（同时也是一个list）
            dest.addFirst(currNode.val);

            /* 3-2 把1中取出元素的子节点从左到右依次入队————这样在把元素存入栈中时，栈中元素的顺序就是预期的后序遍历结果 */
            for (Node child :
                    currNode.children) {
                temp.addLast(child);
            }
        }

        return dest;
    }
}

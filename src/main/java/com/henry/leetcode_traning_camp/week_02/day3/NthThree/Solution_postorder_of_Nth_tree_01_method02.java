package com.henry.leetcode_traning_camp.week_02.day3.NthThree;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Solution_postorder_of_Nth_tree_01_method02 {
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
        Deque<Node> temp = new LinkedList<>();
        LinkedList<Integer> dest = new LinkedList<>();

        if (root == null) return dest;

        // 2 根
        temp.addLast(root);
//        temp.add(root);

        // 3
        while (!temp.isEmpty()) {

            Node currNode = temp.pollLast();
            // 左 <- 右 <- 根
            dest.addFirst(currNode.val);
//            dest.add(currNode.val);

            for (Node child : currNode.children) { // expr: this is where u go wrong!
                if (child != null) {
                    temp.addLast(child); // 左 -> 右
                }
            }
        }

        return dest;
    }
}
/*
expr2:
    LinkedList既是List又是Deque，所以编程时可以使用多态（把实例变量绑定到父类型的变量上）    aka 向上转型
    向上转型后，使用变量时所能使用API其实是变量类型的APIs。也就是说，对象的有些API由于多态的用法而无法使用了
    结论：向上转型会使变量丢失一部分对象的API，所以谨慎在代码中使用多态

 */

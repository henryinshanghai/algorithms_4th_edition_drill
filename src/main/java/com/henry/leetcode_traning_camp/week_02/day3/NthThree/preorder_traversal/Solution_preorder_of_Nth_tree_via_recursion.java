package com.henry.leetcode_traning_camp.week_02.day3.NthThree.preorder_traversal;


import java.util.ArrayList;
import java.util.List;

// 验证：可以使用递归的手段 来 实现对N叉树中结点的前序遍历
// 递归方法的作用：把 当前结点 及 以当前节点作为根结点的树中所有的子结点，正确地添加到 前序遍历的结果序列中
// 前序遍历的约束：对于树中的任何结点，以其为根结点的子树，其结点在结果序列中的顺序 符合 “根-左-右”的次序
public class Solution_preorder_of_Nth_tree_via_recursion {
    public static void main(String[] args) {
        Node rootNode = constructNTree();
        // 以后序遍历规则遍历N叉树中的节点，并把遍历到的节点存储到列表中
        List<Integer> nodeValueSequence = preorderTraversal(rootNode);
        print(nodeValueSequence);
    }

    private static void print(List<Integer> res) {
        for (Integer curr : res) {
            System.out.print(curr + "->");
        }
    }

    private static Node constructNTree() {
        List<Node> subNodeChildren = new ArrayList<>();
        subNodeChildren.add(new Node(5, new ArrayList<>())); // 这里需要在初始化的时候为children属性绑定一个空数组，否则在递归函数中遍历时会出现NPE
        subNodeChildren.add(new Node(6, new ArrayList<>()));
        Node subNode = new Node(2, subNodeChildren);


        List<Node> children = new ArrayList<>();
        children.add(subNode);
        children.add(new Node(3, new ArrayList<>()));
        children.add(new Node(4, new ArrayList<>()));
        Node root = new Node(1, children);
        return root;
    }

    static List<Integer> preorderNodeSequence = new ArrayList<>();

    // 递归方法的作用：把 当前结点 及 以当前节点作为根结点的树中所有的子结点，正确地添加到 前序遍历的结果序列中
    private static List<Integer> preorderTraversal(Node currentRootNode) {
        // #0 递归终结条件
        // 如果当前结点是一个nil结点，说明已经执行到了叶子节点处，则：
        if (currentRootNode == null) {
            // 返回当前得到的结点序列 到上一级调用
            return preorderNodeSequence;
        }

        // #1 本级递归需要做的事情
        // Ⅰ 把当前根结点的value 添加到结果序列中
        preorderNodeSequence.add(currentRootNode.val);
        // Ⅱ 对于当前结点的所有子结点，从左往右地 将之递归地添加到 结果序列中
        for (Node curr : currentRootNode.children) {
            preorderTraversal(curr);
        }

        // #2 返回最终的结果序列
        return preorderNodeSequence;
    }
}
/*
    expr:确保你的下一次练习更加简洁，也更有启发性
 */

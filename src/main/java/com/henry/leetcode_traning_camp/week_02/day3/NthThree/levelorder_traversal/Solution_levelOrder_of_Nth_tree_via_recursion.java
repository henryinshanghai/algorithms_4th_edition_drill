package com.henry.leetcode_traning_camp.week_02.day3.NthThree.levelorder_traversal;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// 验证：可以使用递归来实现 对二叉树中结点的层序遍历；
// 递归的子问题：把 当前节点 及 以其作为根结点的子结点 添加到 其在结果序列中所对应的list中；
public class Solution_levelOrder_of_Nth_tree_via_recursion {
    public static void main(String[] args) {
        // 创建N叉树对象  this ain't easy for sure😳
        Node rootNodeOfTree = constructNTree();

        // 得到该二叉树层序遍历的结果序列
        List<List<Integer>> emptyListSequence = new LinkedList<>();
        List<List<Integer>> levelOrderedNodeValueSequence
                = putNodeIntoCorrectLevelList(rootNodeOfTree, emptyListSequence, 0);

        // 打印 二叉树层序遍历的结果序列
        print(levelOrderedNodeValueSequence);
    }

    private static void print(List<List<Integer>> res) {
        for (List<Integer> currLevel : res) {
            for (Integer item : currLevel) {
                System.out.print(item + ",");
            }

            System.out.println();
        }
    }

    private static Node constructNTree() {
        List<Node> subNodeChildren = new ArrayList<>();
        subNodeChildren.add(new Node(5, new ArrayList<>())); // 这里需要在初始化的时候为children属性绑定一个空数组，否则在递归函数中遍历时会出现NPE
        subNodeChildren.add(new Node(6, new ArrayList<>()));
        subNodeChildren.add(new Node(7, new ArrayList<>()));
        Node subNode = new Node(2, subNodeChildren);


        List<Node> children = new ArrayList<>();
        children.add(subNode);
        children.add(new Node(3, new ArrayList<>()));
        children.add(new Node(4, new ArrayList<>()));
        Node root = new Node(1, children);
        return root;
    }

    // 使用DFS的方式来实现层序遍历
    // 递归方法的作用：对指定的二叉树进行层序遍历，并把特定层的节点 及 以该结点为根结点的树中的所有结点 都添加到 其在结果序列中所对应的列表中
    // 🐖 方法的作用 决定了 方法的参数 以及 进入下一层递归时参数的变化
    private static List<List<Integer>> putNodeIntoCorrectLevelList(Node currentRootNode, List<List<Integer>> levelSequence, int itsCorrectLevel) {
        // #0 递归终结条件
        // 如果当前结点为nil结点，说明已经没有结点需要继续添加到序列中了，则：
        if (currentRootNode == null) {
            // 把当前序列返回给上一层调用
            return levelSequence;
        }

        // #1 本级递归要做的事情【只考虑本级递归，剩下的事情默认是已经实现了的】
        // Ⅰ 把“当前结点” 添加到 序列中 其所对应的list中
        // ① 如果当前sequence的大小 与 传入的level大小相同，说明 需要在序列中，为传入的level 添加对应的列表，则：
        if (levelSequence.size() == itsCorrectLevel) {
            // 在序列中 为当前层添加一个空列表
            levelSequence.add(new ArrayList<>());
        }

        // ② 把“当前节点的值” 添加到 “节点所在层”所对应的列表中...
        List<Integer> nodeValuesOnCurrentLevel = levelSequence.get(itsCorrectLevel);
        nodeValuesOnCurrentLevel.add(currentRootNode.val);

        // Ⅱ 对当前节点的每一个子节点做相同的处理
        // 🐖 所有的子结点都在同一层中，因此会被添加到同一个list中
        for (Node currentChildNode : currentRootNode.children) {
            // 子问题：把“当前子结点” 添加到 序列中 下一层level所对应的list中
            // 子问题的答案如何能够帮助解决原始问题?? - 子问题本身原始问题的一部分
            putNodeIntoCorrectLevelList(currentChildNode, levelSequence, itsCorrectLevel + 1);
        }

        // #3 最终返回 得到的list序列
        return levelSequence;
    }
}

package com.henry.leetcode_traning_camp.week_02.day3.NthThree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Solution_levelOrder_of_Nth_tree_03_method02_recursive_to_dig {
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
        List<List<Integer>> param1 = new LinkedList<>();
        List<List<Integer>> res = dfs(root, param1, 0);

        for (List<Integer> currLevel : res) {
            for (Integer item : currLevel) {
                System.out.print(item + ",");
            }

            System.out.println();
        }
    }

    // 使用DFS的方式来实现层序遍历
    /*
        递归方法的模板：
            1 递归终结条件；
            2 本级递归的逻辑；
            3 下一层递归；
            4 递归完成后清理一些变量（可选）；
     */
    // 0 递归方法的作用：
    //      对指定的二叉树进行层序遍历，并把特定层的节点添加到特定索引的列表中
    // 方法的作用决定了方法的参数 以及 进入下一层递归时参数的变化
    private static List<List<Integer>> dfs(Node node, List<List<Integer>> res, int level) {
        // 1 terminator
        if (node == null) return res; // 传进来的是啥就返回啥

        // 2 本级递归要做的事情【只考虑本级递归，剩下的事情默认是已经实现了的】
        // 为了存储当前层中的节点，需要在res列表中，为当前层level添加一个list元素
        // 手段：由于res最开始是空的，所以直接使用level 与 res列表的尺寸判断即可
        // 2-1 检查当前level是否已经有一个初始化的列表（该列表用来记录当前层中的节点）
        // 如果没有，则：为当前层初始化一个空列表
        if (res.size() == level) res.add(new ArrayList<>()); // 为当前层添加一个空列表

        // 2-2 把当前节点的值添加到节点所在层对应的列表中...【只考虑本级递归，剩下的事情默认是已经实现了的】
        res.get(level).add(node.val);

        // 2-3 对当前节点的每一个子节点做相同的处理
        for (Node child : node.children)
            // 递归调用下一层时，层数+1
            dfs(child, res, level + 1);
        return res;
    }
}

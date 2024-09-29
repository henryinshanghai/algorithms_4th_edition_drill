package com.henry.leetcode_traning_camp.week_02.day3.NthThree.levelorder_traversal;


import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// 验证：可以使用一个队列 来 以迭代的方式 实现二叉树中结点的层序遍历
// 原理：把当前层的结点 从队尾追加到队列中，然后从队首出队 逐一处理
public class Solution_levelOrder_of_Nth_tree_via_iteration {
    public static void main(String[] args) {
        // 创建N叉树对象  this ain't easy for sure😳
        Node root = constructNTree();

        // 以后序遍历规则遍历N叉树中的节点，并把遍历到的节点存储到列表中
        List<List<Integer>> levelOrderNodeValueSequence = levelOrderTraversal(root);

        print(levelOrderNodeValueSequence);
    }

    private static void print(List<List<Integer>> levelOrderNodeValueSequence) {
        for (List<Integer> currLevel : levelOrderNodeValueSequence) {
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

    /**
     * 以层序遍历的规则访问树中的节点，并且把访问到的节点以层为单位添加到
     *
     * @param rootNode
     * @return
     */
    private static List<List<Integer>> levelOrderTraversal(Node rootNode) {
        // 准备一个双端队列 用于表示当前层的结点
        Deque<Node> nodesQueue = new LinkedList<>();
        // 准备一个元素为列表的列表/序列 用于收集结点value
        LinkedList<List<Integer>> nodeListSequencePerLevel = new LinkedList<>();

        // #1 健壮性代码
        if (rootNode == null) {
            return nodeListSequencePerLevel;
        }

        // #2 把根节点 从队尾位置入队
        nodesQueue.addLast(rootNode);

        // #3 准备一个循环，借助队列 来 逐层处理 树中的结点
        while (!nodesQueue.isEmpty()) {
            // 准备一个列表 用于存储“当前层的所有节点”
            List<Integer> currentLevelNodes = new LinkedList<>();
            // 获取到当前队列中的元素个数  aka 当前层的节点数量
            int currentDequeSize = nodesQueue.size();

            // Ⅰ 对于当前层 currentDeque: #1 把当前层的结点 添加到 结点序列currentLevelNodes中； #2 把下一层的结点 添加到 双端队列nodesDeque的队尾中
            // 手段：一个for循环 遍历 当前层中的所有结点
            for (int currentNodeSpot = 0; currentNodeSpot < currentDequeSize; currentNodeSpot++) {
                // ① 获取并移除 队首元素 aka 当前层的第一个结点
                Node currentNode = nodesQueue.pollFirst();
                // ② 把队首元素的值 添加到列表中
                currentLevelNodes.add(currentNode.val); // 这里会有NPE吗？

                // ③ 把当前节点的所有子节点 从队列尾部（由左往右地）入队
                nodesQueue.addAll(currentNode.children);
            } // 循环结束后，#1 “当前层的所有节点的值” 就已经添加到了list中；#2 所有当前节点的所有子节点(下一层的所有结点)也都已经添加到队列中

            // Ⅱ 把当前层的list添加到列表中
            nodeListSequencePerLevel.add(currentLevelNodes);
        }

        return nodeListSequencePerLevel;
    }
}

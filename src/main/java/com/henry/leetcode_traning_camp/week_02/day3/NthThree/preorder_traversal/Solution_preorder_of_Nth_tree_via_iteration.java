package com.henry.leetcode_traning_camp.week_02.day3.NthThree.preorder_traversal;


import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// 验证：可以使用迭代的手段 来 实现对N叉树中结点的前序遍历
// 手段：使用 栈的先进后出特性 与 单开口特性 + N叉树的结点关系特性 来 把结点按照预期的顺序 添加到 结果序列中 - 使得结果序列满足预期的约束
public class Solution_preorder_of_Nth_tree_via_iteration {
    public static void main(String[] args) {
        // 创建N叉树对象
        Node rootNodeOfTree = constructNTree();
        // 获取到 对此N叉树进行先序遍历得到的结果序列
        List<Integer> nodeValuePreOrderSequence = preorderTraversal(rootNodeOfTree);
        // 打印结果序列
        print(nodeValuePreOrderSequence);
    }

    private static void print(List<Integer> nodeValueSequence) {
        for (Integer currentNodeValue : nodeValueSequence) {
            System.out.print(currentNodeValue + "->");
        }
    }

    private static Node constructNTree() {
        List<Node> subNodeChildren = new ArrayList<>();
        subNodeChildren.add(new Node(5, new ArrayList<>())); // 这里需要在初始化的时候为children属性绑定一个空数组，否则在递归函数中遍历时会出现NPE
        subNodeChildren.add(new Node(6, new ArrayList<>()));
        Node subNode = new Node(2, subNodeChildren);


        List<Node> children = new ArrayList<>();
        children.add(subNode); // 2
        children.add(new Node(3, new ArrayList<>()));
        children.add(new Node(4, new ArrayList<>()));
        Node root = new Node(1, children);
        return root;
    }

    /**
     * 以前序遍历的规则访问树中的节点，并把节点添加到list中
     * 手段：循环 + 特殊数据结构的apis
     *
     * @param rootNode
     * @return
     */
    private static List<Integer> preorderTraversal(Node rootNode) {
        Deque<Node> nodeStack = new LinkedList<>();
        LinkedList<Integer> nodeValuePreOrderSequence = new LinkedList<>();

        if (rootNode == null) {
            return nodeValuePreOrderSequence;
        }

        // 入栈 根结点 - 此时栈口元素是根结点
        nodeStack.addFirst(rootNode);

        while (!nodeStack.isEmpty()) {
            // 获取并移除 栈口元素
            Node nodeOnStackHatch = nodeStack.pollFirst();
            // 向前序遍历结果序列中 添加 栈口结点的值
            nodeValuePreOrderSequence.add(nodeOnStackHatch.val); // 根结点的value

            // 对于树中 当前栈口结点的所有子结点...
            // 把它们按照 从右到左地顺序 入栈（压栈操作）- 这样树左边的结点就会最先出栈
            // 示例：① 4；② 3；③ 2；=> 2 - 3 - 4
            List<Node> childrenNodes = nodeOnStackHatch.children;
            for (int currentChildrenSpot = childrenNodes.size() - 1; currentChildrenSpot >= 0; currentChildrenSpot--) {
                Node currentChildrenNode = childrenNodes.get(currentChildrenSpot); // 获取 当前子结点
                nodeStack.addFirst(currentChildrenNode); // 把该子结点 入栈
            }
        }

        return nodeValuePreOrderSequence;
    }
}

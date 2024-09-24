package com.henry.leetcode_traning_camp.week_02.day3.NthThree.post_order_traversal;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

// 结论：可以利用栈的先进后出特性、单开口特性 与 双端队列的双端增删的特性 来 以迭代的方式 得到 N叉树中结点的后序遍历序列；
// 原理：栈的FILO特性 与 双端队列的头插操作相结合
public class Solution_postorder_of_Nth_tree_via_iteration {
    public static void main(String[] args) {

        Node rootNodeOfTree = constructNTree();

        // 以后序遍历规则遍历N叉树中的节点，并把遍历到的节点存储到列表中
        List<Integer> nodesValueSequenceInPostOrder = postOrderTraversal(rootNodeOfTree);

        print(nodesValueSequenceInPostOrder);

    }

    private static void print(List<Integer> resultSequence) {
        for (Integer curr : resultSequence) {
            System.out.print(curr + "->");
        }
    }

    private static Node constructNTree() {
        // 创建N叉树对象  this ain't easy for sure😳
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

    private static List<Integer> postOrderTraversal(Node rootNode) {
        // #0 准备需要用到的变量
        Deque<Node> nodesStack = new LinkedList<>(); // 由结点构成的栈
        LinkedList<Integer> nodeValueSequence = new LinkedList<>(); // 由结点的value所组成的双向链表

        // #1 健壮性代码 - 如果传入的结点为nil结点，则：直接返回空列表
        if (rootNode == null) {
            return nodeValueSequence;
        }

        // #2 初始化栈中的结点 - 向栈口 添加当前根结点
        nodesStack.addLast(rootNode);

        // #3 使用双端队列 来 把树中的结点 按照后序规则添加到链表中
        while (!nodesStack.isEmpty()) {
            // 获取并移除 栈口结点
            Node nodeOnStackHatch = nodesStack.pollLast();
            // 在列表头部添加元素    🐖：这个方法类似于链表的头插法 会把元素添加到list的起始位置
            // 不断生长的结果序列：③ 左子结点 <- ② 右子结点 <- ① 根
            nodeValueSequence.addFirst(nodeOnStackHatch.val);

            // 对于 nodeOnStackHatch 的每一个子结点...
            for (Node currentChildNode : nodeOnStackHatch.childrenNodes) { // expr: this is where u go wrong!
                // 从左到右地 把子结点添加到 结点栈中 - 这样在取出结点时就是 先右后左，配合头插法就能得到结果序列
                if (currentChildNode != null) {
                    nodesStack.addLast(currentChildNode); // 左 -> 右
                }
            }
        }

        return nodeValueSequence;
    }
}
/*
expr2:
    LinkedList既是List又是Deque，所以编程时可以使用多态（把实例变量绑定到父类型的变量上）    aka 向上转型
    向上转型后，使用变量时所能使用API其实是变量类型的APIs。也就是说，对象的有些API由于多态的用法而无法使用了
    结论：向上转型会使变量丢失一部分对象的API，所以谨慎在代码中使用多态

 */

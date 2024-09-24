package com.henry.leetcode_traning_camp.week_02.day3.NthThree.post_order_traversal;

import java.util.ArrayList;
import java.util.List;

// 验证：可以使用递归的方式 来 得到N叉树的结点后序遍历序列
// 特征：list中元素是从左往右添加的
public class Solution_postorder_of_Nth_tree_via_recursion {
    public static void main(String[] args) {
        // 创建N叉树对象  this ain't easy for sure😳
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
        // 第二层子结点
        List<Node> subNodeChildren = new ArrayList<>();
        subNodeChildren.add(new Node(5, new ArrayList<>())); // 这里需要在初始化的时候为children属性绑定一个空数组，否则在递归函数中遍历时会出现NPE
        subNodeChildren.add(new Node(6, new ArrayList<>()));
        subNodeChildren.add(new Node(7, new ArrayList<>()));
        Node subNode = new Node(2, subNodeChildren);

        // 第一层子结点
        List<Node> children = new ArrayList<>();
        children.add(subNode); // 第一个结点是一个子树
        children.add(new Node(3, new ArrayList<>()));
        children.add(new Node(4, new ArrayList<>()));

        // 根结点
        Node root = new Node(1, children);
        return root;
    }

    static List<Integer> postorderNodeValueSequence = new ArrayList<>();

    private static List<Integer> postOrderTraversal(Node currentRootNode) {
        // #0 跳出递归的条件
        // 如果当前根节点为nil，说明已经执行到了叶子节点处，则：
        if (currentRootNode == null) {
            // 把当前的结果序列返回给上一级
            return postorderNodeValueSequence;
        }

//         把当前结点的value 添加到 结果序列中
//        postorderNodeValueSequence.add(currentRootNode.val);

        /* #1 本级递归要做的事情 */
        // Ⅰ 先 对当前结点的每一个子结点，把“以其作为根结点的子树“中的所有结点 （从左往右地）添加到后序遍历的结果序列中
        for (Node currentChildrenNode : currentRootNode.childrenNodes) {
            postOrderTraversal(currentChildrenNode);
        }

        // Ⅱ 再 把当前结点的value 添加到 结果序列中
        postorderNodeValueSequence.add(currentRootNode.val);

        // #2 返回最终的结点序列
        return postorderNodeValueSequence;
    }
}

class Node {
    public int val;
    // 当前结点的所有子结点的列表(从左往右)
    public List<Node> childrenNodes;

    public Node() {
    }

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, List<Node> _childrenNodes) {
        val = _val;
        childrenNodes = _childrenNodes;
    }
}

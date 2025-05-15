package com.henry.leetcode_traning_camp.week_03.day02.serialize_binary_tree;

import com.henry.leetcode_traning_camp.week_03.day02.TreeNode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

// 验证：#1 对于任何一棵二叉树，我们都可以选择 前序、中序、后序或层序的方式 来 字符串表示出它
// #2 所谓 二叉树的序列化，就是由非线性 转换得到 线性结构的过程 - 我们可以选择任何规则进行这个过程，只要保证序列化 与 反序列遵守相同的规则即可
public class Solution_serialise_binaryTree_via_recursion {

    /**
     * 把一棵二叉树对象 转化成为 一个对应的字符串
     * 手段：以某种方式(这里选择 前序遍历顺序) 来 遍历树中的节点(包括nil节点)
     * 具体方法：递归地把树的不同部分 转化成为 一个字符串
     *
     * @param currentRootNode
     * @return
     */
    public static String serializeTreeInPreOrder(TreeNode currentRootNode) {
        // #0 递归终结条件
        // 如果 当前节点为 null，说明 递归/路径执行到了nil结点，则：
        if (currentRootNode == null) {
            // 返回 nil结点 所对应的占位符 “X,” 给上一级调用
            return "X,";
        }

        // #1 本级递归需要完成的事情 - 分别对左子树、右子树进行序列化，并使用两者的序列化结果 得到原始树的序列化结果
        // Ⅰ 对左子树进行序列化
        String leftSubTreeResult = serializeTreeInPreOrder(currentRootNode.left);
        // Ⅱ 对右子树进行序列化
        String rightSubTreeResult = serializeTreeInPreOrder(currentRootNode.right);

        // #2(Ⅲ) 把根节点 + 左子树得到的字符串 + 右子树得到的字符串 串联连接起来 以得到 原始树的序列化结果
        return currentRootNode.val + "," + leftSubTreeResult + rightSubTreeResult;
    }

    /**
     * 解码你的编码序列 来 得到一棵树    aka     从一棵树的“前序遍历结果序列”来恢复这棵树
     * 特征：需要按照编码时的规则/顺序（根 - 左 - 右）
     * 具体方法：先把字符串转化成为一个数字字符的队列，再从队列元素上创建一棵树
     *
     * @param characterSequence
     * @return
     */
    public static TreeNode deserialize(String characterSequence) {
        // 1 从字符串中获取到二叉树中的节点值
        String[] nodeValuesArrInPreOrder = characterSequence.split(",");
        Queue<String> nodeValuesQueueInPreOrder = new LinkedList<>(Arrays.asList(nodeValuesArrInPreOrder));

        // 2 使用buildTree方法来从队列中还原得到一棵树
        return buildTreeFrom(nodeValuesQueueInPreOrder);
    }

    /**
     * 从一个指定的队列中按照特定的顺序(前序规则)，使用队列元素来还原一棵树
     * 手段：递归地创建树——根节点、左子树、右子树；
     *
     * @param nodeValuesQueueInPreOrder
     * @return
     */
    private static TreeNode buildTreeFrom(Queue<String> nodeValuesQueueInPreOrder) {
        /* #1 处理根结点 */
        // 手段：获取并移除 简单队列的队首元素
        String currentNodeValue = nodeValuesQueueInPreOrder.poll();

        // 如果当前元素的值等于X，说明当前元素 表示的是 一个nil节点。则：
        if (currentNodeValue.equals("X")) {
            // 返回一个null树 给上一级调用
            return null;
        }

        // #2 本级递归需要做的事情：使用队首元素 来 创建一个结点，再使用剩下的队列元素 来 创建左子树、右子树
        // 规模更小的子问题：使用 队列中的剩余元素 来 创建出 左子树、右子树
        // 子问题的解 怎么帮助解决 原始问题：子问题的解 就是 原始问题的解的一部分
        // Ⅰ 使用当前元素作为节点的值 来 创建一个节点
        TreeNode currentNode = new TreeNode(Integer.parseInt(currentNodeValue));

        /* 按顺序来处理左右子树 */
        // 🐖：两个语句在执行时，实际传入的res参数是不一样的
        // Ⅱ 从队列的元素中 构建出左子树，并 把构建结果 绑定到 左子结点的引用上
        currentNode.left = buildTreeFrom(nodeValuesQueueInPreOrder);
        // Ⅲ 从队列的元素中 构建出右子树，并 把构建结果 绑定到 右子结点的引用上
        currentNode.right = buildTreeFrom(nodeValuesQueueInPreOrder);

        /* #3 返回二叉树的根结点 */
        return currentNode;
    }

    public static void main(String[] args) {
        // 创建一个二叉树对象：1 - 2 3 - nil nil 4 5
        TreeNode rootNodeOfBinaryTree = constructABinaryTree();

        /* 序列化此二叉树对象 */
        // 得到 其前序遍历结果序列(包含nil结点) 作为序列化结果
        String serializedSequence = serializeTreeInPreOrder(rootNodeOfBinaryTree);
        System.out.println("二叉树序列化的结果(前序遍历结果序列[含nil结点])为： " + serializedSequence);

        System.out.println("=======================");

        /* 反序列化此二叉树对象 */
        // 以前序顺序 来 从序列中创建出原始的树
        TreeNode resumedTree = deserialize(serializedSequence);
        System.out.print("反序列化得到的二叉树[包含nil结点]为：");
        // 使用 树的“前序遍历结果序列” 来 表示 此二叉树
        preOrderOfTree(resumedTree);
    }

    private static TreeNode constructABinaryTree() {
        // 创建一个二叉树对象：1 - 2 3 - nil nil 4 5
        TreeNode root = new TreeNode(1);

        TreeNode left_child = new TreeNode(2);
        TreeNode right_child = new TreeNode(3);

        // 根节点的左右子节点
        root.left = left_child;
        root.right = right_child;

        TreeNode lefty_of_right_child = new TreeNode(4);
        TreeNode right_of_right_child = new TreeNode(5);

        // 右子节点的左右子节点
        right_child.left = lefty_of_right_child;
        right_child.right = right_of_right_child;

        return root;
    }

    public static void preOrderOfTree(TreeNode root) {
        // 根 - 左 - 右
        if (root == null) {
            System.out.print("nil ");
            return;
        }

        System.out.print(root.val + " ");

        preOrderOfTree(root.left);
        preOrderOfTree(root.right);
    }
}

package com.henry.leetcode_traning_camp.week_03.day02.serialize_binary_tree;

import com.henry.leetcode_traning_camp.week_03.day02.TreeNode;

import java.util.Deque;
import java.util.LinkedList;

import static com.henry.leetcode_traning_camp.week_03.day02.serialize_binary_tree.Solution_serialise_binaryTree_via_recursion.preOrderOfTree;

public class Solution_serial_binary_tree_via_BFS {
    public static void main(String[] args) {
        TreeNode rootNodeOfTree = constructABinaryTree();

        // 把二叉树对象转发成为一个字符串对象 aka 对二叉树对象进行序列化
        // 序列化后字符的顺序是二叉树的前序遍历结果
        String serial_character_sequence = serialize(rootNodeOfTree);
        System.out.println("二叉树序列化的结果为： " + serial_character_sequence);

        System.out.println("=======================");

        TreeNode resume_tree = deserialize(serial_character_sequence);
        System.out.println("使用字符串反序列化得到的二叉树结果为： ");
        preOrderOfTree(resume_tree);
    }
    /**
     * 从一个字符串反序列化得到一棵树
     * 注：从一个字符串得到一个树可能会有多种结果 但是这里我们会使用特定的顺序
     * 手段：指针 + 使用数字字符来创建树节点
     * @param serial_character_sequence
     */
    private static TreeNode deserialize(String serial_character_sequence) {
        if (serial_character_sequence == "") return null;
        String[] serial_character_arr = serial_character_sequence.split(", ");

        TreeNode rootNode = new TreeNode(Integer.parseInt(serial_character_arr[0]));
        // 准备一个结点组成的双端队列
        Deque<TreeNode> nodeDeque = new LinkedList<>();
        // 把根结点 从队尾入队
        nodeDeque.addLast(rootNode);

        // 准备一个循环   在循环中：#1 遍历剩下的字符； #2 使用剩下的字符 来 逐一创建树中的节点 并 建立链接关系
        for (int current_character_spot = 1; current_character_spot < serial_character_arr.length; current_character_spot++) { // 指针从第二个位置开始
            // 出列队首元素，并用它来 创建树的根节点
            TreeNode node_on_currentHead = nodeDeque.pollFirst();
            // 获取到 数组指针指向的下一个编码字符
            String current_character = serial_character_arr[current_character_spot];

            // 如果该编码字符不是nil，说明是有效节点元素。则：
            if (isNotNil(current_character)) {
                // 按照顺序创建树节点 并 将之作为左子节点进行关联
                TreeNode left_child = new TreeNode(Integer.parseInt(current_character));
                node_on_currentHead.left = left_child;

                // 把新的树节点添加到队列中去 🐖：循环会把这个新节点取出，再去给它建立树节点的连接关系
                nodeDeque.addLast(left_child);
            }

            // 如果 指针指向的当前编码字符的 下一个编码字符也不是nil，说明下一个元素也是 有效的树节点。则：
            if (!serial_character_arr[++current_character_spot].equals("nil")) {
                // 按照顺序创建树节点 并 将之作为右子节点进行关联
                TreeNode right_child = new TreeNode(Integer.parseInt(current_character));
                node_on_currentHead.right = right_child;

                // 把新的树节点添加到队列中去 🐖：循环会把这个新节点取出，再去给它建立树节点的连接关系
                nodeDeque.addLast(right_child);
            }
        }

        return rootNode;
    }

    private static boolean isNotNil(String current_character) {
        return !current_character.equals("nil");
    }

    /**
     * 对一棵树按照特定规则进行序列化，得到一个序列化后的字符串
     * 手段：对树进行”层序遍历“（这意味着解码时 应该按照层序的方式来创建树）来得到“序列化后的字符串”
     * @param root  指定的树对象
     * @return
     */
    private static String serialize(TreeNode root) {
        // 准备一个队列   用于支持层序遍历
        Deque<TreeNode> nodeDequeInPreOrder = new LinkedList<>();
        // 准备一个StringBuffer对象   用于添加遍历到的树节点元素值
        StringBuffer serial_character_sb = new StringBuffer();

        // 2 把树的根节点入队
        nodeDequeInPreOrder.addLast(root);

        // 3 准备一个循环
        while (!nodeDequeInPreOrder.isEmpty()) {
            // 获取并移除 双端队列的队首元素
            TreeNode current_node = nodeDequeInPreOrder.pollFirst();

            // 特殊情况：如果队列中的元素为nil，说明当前树节点是一个nil节点。则：
            if (current_node == null) {
                // 向sb对象中追加一个"nil"字符串，并跳过本轮循环
                serial_character_sb.append("nil, ");
                continue;
            }

            // 一般情况：向sb对象中 追加当前结点的value
            serial_character_sb.append(current_node.val + ", ");

            // 向队列的队尾 添加当前节点的左子节点、右子节点
            nodeDequeInPreOrder.addLast(current_node.left);
            nodeDequeInPreOrder.addLast(current_node.right);
        }

        return serial_character_sb.toString();

    }

    private static TreeNode constructABinaryTree() {
        // 创建一个二叉树对象root
        TreeNode root = new TreeNode(1);

        TreeNode left_child = new TreeNode(2);
        TreeNode right_child = new TreeNode(3);

        root.left = left_child;
        root.right = right_child;

        TreeNode lefty_of_right_child = new TreeNode(4);
        TreeNode right_of_right_child = new TreeNode(5);
        right_child.left = lefty_of_right_child;
        right_child.right = right_of_right_child;

        return root;
    }
}

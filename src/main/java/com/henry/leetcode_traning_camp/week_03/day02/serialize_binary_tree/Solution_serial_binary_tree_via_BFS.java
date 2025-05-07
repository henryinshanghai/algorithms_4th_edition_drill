package com.henry.leetcode_traning_camp.week_03.day02.serialize_binary_tree;

import com.henry.leetcode_traning_camp.week_03.day02.TreeNode;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import static com.henry.leetcode_traning_camp.week_03.day02.serialize_binary_tree.Solution_serialise_binaryTree_via_recursion.preOrderOfTree;

// 验证：#1 可以使用BFS的方式(#1 出队父节点; #2 处理父节点; #3 按顺序入队左右子节点) 来 对二叉树中的结点进行序列化
// #2 可以从BFS得到的序列中，依据“父节点 - 左子节点 - 右子节点”的顺序 来 使用一个简单队列 反序列化出二叉树本身
public class Solution_serial_binary_tree_via_BFS {
    public static void main(String[] args) {
        // 创建一个二叉树对象root: 1 - 2 3  - nil nil 4 5
        TreeNode rootNodeOfTree = constructABinaryTree();

        // 把二叉树对象转发成为一个字符串对象 aka 对二叉树对象进行序列化
        // 序列化后字符的顺序是二叉树的前序遍历结果
        String serializedResult = serializeViaSequenceOrder(rootNodeOfTree);
        System.out.println("二叉树序列化（层序遍历）的结果为： " + serializedResult);

        TreeNode originalTree = deserializeViaSequenceOrder(serializedResult);
        System.out.print("使用字符串反序列化得到的二叉树结果（前序遍历表示）为： ");
        preOrderOfTree(originalTree);
    }

    /**
     * 从一个字符串 通过BFS的规则 来 反序列化得到一棵树
     * 🐖 我们已经知道 待反序列化的序列 满足：父节点 - [nil, nil] - 左子节点 - 右子节点 的模式
     * 手段：指针 + 使用数字字符来创建树节点
     * @param serializedResult
     */
    private static TreeNode deserializeViaSequenceOrder(String serializedResult) {
        if (serializedResult == "") return null;
        String[] nodesSerialSequence = serializedResult.split(", ");

        System.out.println("二叉树经层序遍历后的字符串表示为：");
        printArr(nodesSerialSequence);

        String firstNodeValue = nodesSerialSequence[0];
        TreeNode rootNode = new TreeNode(Integer.parseInt(firstNodeValue));

        // 准备一个简单队列
        Queue<TreeNode> nodeSimpleQueue = new LinkedList<>();
        // 把 根结点 从队尾入队
        nodeSimpleQueue.offer(rootNode); // offer() = add()

        // 准备一个循环
        // 在循环中：#1 遍历剩下的字符； #2 使用剩下的字符 来 逐一创建树中的节点 并 建立链接关系
        for (int currentNodeSpot = 1; currentNodeSpot < nodesSerialSequence.length; currentNodeSpot += 2) { // 指针从第二个位置开始，且移动步距为2
            // #1 出列“父节点”??
            TreeNode currentFatherNode = nodeSimpleQueue.poll();

            // #2 获取到 “左子节点” aka 数组指针指向的下一个编码字符
            String currentNodeValue = nodesSerialSequence[currentNodeSpot];
            // 如果该字符不是nil，说明是有效节点元素。则：
            if (isNotNil(currentNodeValue)) {
                // 按照顺序创建树节点 并 将之作为左子节点进行关联
                TreeNode leftSubChild = new TreeNode(Integer.parseInt(currentNodeValue));
                currentFatherNode.left = leftSubChild;

                // 把新的树节点添加到队列中去 🐖：循环会把这个新节点取出，再去给它建立树节点的连接关系
                nodeSimpleQueue.offer(leftSubChild);
            }

            // #3 获取到 “右子节点” aka 数组指针指向的下一个编码字符
            String nextNodeValue = nodesSerialSequence[currentNodeSpot + 1];
            // 如果 指针指向的当前编码字符的 下一个编码字符也不是nil，说明下一个元素也是 有效的树节点。则：
            if (!nextNodeValue.equals("nil")) {
                // 按照顺序创建树节点 并 将之作为右子节点进行关联
                TreeNode rightSubChild = new TreeNode(Integer.parseInt(nextNodeValue));
                currentFatherNode.right = rightSubChild;

                // 把新的树节点添加到队列中去 🐖：循环会把这个新节点取出，再去给它建立树节点的连接关系
                nodeSimpleQueue.offer(rightSubChild);
            }
        }

        return rootNode;
    }

    private static void printArr(String[] nodesSerialSequence) {
        for (String node : nodesSerialSequence) {
            System.out.print(node + " ");
        }
        System.out.println();
    }

    private static boolean isNotNil(String current_character) {
        return !current_character.equals("nil");
    }

    /**
     * 对一棵树按照特定规则进行序列化，得到一个序列化后的字符串
     * 手段：对树进行”层序遍历“（这意味着解码时 应该按照层序的方式来创建树）来得到“序列化后的字符串”
     * 模式：出队“父节点” + 处理该结点 + 入队“左右子结点”，直到队列为空
     * @param rootNodeOfTree 指定的树对象
     * @return
     */
    private static String serializeViaSequenceOrder(TreeNode rootNodeOfTree) {
        // 准备一个普通队列   用于支持层序遍历
        Queue<TreeNode> nodeSimpleQueueInPreOrder = new LinkedList<>();
        // 准备一个StringBuffer对象   用于添加遍历到的树节点元素值
        StringBuilder serializedResult = new StringBuilder();

        // #〇 把“树的根节点”入队 offer() = add();
        nodeSimpleQueueInPreOrder.offer(rootNodeOfTree);

        // 准备一个循环
        while (!nodeSimpleQueueInPreOrder.isEmpty()) {
            // #1 获取并移除 双端队列的队首元素 poll() = remove();
            TreeNode currentNode = nodeSimpleQueueInPreOrder.poll();

            /* #2 按需处理 当前节点 */
            // ① 特殊情况：如果队列中的元素为nil，说明当前树节点是一个nil节点。则：
            if (currentNode == null) {
                // 向sb对象中追加一个"nil"字符串，并跳过本轮循环
                serializedResult.append("nil, ");
                continue;
            }

            // ② 一般情况：向sb对象中 追加当前结点的value
            serializedResult.append(currentNode.val + ", ");

            // #3 向队列的队尾 按顺序添加 当前节点的左子节点、右子节点;
            nodeSimpleQueueInPreOrder.offer(currentNode.left); // offer() = add()
            nodeSimpleQueueInPreOrder.offer(currentNode.right);
        }

        return serializedResult.toString();
    }

    // 创建一个二叉树对象root: 1 - 2 3  - nil nil 4 5
    private static TreeNode constructABinaryTree() {
        // 根节点
        TreeNode root = new TreeNode(1);

        // 左右子结点
        TreeNode left_child = new TreeNode(2);
        TreeNode right_child = new TreeNode(3);

        root.left = left_child;
        root.right = right_child;

        // 右子节点的左右子节点
        TreeNode lefty_of_right_child = new TreeNode(4);
        TreeNode right_of_right_child = new TreeNode(5);
        right_child.left = lefty_of_right_child;
        right_child.right = right_of_right_child;

        // 返回二叉树的根节点（等价于 二叉树本身）
        return root;
    }
}

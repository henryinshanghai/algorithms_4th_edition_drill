package com.henry.leetcode_traning_camp.week_03.day02;

import java.util.Deque;
import java.util.LinkedList;

import static com.henry.leetcode_traning_camp.week_03.day02.Solution_codeC_04_method01_recursion.preOrderOfTree;

public class Solution_codeC_04_method02_BFS {
    public static void main(String[] args) {
        TreeNode root = createACertainTree();

        // 把二叉树对象转发成为一个字符串对象 aka 对二叉树对象进行序列化
        // 序列化后字符的顺序是二叉树的前序遍历结果
        String serial_res = serialize(root);

        System.out.println("二叉树序列化的结果为： " + serial_res);

        System.out.println("=======================");

        TreeNode resume_tree = deserialize(serial_res);
        System.out.println("使用字符串反序列化得到的二叉树结果为： ");
        preOrderOfTree(resume_tree);


    }
    /**
     * 从一个字符串反序列化得到一棵树
     * 注：从一个字符串得到一个树可能会有多种结果 但是这里我们会使用特定的顺序
     * 手段：指针 + 使用数字字符来创建树节点
     */
    private static TreeNode deserialize(String data) {
        // 0 鲁棒性代码
        if (data == "") return null;

        // 1 准备一个队列 用于顺序逐一处理字符串中的数字字符
        Deque<TreeNode> deque = new LinkedList<>();

        // 2 拆分字符串，得到子字符串的数组
        String[] values = data.split(", ");

        // 3 使用子字符串数组的第一个元素来创建树的根节点
        TreeNode root = new TreeNode(Integer.valueOf(values[0]));

        // 4 把创建的根节点添加到队列中
        deque.addLast(root);

        // 5 准备一个循环   在循环中：1 遍历剩下的字符； 2 使用剩下的字符来逐一创建树中的节点并建立链接关系
        for (int i = 1; i < values.length; i++) { // 指针从第二个位置开始
            // 5-1 出列队首元素，并用它来创建树的根节点
            TreeNode currNode = deque.pollFirst();

            // 5-2 获取到数组指针指向的下一个元素，如果不是nil，说明是有效节点元素。则：按照顺序创建树节点并进行关联
            // 如果当前数组元素不是nil，说明这是一个有效的树节点。则：
            if (!values[i].equals("nil")) {
                // 使用这个数组元素来创建一个树节点
                TreeNode left_child = new TreeNode(Integer.valueOf(values[i]));

                // （按照特定的规则）把创建的树节点连接到对应的节点上去
                currNode.left = left_child;

                // 把新的树节点添加到队列中去    循环会把这个新节点取出，再去给它建立树节点的连接关系
                deque.addLast(left_child);
            }

            // 如果指针指向的当前元素的下一个元素不是nil，说明下一个元素也是有效的树节点。则：
            if (!values[++i].equals("nil")) {
                TreeNode right_child = new TreeNode(Integer.valueOf(values[i]));
                currNode.right = right_child;
                deque.addLast(right_child);
            }
        }

        return root;
    }

    /**
     * 对一棵树按照特定规则进行序列化，得到一个序列化后的字符串
     * 手段：对树进行层序遍历来得到“序列化后的字符串”
     * @param root  指定的树对象
     * @return
     */
    private static String serialize(TreeNode root) {
        // 准备一个队列   用于支持层序遍历
        Deque<TreeNode> deque = new LinkedList<>();
        // 准备一个StringBuffer对象   用于添加遍历到的树节点元素值
        StringBuffer sb = new StringBuffer();

        // 2 把树的根节点入队
        deque.addLast(root);

        // 3 准备一个循环
        while (!deque.isEmpty()) {
            TreeNode currNode = deque.pollFirst();
            if (currNode == null) { // 如果队列中的元素为nil，说明当前树节点是一个nil节点。则：向sb对象中添加一个"nil"字符串
                sb.append("nil, ");
                continue;
            }
            sb.append(currNode.val + ", ");

            deque.addLast(currNode.left);
            deque.addLast(currNode.right);
        }

        return sb.toString();

    }

    private static TreeNode createACertainTree() {
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

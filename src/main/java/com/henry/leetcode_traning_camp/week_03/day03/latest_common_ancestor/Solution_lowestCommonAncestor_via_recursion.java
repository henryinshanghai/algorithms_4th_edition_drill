package com.henry.leetcode_traning_camp.week_03.day03.latest_common_ancestor;

// 验证：可以使用 递归 + 分类讨论的方式 来 找到 树中的两个结点的最近公共祖先结点
// 递归方法的作用：在树中找到 结点P/结点Q/结点P与结点Q的公共祖先结点 找到啥算啥
public class Solution_lowestCommonAncestor_via_recursion {
    public static void main(String[] args) {
        TreeNode root = buildTree();

        // 调用方法 来 返回两个树节点在二叉树中的最低公共祖先节点
//        TreeNode lca = lowestCommonAncestorIn(root, root.left.right, root.left); // 2 & 5 => 5
        TreeNode lca = lowestCommonAncestorIn(root, root.left.right.left, root.right); // 7 & 1 => 3

        System.out.println("二叉树中p、q节点的公共祖先节点的值为： " + lca.val);
    }

    /*
            3
          5   1
        6  2 0  8
          7 4
     */
    private static TreeNode buildTree() {
        TreeNode root = new TreeNode(3);

        root.left = new TreeNode(5);
        root.right = new TreeNode(1);

        TreeNode leftSubNodeOfLefty = new TreeNode(6);
        TreeNode rightSubNodeOfLefty = new TreeNode(2);

        root.left.left = leftSubNodeOfLefty;
        root.left.right = rightSubNodeOfLefty;

        TreeNode leftSubNodeOfRighty = new TreeNode(0);
        TreeNode rightSubNodeOfRighty = new TreeNode(8);

        root.right.left = leftSubNodeOfRighty;
        root.right.right = rightSubNodeOfRighty;

        TreeNode leftLeafNode = new TreeNode(7);
        TreeNode rightLeafNode = new TreeNode(4);

        rightSubNodeOfLefty.left = leftLeafNode;
        rightSubNodeOfLefty.right = rightLeafNode;

        return root;
    }

    // 递归方法作用：在 以指定结点为根结点的树 中，查找 目标节点：#1 结点P; 或 #2 结点Q；或 #3 结点P与结点Q的最近公共祖先结点
    // 🐖 使用 后序遍历 才能把结果向上传给 “当前的根结点”
    private static TreeNode lowestCommonAncestorIn(TreeNode currentRootNode, TreeNode nodeP, TreeNode nodeQ) {
        // case0: 如果 当前节点为nil，说明 递归已经执行到了叶子节点，仍旧没能找到 目标结点，则：
        if (currentRootNode == null) {
            // 返回 null树 给上一级
            return null;
        }

        // case1: 如果当前结点 是 结点P 或者 结点Q，说明 找到了目标节点，则：
        if (currentRootNode == nodeP || currentRootNode == nodeQ) {
            // 返回其本身 给上一级
            return currentRootNode;
        }

        // 本级递归需要做的事情：
        // #1 在左子树中，查找目标节点；
        TreeNode ancestor_in_left = lowestCommonAncestorIn(currentRootNode.left, nodeP, nodeQ);
        // #2 在右子树中，查找目标节点；
        TreeNode ancestor_in_right = lowestCommonAncestorIn(currentRootNode.right, nodeP, nodeQ);

        // 如果左右子树中查找的目标节点 都不为null，说明 结点P与结点Q分别位于左右子树中，则：
        if (ancestor_in_left != null && ancestor_in_right != null) {
            // 把“根节点” 作为 结点P与结点Q的最近公共祖先 返回给上一级
            return currentRootNode;
        } else if (ancestor_in_left != null) { // 如果 只是“左子树中的祖先结点”不为null，说明 结点P与结点Q都存在于左子树，则：
            // 把“左子树所返回的结点” 作为 lca(p,q) 接着返回给上一级
            return ancestor_in_left;
        } else if (ancestor_in_right != null) { // 如果 只是右子树的祖先不为null，说明 结点P与结点Q都存在于右子树，则：
            // 把“右子树所返回的结点” 作为 lca(p,q) 接着返回给上一级
            return ancestor_in_right;
        } else { // 如果 左子树的祖先 与 右子树的祖先 都为null，说明 结点P与结点Q不存在于“以当前节点作为根结点的子树”中,则：
            // 把null 返回给上一级,用于表示 当前子树中不存在结点P/结点Q
            return null;
        }
    }
}

class TreeNode {
    int val;

    TreeNode left;
    TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }
}

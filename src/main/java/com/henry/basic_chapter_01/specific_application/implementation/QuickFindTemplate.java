package com.henry.basic_chapter_01.specific_application.implementation;

import edu.princeton.cs.algs4.StdIn;

/**
 * 用于解决 “动态连通性问题”  see introduce
 * 作用：
 * 1 记录元素集合中任意两个元素之间的连通性；
 * 2 对任意两个元素执行连通操作；
 * 3 判断指定的两个元素之间是否连通；
 * 4 返回集合中当前连通分量（子数组）的数量
 * <p>
 * 手段1：quick-find算法
 * 特征：对于同一个分量中的不同元素，nodeToItsGroupIdArray[]数组中存储的是同一个值————分量的id标识
 */
// 执行手段：redirect from input ： <tinyUF>
public class QuickFindTemplate {
    private int[] nodeToItsGroupIdArray; // 存储节点 -> 节点所属子组的信息
    private int groupAmount; // 集合分组后，子集合的数量

    public QuickFindTemplate(int nodeAmount) {
        groupAmount = nodeAmount; // 初始化分量数量 = 元素数量

        // 数组容量初始化
        nodeToItsGroupIdArray = new int[nodeAmount];
        // 数组元素初始化
        for (int currentNode = 0; currentNode < nodeAmount; currentNode++) {
            // 初始状态：groupId = 节点本身的值 - 后继连接节点后，会把同一个组中的节点 映射到 同一个组Id中
            nodeToItsGroupIdArray[currentNode] = currentNode;
        }
    }

    public int getConnectedComponentAmount() {
        return groupAmount;
    }

    public boolean isConnectedBetween(int nodeP, int nodeQ) {
        return findGroupIdOf(nodeP) == findGroupIdOf(nodeQ);
    }


    /**
     * 辅助方法：返回触点/元素所在的分量（子集合）
     * 说明：需要一个东西能够标识其所在的分量；
     * 已知：现在使用id[]来存储所有的分量信息
     * 元素连接后，分量的个数减少 但是id[]数组的长度并不会减小
     * <p>
     * 手段1：同一个分量中的各个元素对应的值(分量id)相同；
     * 具体方法：nodeToItsGroupIdArray[]使用元素作为索引，使用分量的唯一标识作为值；
     *
     * @param passedNode
     * @return
     */
    private int findGroupIdOf(int passedNode) {
        return nodeToItsGroupIdArray[passedNode]; // 返回id[]数组在索引p处存储的值：分量的唯一标识
    }

    /**
     * 把两个元素连接起来（到相同的分量中）
     *  @param nodeP
     * @param nodeQ*/
    public void unionToSameGroup(int nodeP, int nodeQ) {
        int groupIdOfNodeP = findGroupIdOf(nodeP);
        int groupIdOfNodeQ = findGroupIdOf(nodeQ);

        // 判断两个元素是不是已经属于同一分量了   手段：nodeToItsGroupIdArray[]数组中存储的分量标识是否相等
        if (groupIdOfNodeP == groupIdOfNodeQ) {
            return; // 如果已经在一个分量中，说明已经连通了 不需要操作
        }

        // 把两个元素添加到同一个分量（子集合）中  手段：把其中一个的分量标识id设置为另一个的分量标识Id
        for (int currentNode = 0; currentNode < nodeToItsGroupIdArray.length; currentNode++) {
            // 把 两个node 合并到 同一个子组中去
            // 手段：为 nodeP 绑定 nodeQ的分组id
            if (nodeToItsGroupIdArray[currentNode] == groupIdOfNodeP) { // 找到 nodeP
                nodeToItsGroupIdArray[currentNode] = groupIdOfNodeQ; // 设置它在 nodeToItsGroupArray[]中的值
            }
        }

        // 把分量的个数-1
        groupAmount--;
    }

    public static void main(String[] args) {
        // 从输入流中读取 节点数量
        int nodeAmount = StdIn.readInt();
        QuickFindTemplate nailBoard = new QuickFindTemplate(nodeAmount);

        while (!StdIn.isEmpty()) {
            // 读取整数对 pair
            int nodeP = StdIn.readInt();
            int nodeQ = StdIn.readInt();

            // 判断这对元素是否已经连通
            if (nailBoard.isConnectedBetween(nodeP, nodeQ)) {
                continue; // 如果已经连通了，就什么都不做
            }

            nailBoard.unionToSameGroup(nodeP, nodeQ); // 否则，就把两个元素连接到同一个分量中
            System.out.println("在 " + nodeP + " " + nodeQ + " 之间建立连接");

        }

        System.out.println(nailBoard.getConnectedComponentAmount() + "分量（子集合）");
    }
}

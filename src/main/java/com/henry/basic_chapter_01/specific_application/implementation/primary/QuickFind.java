package com.henry.basic_chapter_01.specific_application.implementation.primary;

import edu.princeton.cs.algs4.StdIn;

// 验证：可以使用 QuickFind算法 来 解决 连通性问题；
// 概念：连通性问题 - 对于一幅无向图，判断给定的两个节点 是不是连通的？
// 特征：之所以叫做 quick find，是因为 我们能够 快速找到 元素所属的分组groupId
// 核心API：#1 合并； #2 获取节点所属分组；#3 判断两个节点之间是否相互连通
public class QuickFind {
    // 用于记录 元素 -> 元素所属的分组 映射关系的数组
    private int[] numToItsGroupIdArray;
    // 组的数量
    private int groupAmount;

    // 类的构造器 用于创建对象
    public QuickFind(int itemAmount) {
        groupAmount = itemAmount;
        numToItsGroupIdArray = new int[itemAmount];

        // 为每个元素 初始化 其所属的组别ID 为 其本身大小
        for (int currentNum = 0; currentNum < numToItsGroupIdArray.length; currentNum++) {
            // 初始化 其组别 为 其本身
            numToItsGroupIdArray[currentNum] = currentNum;
        }
    }


    /**********************************************************
     * 为了完成任务而设计的APIs
     **********************************************************/

    /**
     * 把 两个元素 合并到 同一个组中
     *
     * @param num1 元素1
     * @param num2 元素2
     */
    public void unionToSameComponent(int num1, int num2) {
        // #1 获取到元素1 所属的分组、元素2 所属的分组
        int groupIdOfNum1 = findGroupIdOf(num1);
        int groupIdOfNum2 = findGroupIdOf(num2);

        // #2 如果 两个元素 同属于 同一个分组，说明 不需要进行合并操作，
        if (groupIdOfNum1 == groupIdOfNum2) {
            // 则：直接 return
            return;
        }

        /* #3 把 group1中所有元素的分组 更改为 group2 */
        // 遍历所有元素，对于当前元素... 🐖 由于需要遍历所有元素，因此 union操作会比较耗时
        for (int currentNum = 0; currentNum < numToItsGroupIdArray.length; currentNum++) {
            // 如果 该元素的组别是 group1，说明 它是 group1中的元素，则：
            if (numToItsGroupIdArray[currentNum] == groupIdOfNum1) {
                // 把 它的组别 更改成group2，使它成为 group2中的元素
                numToItsGroupIdArray[currentNum] = groupIdOfNum2;
            }
        }

        // #4 在完成 group1中的元素 与 group2中的元素 合并后，把 组的数量-1
        groupAmount--;
    }

    // 找到 指定元素 所属的分组
    public int findGroupIdOf(int num) {
        return numToItsGroupIdArray[num];
    }

    // 获取到 所有组的数量
    public int getGroupAmount() {
        return groupAmount;
    }


    /**
     * 判断 两个节点之间 是否相连通
     * 手段：判断 两个元素 是否 在同一个组中
     * 原理：我们 把 相互连通的元素 都会 放到同一个组中
     *
     * @param num1 元素1
     * @param num2 元素2
     * @return 如果 两个节点之间相连通，则 返回true。如果不连通，则 返回false
     */
    public boolean isConnectedBetween(int num1, int num2) {
        return findGroupIdOf(num1) == findGroupIdOf(num2);
    }

    /***********************************************
     * 单元测试：使用所提供的API 来 完成预期任务（把相互连通的节点合并到同一个组中）
     ***********************************************/
    public static void main(String[] args) {
        // 从 标准输入中 读取 int类型的整数 来 作为 节点对的数量(同时也是 节点的上限(不包含))
        int vertexAmount = StdIn.readInt();
        // 使用 读取到的最大上限 作为参数 来 初始化QF
        QuickFind separatedVertexes = new QuickFind(vertexAmount);

        while (!StdIn.isEmpty()) {
            // 从标准输入中 读取当前行 所提供的节点1、节点2
            int vertex1 = StdIn.readInt();
            int vertex2 = StdIn.readInt();

            // 如果 两个节点之间 是相连通的，说明 不需要再 对它们做union操作，
            if (separatedVertexes.isConnectedBetween(vertex1, vertex2)) {
                // 则：跳过 本轮循环
                System.out.println("+++ " + vertex1 + " " + vertex2 + " 在同一个stream中，不需要合并 +++");
                continue;
            }

            // 如果 两个节点之间 当前不是相连通的，说明 需要对它们进行union操作，则：执行合并
            separatedVertexes.unionToSameComponent(vertex1, vertex2);
            System.out.println("--- 把 " + vertex1 + " 与 " + vertex2 + " 合并到同一个小组中 ---");
        } // 循环结束后，所有相互连通的元素 都已经在同一个小组中了

        System.out.println("separatedVertexes中的元素，经过标准输入所表示的合并操作后，最后剩余有" + separatedVertexes.getGroupAmount() + "个小组");
    }
}

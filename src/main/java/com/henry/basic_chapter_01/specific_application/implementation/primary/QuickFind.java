package com.henry.basic_chapter_01.specific_application.implementation.primary;

import edu.princeton.cs.algs4.StdIn;

// 验证：可以使用 QuickFind算法 来 解决 连通性问题；
// 概念：连通性问题 - 对于一幅无向图，判断给定的两个节点 是不是连通的？
public class QuickFind {
    // 元素 -> 元素所属的分组
    private int[] numToItsGroupIdArray;
    // 组的数量
    private int groupAmount;

    public QuickFind(int maxNumber) {
        groupAmount = maxNumber;
        numToItsGroupIdArray = new int[maxNumber];

        // 为每个元素 初始化 其所属的组别ID
        for (int currentNum = 0; currentNum < numToItsGroupIdArray.length; currentNum++) {
            numToItsGroupIdArray[currentNum] = currentNum;
        }
    }

    // 把 两个元素 合并到 同一个组中
    public void unionToSameComponent(int num1, int num2) {
        int groupIdOfNum1 = findGroupIdOf(num1);
        int groupIdOfNum2 = findGroupIdOf(num2);

        // 如果 本身就是同一个组的元素，则：不需要合并，直接return
        if (groupIdOfNum1 == groupIdOfNum2) {
            return;
        }

        // 遍历所有的元素...
        for (int currentNum = 0; currentNum < numToItsGroupIdArray.length; currentNum++) {
            // 如果 元素的组别是 group1，说明 它是 group1中的元素，则：
            if (numToItsGroupIdArray[currentNum] == groupIdOfNum1) {
                // 把它的组别 更改成group2，使它成为 group2中的元素
                numToItsGroupIdArray[currentNum] = groupIdOfNum2;
            }
        }

        // group1与group2合并完成后，组的数量-1
        groupAmount--;
    }

    public int findGroupIdOf(int num) {
        return numToItsGroupIdArray[num];
    }

    public int getGroupAmount() {
        return groupAmount;
    }

    // 判断两个节点是否相连通
    public boolean isConnectedBetween(int num1, int num2) {
        // 手段：判断两个元素 是否在同一个组中
        // 原理：我们把 相互连通的元素 都会放到同一个组中
        return findGroupIdOf(num1) == findGroupIdOf(num2);
    }

    public static void main(String[] args) {
        int maxNumber = StdIn.readInt();
        QuickFind team = new QuickFind(maxNumber);

        while (!StdIn.isEmpty()) {
            int num1 = StdIn.readInt();
            int num2 = StdIn.readInt();

            if (team.isConnectedBetween(num1, num2)) {
                System.out.println("+++ " + num1 + " " + num2 + " 在同一个stream中，不需要合并 +++");
                continue;
            }

            team.unionToSameComponent(num1, num2);
            System.out.println("--- 把 " + num1 + " 与 " + num2 + " 合并到同一个小组中 ---");
        }

        System.out.println("team中最后存在有" + team.getGroupAmount() + "个小组");
    }
}

package com.henry.basic_chapter_01.specific_application.implementation.primary;

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
 * 特征：对于同一个分量中的不同元素，numToItsGroupIdArray[]数组中存储的是同一个值————分量的id标识
 */
// 执行手段：redirect from input ： <tinyUF>
public class QuickFindTemplate {
    private int[] numToItsGroupIdArray; // 存储节点 -> 节点所属子组的信息
    private int groupAmount; // 集合分组后，子集合的数量

    public QuickFindTemplate(int itemAmount) {
        groupAmount = itemAmount; // 初始化分量数量 = 数字对中所存在的最大数字

        // 数组容量初始化
        numToItsGroupIdArray = new int[itemAmount];
        // 数组元素初始化
        for (int currentNumber = 0; currentNumber < itemAmount; currentNumber++) {
            // 初始状态：groupId = 数字本身 - 后继连接数字对后，会把数字对中的两个数字的 groupId设置为一样的值
            numToItsGroupIdArray[currentNumber] = currentNumber;
        }
    }

    public int getComponentAmount() {
        return groupAmount;
    }

    public boolean isConnectedBetween(int num1, int num2) {
        return findGroupIdOf(num1) == findGroupIdOf(num2);
    }


    /**
     * 辅助方法：返回触点/元素所在的分量（子集合）
     * 说明：需要一个东西能够标识其所在的分量；
     * 已知：现在使用id[]来存储所有的分量信息
     * 元素连接后，分量的个数减少 但是id[]数组的长度并不会减小
     * <p>
     * 手段1：同一个分量中的各个元素对应的值(分量id)相同；
     * 具体方法：numToItsGroupIdArray[]使用元素作为索引，使用分量的唯一标识作为值；
     *
     * @param passedNumber
     * @return
     */
    private int findGroupIdOf(int passedNumber) {
        return numToItsGroupIdArray[passedNumber]; // 返回id[]数组在索引p处存储的值：分量的唯一标识
    }

    /**
     * 把两个元素连接起来（到相同的分量中）
     * @param num1
     * @param num2*/
    public void unionToSameGroup(int num1, int num2) {
        int groupIdOfNum1 = findGroupIdOf(num1);
        int groupIdOfNum2 = findGroupIdOf(num2);

        // 判断两个元素是不是已经属于同一分量了   手段：numToItsGroupIdArray[]数组中存储的groupId是否相等
        if (groupIdOfNum1 == groupIdOfNum2) {
            return;
        }

        // 把两个元素添加到同一个分量（子集合）中  手段：把其中一个的分量标识id设置为另一个的分量标识Id
        for (int currentNum = 0; currentNum < numToItsGroupIdArray.length; currentNum++) {

            // 对于组别为groupIdOfNum1的所有元素...
            if (numToItsGroupIdArray[currentNum] == groupIdOfNum1) {
                // 把它们的groupId设置为 num2的groupId - 从而把两者连通起来/分到同一组中 🐖 这可能会访问并修改多个数组元素
                numToItsGroupIdArray[currentNum] = groupIdOfNum2;
            }
        }

        // 把分量的个数-1
        groupAmount--;
    }

    public static void main(String[] args) {
        // 从输入流中读取 节点数量
        int maxNumber = StdIn.readInt();
        QuickFindTemplate team = new QuickFindTemplate(maxNumber); // team下面存在有不同的 stream

        while (!StdIn.isEmpty()) {
            // 读取整数对 pair
            int num1 = StdIn.readInt();
            int num2 = StdIn.readInt();

            // 判断这对元素是否已经连通
            if (team.isConnectedBetween(num1, num2)) {
                continue; // 如果已经连通了，就什么都不做
            }

            team.unionToSameGroup(num1, num2); // 否则，就把两个元素连接到同一个分量中
            System.out.println("在 " + num1 + " " + num2 + " 之间建立连接");

        }

        System.out.println(team.getComponentAmount() + "分量（子集合）");
    }
}

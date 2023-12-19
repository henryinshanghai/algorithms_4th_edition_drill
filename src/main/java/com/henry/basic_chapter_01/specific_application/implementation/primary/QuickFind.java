package com.henry.basic_chapter_01.specific_application.implementation.primary;

import edu.princeton.cs.algs4.StdIn;

public class QuickFind {
    private int[] numToItsGroupIdArray;
    private int groupAmount;

    public QuickFind(int maxNumber) {
        groupAmount = maxNumber;
        numToItsGroupIdArray = new int[maxNumber];

        for (int currentNum = 0; currentNum < numToItsGroupIdArray.length; currentNum++) {
            numToItsGroupIdArray[currentNum] = currentNum;
        }
    }

    public void unionToSameComponent(int num1, int num2) {
        int groupIdOfNum1 = findGroupIdOf(num1);
        int groupIdOfNum2 = findGroupIdOf(num2);

        if (groupIdOfNum1 == groupIdOfNum2) {
            return;
        }

        for (int currentNum = 0; currentNum < numToItsGroupIdArray.length; currentNum++) {
            if (numToItsGroupIdArray[currentNum] == groupIdOfNum1) {
                numToItsGroupIdArray[currentNum] = groupIdOfNum2;
            }
        }

        groupAmount--;
    }

    public int findGroupIdOf(int num) {
        return numToItsGroupIdArray[num];
    }

    public int getGroupAmount() {
        return groupAmount;
    }

    public boolean isConnectedBetween(int num1, int num2) {
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

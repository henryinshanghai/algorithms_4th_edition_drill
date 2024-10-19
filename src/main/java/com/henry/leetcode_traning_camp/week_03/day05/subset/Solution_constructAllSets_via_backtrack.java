package com.henry.leetcode_traning_camp.week_03.day05.subset;

import java.util.ArrayList;
import java.util.List;

// 验证：对于 增量式地构建结果集 类型的“构造所有子集”的问题，可以使用 回溯算法 + 在N叉树的每一个结点收集子集的做法 来 得到所有可能的子集
// #1 为了避免重复，需要使用 startItemIndex 来 指定可选元素的开始位置； #2 为了收集所有子集，需要在每次递归开始时，收集“当前子集”
public class Solution_constructAllSets_via_backtrack {
    public static void main(String[] args) {
        int[] numSequence = {1, 2, 3};
        List<List<Integer>> legitSetList = generateAllSubSetsFrom(numSequence);

        print(legitSetList);
    }

    private static void print(List<List<Integer>> legitSetList) {
        for (int currentSetCursor = 0; currentSetCursor < legitSetList.size(); currentSetCursor++) {
            List<Integer> currentSet = legitSetList.get(currentSetCursor);
            System.out.println(currentSet);
        }

        System.out.println();
    }

    private static List<List<Integer>> legitSetList = new ArrayList<>();
    private static List<Integer> currentGeneratedSet = new ArrayList<>();

    private static List<List<Integer>> generateAllSubSetsFrom(int[] numSequence) {
        int numAmount = numSequence.length;
        if (numAmount == 0) {
            return legitSetList;
        }

        generateAllPossibleSets(numSequence, 0);
        return legitSetList;
    }

    /**
     * #0 递归方法作用：在包含有N个自然数的序列中，由指定的位置开始，构造出不限大小的子集；
     *  @param numSequence    原始的元素序列
     * @param startItemIndex 从[startItemIndex, numSequence.size())的区间中选择元素 来 构造子集 - 用于避免构造出重复子集
     */
    private static void generateAllPossibleSets(int[] numSequence, int startItemIndex) {
        // 收获结果：把”当前子集“添加到 “结果集”中
        // 为什么是在这个位置收获结果?? 答：因为每次遇到一个N叉树中的结点，就需要把当前结点所对应的子集 添加到结果集中
        List<Integer> legitSet = new ArrayList<>(currentGeneratedSet);
        legitSetList.add(legitSet);

        // #1 递归终结条件 - 🐖 这里的递归终结条件是optional的
        // 如果 可选元素区间的起始位置 大于等于 原始序列的长度，说明 当前已经没有任何元素可选，则：
        if (startItemIndex >= numSequence.length) {
            // 返回到上一级调用
            return;
        }

        // 对于每一个“当前元素”，构造出 由它作为起始元素的合法子集
        for (int currentIndex = startItemIndex; currentIndex < numSequence.length; currentIndex++) { // 每次分叉都从当前调用所传入的start索引位置开始————所以能够不重不漏
            // #2-1 把“当前索引所对应的元素” 添加到当前子集中
            int currentItem = numSequence[currentIndex];
            currentGeneratedSet.add(currentItem);

            // #2-2 递归地 从剩余的(N-1)个元素中，挑选元素 用于构造子集
            // 🐖 递归调用结束后，N叉树会一层层地返回到 当前结点
            generateAllPossibleSets(numSequence, currentIndex + 1);

            // #2-3 回溯掉”当前的选择“，以便 算法能够”重新选择“后继的元素
            currentGeneratedSet.remove(currentGeneratedSet.size() - 1);
        }
    }
}

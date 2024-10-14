package com.henry.leetcode_traning_camp.week_03.day04.permunation;

import java.util.*;

// 验证：可以使用回溯法 来 增量式地构造出 指定元素序列的所有排列；
// 子问题：使用元素序列中“未被使用”的元素 来 构造排列中的剩余部分
public class Solution_constructPermutations_via_backtrack {
    public static void main(String[] args) {
        // 原始的元素序列
        int[] numSequence = new int[]{1, 2, 3};
        // 获取到此元素序列 所能得到的所有的全排列
        List<List<Integer>> permutationList = permute(numSequence);
        // 逐个打印这些个全排列
        System.out.println(permutationList);
    }

    // 准备元素为列表的列表   用于存储每一个全排列结果
    private static List<List<Integer>> permutationList = new ArrayList<>();
    // 用于存储当前得到的全排列结果
    private static Deque<Integer> currentGeneratedPermutation = new ArrayDeque<Integer>();

    private static List<List<Integer>> permute(int[] numSequence) {
        // 健壮性代码
        int numAmount = numSequence.length;
        if (numAmount == 0) {
            return permutationList;
        }

        // 准备一个 boolean类型的数组    用于记录 元素有没有被使用过(“标记法”)
        boolean[] itemIndexToIfUsedArr = new boolean[numAmount];


        // 求出 所有可能的全排列,并返回
        constructPermutationFrom(numSequence, itemIndexToIfUsedArr);
        return permutationList;

    }

    // #0 递归方法的作用：在[1,N]的正整数序列中，得到由其所有元素组成的全排列。求出所有的排列
    private static void constructPermutationFrom(int[] numSequence, boolean[] itemIndexToIsUsedArr) {
        // #1 递归终结条件 构造出了一个完整的全排列
        if (currentGeneratedPermutation.size() == numSequence.length) {
            List<Integer> legitPermutation = new ArrayList<>(currentGeneratedPermutation);
            permutationList.add(legitPermutation);
            return;
        }

        // #2 遍历序列中的每一个数字元素
        for (int currentItemIndex = 0; currentItemIndex < numSequence.length; currentItemIndex++) {
            boolean hasBeenUsed = itemIndexToIsUsedArr[currentItemIndex];
            if (!hasBeenUsed) {
                // #2-1 选择当前元素 来 构造当前排列
                int currentItem = numSequence[currentItemIndex];
                itemIndexToIsUsedArr[currentItemIndex] = true;
                currentGeneratedPermutation.addLast(currentItem);

                // #2-2 在[1,N]的正整数序列中，得到由其中所有“未被使用过的元素”所组成的子排列。
                // 子问题的解就是原始问题的解的一部分
                constructPermutationFrom(numSequence, itemIndexToIsUsedArr);

                // #2-3 回溯“对当前元素的选择”
                itemIndexToIsUsedArr[currentItemIndex] = false;
                currentGeneratedPermutation.removeLast();
            }
        }
    }
}

package com.henry.leetcode_traning_camp.week_06.day05;

public class Solution_editDistance_03_dp_by_nationalSite_drill01 {
    public static void main(String[] args) {
        String word1 = "horse";
        String word2 = "ros";

        int operationTimes = minDistance(word1, word2); // 3

        System.out.println("从 " + word1 + "得到 " + word2 + "所需要的最少操作数为：" + operationTimes);
    }

    private static int minDistance(String word1, String word2) {
        /* 〇 把字符串转化成为字符数组   方便得到子字符序列 */
        char[] charSequence1 = word1.toCharArray();
        char[] charSequence2 = word2.toCharArray();

        /* Ⅰ 准备一个dpTable[]  这里有两个需要比较的字符串，所以dpTable[][] */
        int[][] dpTable = new int[charSequence1.length + 1][charSequence2.length + 1];

        /* Ⅱ 初始化dpTable[][]中的一些位置  用来build up the dpTable[][] from */
        for (int i = 0; i < dpTable.length; i++) {
//            dpTable[0][i] = i;
            dpTable[i][0] = i;
        }

        for (int i = 0; i < dpTable[0].length; i++) {
//            dpTable[i][0] = i;
            dpTable[0][i] = i;
        }
        
        /* Ⅲ 使用 已经初始化的元素值 + 不同规模子问题之间的递推关系 来 填充dpTable[][] */
        for (int i = 1; i <= charSequence1.length; i++) {
            for (int j = 1; j <= charSequence2.length; j++) { // EXPR2: 这里是<=
                // 1 比较最后一个位置的字符是否相等  如果相等,说明xxx。则问题可以直接简化为...
//                char lastCharInSequence1 = charSequence1[i - 1];
//                char lastCharInSequence2 = charSequence1[j - 1];
//                if (lastCharInSequence1 == lastCharInSequence2) { // this is weird, why calculate them first will make it wrong?
                if (charSequence1[i - 1] == charSequence2[j - 1]) {
                    // correct content: charSequence1[i - 1] == charSequence2[j - 1]
                    // 当前问题的值 就等于 子问题的元素值
                    dpTable[i][j] = dpTable[i - 1][j - 1];
                } else {
                    // 否则，找出回退得到的子问题的最小解
                    dpTable[i][j] = 1 + Math.min(
                            dpTable[i - 1][j],
                            Math.min(
                                    dpTable[i][j - 1],
                                    dpTable[i - 1][j - 1]
                            )
                    );
                }
            }
        }


        /* Ⅳ 返回dpTable[][]中最右下角的值 */
        return dpTable[charSequence1.length][charSequence2.length];
    }
}
/*
puzzle: why if I calculate the char first, them compare the last char would lead up to a different result?
 */

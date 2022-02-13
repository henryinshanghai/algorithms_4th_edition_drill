package com.henry.leetcode_traning_camp.week_06.day05;

public class Solution_editDistance_03_dp_by_nationalSite_drill02 {
    public static void main(String[] args) {
        String word1 = "horse";
        String word2 = "ros";

        int operationTimes = minDistance(word1, word2); // 3

        System.out.println("从 " + word1 + "得到 " + word2 + "所需要的最少操作数为：" + operationTimes);
    }

    private static int minDistance(String word1, String word2) {
        char[] charSequence1 = word1.toCharArray();
        char[] charSequence2 = word2.toCharArray();

        // the number inside the [] is array's size, not index
        // the relationship of size and biggest index is : biggest_index = size - 1
        int[][] dpTable = new int[charSequence1.length + 1][charSequence2.length + 1];

        // do some initialization
        for (int i = 0; i < dpTable.length; i++) {
            dpTable[i][0] = i; // EXPR1: this binding matter
        }

        for (int i = 0; i < dpTable[0].length; i++) {
            dpTable[0][i] = i;
        }

        // fill up the dpTable
        for (int i = 1; i <= charSequence1.length; i++) {
            for (int j = 1; j <= charSequence2.length; j++) { // EXPR2: this we need to use <=
                // compare the last char of current subSequences
                if (charSequence1[i - 1] == charSequence2[j - 1]) {
                    // bind this smaller sub-problem's solution to this bigger problem
                    dpTable[i][j] = dpTable[i - 1][j - 1];
                } else {
                    // make a move based on the cheapest foundation
                    dpTable[i][j] = 1 + Math.min(
                            dpTable[i - 1][j], // based on a foundation that if you do an insert, you get the goal
                            // sequence
                            Math.min(
                                    dpTable[i][j - 1], // based on a foundation that if you do an delete, you get the
                                    // goal sequence
                                    dpTable[i - 1][j - 1] // based on a foundation that if you do a replace, you'll
                                    // get the goal sequence
                            )
                    );
                }
            }
        }

        return dpTable[charSequence1.length][charSequence2.length];
    }
}
/*
the puzzle remains.
why when i do get and compare separately, the result went off the trial
🐏
 */

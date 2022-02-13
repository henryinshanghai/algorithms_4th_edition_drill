package com.henry.leetcode_traning_camp.week_06.day05;

import java.util.Arrays;

public class Solution_perfectSquares_02_method01_dp_by_happygirl_drill01 {
    public static void main(String[] args) {
        int num = 12;
        int minimumNum = numSquares(num);
        System.out.println("凑出正整数 " + num + " 所需要的完全平方数的最少个数为：" + minimumNum);
    }

    private static int numSquares(int num) {
        // 1 prepare the dpTable
        int[] dpTable = new int[num + 1];

        // 2 fill the dpTable with a value, so that we can update each item later
        Arrays.fill(dpTable, num);
        
        // 3 prepare the first couple of item's value, to build up the dpTable all the way to the end
        dpTable[0] = 0;
        dpTable[1] = 1;
        
        // 4 to 'make up to N step by step'
        for (int i = 1; i <= num; i++) {

            // for each subProblem i, we try to make it up with the choices collection
            // how do you represent the choices collection?
            // dpTable[i] equals to either (itself) or (its subProblem's solution + 1)
            for (int j = 1; j * j <= i; j++) { // when to stop?
                dpTable[i] = Math.min(dpTable[i], dpTable[i - j * j] + 1);
            }
        }

        return dpTable[num];
    }
}

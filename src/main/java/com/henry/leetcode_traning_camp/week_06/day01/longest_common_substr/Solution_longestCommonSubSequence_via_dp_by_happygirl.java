package com.henry.leetcode_traning_camp.week_06.day01.longest_common_substr;

// 验证：对于 两个字符串之间所存在的最长公共子序列的长度 这样的问题，可以使用 动态规划的思路 来 得到答案
// dp[][]数组的含义：currentStrsComboToItsLCSS - 指定的子字符串组合(subStr1 × subStr2)间 所存在的最长公共子字符串的长度
// 最优子结构：原始问题的最优解 包含有 子问题的最优解；
// 子字符串之间的 LCSS(longest common subsequence)的长度，能够用于得到 原始字符串之间的LCSS的长度
// 原理：情形#1 新增的字符相等 - 此时只需要 把X+1； 情形#2 新增的字符不相等 - 此时则需要keep the max value of(left, above)；
public class Solution_longestCommonSubSequence_via_dp_by_happygirl {
    public static void main(String[] args) {
        String str1 = "ABCDGHLQR";
        String str2 = "AEDPHR"; // ADHR  公共子序列的长度为4

//        int longestCommonSubSequence = lcsDynamic(str1.toCharArray(), str2.toCharArray());

        int longestCommonSubSequence = getLongestCommonSubSequenceOf(str1, str2);
        System.out.println(str1 + " 与 " + str2 + "之间的最大公共序列的长度为：" + longestCommonSubSequence);
    }

    private static int getLongestCommonSubSequenceOf(String str1, String str2) {
        /* Ⅰ 准备dpTable[][] */
        int str1Length = str1.length();
        int str2Length = str2.length();

        int[][] currentStrsComboToItsLCSS = new int[str1Length + 1][str2Length + 1];

        /* Ⅱ 计算并填充dpTable[][]二维数组中元素的值 */
        int counter = 0;
        for (int str1CurrentCursor = 0; str1CurrentCursor < str1Length; str1CurrentCursor++) {
            for (int str2CurrentCursor = 0; str2CurrentCursor < str2Length; str2CurrentCursor++) {

                char str1CurrentCharacter = str1.charAt(str1CurrentCursor),
                        str2CurrentCharacter = str2.charAt(str2CurrentCursor);

                // 如果 两个指针 当前指向的字符相等，说明 最长公共子序列的长度 相对于上一个值要+1，
                if (str1CurrentCharacter == str2CurrentCharacter) {
                    // EXPR:这里计算的是 currentStrsComboToItsLCSS[str1CurrentCursor + 1][str2CurrentCursor + 1]
                    currentStrsComboToItsLCSS[str1CurrentCursor + 1][str2CurrentCursor + 1]
                            = currentStrsComboToItsLCSS[str1CurrentCursor][str2CurrentCursor] + 1;
                } else { // 如果 两个指针 当前指向的字符不相等，说明 这种情况下的最长公共子序列的长度
                    currentStrsComboToItsLCSS[str1CurrentCursor + 1][str2CurrentCursor + 1]
                            // #3 取两者中的较大值
                            = Math.max(currentStrsComboToItsLCSS[str1CurrentCursor + 1][str2CurrentCursor], // #1 要么是 长str1×短str2 这种组合的 最长公共子字符串的长度
                            currentStrsComboToItsLCSS[str1CurrentCursor][str2CurrentCursor + 1]); // #2 要么是 短str1×长str2 这种组合的 最长公共子序列的长度
                }
            }

            System.out.println("当前第" + (++counter) + "次循环👇");
            print2DimensionArr(currentStrsComboToItsLCSS);
        }

        System.out.println("在循环之外打印二维数组👇");
        print2DimensionArr(currentStrsComboToItsLCSS);

        /* Ⅲ 返回dpTable[][]中 符合题目要求的元素值 */
        System.out.println("str1Length: " + str1Length + ", str2Length: " + str2Length);
        return currentStrsComboToItsLCSS[str1Length][str2Length];
    }

    private static void print2DimensionArr(int[][] currentStrsComboToItsLCSS) {
        for (int currentRow = 0; currentRow < currentStrsComboToItsLCSS.length; currentRow++) {
            for (int currentColumn = 0; currentColumn < currentStrsComboToItsLCSS[currentRow].length; currentColumn++) {
                System.out.print(currentStrsComboToItsLCSS[currentRow][currentColumn] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }
}

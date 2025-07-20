package com.henry.leetcode_traning_camp.week_06.preview.longest_common_substr;

// 🐖 这里更直白地说明了 计算dp[][]元素的起点 是dp[1][1].
public class Solution_via_dp_by_tushor {
    public static void main(String[] args) {
        String str1 = "ABCDGHLQR";
        String str2 = "AEDPHR"; // ADHR  公共子序列的长度为4

        int lengthOfLCS = getLCSLengthBetween(str1.toCharArray(), str2.toCharArray());
        System.out.println(str1 + " 与 " + str2 + "之间的最大公共序列的长度为：" + lengthOfLCS);
    }

    public static int getLCSLengthBetween(char[] str1CharArr, char[] str2CharArr) {
        /* 〇 准备一些DP所需要的基础变量 */
        // 准备一个临时的二维数组  用于作为动态规划所需要的计算空间
        int currentComboToTheirLCSLength[][] = new int[str1CharArr.length + 1][str2CharArr.length + 1];

        /* Ⅰ 准备两层的嵌套循环    在循环中，填充二维数组中的每一个位置 */
        // 从dp[1][1]开始来计算dp[][]元素   🐖 这里的游标指针 并不是 字符串中的字符指针
        for (int currentRow = 1; currentRow < currentComboToTheirLCSLength.length; currentRow++) {
            for (int currentColumn = 1; currentColumn < currentComboToTheirLCSLength[currentRow].length; currentColumn++) {
                // EXPR：递推公式
                // 1 如果两个指针指向的字符相同，说明：这个字符可以用于构成最长公共子串
                int currentStr1Cursor = currentRow - 1;
                int currentStr2Cursor = currentColumn - 1;

                char currentStr1Character = str1CharArr[currentStr1Cursor];
                char currentStr2Character = str2CharArr[currentStr2Cursor];

                if (currentStr1Character == currentStr2Character) {
                    // 则：当前位置的元素值 = 左上角位置的元素值 + 1
                    currentComboToTheirLCSLength[currentRow][currentColumn]
                            = currentComboToTheirLCSLength[currentRow - 1][currentColumn - 1] + 1;
                } else {
                    // 2 如果两个指针指向的字符不相同，说明当前字符不能用来构成最长公共子串
                    // 则：当前位置的元素值(公共子序列的长度) = Max(上一行（同一列）的元素值, 同一行(上一列)的元素值)
                    currentComboToTheirLCSLength[currentRow][currentColumn]
                            = Math.max(currentComboToTheirLCSLength[currentRow][currentColumn - 1],
                                currentComboToTheirLCSLength[currentRow - 1][currentColumn]);
                }
            }
        }

        /* Ⅱ 返回最大值 */
        return currentComboToTheirLCSLength[str1CharArr.length][str2CharArr.length];
    }
}

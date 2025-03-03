package com.henry.leetcode_traning_camp.week_06.preview.longest_common_substr;

// 验证：对于 求两个字符串之间的 最长公共子序列这样的问题，可以使用
// #1 在两个字符串中使用游标截取字符串 + #2 动态规划 + #3 分类讨论 的方式 来 求出LCS的length
// dp[]数组的含义：当前的两个子字符串组合 => 它们的最长公共子序列的length；
// 最优子结构/子问题：dp[i][j] 与 更小规模的 dp[i-1][j] 有什么关系??
// 如果 当前游标所指向的字符相同，说明 当前组合的LCS 是在 更小组合的LCS的基础上 扩展了一个新字符，则：dp[i][j] = dp[i-1][j-1] + 1;
// 如果 当前游标所指向的字符不相同，说明 没能扩展字符，应该在所有可选的组合中 取最大值，则：dp[i][j] = max(dp[i][j-1], dp[i-1][j]);
public class Solution_via_dp_by_backTobackSWT {
    public static void main(String[] args) {
        String str1 = "ABCDGHLQR";
        String str2 = "AEDPHR"; // ADHR  公共子序列的长度为4

        int LCSLength = getLongestCommonSequenceLengthOf(str1, str2);
        System.out.println(str1 + " 与 " + str2 + "之间的最大公共序列的长度为：" + LCSLength);

    }

    private static int getLongestCommonSequenceLengthOf(String text1, String text2) {
        int text1Length = text1.length();
        int text2Length = text2.length(); // EXPR1：确定需要创建的DP二维数组的大小

        int[][] currentComboToTheirLCSLength = new int[text1Length + 1][text2Length + 1]; // EXPR2：在创建DP数组时，多出一行 & 一列

        // 在填充DPTable的过程中，我们会先计算第一行 & 第一列的值
        // 所以需要先对第一行 & 第一列进行初始化吗？   答：使用默认初始化的值（为0）即可
        for (int text1Cursor = 0; text1Cursor < text1Length; text1Cursor++) { // EXPR3：在进行计算数组元素值时，从0开始
            for (int text2Cursor = 0; text2Cursor < text2Length; text2Cursor++) {

                char currentText1Character = text1.charAt(text1Cursor),
                    currentText2Character = text2.charAt(text2Cursor);

                // 如果这组指针当前所指向的字符相同，说明 公共子序列被扩展了，则：
                if (currentText1Character == currentText2Character) {
                    // 相比于 先前combo的公共子序列长度，当前combo的公共子序列的长度 可以在其基础上 + 1
                    // 🐖 这里有点子奇怪，计算的是 dp[i+1][j+1] 而不是 dp[i][j]
                    currentComboToTheirLCSLength[text1Cursor + 1][text2Cursor + 1]
                            = currentComboToTheirLCSLength[text1Cursor][text2Cursor] + 1; // EXPR3：计算数组元素时，不需要再去计算第0-th行与第0-th列的元素值
                    System.out.println("当前text1与text2的字符相同，公共序列的长度值为： " + currentComboToTheirLCSLength[text1Cursor][text2Cursor]);
                } else { // 如果 所指向的字符不相同，说明 公共子序列没有被扩展，则：
                    currentComboToTheirLCSLength[text1Cursor + 1][text2Cursor + 1]
                            // 从先前所有可能的combo中 取最大值即可
                            = Math.max(currentComboToTheirLCSLength[text1Cursor + 1][text2Cursor], // henry
                                currentComboToTheirLCSLength[text1Cursor][text2Cursor + 1]); // mahendra
                    System.out.println("当前text1与text2的字符不相同，公共序列的长度值为： " + currentComboToTheirLCSLength[text1Cursor][text2Cursor]);
                }
            }
        }

        // 二维数组所表示的映射关系：currentCombo => their LCS length
        return currentComboToTheirLCSLength[text1Length][text2Length]; // EXPR4：最后返回DP数组最右下角的元素值
    }
}
/*
    启示：纸上得来终觉浅，绝知此事要躬行
    backToBackSWT的代码暂时看不到，这里是自己拼写的代码
 */

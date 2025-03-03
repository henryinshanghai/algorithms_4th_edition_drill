package com.henry.leetcode_traning_camp.week_06.preview.longest_common_substr;

// 🐖 #1 这里对于dp[][]元素的计算，是从dp[1][1]开始的；
// #2 这里没有对于dp[][]起始元素的初始化，因为 它们的默认值 就可以帮助我们计算dp[][]元素了
public class Solution_via_dp_by_happygirl {
    public static void main(String[] args) {
        String str1 = "ABCDGHLQR";
        String str2 = "AEDPHR"; // ADHR  公共子序列的长度为4

//        int lengthOfLCS = lcsDynamic(str1.toCharArray(), str2.toCharArray());

        int lengthOfLCS = getLongestCommonSequenceLengthOf(str1, str2);
        System.out.println(str1 + " 与 " + str2 + "之间的最大公共序列的长度为：" + lengthOfLCS);


    }

    private static int getLongestCommonSequenceLengthOf(String text1, String text2) {
        int text1Length = text1.length(), // text1的有效索引为 [0, text1Length-1]
            text2Length = text2.length(); // text2的有效索引为 [0, text2Length-1]

        // 行的有效索引索引为 [0, text1Length], 列的有效索引为[0, text2Length]
        // 🐖 为什么这里要 +1呢?
        // todo 拿个方格纸(goodNote) 比划比划应该就能明白 take a shower now
        int[][] currentComboToTheirLCSLength = new int[text1Length + 1][text2Length + 1];

        for (int text1Cursor = 0; text1Cursor < text1Length; text1Cursor++) { // [0, text1Length-1]
            for (int text2Cursor = 0; text2Cursor < text2Length; text2Cursor++) { // [0, text2Length-1]
                // 获取到当前的两个指针所指向的字符
                char currentText1Character = text1.charAt(text1Cursor),
                    currentText2Character = text2.charAt(text2Cursor);

                /* 从dp[1][1]开始 计算dp[][]数组 元素的正确值，第0行 与 第0列的值 保持默认值0即可 */
                // 比较这两个字符是否相同
                if (currentText1Character == currentText2Character) {
                    // 如果相同，说明这个字符可以用来构成公共子序列。则：
                    currentComboToTheirLCSLength[text1Cursor + 1][text2Cursor + 1] = currentComboToTheirLCSLength[text1Cursor][text2Cursor] + 1;
                } else {
                    // 如果不相同，说明这个字符不能够用来构成公共子序列。则：...
                    currentComboToTheirLCSLength[text1Cursor + 1][text2Cursor + 1] =
                            Math.max(currentComboToTheirLCSLength[text1Cursor + 1][text2Cursor],
                                    currentComboToTheirLCSLength[text1Cursor][text2Cursor + 1]);
                }
            }
        }

        return currentComboToTheirLCSLength[text1Length][text2Length]; // 二维数组的最右下角
    }
}

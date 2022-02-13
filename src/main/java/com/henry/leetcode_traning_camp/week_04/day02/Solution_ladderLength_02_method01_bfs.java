package com.henry.leetcode_traning_camp.week_04.day02;

import java.util.*;

public class Solution_ladderLength_02_method01_bfs {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> wordList =  Arrays.asList("hot","dot","dog","lot","log");

        int shortestChangePath = ladderLength(beginWord, endWord, wordList);
        System.out.println("最短转换序列的长度为： " + shortestChangePath);
    }

    private static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        // 准备一个Set对象    用于去除不再需要的元素/重复元素
        Set<String> set = new HashSet<>(wordList);

        // 准备一个queue对象      用于支持实现BFS（列举所有可能性）
        Queue<String> queue = new LinkedList<>();

        // 入队原始字符串 beginWord
        queue.add(beginWord);

        int level = 0; // 初始化一个整数  用于表示“转换序列”的长度

        // 开始BFS的典型while循环
        while (!queue.isEmpty()) {
            // 记录当前queue中元素的个数
            int levelSize = queue.size();

            // 遍历当前层的每一个节点
            for (int i = 0; i < levelSize; i++) {
                // 获取到当前节点/当前待转换的单词
                String curr = queue.remove();
                char[] wordUnit = curr.toCharArray(); // 把当前单词转化成为字符数组

                // 判断当前单词是不是就已经是目标单词了
                if (curr.equals(endWord)) return level + 1; // 返回当前转换序列的长度

                // 遍历当前单词中的每一个字符
                for (int j = 0; j < curr.length(); j++) {
                    // 获取到当前字符
                    char currChar = wordUnit[j];

                    // 替换当前字符，替换后重新创建字符串并判断字符串是不是在 validTempWordList中
                    for (char c = 'a'; c <= 'z'; c++) { // 遍历字符序列：a - z
                        // 替换当前字符
                        wordUnit[j] = c;
                        // 创建替换后的字符串
                        String tempWord = new String(wordUnit);
                        // 判断替换后的字符串是不是已经在 validTempWordList中了
                        if (set.contains(tempWord)) {
                            queue.add(tempWord); // 找到了序列中一个有效的中间变化结果，把它添加到队列中
                            set.remove(tempWord); // 从 validTempWordList中移除它————防止出现自己变化成自己的死循环
                        }
                    }

                    // 替换回来当前字符————以便进行下一个字符的替换工作
                    wordUnit[j] = currChar;
                }
            }

            // 当前层所有节点遍历结束后，BFS开始遍历下一层的节点 对应到本题中，就是转换序列中的下一个可能字符串
            level++;
        }

        // 如果while循环执行完成，说明没有找到endWord 这时候按照题意要求，返回0
        return 0;
    }
}

package com.henry.leetcode_traning_camp.week_04.day02;

import java.util.*;

public class Solution_ladderLength_02_method01_bfs_drill01 {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
//        List<String> wordList =  Arrays.asList("hot","dot","dog","lot","log");
        List<String> wordList =  Arrays.asList("hot","dot","dog","lot","log","cog");

        int shortestChangePath = ladderLength(beginWord, endWord, wordList);
        System.out.println("最短转换序列的长度为： " + shortestChangePath);
    }

    private static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> validTempWordList = new HashSet<>(wordList);
        Queue<String> queue = new LinkedList<>();

        int level = 0;
        queue.add(beginWord);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                String currWord = queue.remove();
                char[] charCombine = currWord.toCharArray();

                if (currWord.equals(endWord)) return level + 1;
                for (int j = 0; j < currWord.length(); j++) {
                    char currChar = charCombine[j];

                    for (char ch = 'a'; ch <= 'z'; ch++) {
                        charCombine[j] = ch;
                        String tempWord = new String(charCombine);
                        if (validTempWordList.contains(tempWord)) {
                            queue.add(tempWord);
                            validTempWordList.remove(tempWord);
                        }
                    }

                    charCombine[j] = currChar;
                }
            }

            level++;
        }

        return 0;
    }
}

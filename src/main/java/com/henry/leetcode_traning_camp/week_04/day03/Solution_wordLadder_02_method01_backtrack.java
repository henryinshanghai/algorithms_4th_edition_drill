package com.henry.leetcode_traning_camp.week_04.day03;

import java.util.*;

public class Solution_wordLadder_02_method01_backtrack {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";

        List<String> wordList = new ArrayList<>(
                Arrays.asList("hot", "dot", "dog", "lot", "log", "cog")
        );

        int length = ladderLength(beginWord, endWord, wordList);
        System.out.println("从 \"" + beginWord + "\" 到 \"" + endWord + "\" 的最短转换序列的长度为： " + length);
    }

    private static int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Queue<String> queue = new LinkedList<>();
        Set<String> validTempWords = new HashSet<>(wordList);

        queue.add(beginWord);
        int sequenceLength = 0;

        while (!queue.isEmpty()) {

            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                String currWord = queue.poll();
                if (currWord.equals(endWord)) return sequenceLength + 1;

                char[] currWordArr = currWord.toCharArray();
                for (int j = 0; j < currWordArr.length; j++) {
                    char currChar = currWordArr[j];

                    for (char ch = 'a'; ch <= 'z'; ch++) {
                        currWordArr[j] = ch;
                        String tempWord = new String(currWordArr);
                        if (validTempWords.contains(tempWord)) {
                            queue.add(tempWord);
                            validTempWords.remove(tempWord);
                        }
                    }

                    currWordArr[j] = currChar;
                }
            }

            sequenceLength++;
        }

        return 0;
    }
}

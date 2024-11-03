package com.henry.leetcode_traning_camp.week_04.day02.change_to_target_word;

import java.util.*;

// 验证：可以 使用 BFS 以 在每一层中，验证单词变体 是不是 目标单词 来 得到“经有效中间变体得到目标单词的最短转换序列的长度”
// 原理：#1 转换单词变体的任务，可以抽象为 在一棵树中找到目标结点的任务；
// #2 使用BFS能够在一棵树中，找到到达目标结点的最短路径；
public class Solution_wordTransformSequence_shortest_length_via_bfs {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> validVariantWordList = Arrays.asList("hot", "dot", "dog", "lot", "log");

        int shortestSequenceLength = transformSequenceLengthPer(beginWord, endWord, validVariantWordList);
        System.out.println("从起始单词转换到目标单词 的最短转换序列的长度为： " + shortestSequenceLength);
    }

    private static int transformSequenceLengthPer(String beginWord,
                                                  String endWord,
                                                  List<String> validVariantWordList) {
        // 准备一个Set对象    用于去除不再需要的元素/重复元素
        Set<String> validVariantWordSet = new HashSet<>(validVariantWordList);
        // 准备一个queue对象      用于支持实现对 “所有可能产生的变体单词” 的BFS（列举所有可能性）
        Queue<String> validVariantWordQueue = new LinkedList<>();

        // 入队起始单词 beginWord
        validVariantWordQueue.add(beginWord);

        int currentLevel = 0; // 初始化一个整数  用于表示逻辑树中路径的长度 aka “转换序列”的长度

        // 开始BFS的典型while循环
        while (!validVariantWordQueue.isEmpty()) {
            // 记录当前queue中元素的个数
            int validVariantWordAmountOnCurrentLevel = validVariantWordQueue.size();

            // 遍历当前层的每一个节点
            for (int currentLevelCursor = 0; currentLevelCursor < validVariantWordAmountOnCurrentLevel; currentLevelCursor++) {
                // 获取到 “当前节点/当前变体单词”
                String currentValidVariantWord = validVariantWordQueue.remove();
                // 如果“当前变体单词” equals “目标单词”，说明 转换得到了 目标单词，则：
                if (currentValidVariantWord.equals(endWord)) {
                    // 返回 当前层的层数 + 1（因为currentLevel是从0开始的），即为 转换序列的长度
                    return currentLevel + 1;
                }

                // 把“当前变体单词”转化成为“字符数组” - 方便对其每一个位置上的字符做操作
                char[] currentValidVariantLetterArr = currentValidVariantWord.toCharArray();
                // 对于“当前有效变体单词”中的每一个位置
                for (int currentCharacterSpotToReplace = 0; currentCharacterSpotToReplace < currentValidVariantWord.length(); currentCharacterSpotToReplace++) {
                    // 获取到“当前位置上的字符”
                    char currentCharacter = currentValidVariantLetterArr[currentCharacterSpotToReplace];

                    // 使用[a-z]来逐一替换“当前字符”，替换后得到“变体字符串” 并 判断字符串是不是在 validTempWordList中
                    for (char replacedLetter = 'a'; replacedLetter <= 'z'; replacedLetter++) { // 遍历字符序列：a - z
                        // 替换“当前位置上的字符”为“替换字符” 来 得到“变体单词”
                        currentValidVariantLetterArr[currentCharacterSpotToReplace] = replacedLetter;
                        // 从“替换字符后的变体字符数组” 来 得到一个“可能的变体单词”
                        String currentPossibleVariantWord = new String(currentValidVariantLetterArr);

                        // 如果 这个“可能的变体单词字符串” 存在于 “有效变体单词集合” 中，说明 找到了 转换序列中的一个“有效的中间变化结果”，则：👇
                        if (validVariantWordSet.contains(currentPossibleVariantWord)) {
                            // #1 把它添加到“有效变体单词队列”中 来 把所有“有效变体单词”接在树的下一层
                            validVariantWordQueue.add(currentPossibleVariantWord);
                            // #2 从“有效变体单词集合”中 移除此“有效变体单词” 来 防止出现“再次变化成为此单词变体”的情况
                            validVariantWordSet.remove(currentPossibleVariantWord);
                        }
                    }

                    // 替换回来“原来的字符”————以便进行下一个字符的替换工作
                    currentValidVariantLetterArr[currentCharacterSpotToReplace] = currentCharacter;
                }
            }

            // 当前层所有节点遍历结束后，BFS开始遍历下一层的节点 对应到本题中，就是转换序列中的下一个可能字符串
            currentLevel++;
        }

        // 如果while循环执行完成，说明没有找到endWord 这时候按照题意要求，返回0
        return 0;
    }
}

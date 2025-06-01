package com.henry.leetcode_traning_camp.week_04.day02.change_to_target_word;

import java.util.*;

// 验证：可以 使用 BFS 以 在每一层中，验证单词变体 是不是 目标单词 来 得到“经有效中间变体得到目标单词的最短转换序列的长度”
// 原理：#1 转换单词变体的任务，可以抽象为 在一棵树中找到目标结点的任务；
// #2 使用BFS能够在一棵树中，找到到达目标结点的最短路径；
public class Solution_wordTransformSequence_shortest_length_via_bfs {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> validMiddleWordList = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
//        List<String> validMiddleWordList = Arrays.asList("hot", "dot", "dog", "lot", "log");

        int shortestSequenceLength = transformSequenceLengthWhen(beginWord, endWord, validMiddleWordList);
        System.out.println("从起始单词转换到目标单词 的最短转换序列的长度为： " + shortestSequenceLength);
    }

    private static int transformSequenceLengthWhen(String beginWord,
                                                   String endWord,
                                                   List<String> validMiddleWordList) {
        // 准备一个Set对象    用于去除不再需要的元素/重复元素
        Set<String> validMiddleWordSet = new HashSet<>(validMiddleWordList);

        // 准备一个queue对象      用于支持实现对 “所有可能产生的变体单词” 的BFS（列举所有可能性）
        Queue<String> validReplacedResultQueue = new LinkedList<>();
        // 入队起始单词 beginWord
        validReplacedResultQueue.add(beginWord);

        int currentLevel = 0; // 初始化一个整数  用于表示逻辑树中路径的长度 aka “转换序列”的长度

        // 开始BFS的典型while循环
        while (!validReplacedResultQueue.isEmpty()) {
            // 记录当前queue中元素的个数
            int middleWordAmountOnCurrentLevel = validReplacedResultQueue.size();

            // 遍历当前层的每一个节点
            for (int currentLevelCursor = 0; currentLevelCursor < middleWordAmountOnCurrentLevel; currentLevelCursor++) {
                // 获取到 “当前节点/当前中间单词”
                String currentMiddleWord = validReplacedResultQueue.remove();
                // 如果“当前中间单词” equals “目标单词”，说明 转换得到了 目标单词，则：
                if (currentMiddleWord.equals(endWord)) {
                    // 返回 当前层的层数 + 1（因为currentLevel是从0开始的），即为 转换序列的长度
                    return currentLevel + 1;
                }

                // 把“当前中间单词”转化成为“字符数组” - 方便对其每一个位置上的字符做操作
                char[] currentMiddleWordLetterArr = currentMiddleWord.toCharArray();
                // 对于“当前有效变体单词”中的每一个位置
                for (int currentSpotToReplaceCharacter = 0; currentSpotToReplaceCharacter < currentMiddleWord.length(); currentSpotToReplaceCharacter++) {
                    // 获取到“当前位置上的字符”
                    char currentCharacter = currentMiddleWordLetterArr[currentSpotToReplaceCharacter];

                    // 使用[a-z]来逐一替换“当前字符”，替换后得到“变体字符串” 并 判断字符串是不是在 validTempWordList中
                    for (char replacedLetter = 'a'; replacedLetter <= 'z'; replacedLetter++) { // 遍历字符序列：a - z
                        // 替换“当前位置上的字符”为“替换字符” 来 得到“变体单词”
                        currentMiddleWordLetterArr[currentSpotToReplaceCharacter] = replacedLetter;
                        // 从“替换字符后的变体字符数组” 来 得到“替换字符后的单词”
                        String currentReplacedResult = new String(currentMiddleWordLetterArr);

                        // 如果 这个“替换字符后的单词” 存在于 “有效变体单词集合” 中，说明 找到了 转换序列中的一个“有效的中间变化结果”，则：👇
                        if (validMiddleWordSet.contains(currentReplacedResult)) {
                            // #1 把它添加到“有效中间单词队列”中 来 把所有“有效变体单词”接在树的下一层
                            validReplacedResultQueue.add(currentReplacedResult);
                            // #2 从“有效中间单词集合”中 移除此“有效变体单词” 来 防止出现“再次变化成为此单词变体”的情况
                            validMiddleWordSet.remove(currentReplacedResult);
                        }
                    }

                    // 替换回来“原来的字符”————以便进行下一个字符的替换工作
                    currentMiddleWordLetterArr[currentSpotToReplaceCharacter] = currentCharacter;
                }
            }

            // 当前层所有节点遍历结束后，BFS开始遍历下一层的节点 对应到本题中，就是转换序列中的下一个可能字符串
            currentLevel++;
        }

        // 如果while循环执行完成，说明没有找到endWord 这时候按照题意要求，返回0
        return 0;
    }
}

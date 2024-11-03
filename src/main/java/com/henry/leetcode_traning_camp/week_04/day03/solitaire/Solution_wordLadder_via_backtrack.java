package com.henry.leetcode_traning_camp.week_04.day03.solitaire;

import java.util.*;

// 验证：对于 经过“有效的单词变体”得到“目标单词”的问题，可以使用 BFS 来 得到最短转换路径的长度；
// 相关概念：替换字符后的单词变体、有效的单词变体、有效的单词变体的集合、有效的单词变体队列、当前层序号
public class Solution_wordLadder_via_backtrack {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";

        List<String> validWordVariants = new ArrayList<>(
                Arrays.asList("hot", "dot", "dog", "lot", "log", "cog")
        );

        int shortestTransformSequenceLength = ladderLength(beginWord, endWord, validWordVariants);
        System.out.println("从 \"" + beginWord + "\" 到 \"" + endWord + "\" 的最短转换序列的长度为： " + shortestTransformSequenceLength);
    }

    private static int ladderLength(String beginWord, String endWord, List<String> validWordVariantsList) {
        Queue<String> validWordVariantQueueOfCurrentLevel = new LinkedList<>();
        Set<String> validWordVariantSet = new HashSet<>(validWordVariantsList);

        validWordVariantQueueOfCurrentLevel.add(beginWord);
        int currentLevelAmount = 0;

        while (!validWordVariantQueueOfCurrentLevel.isEmpty()) {

            int validVariantWordsAmountOfCurrentLevel = validWordVariantQueueOfCurrentLevel.size();
            for (int currentValidVariantWordCursor = 0; currentValidVariantWordCursor < validVariantWordsAmountOfCurrentLevel; currentValidVariantWordCursor++) {
                // 获取到 队列中的队首元素 aka “当前有效的单词变体”
                String currentValidVariantWord = validWordVariantQueueOfCurrentLevel.poll();

                // 如果 当前有效的单词变体 与 目标单词 相等，说明 找到了 转化到目标单词的最短路径，则：
                if (currentValidVariantWord.equals(endWord))
                    // 把 currentLevelAmount + 1, 作为 转换序列的长度 返回
                    return currentLevelAmount + 1;

                // 尝试 “当前有效的单词变体” 所能产生的 所有单词变体
                char[] currentValidVariantCharacterArr = currentValidVariantWord.toCharArray();
                // 当前位置...
                for (int currentCharacterSpotToReplace = 0; currentCharacterSpotToReplace < currentValidVariantCharacterArr.length; currentCharacterSpotToReplace++) {
                    char characterOnCurrentSpot = currentValidVariantCharacterArr[currentCharacterSpotToReplace];

                    // 当前替换字符...
                    for (char replacedCharacterOption = 'a'; replacedCharacterOption <= 'z'; replacedCharacterOption++) {
                        // 替换 当前位置上的字符
                        currentValidVariantCharacterArr[currentCharacterSpotToReplace] = replacedCharacterOption;
                        // 得到 “替换字符后的单词变体”
                        String possibleWordVariantStr = new String(currentValidVariantCharacterArr);

                        // 如果 该替换字符后的单词变体 存在于 有效单词变体集合 中，说明 找到了转换的 “下一个有效中间结果”，则：
                        if (validWordVariantSet.contains(possibleWordVariantStr)) {
                            // #1 把 该单词变体 添加到 “有效单词变体队列”中
                            validWordVariantQueueOfCurrentLevel.add(possibleWordVariantStr);
                            // #2 从 “有效的单词变体集合” 中，移除 该单词变体 - 以防止序列中出现环
                            validWordVariantSet.remove(possibleWordVariantStr);
                        }
                    }

                    // 恢复 当前位置上的字符，继续产生其他单词变体
                    currentValidVariantCharacterArr[currentCharacterSpotToReplace] = characterOnCurrentSpot;
                }
            }

            currentLevelAmount++;
        }

        return 0;
    }
}

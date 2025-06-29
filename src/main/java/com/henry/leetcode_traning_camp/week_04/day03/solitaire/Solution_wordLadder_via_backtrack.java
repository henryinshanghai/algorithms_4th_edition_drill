package com.henry.leetcode_traning_camp.week_04.day03.solitaire;

import java.util.*;

// 验证：对于 经过“有效的单词变体”得到“目标单词”的问题，可以使用 BFS 来 得到最短转换路径的长度；
// 相关概念：替换字符后的单词变体、有效的单词变体、有效的单词变体的集合、有效的单词变体队列、当前层序号
// 🐖 我们并不需要完整地构建出图/最短转换序列，才能够得到 最短序列的长度。只要在遇到每个节点时+1就可以了
public class Solution_wordLadder_via_backtrack {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";

        List<String> validMiddleWordList = new ArrayList<>(
                Arrays.asList("hot", "dot", "dog", "lot", "log", "cog")
        );

        int shortestTransformSequenceLength = getShortestLadderLength(beginWord, endWord, validMiddleWordList);
        System.out.println("从 \"" + beginWord + "\" 到 \"" + endWord + "\" 的最短转换序列的长度为： " + shortestTransformSequenceLength);
    }

    private static int getShortestLadderLength(String beginWord,
                                               String endWord,
                                               List<String> validMiddleWordList) {
        Set<String> validMiddleWordSet = new HashSet<>(validMiddleWordList);

        Queue<String> validReplacedResultQueue = new LinkedList<>();
        validReplacedResultQueue.add(beginWord);

        int currentLevelSerialNumber = 0;

        // 每轮循环 处理图中的一层结点
        while (!validReplacedResultQueue.isEmpty()) {
            // #1 获取 图中 当前层上的节点数量
            int middleWordAmountOnCurrentLevel = validReplacedResultQueue.size();

            // #2 基于 当前层中的单词 + 可用的单词变体 来 扩展出下一层的单词（OR 返回结果序列的总长度）
            for (int currentLevelCursor = 0; currentLevelCursor < middleWordAmountOnCurrentLevel; currentLevelCursor++) {
                // 对于“当前层中的当前单词”...
                String currentMiddleWord = validReplacedResultQueue.poll();

                /* 特殊场景：找到了目标单词 */
                // 如果 当前单词 与 目标单词 相等，说明 找到了 “转化到目标单词的最短序列”，则：
                if (currentMiddleWord.equals(endWord))
                    // 把 当前转换序列的长度 + 1（目标单词） 来 作为 转换序列的长度 返回
                    return currentLevelSerialNumber + 1;

                /* 得到 所有由“当前单词” 所能转换得到的 单词变体，并以此来 #1 扩展队列； #2 更新可用单词变体集合 */
                char[] currentMiddleWordCharacterArr = currentMiddleWord.toCharArray();
                // 对于每一个当前位置...
                for (int currentSpotToReplace = 0; currentSpotToReplace < currentMiddleWordCharacterArr.length; currentSpotToReplace++) {
                    // 获取到 当前位置上的字符
                    char characterOnCurrentSpot = currentMiddleWordCharacterArr[currentSpotToReplace];

                    // 使用26个字母 来 逐个尝试替换当前位置上的字符...
                    for (char replacedLetter = 'a'; replacedLetter <= 'z'; replacedLetter++) {
                        // 先替换掉 当前位置上的字符
                        currentMiddleWordCharacterArr[currentSpotToReplace] = replacedLetter;
                        // 而后得到 “替换字符后的单词变体”
                        String currentReplacedResult = new String(currentMiddleWordCharacterArr);

                        // 如果 该“替换字符后的单词变体” 存在于 有效单词变体集合 中，说明 找到了转换的 “下一个有效中间结果”，则：
                        if (validMiddleWordSet.contains(currentReplacedResult)) {
                            // #1 把 “该有效中间结果” 添加到 “单词队列”中
                            validReplacedResultQueue.add(currentReplacedResult);
                            // #2 从 “有效的单词变体集合” 中，移除 “该有效中间结果” - 以防止序列中出现环
                            validMiddleWordSet.remove(currentReplacedResult);
                        }
                    }

                    // 恢复 当前位置上的字符，继续产生其他单词变体
                    currentMiddleWordCharacterArr[currentSpotToReplace] = characterOnCurrentSpot;
                }
            }

            // #3 在图中当前层所有的结点/单词都处理完成后，把层数+1
            // 🐖 这里的层数就是转换序列的长度
            currentLevelSerialNumber++;
        }

        // 如果while{}循环中的返回点没有返回，说明 不存在到“目标单词”的转换序列，则：返回0
        return 0;
    }
}

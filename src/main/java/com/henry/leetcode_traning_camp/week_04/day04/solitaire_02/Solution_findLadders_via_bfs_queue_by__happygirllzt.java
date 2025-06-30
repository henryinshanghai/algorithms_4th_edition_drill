package com.henry.leetcode_traning_camp.week_04.day04.solitaire_02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

// 验证：可以使用BFS + DFS的手段 来 得到由“起始单词”经由“有效的单词变体 中间结果”得到“目标单词”的 所有具体转换序列
// 原理：BFS（递归实现） 用于 构建图中 word -> itsValidWordVariants 的映射关系；
// DFS（递归实现）+回溯 用于 图中所存在的“到目标单词的转换序列”。
// 🐖 DFS/BFS的基本作用是：找到图中与指定顶点相连通的所有顶点
public class Solution_findLadders_via_bfs_queue_by__happygirllzt {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> validMiddleWordList =
                new ArrayList<>(
                        Arrays.asList("hot", "dot", "dog", "lot", "log", "cog")
                );

        List<List<String>> allShortestTransformSequences = findLadders(beginWord, endWord, validMiddleWordList);
        System.out.println("最终的结果为：" + allShortestTransformSequences);
    }

    // 准备一个 元素为list的list对象 - 用于存放所有找到的 “最短转换路径”
    public static List<List<String>> allWantedPathList = new ArrayList<>();

    private static List<List<String>> findLadders(String beginWord,
                                                  String endWord,
                                                  List<String> validMiddleWordList) {

        // 把wordList转化成为一个set对象 用于更快速地判断 是否存在重复元素
        Set<String> validMiddleWordSet = new HashSet<>(validMiddleWordList);

        // 鲁棒性代码 - endWord一定要出现在 可选的单词变体集合中，否则 不存在任何的转换序列，则：
        if (!validMiddleWordSet.contains(endWord)) {
            // 直接return空列表
            return allWantedPathList;
        }

        /* #1 使用bfs 来 构建出图/(word -> itsTransformedWordVariants 一对多映射关系) */
        Map<String, List<String>> wordToItsChangedMiddleWordsMap = new HashMap<>();
        // 准备一个集合(用于存储所有的“起始单词”), 然后把当前的beginWord放进去
        Set<String> startWordSetOnCurrentLevel = new HashSet<>();
        startWordSetOnCurrentLevel.add(beginWord);

        // 构建出 对应的无权无向图
        generateTheMapViaBFS(startWordSetOnCurrentLevel,
                endWord,
                wordToItsChangedMiddleWordsMap, // map参数的作用：在方法执行的过程中被构建
                validMiddleWordSet);

        /* #2 查看下map对象|图 是否 “按照预期被创建” */
        printThe(wordToItsChangedMiddleWordsMap);

        // #3 使用DFS+回溯 来 从图中得到 所有可能的“到目标单词的转换序列” //
        // 准备一个path（用于 收集“到目标单词的转换序列”中的所有单词）然后把beginWord放进去
        LinkedList<String> pathTowardsEndWord = new LinkedList<>();
        pathTowardsEndWord.addFirst(endWord);

        // 使用dfs来生成结果
        generatePathListViaDFS(beginWord,
                endWord,
                wordToItsChangedMiddleWordsMap,
                pathTowardsEndWord
        );


        return allWantedPathList;

    }

    // 打印出所有 word -> its transformed variants的映射关系
    private static void printThe(Map<String, List<String>> wordToItsChangedMiddleWordsMap) {
        if (wordToItsChangedMiddleWordsMap == null) return;

        for (Map.Entry<String, List<String>> currentEntry : wordToItsChangedMiddleWordsMap.entrySet()) {
            String currentWord = currentEntry.getKey();
            List<String> itsChangedMiddleWords = currentEntry.getValue();

            System.out.print("Key - " + currentWord + " | ");
            System.out.println("Value - " + itsChangedMiddleWords);
        }
    }

    /**
     * 在构建好的图中，找到 所需要的路径 pathTowardsEndWord，并把它添加到 pathList中
     * 🐖 为了支持头插元素，这里的 pathTowardsEndWord的类型是 LinkedList
     */
    private static void generatePathListViaDFS(String beginWord,
                                               String endWord,
                                               Map<String, List<String>> wordsToItsChangedMiddleWordsMap,
                                               LinkedList<String> pathTowardsEndWord) {
        // Ⅰ 如果起始单词 与 目标单词 相同，说明
        // #1 要么 不需要进行任何转换 就得到了目标单词(特殊情况) OR
        // #2 要么 转换序列终于转换到了 目标单词(一般情况)，则：
        if (beginWord.equals(endWord)) {
            // 把 当前到endWord的路径（而不是目标单词本身） 添加到 结果列表中
            allWantedPathList.add(new ArrayList(pathTowardsEndWord)); // 因为path本身会被回溯而变化，所以我们使用的是一个新的副本
            // 即刻返回 以 停止当前级方法的继续调用，返回到上一级
            return;
        }

        // Ⅱ 如果“当前起始单词” 不存在 任何“有效的单词变体”，说明 由当前起始单词经“有效单词变体” 无法转变成为“目标单词”，则：
        if (wordsToItsChangedMiddleWordsMap.get(endWord) == null) {
            // 直接return 以 停止当前级方法的调用
            return;
        }

        // Ⅲ 如果“当前起始单词” 存在有 一堆的“有效的单词变体”，说明 由当前起始单词 经“有效单词变体” 有可能转变得到“目标单词”，则：
        // 遍历每一个“有效的单词变体”
        for (String currentChangedMiddleWord : wordsToItsChangedMiddleWordsMap.get(endWord)) {
            // ① 把 当前“有效的单词变体” 添加到 “由起始单词转变到目标单词的路径”中
            pathTowardsEndWord.addFirst(currentChangedMiddleWord);

            // ② 调用dfs 以 得到完整的“到目标单词的路径”
            generatePathListViaDFS(beginWord, // 子问题：此参数发生了变化
                    currentChangedMiddleWord,
                    wordsToItsChangedMiddleWordsMap,
                    pathTowardsEndWord // path参数也发生了变化
            );

            // ③ 回溯当前选择 以便选择下一个“有效的单词变体” 来 构造“到目标单词的路径”
            pathTowardsEndWord.removeFirst();
        }
    }

    /**
     * 每一个level的单词会越来越多，所以使用一个startSet作为参数
     * 一个endWord参数
     * 还需要一个映射关系map：描述一个单词所能转换到的其他有效单词
     * 特征：这个BFS并没有使用到Queue
     * 🐖 这种BFS的实现使用了queue，以此来避免栈溢出的可能
     * 🐖 这里构建的图是反向的 terminal -> depart,为此 dfs的代码也要做对应的修改
     */
    private static void generateTheMapViaBFS(Set<String> startWordSet,
                                             String endWord,
                                             Map<String, List<String>> wordToItsChangedMiddleWordMap,
                                             Set<String> validMiddleWordSet) {

        // 使用队列替代递归实现BFS
        Queue<String> startWordQueue = new LinkedList<>();
        // 将初始单词集合加入队列
        startWordQueue.addAll(startWordSet);

        // 标记是否找到目标单词
        boolean isTransformFinished = false;

        while (!startWordQueue.isEmpty() && !isTransformFinished) {
            // 记录当前层级的节点数量
            int startWordAmountOnCurrentLevel = startWordQueue.size();
            // 用于存储当前层级处理过的单词，避免重复处理
            Set<String> usedMiddleWordsOnCurrentLevel = new HashSet<>();

            // 处理层之前，先把层中的middleWord从validMiddleWordSet中全部移除
            validMiddleWordSet.removeAll(startWordQueue);

            // 处理当前层级的所有节点
            for (int currentStartWordCursor = 0; currentStartWordCursor < startWordAmountOnCurrentLevel; currentStartWordCursor++) {
                // 列举当前单词可能转换到的所有可用的转换结果
                String currentStartWordStr = startWordQueue.poll();
                char[] currentStartWordCharacterArr = currentStartWordStr.toCharArray();

                // 对于"当前起始单词"中的每一个位置...
                for (int currentSpotToReplace = 0; currentSpotToReplace < currentStartWordCharacterArr.length; currentSpotToReplace++) {
                    // 获取到当前位置上的字符
                    char currentCharacter = currentStartWordCharacterArr[currentSpotToReplace];

                    // 尝试使用[a-z]范围内的所有字符对其逐一替换
                    for (char replacedLetter = 'a'; replacedLetter <= 'z'; replacedLetter++) {
                        // 替换字符
                        currentStartWordCharacterArr[currentSpotToReplace] = replacedLetter;
                        // 得到一个"替换字符后的单词变体"
                        String currentReplacedResult = new String(currentStartWordCharacterArr);

                        // 如果"替换字符后的字符串"存在于"有效的单词变体集合"中，说明找到了一个 中间单词
                        if (validMiddleWordSet.contains(currentReplacedResult)) {
                            // 如果当前"替换字符后的单词变体"就是"目标单词",说明转换过程结束(但图还差最后一步生成完成)，则：
                            if (endWord.equals(currentReplacedResult)) {
                                // 把flag标记为true
                                isTransformFinished = true;
                            } else if (!usedMiddleWordsOnCurrentLevel.contains(currentReplacedResult)) { // 如果该中间单词在当前层没有被使用过，说明 可以把它添加到队列中，则：
                                // 把它 添加到“已使用的middleWord”中
                                usedMiddleWordsOnCurrentLevel.add(currentReplacedResult);
                                // 把它添加到 startWordQueue队列中
                                startWordQueue.offer(currentReplacedResult);
                            }

                            // 构建图  手段：建立map中的映射关系
                            // 🐖 为了能够通过leetcode上的测试用例，这里需要建立的是 terminal -> depart 的图
                            wordToItsChangedMiddleWordMap.computeIfAbsent(currentReplacedResult, key -> new ArrayList<>());
                            wordToItsChangedMiddleWordMap.get(currentReplacedResult).add(currentStartWordStr);
                        }
                    }

                    // 恢复当前位置上的字符
                    currentStartWordCharacterArr[currentSpotToReplace] = currentCharacter;
                }
            }
        }
    }
}
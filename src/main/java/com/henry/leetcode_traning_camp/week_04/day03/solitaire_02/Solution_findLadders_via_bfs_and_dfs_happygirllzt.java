package com.henry.leetcode_traning_camp.week_04.day03.solitaire_02;

import java.util.*;

// 验证：可以使用BFS + DFS的手段 来 得到由“起始单词”经由“有效的单词变体 中间结果”得到“目标单词”的 所有具体转换序列
// 原理：BFS 用于 构建图中 word -> itsValidWordVariants 的映射关系；
// DFS+回溯 用于 图中存在的、所有可能的“到目标单词的转换序列”。
public class Solution_findLadders_via_bfs_and_dfs_happygirllzt {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> validWordVariantList =
                new ArrayList<>(
                        Arrays.asList("hot", "dot", "dog", "lot", "log", "cog")
                );

        List<List<String>> allValidTransformSequenceList = findLadders(beginWord, endWord, validWordVariantList);
        System.out.println("最终的结果为：" + allValidTransformSequenceList);
    }

    private static List<List<String>> findLadders(String beginWord,
                                                  String endWord,
                                                  List<String> availableValidWordVariantList) {
        // 准备一个 xxx - 用于存放所有找到的 可行的最短转换路径
        List<List<String>> allValidTransformSequenceList = new ArrayList<>();

        // 把wordList转化成为一个set对象 用于更快速地判断 是否存在重复元素
        Set<String> validWordVariantSet = new HashSet<>(availableValidWordVariantList);

        // 鲁棒性代码 - endWord一定要出现在 可选的单词变体集合中，否则 不存在任何的转换序列，则：
        if (!validWordVariantSet.contains(endWord)) {
            // 直接return空列表
            return allValidTransformSequenceList;
        }

        /* #1 使用bfs 来 得到图中所存在的 word -> itsTransformedWordVariants 映射 */
        Map<String, List<String>> wordVariantToItsTransformedWordVariants = new HashMap<>();
        Set<String> startWordSet = new HashSet<>();
        startWordSet.add(beginWord);

        generateTheMapViaBFS(startWordSet,
            endWord,
            wordVariantToItsTransformedWordVariants,
            validWordVariantSet);

        /* #2 查看下map是否 “按照预期被创建” */
        printThe(wordVariantToItsTransformedWordVariants);

        /* #3 使用DFS+回溯 来 从图中得到 所有可能的“到目标单词的转换序列” */
        // 准备一个path     把beginWord放进去
        List<String> pathTowardsEndWord = new ArrayList<>();
        pathTowardsEndWord.add(beginWord);

        // 使用dfs来生成结果
        generatePathListViaDFS(beginWord,
            endWord,
            pathTowardsEndWord,
            allValidTransformSequenceList,
            wordVariantToItsTransformedWordVariants);

        return allValidTransformSequenceList;

    }

    private static void printThe(Map<String, List<String>> wordVariantToItsTransformedWordVariants) {
        if (wordVariantToItsTransformedWordVariants == null) return;

        for (Map.Entry<String, List<String>> currentWordVariantToItsTransformedWordVariants : wordVariantToItsTransformedWordVariants.entrySet()) {
            String currentWordVariant = currentWordVariantToItsTransformedWordVariants.getKey();
            List<String> itsTransformedWordVariants = currentWordVariantToItsTransformedWordVariants.getValue();
            System.out.print("Key - " + currentWordVariant + " | ");
            System.out.println("Value - " + itsTransformedWordVariants);
        }
    }

    private static void generatePathListViaDFS(String beginWord,
                                               String endWord,
                                               List<String> pathTowardsEndWord,
                                               List<List<String>> allValidTransformSequenceList,
                                               Map<String, List<String>> wordsToItsValidVariantsMap) {
        // 如果起始单词 与 目标单词 相同，说明 不需要进行任何转换 就得到了目标单词，则：
        if (beginWord.equals(endWord)) {
            // 把 当前到endWord的path 添加到 结果列表中
            allValidTransformSequenceList.add(new ArrayList(pathTowardsEndWord)); // 因为list会被回溯，所以我们使用的是一个新的副本
            // 即刻返回 以 停止当前级方法的继续调用，返回到上一级
            return;
        }

        // 如果“当前起始单词”不存在任何“有效的单词变体”，说明 由当前起始单词经“有效单词变体” 无法转变成为“目标单词”，则：
        if (wordsToItsValidVariantsMap.get(beginWord) == null) {
            // 直接return 以 停止当前级方法的调用
            return;
        }

        // 如果“当前起始单词” 存在有 一堆的“有效的单词变体”，说明 由当前起始单词 经“有效单词变体” 有可能转变得到“目标单词”，则：
        // 遍历每一个“有效的单词变体”
        for (String currentValidWordVariant : wordsToItsValidVariantsMap.get(beginWord)) {
            // 把 当前“有效的单词变体” 添加到 “由起始单词转变到目标单词的路径”中
            pathTowardsEndWord.add(currentValidWordVariant);

            // 调用dfs 以 得到完整的“到目标单词的路径”
            generatePathListViaDFS(currentValidWordVariant, endWord, pathTowardsEndWord, allValidTransformSequenceList, wordsToItsValidVariantsMap);

            // 回溯当前选择 以便选择下一个“有效的单词变体” 来 构造“到目标单词的路径”
            pathTowardsEndWord.remove(pathTowardsEndWord.size() - 1);
        }
    }

    /**
     * 每一个level的单词会越来越多，所以使用一个startSet作为参数
     * 一个endWord参数
     * 还需要一个映射关系map：描述一个单词所能转换到的其他有效单词
     * 特征：这个BFS并没有使用到Queue
     */
    private static void generateTheMapViaBFS(Set<String> startWordSet,
                                             String endWord,
                                             Map<String, List<String>> wordToItsTransformedValidVariants,
                                             Set<String> availableValidWordVariantSet) {
        /* Ⅰ 为正确地扩展出图map做一些准备工作 */
        // 如果startSet的size为0，说明 图已经没有下一层顶点了（aka 图已经创建完成），则：直接return
        if (startWordSet.size() == 0) return;

        // 准备一个tempSet  用于存储序列中下一个位置可能的单词（aka 下一层顶点集合）
        Set<String> startWordSetOnNextLevel = new HashSet<>();

        // 准备一个布尔值  判断转换序列是不是已经走到了结尾
        boolean isTransformFinished = false;

        // 从 可选的“有效单词变体集合”中，移除 当前的“起始单词集合”
        availableValidWordVariantSet.removeAll(startWordSet);

        /* Ⅱ 准备一个循环 在当前level上对图进行扩展 */
        // 遍历startSet中的每一个单词，找到其合法的转换结果，并添加到下一层中（并构建图map）/或者直接返回
        for (String currentStartWordStr : startWordSet) {
            // 列举当前单词可能转换到的有效字符串
            char[] currentStartWordCharacterArr = currentStartWordStr.toCharArray();
            for (int currentSpotToReplace = 0; currentSpotToReplace < currentStartWordCharacterArr.length; currentSpotToReplace++) {
                char originalCharacter = currentStartWordCharacterArr[currentSpotToReplace];

                for (char replacedCharacterOption = 'a'; replacedCharacterOption <= 'z'; replacedCharacterOption++) {
                    currentStartWordCharacterArr[currentSpotToReplace] = replacedCharacterOption;
                    String replacedCharacterWord = new String(currentStartWordCharacterArr);

                    // 如果“替换字符后的字符串”存在于“有效的单词变体集合”中，说明 找到了一个 序列中的“转换中间结果”，则：
                    if (availableValidWordVariantSet.contains(replacedCharacterWord)) {
                        /* #1 判断“替换字符后的字符串” 是不是 就是 “目标字符串” */
                        // #1-① 如果是，说明 转换序列已经结束，则：
                        if (endWord.equals(replacedCharacterWord)) {
                            // 把 flag变量 设置为true
                            isTransformFinished = true;
                        } else {
                            // #1-② 如果不是，说明转换序列还没有结束，则：
                            // 把 这个“转换中间结果” 添加到一个 set对象中
                            startWordSetOnNextLevel.add(replacedCharacterWord);
                        }

                        /* #2 把 “当前startWord” -> 其所能转换到的所有“有效中间结果” 的映射 添加到 xxx 中 */
                        // #2-① 新增条目
                        if (wordToItsTransformedValidVariants.get(currentStartWordStr) == null) {
                            wordToItsTransformedValidVariants.put(currentStartWordStr, new ArrayList<>());
                        }
                        // #2-② 向条目中添加item
                        wordToItsTransformedValidVariants.get(currentStartWordStr).add(replacedCharacterWord);
                    }
                }

                // 恢复当前位置上的字符，以便在其他位置继续尝试通过转换 来 得到中间结果或最终结果
                currentStartWordCharacterArr[currentSpotToReplace] = originalCharacter;
            }
        }

        /* Ⅲ 判断图是否已经扩展完成，如果没有，使用bfs继续进行扩展 */
        // 如果经过上述循环后，finish仍旧为false，说明 仍旧没有找到endWord，则：
        if (!isTransformFinished) {
            // 继续做bfs 来 进一步建立图（查找到endWord时，图就建立完成了）
            // 🐖 此时 startWordSet 的值就是 startWordSetOnNextLevel了 - 也就是图中 下一个level的所有结点
            generateTheMapViaBFS(startWordSetOnNextLevel, endWord, wordToItsTransformedValidVariants, availableValidWordVariantSet);
        }

        // 如果finish为true，则：方法结束，图已经创建完成
    }
} // this method failed, I don't know why. but let's skip to next method first
// super stupid：原因是因为传入的beginWord 与 endWord参数传成了空字符串
import java.util.*;

public class Solution_findLadders_by_liweiwei {
    public static void main(String[] args) {
        String beginWord = "hit";
        String endWord = "cog";
        List<String> validMiddleWordList =
                new ArrayList<>(
                        Arrays.asList("hot", "dot", "dog", "lot", "log", "cog")
                );

        Solution_findLadders_by_liweiwei myObject = new Solution_findLadders_by_liweiwei();
        List<List<String>> allShortestTransformSequences = myObject.findLadders(beginWord, endWord, validMiddleWordList);
        System.out.println("最终的结果为：" + allShortestTransformSequences);
    }

    public List<List<String>> findLadders(String beginWord, String endWord, List<String> validMiddleWordList) {
        Set<String> validMiddleWordSet = new HashSet<>(validMiddleWordList);

        List<List<String>> allWantedPaths = new ArrayList<>();
        if (!validMiddleWordSet.contains(endWord)) {
            return allWantedPaths;
        }

        Map<String, List<String>> wordToItsChangedMiddleWordsMap = new HashMap<>();
        boolean found = bfs(beginWord, endWord, validMiddleWordSet, wordToItsChangedMiddleWordsMap);

        printTheMap(wordToItsChangedMiddleWordsMap);

        if (found) {
            Deque<String> path = new ArrayDeque<>();
            path.addFirst(endWord);
            dfs(beginWord, endWord, wordToItsChangedMiddleWordsMap, path, allWantedPaths);
        }
        return allWantedPaths;
    }

    private void printTheMap(Map<String, List<String>> wordToItsChangedMiddleWordsMap) {
        for (Map.Entry<String, List<String>> currentEntry : wordToItsChangedMiddleWordsMap.entrySet()) {
            System.out.println("currentMiddleWord: " + currentEntry.getKey() + " => " + currentEntry.getValue());
        }
    }

    private boolean bfs(String beginWord,
                        String endWord,
                        Set<String> validMiddleWordSet,
                        Map<String, List<String>> wordToItsChangedMiddleWordsMap) {

        boolean isTransformFinished = false;

        Queue<String> startWordQueue = new LinkedList<>();
        startWordQueue.offer(beginWord);
        Set<String> allUsedMiddleWords = new HashSet<>();
        allUsedMiddleWords.add(beginWord);

        Set<String> usedMiddleWordOnCurrentLevel = new HashSet<>();

        while (!startWordQueue.isEmpty()) {
            int middleWordAmountOnCurrentLevel = startWordQueue.size();
            // 清除当前层“使用过的单词” 以此来 为queue的正确性 提供双重保险 - 确保单词不会重复入队
            usedMiddleWordOnCurrentLevel.clear();

            // 处理 当前层的所有节点...
            for (int currentMiddleWordCursor = 0; currentMiddleWordCursor < middleWordAmountOnCurrentLevel; currentMiddleWordCursor++) {
                String currentMiddleWord = startWordQueue.poll();
                List<String> reachableMiddleWords = getReachableWordsOf(currentMiddleWord, validMiddleWordSet);

                for (String currentChangedMiddleWord : reachableMiddleWords) {
                    if (currentChangedMiddleWord.equals(endWord)) {
                        // 只标记找到了，还需要完成这一层前驱关系的建立，才能退出循环
                        isTransformFinished = true;
                    }

                    // 🐖 这种实现方式 没有修改validMiddleWords,而是单纯记录“被使用过的单词”
                    // 如果 当前middleWord没有被使用过 && 它在当前层中也没有被使用过，说明 它是一个全新的middleWord，则：
                    if (!allUsedMiddleWords.contains(currentChangedMiddleWord)) {
                        if (!usedMiddleWordOnCurrentLevel.contains(currentChangedMiddleWord)) {
                            startWordQueue.offer(currentChangedMiddleWord); // 把它添加进队列
                            usedMiddleWordOnCurrentLevel.add(currentChangedMiddleWord); // 把它标记为“当前层已使用”
                        }

                        // 找到middleWord后，使用它来构造图本身  手段：computeIfAbsent();   特征：可以链式调用
                        wordToItsChangedMiddleWordsMap.computeIfAbsent(currentChangedMiddleWord, a -> new ArrayList<>()).add(currentMiddleWord);
                    }
                }
            }

            // 把 当前层所使用的所有middleWord都添加到 allUsedMiddleWords 中
            allUsedMiddleWords.addAll(usedMiddleWordOnCurrentLevel);

            // 如果flag为true，说明找到了endWord && 当前层级所有的路径都已经处理完毕，则：
            if (isTransformFinished) {
                // 直接return，退出循环 因为后继的层无法再提供更短的路径了
                return true;
            }
        }

        return false;
    }

    private void dfs(String beginWord, String endWord, Map<String, List<String>> prevWords, Deque<String> path, List<List<String>> res) {
        if (endWord.equals(beginWord)) {
            res.add(new ArrayList<>(path));
            return;
        }
        // 不作此判断会报空指针异常
        if (!prevWords.containsKey(endWord)) {
            return;
        }
        for (String precursor : prevWords.get(endWord)) {
            path.addFirst(precursor);
            dfs(beginWord, precursor, prevWords, path, res);
            path.removeFirst();
        }
    }

    private List<String> getReachableWordsOf(String currentMiddleWord, Set<String> validMiddleWordSet) {
        List<String> changeableMiddleWords = new ArrayList<>();
        char[] currentMiddleWordLetterArr = currentMiddleWord.toCharArray();

        for (int currentSpotToReplace = 0; currentSpotToReplace < currentMiddleWordLetterArr.length; currentSpotToReplace++) {
            char originChar = currentMiddleWordLetterArr[currentSpotToReplace];

            for (char replacedLetter = 'a'; replacedLetter <= 'z'; replacedLetter++) {
                if (replacedLetter == originChar) {
                    continue;
                }
                // 替换字母
                currentMiddleWordLetterArr[currentSpotToReplace] = replacedLetter;
                // 转换回字符串
                String replacedResultStr = String.valueOf(currentMiddleWordLetterArr);
                // 如果 替换后的字符串 存在于 有效中间单词列表中，说明 找见了一个有效转换，则：
                if (validMiddleWordSet.contains(replacedResultStr)) {
                    changeableMiddleWords.add(replacedResultStr);
                }
            }
            // 恢复当前位置上的原始字符
            currentMiddleWordLetterArr[currentSpotToReplace] = originChar;
        }
        return changeableMiddleWords;
    }

}
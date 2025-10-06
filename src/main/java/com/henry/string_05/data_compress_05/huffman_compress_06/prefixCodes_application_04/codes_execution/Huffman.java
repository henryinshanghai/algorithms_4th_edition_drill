package com.henry.string_05.data_compress_05.huffman_compress_06.prefixCodes_application_04.codes_execution;

/******************************************************************************
 *  Compilation:  javac Huffman.java
 *  Execution:    java Huffman - < input.txt   (compress)
 *  Execution:    java Huffman + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   https://algs4.cs.princeton.edu/55compression/abra.txt
 *                https://algs4.cs.princeton.edu/55compression/tinytinyTale.txt
 *                https://algs4.cs.princeton.edu/55compression/medTale.txt
 *                https://algs4.cs.princeton.edu/55compression/tale.txt
 *
 *  Compress or expand a binary input stream using the Huffman algorithm.
 *
 *  % java Huffman - < abra.txt | java BinaryDump 60
 *  010100000100101000100010010000110100001101010100101010000100
 *  000000000000000000000000000110001111100101101000111110010100
 *  120 bits
 *
 *  (current_path)% java Huffman.java - < abra.txt | java Huffman.java +
 *  ABRACADABRA!
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.MinPQ;

/**
 * The {@code Huffman} class provides static methods for compressing
 * and expanding a binary input using Huffman codes over the 8-bit extended
 * ASCII alphabet.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
// 验证：可以使用 霍夫曼算法（把 最频繁出现的字符 使用 最少位数的二进制数 来 表示）来 对 “文本字符串” 进行 压缩/解压
public class Huffman {

    // ”扩展ASCII表“中的 字符数量
    private static final int characterOption = 256;

    // 为了防止 这个类 被初始化，这里 把 构造方法 设置为 private
    private Huffman() {
    }

    // Huffman单词查找树中的 结点
    private static class Node implements Comparable<Node> {
        private final char character;
        private final int frequency; // 字符 在字符串中出现的频率 或 “以此结点作为根结点”的子树中的 所有“叶子节点中的字符”出现的总频率
        private final Node leftSubNode, rightSubNode;

        Node(char character, int frequency, Node leftSubNode, Node rightSubNode) {
            this.character = character;
            this.frequency = frequency;
            this.leftSubNode = leftSubNode;
            this.rightSubNode = rightSubNode;
        }

        // 当前节点 是不是一个 叶子节点?
        private boolean isLeaf() {
            assert ((leftSubNode == null) && (rightSubNode == null)) || ((leftSubNode != null) && (rightSubNode != null));
            return (leftSubNode == null) && (rightSubNode == null);
        }

        // 基于频率的 比较操作 - 这个API 是为了 支持优先队列的操作
        public int compareTo(Node that) {
            return this.frequency - that.frequency;
        }
    }

    /**
     * 从 标准输入 中读取 8位字节的序列；
     * 使用 具有8位编码的字母表 的Huffman编译表 来 对 输入的字节序列 进行压缩；
     * 把 压缩的结果 写入到 标准输出中；
     */
    public static void compress() {
        // 读取 标准输入中 传入的字符串
        String inputStr = BinaryStdIn.readString();
        char[] inputCharacterSequence = inputStr.toCharArray();

        // #1 对 “输入字符串中”的 各个字符的出现频率 进行计数
        int[] characterToItsFrequency = buildFrequencyTable(inputCharacterSequence);
        printArr(characterToItsFrequency);

        // #2 根据 #1中的统计结果，构造出 霍夫曼树（它是一种“最优前缀码”方案）
        Node huffmanTrie = buildHuffmanTrie(characterToItsFrequency);
        printTrieWithNotice(huffmanTrie);

        // #3 根据 霍夫曼Trie树 来 构造出“霍夫曼编码表”
        String[] characterToItsEncodedBitStr = buildEncodedBitStrTable(huffmanTrie);
        printEncodeTable(characterToItsEncodedBitStr);

        // #4-1 把 “单词查找树本身” 写入到 标准输出 中 - 用于 后续的解码工作
        writeTrieToOutput(huffmanTrie);

        // #4-2 把 “原始字符序列”中的字符数量 写入到 标准输出中 - 用于 后继的解码工作√
        writeCharacterAmountToOutput(inputCharacterSequence);

        // #5 打印 ”原始字符序列“的 编码结果 - 手段：使用 编码表 来 对 “原始字符序列”中的字符 逐个 进行编码/压缩
        writeEncodedResultToOutput(inputCharacterSequence, characterToItsEncodedBitStr);

        // 刷新并关闭 输出流
        BinaryStdOut.close();
    }

    private static void printEncodeTable(String[] characterToItsEncodedBitStr) {
        System.out.println("&& 打印构造出的编码表👇 &&");
        for (int currentCharacter = 0; currentCharacter < characterToItsEncodedBitStr.length; currentCharacter++) {
            String encodedBitStr = characterToItsEncodedBitStr[currentCharacter];
            if (encodedBitStr != null && !encodedBitStr.isBlank()) {
                System.out.println("当前字符 " + (char)currentCharacter + " 对应的编码结果为：" + encodedBitStr);
            }
        }
        System.out.println();
    }

    private static void printTrieWithNotice(Node huffmanTrie) {
        System.out.println("^^ 打印所构造出的二向trie树👇 ^^");
        printTrie(huffmanTrie);
        System.out.println();
        System.out.println();
    }

    // 二向trie树是非线性结构，所以会有多种遍历方式 这里选择 左-根-右
    private static void printTrie(Node currentNode) {
        if (currentNode == null) return;

        printTrie(currentNode.leftSubNode);
        System.out.print(currentNode.character + " ");
        printTrie(currentNode.rightSubNode);
    }

    private static void printArr(int[] characterToItsFrequency) {
        System.out.println("%% 打印字符序列中 各个字符的出现频率👇 %%");
        for (int currentCharacter = 0; currentCharacter < characterToItsFrequency.length; currentCharacter++) {
            int frequency = characterToItsFrequency[currentCharacter];
            if(frequency > 0) {
                System.out.println("当前字符" + (char) currentCharacter + "在原始字符序列中 出现的频率为：" + frequency);
            }
        }
        System.out.println();
    }

    private static int[] buildFrequencyTable(char[] inputCharacterSequence) {
        int[] characterToItsFrequency = new int[characterOption];
        for (int currentSpot = 0; currentSpot < inputCharacterSequence.length; currentSpot++) {
            char currentCharacter = inputCharacterSequence[currentSpot];
            // 使用一个数组 来 记录字符->字符出现频率 的映射关系
            characterToItsFrequency[currentCharacter]++;
        }
        return characterToItsFrequency;
    }

    private static void writeCharacterAmountToOutput(char[] inputCharacterSequence) {
        System.out.println("$$ 打印原始字符序列长度值的二进制数值👇 $$");
        BinaryStdOut.write(inputCharacterSequence.length);
        System.out.println(Integer.toBinaryString(inputCharacterSequence.length));
        System.out.println();
    }

    private static void writeEncodedResultToOutput(char[] inputCharacterSequence, String[] characterToItsEncodedBitStr) {
        System.out.println("## 打印字符序列编码后的结果👇 ##");
        for (int currentSpot = 0; currentSpot < inputCharacterSequence.length; currentSpot++) {
            // #5-1 对于 当前字符，从 编码表 中找到 其所对应的编码结果
            char currentCharacter = inputCharacterSequence[currentSpot];
            String encodedBitStr = characterToItsEncodedBitStr[currentCharacter];

            // #5-2 然后 把 “编码结果”/“压缩结果” 写入到 标准输出中...
            writeToOutput(encodedBitStr);
            System.out.print(encodedBitStr);
        }
        System.out.println();
        System.out.println();
    }

    private static void writeToOutput(String encodedBitStr) {
        // 为什么 不直接 把 字符串 打印到 标准输出中, 而是 逐个打印 布尔值呢?   因为字符串占用的空间太大，而布尔值占用的空间很小
        for (int currentBitSpot = 0; currentBitSpot < encodedBitStr.length(); currentBitSpot++) {
            char currentBit = encodedBitStr.charAt(currentBitSpot);
            // 写入规则：字符0 写成 false，字符1 写成 true
            if (currentBit == '0') {
                BinaryStdOut.write(false);
            } else if (currentBit == '1') {
                BinaryStdOut.write(true);
            } else
                throw new IllegalStateException("Illegal state");
        }
    }

    private static String[] buildEncodedBitStrTable(Node huffmanTrie) {
        // 初始化数组的大小 为 所有字符选项的数量
        String[] characterToEncodedValue = new String[characterOption];
        // 为 当前Trie中的 每一个叶子节点中的字符：#1 生成 其对应的 编码比特结果； #2 并 把 映射 添加到数组中
        generateEncodedBitStrForAllLeafNodesIn(huffmanTrie, "", characterToEncodedValue);

        return characterToEncodedValue;
    }

    // 从 “原始字符串中 字符的出现频率计数” 中，构建出 霍夫曼单词查找树 - 手段：优先级队列
    private static Node buildHuffmanTrie(int[] characterToItsFrequency) {
        // #1 先创建 一堆的 独立的 单节点树，把 它们 作为 队列元素 来 初始化 优先级队列
        MinPQ<Node> nodesMinPQ = createSeparateNodesToInitPQ(characterToItsFrequency);
        printQueue(nodesMinPQ);

        while (nodesMinPQ.size() > 1) {
            // #2 把 当前“最小的两棵树” 合并起来，得到 一棵更大的树 🐖 会 添加一个 新的结点 作为父节点
            // 手段：使用 优先级队列的delMin() 能够 轻易得到 “最小的树/根结点”
            combineAllNodesIntoOneTrie(nodesMinPQ);
        } // 循环结束 后，优先队列中的 所有结点 都已经 被添加到了 Trie树 中

        // #3 获取到 “所有节点 完全合并 之后 得到的huffman树（树的根结点）” - 手段：从 优先级队列 中 删除以获取到 “当前最小的元素” aka Huffman树的根节点
        return nodesMinPQ.delMin();
    }

    private static void printQueue(MinPQ<Node> nodesMinPQ) {
        for (Node currentNode : nodesMinPQ) {
            System.out.println("优先队列中的当前节点为：" + currentNode.character);
        }
        System.out.println();
    }

    private static void combineAllNodesIntoOneTrie(MinPQ<Node> nodesMinPQ) {
        // #1 删除并获取到 当前队列中的 最小的两个结点
        Node leftSubNode = nodesMinPQ.delMin();
        Node rightSubNode = nodesMinPQ.delMin();
        // #2 基于 当前最小的两个结点 来 新建出一个新节点
        // 🐖 新建的父节点：① 是 最小两个结点的 父节点；② 不持有 任何字符； ③ 频率值 为 左右子树的频率之和
        Node newlyCreatedParentNode = createParentBasedOn(leftSubNode, rightSubNode);
        // #3 把 新建的父节点 添加回到 队列 中
        nodesMinPQ.insert(newlyCreatedParentNode);
    }

    private static Node createParentBasedOn(Node leftSubNode, Node rightSubNode) {
        // 扩展Node内部类的成员变量 - 左右子节点 🐖 新建结点所持有的字符为空（这里使用\0来标识）
        return new Node('\0', leftSubNode.frequency + rightSubNode.frequency, leftSubNode, rightSubNode);
    }

    private static MinPQ<Node> createSeparateNodesToInitPQ(int[] characterToItsFrequency) {
        MinPQ<Node> nodesMinPQ = new MinPQ<Node>();
        // 对于每一个字符选项...
        for (char currentCharacter = 0; currentCharacter < characterOption; currentCharacter++) {
            // 获取它 在原始字符串中 出现的频率
            int itsFrequency = characterToItsFrequency[currentCharacter];
            if (itsFrequency > 0) {
                // #1 为 它 创建一个 单独的Trie树节点
                Node nodeForCurrentCharacter = new Node(currentCharacter, itsFrequency, null, null);
                // #2 把 创建的Trie树节点 添加到 优先级队列 中  用于找到所有节点中”最小的”那两个节点
                nodesMinPQ.insert(nodeForCurrentCharacter);
            }
        }

        // 返回 由所有 独立的Trie树节点 所组成的 优先级队列（队列元素为 trie树的节点）
        return nodesMinPQ;
    }

    // 把 用于编码字符的 单词查找树 写入到 标准输出中 - 用于 后继解码工作的 解码依据
    // 手段：使用 前序遍历的规则（根结点 - 左子树 - 右子树） 来 完全处理 树中的所有结点
    private static void writeTrieToOutput(Node trieRootNode) {
        System.out.println("** 把二向trie树 打印到 控制台中👇 **");
        processNodesInPreOrder(trieRootNode);
        System.out.println();
        System.out.println();
    }

    private static void processNodesInPreOrder(Node currentRootNode) {
        // #1 处理 根结点
        // 如果 ”当前根结点“ 是一个 “叶子节点”，则...
        if (currentRootNode.isLeaf()) {
            // ① 向 标准输出 中 写入一个1/true
            BinaryStdOut.write(true);
            System.out.print('1');
            // ② 把 “叶子节点中的字符” 以 8个比特（1个字节） 写入到 标准输出 中...
            BinaryStdOut.write(currentRootNode.character, 8);
            System.out.print(Integer.toBinaryString(((byte) currentRootNode.character) & 0xFF));
            return;
        }
        // 如果它 是 “内部结点”，则：向 标准输出 中 写入一个0/false
        BinaryStdOut.write(false);
        System.out.print('0');

        // #2 处理左子树 - 把 结点的左子树，继续(递归地)写入到 标准输出 中
        processNodesInPreOrder(currentRootNode.leftSubNode);
        // #3 处理右子树 - 把 结点的右子树，继续(递归地)写入到 标准输出 中
        processNodesInPreOrder(currentRootNode.rightSubNode);
    }

    /**
     * 从 huffman trie树中，构造出 静态的编码表
     * 🐖 这是一个 追加类别的问题，其特殊之处在于 字符串、路径、树都有递归性
     * @param currentRootNode   当前trie树的根节点
     * @param currentEncodedBitStr  当前编码所得到的比特序列（当遍历到叶子节点时，它就会是编码结果）    初始为空字符串""
     * @param currentCharToEncodeValueArr   编码表的数组形式
     */
    private static void generateEncodedBitStrForAllLeafNodesIn(Node currentRootNode,
                                                               String currentEncodedBitStr,
                                                               String[] currentCharToEncodeValueArr) {
        // 如果 当前节点 不是叶子节点，说明 编码的过程还没有结束，则：
        if (!currentRootNode.isLeaf()) {
            /* 继续递归地遍历子树，向编码序列中 添加正确的比特 */
            // 如果 路径 选择走左分支，则：向 编码结果 中 添加比特0
            generateEncodedBitStrForAllLeafNodesIn(currentRootNode.leftSubNode, currentEncodedBitStr + '0', currentCharToEncodeValueArr);
            // 如果 路径 选择走右分支，则：向 编码结果 中 添加比特1
            generateEncodedBitStrForAllLeafNodesIn(currentRootNode.rightSubNode, currentEncodedBitStr + '1', currentCharToEncodeValueArr);
        } else { // 如果 当前节点 是叶子节点，说明 得到了一个字符的编码结果，则：
            /* 向编码表中添加 字符 -> 字符的编码结果 条目 */
            currentCharToEncodeValueArr[currentRootNode.character] = currentEncodedBitStr; // 在遍历了所有的叶子节点后，编码表 也就构造完成了
        }
    }

    /**
     * 从 标准输入 中 读取 用于表示霍夫曼压缩消息的 比特序列；
     * 展开 这个比特序列；
     * 把 展开的结果 写入到 标准输出中.
     */
    public static void expand() {
        // 🐖 读取比特序列各部分信息(trie树、字符数量、编码结果)的顺序 与 写入时的顺序 需要相同
        // #1 先 从 输入流 中 读取出 霍夫曼单词查找树(trie)
        Node huffmanTrie = readTrieFromInput();
        System.out.println("@@ 解码出的Huffman trie树为👇 @@");
        printTrie(huffmanTrie);
        System.out.println();

        // #2 再 读取出 待写入的字节的数量
        int expectedCharacterAmount = readCharAmountFromInput();
        System.out.println("!! 解码出的字符数量为：" + expectedCharacterAmount + " !!");

        // #3 对于 期待的每一个字符...
        for (int characterOrdinal = 0; characterOrdinal < expectedCharacterAmount; characterOrdinal++) {
            // 读取 标准输入中的比特序列(字符序列的编码结果)，并 解码得到 具体字符 - 手段：使用对应“trie树的叶子结点”
            readEncodedResultThenDecode(huffmanTrie);
        }

        BinaryStdOut.close();
    }

    private static int readCharAmountFromInput() {
        return BinaryStdIn.readInt();
    }

    // 手段：读取 标准输入中的比特序列(字符的编码结果)，直到 读取到1（字符的比特编码结束）。然后 打印 对应的trie叶子节点中的字符
    private static void readEncodedResultThenDecode(Node huffmanTrie) {
        // #1 逐个读取 标准输入中的比特，并 据此 沿着trie树 导航到 对应的叶子节点
        Node currentLeafNode = readBitSequenceAndNavigateToItsLeafNode(huffmanTrie);

        // #2 把 “叶子结点中的字符” 打印到 标准输出中 - 至此，解码 得到了 当前字符
        BinaryStdOut.write(currentLeafNode.character, 8);
    }

    private static Node readBitSequenceAndNavigateToItsLeafNode(Node huffmanTrie) {
        Node currentNode = huffmanTrie;
        // 当前节点 不是 “叶子节点” 时...
        while (!currentNode.isLeaf()) {
            // 沿着 树的分支 继续导航 - 直到 导航到 叶子结点 时，说明 当前编码值 读取结束，则:...
            currentNode = navigateViaBitSequence(currentNode);
        }
        return currentNode;
    }

    private static Node navigateViaBitSequence(Node currentNode) {
        // #1 从 标准输入 中 读取 单个比特
        boolean currentBitOfInput = BinaryStdIn.readBoolean();
        // #2 根据 读取到的 比特值，在 trie树 中 导航
        // 导航规则：如果 输入bit 为 1，则 导航到 右子树。如果 输入bit 为 0，则 导航到 左子树
        // 🐖 这里的导航规则 需要 与 generateEncodedBitStrForAllLeafNodesIn()中 生成 比特编码序列 的规则 相一致
        if (isBit1(currentBitOfInput))
            currentNode = currentNode.rightSubNode;
        else
            currentNode = currentNode.leftSubNode;
        // #3 返回 导航到的 trie结点
        return currentNode;
    }


    // 从 标准输入的比特序列 中 读取出 霍夫曼单词查找树
    private static Node readTrieFromInput() {
        // 读取 标准输入中的 一个比特值
        boolean currentBitOfInput = BinaryStdIn.readBoolean();
        // 如果 读取到的 单个比特 为 1，说明 读取到的是 一个“trie树中的叶子节点”...
        if (isRepresentLeafNode(currentBitOfInput)) {
            // 则：继续读取 输入中的下8个比特，得到 一个字符。
            char currentCharacterOfInput = BinaryStdIn.readChar();
            // 并 使用此字符，创建出一个 霍夫曼单词查找树中的 一个叶子结点 🐖 解码时，已经不再需要 字符的频率 所以设置为-1
            return new Node(currentCharacterOfInput, -1, null, null);
        } else { // 否则 读取到的 单个比特 为 0，说明 读取到的是 一个“trie树中的内部节点”...
            // 则：创建一个 内部节点，并 递归地 继续构造 它的左右子树(通过读取 后继比特序列)
            return new Node('\0', -1, readTrieFromInput(), readTrieFromInput());
        }
    }

    private static boolean isRepresentLeafNode(boolean currentBitOfInput) {
        return isBit1(currentBitOfInput);
    }

    private static boolean isBit1(boolean currentBitInInput) {
        return currentBitInInput;
    }

    /**
     * 命令行参数为-时，调用 compress(); 命令行参数为+时，调用 expand()
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 如果 命令行参数 为 -，则：执行压缩
        if (args[0].equals("-")) compress();
            // 如果 命令行参数 为 +，则：执行展开
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
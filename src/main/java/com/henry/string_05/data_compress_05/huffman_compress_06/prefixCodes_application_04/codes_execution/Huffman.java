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
// 验证：可以使用霍夫曼算法（把最频繁出现的字符使用最少位数的二进制数表示）来 对“文本字符串”进行压缩/解压
public class Huffman {

    // 扩展ASCII表中的字符数量
    private static final int characterOption = 256;

    // 为了防止这个类被初始化，这里把构造方法设置为private
    private Huffman() {
    }

    // Huffman单词查找树中的结点
    private static class Node implements Comparable<Node> {
        private final char character;
        private final int frequency; // 字符在字符串中出现的频率 或 “以此结点作为根结点”的子树中的所有“叶子节点中的字符”出现的总频率
        private final Node leftSubNode, rightSubNode;

        Node(char character, int frequency, Node leftSubNode, Node rightSubNode) {
            this.character = character;
            this.frequency = frequency;
            this.leftSubNode = leftSubNode;
            this.rightSubNode = rightSubNode;
        }

        // 当前节点是不是一个叶子节点?
        private boolean isLeaf() {
            assert ((leftSubNode == null) && (rightSubNode == null)) || ((leftSubNode != null) && (rightSubNode != null));
            return (leftSubNode == null) && (rightSubNode == null);
        }

        // 基于频率的比较操作 - 这个API是为了支持优先队列的操作
        public int compareTo(Node that) {
            return this.frequency - that.frequency;
        }
    }

    /**
     * Reads a sequence of 8-bit bytes from standard input; compresses them
     * using Huffman codes with an 8-bit alphabet; and writes the results
     * to standard output.
     * 从标准输入中读取 8位字节的序列；
     * 使用 具有8位编码的字母表的Huffman编译表 来 对输入的字节序列进行压缩；
     * 把压缩的结果 写入到标准输出中；
     */
    public static void compress() {
        // 读取标准输入中传入的字符串
        String inputStr = BinaryStdIn.readString();
        char[] inputCharacterSequence = inputStr.toCharArray();

        // #1 对“输入字符串中的各个字符”的出现频率进行计数
        int[] characterToItsFrequency = buildFrequencyTable(inputCharacterSequence);

        // #2 根据#1中的统计结果，构造出 霍夫曼树（它是一种“最优前缀码”方案）
        Node huffmanTrie = buildTrie(characterToItsFrequency);

        // #3 根据霍夫曼Trie树 来 构造出“霍夫曼编码表”
        String[] characterToItsEncodedBitStr = buildEncodedBitStrTable(huffmanTrie);

        // #4-1 把“单词查找树本身”写入到标准输出中 - 用于后续的解码工作
        writeTrieToOutput(huffmanTrie);

        // #4-2 把“原始字符序列”中的字符数量 写入到标准输出中 - 用于后继的解码工作√
        writeCharacterAmountToOutput(inputCharacterSequence);

        // #5 打印”原始字符序列“的编码结果 - 手段：使用编码表 来 对“原始字符序列”中的字符逐个进行编码/压缩
        printEncodedResultFor(inputCharacterSequence, characterToItsEncodedBitStr);

        // 关闭输出流
        BinaryStdOut.close();
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
        BinaryStdOut.write(inputCharacterSequence.length);
    }

    private static void printEncodedResultFor(char[] inputCharacterSequence, String[] characterToItsEncodedBitStr) {
        for (int currentSpot = 0; currentSpot < inputCharacterSequence.length; currentSpot++) {
            // #5-1 对于当前字符，从编码表中找到 其所对应的编码结果
            char currentCharacter = inputCharacterSequence[currentSpot];
            String encodedBitStr = characterToItsEncodedBitStr[currentCharacter];

            // #5-2 然后把“编码结果”/“压缩结果” 写入到 标准输出中...
            printToOutput(encodedBitStr);
        }
    }

    private static void printToOutput(String encodedBitStr) {
        // 为什么不直接把字符串打印到标准输出中, 而是逐个打印布尔值呢?
        for (int currentBitSpot = 0; currentBitSpot < encodedBitStr.length(); currentBitSpot++) {
            char currentBit = encodedBitStr.charAt(currentBitSpot);
            // 写入规则：字符0写成false，字符1写成true
            if (currentBit == '0') {
                BinaryStdOut.write(false);
            } else if (currentBit == '1') {
                BinaryStdOut.write(true);
            } else
                throw new IllegalStateException("Illegal state");
        }
    }

    private static String[] buildEncodedBitStrTable(Node huffmanTrie) {
        // 初始化数组的大小为 所有字符选项的数量
        String[] characterToEncodedValue = new String[characterOption];
        // 为当前Trie中的每一个叶子节点中的字符：#1 生成其对应的编码比特结果； #2 并把映射添加到数组中
        generateEncodedBitStrForAllLeafNodesIn(huffmanTrie, characterToEncodedValue, "");

        return characterToEncodedValue;
    }

    // 从“原始字符串中字符的出现频率计数”中，构建出 霍夫曼单词查找树 - 手段：优先级队列
    private static Node buildTrie(int[] characterToItsFrequency) {
        // #1 先创建一堆的独立的单节点树，把它们作为作为队列元素 来 初始化优先级队列
        MinPQ<Node> nodesMinPQ = createSeparateNodesToInitPQ(characterToItsFrequency);

        while (nodesMinPQ.size() > 1) {
            // #2 把当前“最小的两棵树”合并起来，得到一棵更大的树 🐖 会添加一个新的结点作为父节点
            // 手段：使用优先级队列的delMin()能够轻易得到“最小的树/根结点”
            combineAllNodesIntoOneTrie(nodesMinPQ);
        } // 循环结束后，优先队列中的所有结点 都已经被添加到了 Trie树中

        // #3 获取到“所有节点完全合并之后得到的huffman树（树的根结点）” - 手段：从优先级队列中删除以获取到“当前最小的元素”
        return nodesMinPQ.delMin();
    }

    private static void combineAllNodesIntoOneTrie(MinPQ<Node> nodesMinPQ) {
        // #1 删除并获取到 当前队列中的 最小的两个结点
        Node leftSubNode = nodesMinPQ.delMin();
        Node rightSubNode = nodesMinPQ.delMin();
        // #2 基于当前最小的两个结点 来 新建出一个新节点
        // 🐖 新建的父节点：① 是最小两个结点的父节点；② 不持有任何字符； ③ 频率值为左右子树的频率之和
        Node newlyCreatedParentNode = createParentBasedOn(leftSubNode, rightSubNode);
        // #3 把 新建的父节点 添加回到队列中
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
            // 获取它在原始字符串中出现的频率
            int itsFrequency = characterToItsFrequency[currentCharacter];
            if (itsFrequency > 0) {
                // #1 为它创建一个单独的Trie树节点
                Node nodeForCurrentCharacter = new Node(currentCharacter, itsFrequency, null, null);
                // #2 把创建的Trie树节点添加到 优先级队列中
                nodesMinPQ.insert(nodeForCurrentCharacter);
            }
        }

        // 返回 由所有独立的Trie树节点组成的优先级队列
        return nodesMinPQ;
    }

    // 把 用于编码字符的单词查找树 写入到 标准输出中 - 后继的解码工作
    // 手段：使用前序遍历的规则（根结点 - 左子树 - 右子树） 来 完全处理树中的所有结点
    // method naming: what does it do
    private static void writeTrieToOutput(Node trieRootNode) {
        // in-body method naming: how to achieve it
        processNodesInPreOrder(trieRootNode);
    }

    private static void processNodesInPreOrder(Node currentRootNode) {
        // #1 处理根结点
        // 如果”当前根结点“是一个“叶子节点”，则...
        if (currentRootNode.isLeaf()) {
            // ① 向标准输出中写入一个1/true
            BinaryStdOut.write(true);
            // ② 把“叶子节点中的字符”以8个比特（1个字节）写入到标准输出中...
            BinaryStdOut.write(currentRootNode.character, 8);
            return;
        }
        // 如果它是“内部结点”，则：向标准输出中写入一个0/false
        BinaryStdOut.write(false);

        // #2 处理左子树 - 把结点的左子树，继续(递归地)写入到标准输出中
        processNodesInPreOrder(currentRootNode.leftSubNode);
        // #3 处理右子树 - 把结点的右子树，继续(递归地)写入到标准输出中
        processNodesInPreOrder(currentRootNode.rightSubNode);
    }

    // 构造一个编码表 用于建立 字符(符号) 与其编码之间的映射关系 aka a lookup table
    // 这个方法的主要作用是什么？副作用是什么？为什么可以实现为一个递归方法？
    // 方法的作用：根据当前Trie树中根结点到叶子节点的路径(手段) 来 #1 为其叶子节点中的字符，生成对应的编码结果(作用)，#2 并把编码结果添加到数组中
    // 规模更小的问题：根据”Trie子树“中根结点到叶子结点的路径 来 #1 为其叶子节点中的字符，生成其对应的编码结果； #2 把编码结果添加到数组中
    // 小问题的结果能否用来帮助解决原始问题：子树叶子节点字符的编码结果 加上 根结点的链接所表示的比特 就是 当前树叶子节点字符的编码结果了
    // 能够使用递归的原理：子树中叶子节点的路径(编码结果)，是原始树中叶子节点路径的一个子路径（本质上仍旧是Trie结构的递归性）
    // 方法名的规则：what does it do...
    private static void generateEncodedBitStrForAllLeafNodesIn(Node currentRootNode, String[] currentCharToEncodeValueArr, String currentEncodedBitStr) {
        if (!currentRootNode.isLeaf()) {
            // 路径走左分支，则：向编码结果中添加比特0
            generateEncodedBitStrForAllLeafNodesIn(currentRootNode.leftSubNode, currentCharToEncodeValueArr, currentEncodedBitStr + '0');
            // 路径走右分支，则：向编码结果中添加比特1
            generateEncodedBitStrForAllLeafNodesIn(currentRootNode.rightSubNode, currentCharToEncodeValueArr, currentEncodedBitStr + '1');
        } else {
            // 方法的主要作用：为 字符 生成一个 比特编码结果
            currentCharToEncodeValueArr[currentRootNode.character] = currentEncodedBitStr;
        }
    }

    /**
     * Reads a sequence of bits that represents a Huffman-compressed message from
     * standard input; expands them; and writes the results to standard output.
     * 从标准输入中读取 用于表示霍夫曼压缩消息的 比特序列；
     * 展开 这个比特序列；
     * 把展开的结果写入到标准输出中.
     */
    public static void expand() {
        // 🐖 读取比特序列各部分信息(trie树、字符数量、编码结果)的顺序 与 写入时的顺序 需要相同
        // #1 先从输入流中读取出 霍夫曼单词查找树(trie)
        Node huffmanTrie = readTrieFromInput();

        // #2 再读取出 待写入的字节的数量
        int expectedCharacterAmount = BinaryStdIn.readInt();

        // #3 对于期待的每一个字符...
        for (int characterOrdinal = 0; characterOrdinal < expectedCharacterAmount; characterOrdinal++) {
            // 读取标准输入中的比特序列(字符序列的编码结果)，并解码得到具体字符 - 手段：使用对应“trie树的叶子结点”
            readInputBitsAndDecodeOutCurrentCharacterVia(huffmanTrie);
        }

        BinaryStdOut.close();
    }

    // 手段：读取标准输入中的比特序列(字符的编码结果)，直到读取到1（字符的比特编码结束）。然后打印对应的trie叶子节点中的字符
    private static void readInputBitsAndDecodeOutCurrentCharacterVia(Node huffmanTrie) {
        // #1 逐个读取标准输入中的比特，并据此沿着trie树导航到对应的叶子节点
        Node currentLeafNode = readInputBitsAndGetLeafNodeOf(huffmanTrie);

        // #2 把“叶子结点中的字符” 打印到 标准输出中 - 至此，解码得到了当前字符
        BinaryStdOut.write(currentLeafNode.character, 8);
    }

    private static Node readInputBitsAndGetLeafNodeOf(Node huffmanTrie) {
        Node currentNode = huffmanTrie;
        // 当前节点不是“叶子节点”时...
        while (!currentNode.isLeaf()) {
            // 沿着树的分支继续导航 - 直到导航到叶子结点时，说明当前编码值读取结束，则:...
            currentNode = navigateViaInputBits(currentNode);
        }
        return currentNode;
    }

    private static Node navigateViaInputBits(Node currentNode) {
        // #1 从标准输入中读取单个比特
        boolean currentBitOfInput = BinaryStdIn.readBoolean();
        // #2 根据读取到的比特值，在trie树中导航
        // 导航规则：如果输入bit为1，则 导航到右子树。如果输入bit为0，则 导航到左子树
        // 🐖 这里的导航规则 需要 与generateEncodedBitStrForAllLeafNodesIn()中生成比特编码的规则 相一致
        if (isBit1(currentBitOfInput))
            currentNode = currentNode.rightSubNode;
        else
            currentNode = currentNode.leftSubNode;
        // #3 返回导航到的trie结点
        return currentNode;
    }


    // 从标准输入的比特序列中读取出 霍夫曼单词查找树
    private static Node readTrieFromInput() {
        // 读取标准输入中的一个比特值
        boolean currentBitOfInput = BinaryStdIn.readBoolean();
        // 如果读取到的单个比特为1，说明读取到的是 一个“trie树中的叶子节点”...
        if (isRepresentLeafNode(currentBitOfInput)) {
            // 则：继续读取输入中的下8个比特，得到一个字符。
            char currentCharacterOfInput = BinaryStdIn.readChar();
            // 并使用此字符，创建出一个 霍夫曼单词查找树中的一个叶子结点 🐖 解码时，已经不再需要 字符的频率 所以设置为-1
            return new Node(currentCharacterOfInput, -1, null, null);
        } else { // 否则读取到的单个比特为0，说明读取到的是一个“trie树中的内部节点”...
            // 则：创建一个内部节点，并递归地继续构造它的左右子树(通过读取后继比特序列)
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
     * Sample client that calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // 如果命令行参数为-，执行压缩
        if (args[0].equals("-")) compress();
        // 如果命令行参数为+，执行展开
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
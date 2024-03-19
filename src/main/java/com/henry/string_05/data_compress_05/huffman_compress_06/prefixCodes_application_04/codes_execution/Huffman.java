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
 *  % java Huffman - < abra.txt | java Huffman +
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
        int[] characterToItsFrequency = new int[characterOption];
        for (int currentSpot = 0; currentSpot < inputCharacterSequence.length; currentSpot++) {
            char currentCharacter = inputCharacterSequence[currentSpot];
            // 使用一个数组 来 记录字符->字符出现频率 的映射关系
            characterToItsFrequency[currentCharacter]++;
        }

        // #2 根据#1中的统计结果，构造出 霍夫曼树（它是一种“最优前缀码”方案）
        Node huffmanTrie = buildTrie(characterToItsFrequency);

        // #3 根据霍夫曼Trie树 来 构造出“霍夫曼编码表”
        String[] characterToEncodedValue = new String[characterOption];
        buildEncodedValueTable(huffmanTrie, characterToEncodedValue, "");

        // #4-1 把“单词查找树本身”写入到标准输出中 - 用于后续的解码工作
        writeTrie(huffmanTrie);

        // #4-2 把“未压缩的原始字符序列”中的比特数量 写入到标准输出中 - 用于后继的解码工作??
        BinaryStdOut.write(inputCharacterSequence.length);

        // #5 使用 霍夫曼编译表 来 对“原始字符序列”进行编码/压缩
        for (int currentSpot = 0; currentSpot < inputCharacterSequence.length; currentSpot++) {
            // #5-1 对于当前字符，从编码表中找到 其所对应的编码结果
            char currentCharacter = inputCharacterSequence[currentSpot];
            String encodedBitStr = characterToEncodedValue[currentCharacter];

            // #5-2 然后把“编码结果”/“压缩结果” 写入到 标准输出中...
            for (int cursor = 0; cursor < encodedBitStr.length(); cursor++) {
                char currentBit = encodedBitStr.charAt(cursor);
                // 写入规则：字符0写成false，字符1写成true
                if (currentBit == '0') {
                    BinaryStdOut.write(false);
                } else if (currentBit == '1') {
                    BinaryStdOut.write(true);
                } else throw new IllegalStateException("Illegal state");
            }
        }

        // 关闭输出流
        BinaryStdOut.close();
    }

    // 从“原始字符串中字符的出现频率计数”中，构建出 霍夫曼单词查找树 - 手段：优先级队列
    private static Node buildTrie(int[] characterToItsFrequency) {
        // #1 先创建一堆的独立的单节点树，把它们作为作为队列元素 来 初始化优先级队列
        MinPQ<Node> nodesMinPQ = createSeparateNodesToInitPQ(characterToItsFrequency);

        while (nodesMinPQ.size() > 1) {
            // #2 把当前“最小的两棵树”合并起来，得到一棵更大的树 🐖 会添加一个新的结点作为父节点
            // 手段：使用优先级队列的delMin()能够轻易得到“最小的树/根结点”
            combineIntoOneNode(nodesMinPQ);
        }

        // #3 获取到“所有节点完全合并之后得到的huffman树” - 手段：从优先级队列中删除以获取到“当前最小的元素”
        return nodesMinPQ.delMin();
    }

    private static void combineIntoOneNode(MinPQ<Node> nodesMinPQ) {
        // 获取到队列中的 最小的两个结点
        Node leftSubNode = nodesMinPQ.delMin();
        Node rightSubNode = nodesMinPQ.delMin();
        // 创建的父节点中 不持有任何字符、频率值为左右子树的频率之和
        Node parentNode = createParentBasedOn(leftSubNode, rightSubNode);
        // 把创建出的父节点 添加回到队列中
        nodesMinPQ.insert(parentNode);
    }

    private static Node createParentBasedOn(Node leftSubNode, Node rightSubNode) {
        return new Node('\0', leftSubNode.frequency + rightSubNode.frequency, leftSubNode, rightSubNode);
    }

    private static MinPQ<Node> createSeparateNodesToInitPQ(int[] characterToItsFrequency) {
        MinPQ<Node> nodesMinPQ = new MinPQ<Node>();
        for (char currentCharacter = 0; currentCharacter < characterOption; currentCharacter++) {
            int itsFrequency = characterToItsFrequency[currentCharacter];
            if (itsFrequency > 0)
                nodesMinPQ.insert(new Node(currentCharacter, itsFrequency, null, null));
        }
        return nodesMinPQ;
    }

    // 把 “比特字符串”所编码的单词查找树 写入到 标准输出中
    // 手段：使用前序遍历的规则（根结点 - 左子树 - 右子树） 来 完全处理树中的所有结点
    private static void writeTrie(Node passedNode) {
        if (passedNode.isLeaf()) { // 如果传入的结点是一个“叶子节点”，则...
            // ① 向标准输出中写入一个1/true
            BinaryStdOut.write(true);
            // ② 把“叶子节点中的字符”以8个比特（1个字节）写入到标准输出中...
            BinaryStdOut.write(passedNode.character, 8);
            return;
        }
        // 如果是“内部结点”，则：向标准输出中写入一个0/false
        BinaryStdOut.write(false);
        // 把结点的左子树，继续(递归地)写入到标准输出中
        writeTrie(passedNode.leftSubNode);
        // 把结点的右子树，继续(递归地)写入到标准输出中
        writeTrie(passedNode.rightSubNode);
    }

    // 构造一个编码表 用于建立 字符(符号) 与其编码之间的映射关系 aka a lookup table
    private static void buildEncodedValueTable(Node passedNode, String[] symbolTable, String encodedBitStr) {
        if (!passedNode.isLeaf()) {
            buildEncodedValueTable(passedNode.leftSubNode, symbolTable, encodedBitStr + '0');
            buildEncodedValueTable(passedNode.rightSubNode, symbolTable, encodedBitStr + '1');
        } else {
            symbolTable[passedNode.character] = encodedBitStr;
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
        // #1 先从输入流中读取出 霍夫曼单词查找树(trie)
        Node huffmanTrie = readTrie();

        // #2 再读取出 待写入的字节的数量
        int expectedCharacterAmount = BinaryStdIn.readInt();

        // #3 对于期待的每一个字符...
        for (int characterOrdinal = 0; characterOrdinal < expectedCharacterAmount; characterOrdinal++) {
            // 逐个读取“输入流中的比特”，然后使用霍夫曼单词查找树 对读到的“比特序列”进行解码，来得到具体字符
            decodeInputBitsUsing(huffmanTrie);
        }

        BinaryStdOut.close();
    }

    private static void decodeInputBitsUsing(Node huffmanTrie) {
        Node currentNode = huffmanTrie;
        // 当前节点不是“叶子节点”时，沿着树的分支继续查找...
        while (!currentNode.isLeaf()) {
            currentNode = navigateViaInputBits(currentNode);
        }
        // 直到导航到“叶子节点”时（只有叶子节点中才持有字符），就可以把“结点中的字符” 打印到 标准输出中 - 至此，解码得到了当前字符
        BinaryStdOut.write(currentNode.character, 8);
    }

    private static Node navigateViaInputBits(Node currentNode) {
        boolean currentBitOfInput = BinaryStdIn.readBoolean();
        // 导航规则：如果输入bit为1，则 向右子树前进。如果输入bit为0，则向左子树前进
        if (currentBitOfInput)
            currentNode = currentNode.rightSubNode;
        else
            currentNode = currentNode.leftSubNode;
        return currentNode;
    }


    // 从标准输入中读取 霍夫曼单词查找树
    private static Node readTrie() {
        // 读取标准输入中的一个比特值
        boolean isLeaf = BinaryStdIn.readBoolean();
        // 如果比特值为1，说明对应到一个“单词查找树中的叶子节点”...
        if (isLeaf) {
            // 则：读取输入中的下8个比特，得到一个字符。并使用此字符，创建出一个 霍夫曼单词查找树中的一个叶子结点
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        } else { // 如果比特值为0，说明对应到一个“单词查找树中的内部节点”...
            // 则：创建一个内部节点，并递归地继续构造它的左右子树
            return new Node('\0', -1, readTrie(), readTrie());
        }
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
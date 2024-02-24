package com.henry.string_05.data_compress_05.huffman_compress_06.prefixCodes_application_04;

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
public class Huffman {

    // alphabet size of extended ASCII
    private static final int characterOption = 256;

    // Do not instantiate.
    private Huffman() {
    }

    // Huffman trie node
    private static class Node implements Comparable<Node> {
        private final char character;
        private final int frequency; // 字符在字符串中出现的频率 或 以此结点作为根结点的子树中的所有字符出现的总频率
        private final Node leftSubNode, rightSubNode;

        Node(char character, int frequency, Node leftSubNode, Node rightSubNode) {
            this.character = character;
            this.frequency = frequency;
            this.leftSubNode = leftSubNode;
            this.rightSubNode = rightSubNode;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert ((leftSubNode == null) && (rightSubNode == null)) || ((leftSubNode != null) && (rightSubNode != null));
            return (leftSubNode == null) && (rightSubNode == null);
        }

        // compare, based on frequency 这个API是为了支持优先队列的操作
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
        // #1 读取标准输入中传入的字符串
        String inputStr = BinaryStdIn.readString();
        char[] characterSequence = inputStr.toCharArray();

        // 对“输入字符串中的各个字符”的出现频率进行计数
        int[] characterToItsFrequency = new int[characterOption];
        for (int currentSpot = 0; currentSpot < characterSequence.length; currentSpot++) {
            char currentCharacter = characterSequence[currentSpot];
            characterToItsFrequency[currentCharacter]++;
        }

        // 根据标准输入中 字符->字符出现的频率的映射关系，构造出 霍夫曼树（最优前缀码）
        Node huffmanTrie = buildTrie(characterToItsFrequency);

        // 根据霍夫曼树 来 构造出编译表/符号表/字符编码查找表
        String[] lookupTable = new String[characterOption];
        buildLookupTable(lookupTable, huffmanTrie, "");

        // print trie for decoder
        // 把“单词查找树本身”写入到标准输出中 - 用于后续的解码工作
        writeTrie(huffmanTrie);

        // print number of bytes in original uncompressed message
        // 打印“未压缩的原始字符序列”中的比特数量
        BinaryStdOut.write(characterSequence.length);

        // #2 使用 霍夫曼编译表 来 对“原始字符序列”进行编码/压缩
        for (int currentSpot = 0; currentSpot < characterSequence.length; currentSpot++) {
            // 对于当前字符
            char currentCharacter = characterSequence[currentSpot];
            // 从编译表中找到 字符对应的编码结果
            String encodedBitStr = lookupTable[currentCharacter];

            // #3 把“编码结果”/“压缩结果” 写入到 标准输出中...  规则：字符0写成false，字符1写成true
            for (int cursor = 0; cursor < encodedBitStr.length(); cursor++) {
                char currentBit = encodedBitStr.charAt(cursor);
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

    // 从“原始字符串中字符的出现频率计数”中，构建出 霍夫曼单词查找树
    private static Node buildTrie(int[] characterToItsFrequency) {

        // initialize priority queue with singleton trees
        MinPQ<Node> nodeMinPQ = new MinPQ<Node>();
        for (char currentCharacter = 0; currentCharacter < characterOption; currentCharacter++) {
            int itsFrequency = characterToItsFrequency[currentCharacter];
            if (itsFrequency > 0)
                nodeMinPQ.insert(new Node(currentCharacter, itsFrequency, null, null));
        }

        // merge two smallest trees
        // 把当前最小的两棵树合并起来，得到一棵更大的树 🐖 会添加一个新的结点作为父节点
        while (nodeMinPQ.size() > 1) {
            Node leftSubNode = nodeMinPQ.delMin();
            Node rightSubNode = nodeMinPQ.delMin();
            Node parentNode = new Node('\0', leftSubNode.frequency + rightSubNode.frequency, leftSubNode, rightSubNode);
            nodeMinPQ.insert(parentNode);
        }
        return nodeMinPQ.delMin();
    }


    // write bitstring-encoded trie to standard output
    // 把 比特字符串编码的单词查找树 写入到 标准输出中
    // 手段：使用前序遍历的规则（根结点 - 左子树 - 右子树） 来 完全处理树中的所有结点
    private static void writeTrie(Node passedNode) {
        if (passedNode.isLeaf()) { // 如果传入的结点是一个“叶子节点”，则...
            // ① 向标准输出中写入一个1/true
            BinaryStdOut.write(true);
            // ② 把“叶子节点中的字符”以8个比特（1个字节）写入到标准输出中...
            BinaryStdOut.write(passedNode.character, 8);
            return;
        }
        // 如果是“内部结点”，则...
        // 向标准输出中写入一个0/false
        BinaryStdOut.write(false);
        // 把结点的左子树，继续(递归地)写入到标准输出中
        writeTrie(passedNode.leftSubNode);
        // 把结点的右子树，继续(递归地)写入到标准输出中
        writeTrie(passedNode.rightSubNode);
    }

    // make a lookup table from symbols and their encodings
    // 构造一个编译表 用于建立 字符(符号) 与其编码之间的映射关系 aka a lookup table
    private static void buildLookupTable(String[] symbolTable, Node passedNode, String encodedBitStr) {
        if (!passedNode.isLeaf()) {
            buildLookupTable(symbolTable, passedNode.leftSubNode, encodedBitStr + '0');
            buildLookupTable(symbolTable, passedNode.rightSubNode, encodedBitStr + '1');
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

        // 从输入流中读取出 霍夫曼单词查找树(trie)
        Node huffmanTrie = readTrie();

        // 获取到 待写入的字节的数量
        int length = BinaryStdIn.readInt();

        // 使用霍夫曼单词查找树，对“输入流中的比特流”进行解码
        for (int currentByte = 0; currentByte < length; currentByte++) {
            Node currentNode = huffmanTrie;
            // 当前节点不是“叶子节点”时，沿着树的分支继续查找...
            while (!currentNode.isLeaf()) {
                boolean currentBit = BinaryStdIn.readBoolean();
                if (currentBit) currentNode = currentNode.rightSubNode;
                else currentNode = currentNode.leftSubNode;
            }
            // 当前结点是“叶子节点”时，把结点中的字符打印到 标准输出中
            BinaryStdOut.write(currentNode.character, 8);
        }
        BinaryStdOut.close();
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
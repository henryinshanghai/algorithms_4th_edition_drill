package com.henry.symbol_table_chapter_03.balance_search_tree_03.insert_01;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class RedBlackST_drill01<Key extends Comparable<Key>, Value> {
    // 静态常量 - 用于全局引用
    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;
    private int n;         // number of key-value pairs in BST

    class Node {
        private Key key;
        private Value val;
        private Node left, right;
        private boolean color;

        public Node(Key key, Value val, boolean color) {
            this.key = key;
            this.val = val;
            this.color = color;
        }
    }

    /***************************************************************************
     *  Red-black tree insertion.
     ***************************************************************************/
    // 核心API - 插入键值对操作
    public void put(Key key, Value val) {
        // 查找key 找到则更新其值，否则为它创建一个节点
        root = insert(root, key, val);
        root.color = BLACK;
//        assert check();
    }

    private Node insert(Node h, Key key, Value val) {
        // 标准的插入操作，和父节点之间使用红链接相连
        if (h == null) {
            n++; // 这里对树中的键值对个数进行了更新
            return new Node(key, val, RED);
        }

        int cmp = key.compareTo(h.key);
        if      (cmp < 0) h.left  = insert(h.left,  key, val);
        else if (cmp > 0) h.right = insert(h.right, key, val);
        else              h.val   = val;

        // 在递归调用之后添加这些代码：路径中的每一个节点都需要经过这些处理
        // 红黑树的三种需要处理的情况：       参考： 把红节点在树上向上传递 - 插入节点到红黑树的算法（重述）
        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h); // 二叉树含有红色右链接：左旋转
        if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h); // 二叉树含有红色左链接，且左子树含有红色左链接：先对上层链接进行右旋转...
        if (isRed(h.left)  &&  isRed(h.right))     flipColors(h); // 二叉树有红色左链接&红色右链接：进行颜色转换，并把红链接向上传递

        return h;
    }
//    public void put(Key key, Value val){
//        if (key == null) throw new IllegalArgumentException("the key can not be null");
//
//        if (val == null) {
////            delete(key);
//            return;
//        }
//
//        root = put(root, key, val);
//        root.color = BLACK;
//    }

//    private Node put(Node h, Key key, Value val) {
////        if (key == null) throw new IllegalArgumentException("the key can not be null");
//
//        if (h == null) {
//            n++;
//            return new Node(key, val, RED);
//        }
//
//        int cmp = key.compareTo(h.key);
//        if (cmp > 0) h.right = put(h.right, key, val);
//        else if(cmp < 0) h.left = put(h.left, key, val);
//        else h.val = val;
//
//        // 添加节点后，调整红黑树以保持与2-3树的一一对应关系
//        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
//        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
//        if (isRed(h.left) && isRed(h.right)) flipColors(h);
//
//        return h;
//    }

    /**
     * 反转树的颜色到特定的状态
     * @param h
     */
//    private void flipColors(Node h) {
//        h.color = RED;
//        h.left.color = BLACK;
//        h.right.color = BLACK;
//    }
    private void flipColors(Node h) {
        assert !isRed(h) && isRed(h.left) && isRed(h.right);
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
    }

    /**
     * 对指定的树进行右旋转操作
     * @param h
     * @return
     */
//    private Node rotateRight(Node h) {
//        Node x = h.left;
//        h.left = x.right;
//
//        x.right = h;
//        x.color = h.color;
//        h.color = RED;
//
//        return x;
//    }

    private Node rotateRight(Node h) {
        assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }
    /**
     * 对指定的树进行左旋转操作
     * @param h
     * @return
     */
//    private Node rotateLeft(Node h) {
//        // 处理节点h
//        Node x = h.right;
//        h.right = x.left;
//
//        //处理节点x
//        x.left = h;
//        x.color = h.color;
//
//        // 设置节点h的新颜色————RED
//        h.color = RED;
//
//        return x;
//    }

    private Node rotateLeft(Node h) {
        assert (h != null) && isRed(h.right);
        Node x = h.right;  // 获取右子树x
        h.right = x.left; // 获取x的左子树，并绑定为当前节点的右子树
        x.left = h; // 把当前节点作为x的左子树
        x.color = h.color; // 更新x的颜色（用当前节点的颜色）
        h.color = RED; // 更新当前节点的颜色为红色
        return x; // 返回更新后的树的根节点
    }

    /**
     * 判断指定节点是否为红节点
     * @param x
     * @return
     */
    private boolean isRed(Node x) {
        if(x == null) return false; // 空节点视为黑链接/节点
        return x.color == RED;
    }

    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("the key can not be null");

        return get(root, key);
    }

    private Value get(Node x, Key key) {
//        if (x == null) return null;
//
//        int cmp = key.compareTo(root.key);
//        if (cmp > 0) return get(root.right, key);
//        else if(cmp < 0) return get(root.left, key);
//        else return x.val;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else              return x.val;
        }

        return null;
    }

    // 测试用例所需要的其他方法
    public Iterable<Key> keys(){
        Queue<Key> queue = new Queue<>();
        keys(root, queue);
        return queue;
    }

    /**
     * 从二叉树中获取到所有键的集合
     * 二叉树是一个递归结构
     * @param root
     * @param queue
     */
    private void keys(Node root, Queue<Key> queue) {
        if (root == null) return;

        keys(root.left, queue);
        queue.enqueue(root.key);
        keys(root.right, queue);
    }

    public static void main(String[] args) {
        String test = "S E A R C H E X A M P L E";
        String[] keys = test.split(" ");

        RedBlackST_drill01<String, Integer> rst = new RedBlackST_drill01<>();
        for (int i = 0; i < keys.length; i++) {
            rst.put(keys[i], i);
        }

//        StdOut.println("size = " + rst.size());
//        StdOut.println("min  = " + rst.min());
//        StdOut.println("max  = " + rst.max());
        StdOut.println();

        for (String key : rst.keys()) {
            System.out.println(key + " ： " + rst.get(key));
        }
    }

}

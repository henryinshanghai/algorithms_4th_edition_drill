package com.henry.symbol_table_chapter_03.binary_search_tree_02;

public class BST_learn<Key extends Comparable<Key>, Value> {
    // 实例变量
    private Node root;

    // 实例变量所需要的内部类
    private class Node{
        private Key key; //节点中存储的键值对的键
        private Value val; // 节点中存储的键值对的值
        private Node left, right; // 指向子树的链接：左子树、右子树
        private int N; // 以此节点作为根节点的子树中的节点数量

        // 构造方法为什么会有这几个参数而不是其他参数？
        // 答：这里的参数主要是为了对实例变量进行强制初始化 其他的参数可以通过 实例.变量 = 值; 的方式再去初始化
        // 主要是方便使用者使用
        public Node(Key key, Value val, int n) {
            // this用来区分实例变量 与 方法参数
            this.key = key;
            this.val = val;
            N = n;
        }
    }

    // 快捷API
    public int size(){
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }

    // 核心API
    /**
     * 作用：获取到键为key的键值对所存储的值
     * @param key
     * @return
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("传入的参数不能为null");
        return get(root, key);
    }

    /**
     * 递归方法的作用：从二叉树中获取到键为key的节点中所存储的值
     * @param x
     * @param key
     * @return
     */
    private Value get(Node x, Key key) {
        if (key == null) throw new IllegalArgumentException("传入的参数不能为null");

        if (x == null) return null;

        int cmp = x.key.compareTo(key);
        if (cmp > 0) return get(x.left, key);
        else if(cmp < 0) return get(x.right, key);
        else return x.val;
    }

    /**
     * 向键值对集合中添加新的键值对/更新已经存在的键值对中的值
     * 疑问：删除操作呢？
     * @param key
     * @param val
     */
    public void put(Key key, Value val) {
        // 查找key 找到则更新key的值。 否则为它来创建一个新的节点————这会更新二叉树本身，所以需要一个递归方法
        root = put(root, key, val);
    }

    /**
     * 递归方法的作用：1 如果key在二叉树中已经存在，就更新key的值。返回更新后的二叉树；
     * 2 如果key在二叉树中不存在，就创建新的节点，添加到二叉树中。返回更新后的二叉树
     *
     * 特征：如果一直找不到对应的key的话，最终会得到一个空的子树。这时候就需要创建一个新的节点
     * @param x
     * @param key
     * @param val
     * @return
     */
    public Node put(Node x, Key key, Value val){
        // 2 递归终止条件：树是一个空树 aka 根节点为null 这时候直接返回创建的新节点即可
        if(x == null) return new Node(key, val, 1);

        // 3 本级递归中做什么？
        /*
            ① 把二叉树 = 根节点 + 左子树 + 右子树；
            ② 判断根节点是不是预期的节点
            ③ 如果是，就更新根节点的值，并返回更新后的二叉树；
            ④ 如果不是，就在左右子树中继续调用递归方法，以查找/更新节点；调用返回的是更新后的二叉树
            pickle：这里的方法兼具更新 / 插入功能。所以代码有点子理不清了
            ⑤ 更新完成二叉树后，需要同步更新二叉树中的节点数N
         */
        int cmp = key.compareTo(x.key);
        if(cmp < 0) x.left = put(x.left, key, val);
        else if(cmp > 0) x.right = put(x.right, key, val); // 如果更新子树后，一直找不到，最终会得到一个空子树————这是终结条件
        else x.val = val;

        // 这里的节点数不一定就+1了 怎么办？ 使用通用公式
        x.N = size(x.left) + size(x.right) + 1;

        return x; // 返回更新后的新二叉树
    }
    // 其他辅助方法

}
// 对于用户来说，这就是一个符号表/键值对集合
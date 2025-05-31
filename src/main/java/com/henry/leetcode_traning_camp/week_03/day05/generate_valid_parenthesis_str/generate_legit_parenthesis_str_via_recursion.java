package com.henry.leetcode_traning_camp.week_03.day05.generate_valid_parenthesis_str;

import java.util.ArrayList;
import java.util.List;

// 验证：对于每次只有两种option的增量构建问题，可以使用 两次递归的方式 以 从空字符串""+当前可用的左右字符的数量 来 构建出有效的括号字符串；
// 此问题底层对应的逻辑结构是一棵二叉树，因此递归方法中 出现了两次对自己的调用
public class generate_legit_parenthesis_str_via_recursion {
    public static void main(String[] args) {
        int pairAmount = 3;
        List<String> generatedLegitParenthesis = generateParenthesis(pairAmount);
        System.out.println(generatedLegitParenthesis);
    }

    private static List<String> legitStrList = new ArrayList<>();

    /**
     * 构建有效的括号组合
     *
     * @param pairAmount
     * @return
     */
    private static List<String> generateParenthesis(int pairAmount) {
        // 对特殊情况的判断
        if (pairAmount == 0) {
            return legitStrList;
        }

        // 执行深度优先遍历，搜索可能的结果
        generateAndAddLegitStrIntoList("", pairAmount, pairAmount);
        return legitStrList;
    }

    /**
     * @param currentGeneratedParenthesisStr  组合的当前值
     * @param availableLeftParenthesisAmount  当前可用的左括号的数量
     * @param availableRightParenthesisAmount 当前可用的右括号的数量
     */
    private static void generateAndAddLegitStrIntoList(String currentGeneratedParenthesisStr,
                                                       int availableLeftParenthesisAmount,
                                                       int availableRightParenthesisAmount) {
        // 因为每一次尝试，都使用新的字符串变量，所以无需回溯
        // 在递归终止的时候，直接把它添加到结果集即可，注意与「力扣」第 46 题、第 39 题区分
        if (availableLeftParenthesisAmount == 0 && availableRightParenthesisAmount == 0) {
            legitStrList.add(currentGeneratedParenthesisStr);
            return;
        }

        // 剪枝（如图，可用的左括号数量 严格大于 可用的右括号的数量，才剪枝，注意这个细节）
        if (availableLeftParenthesisAmount > availableRightParenthesisAmount) {
            return;
        }

        // 可以直接添加左括号，不会导致任何breach
        if (availableLeftParenthesisAmount > 0) {
            generateAndAddLegitStrIntoList(currentGeneratedParenthesisStr + "(", availableLeftParenthesisAmount - 1, availableRightParenthesisAmount);
        }

        // 只有当 字符串中的左括号数量 > 字符串中的右括号数量时，才能够添加右括号字符
        if (availableRightParenthesisAmount > 0 && availableLeftParenthesisAmount < availableRightParenthesisAmount) {
            generateAndAddLegitStrIntoList(currentGeneratedParenthesisStr + ")", availableLeftParenthesisAmount, availableRightParenthesisAmount - 1);
        }
    }
}

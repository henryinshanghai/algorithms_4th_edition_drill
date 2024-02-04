package com.henry.basic_chapter_01.collection_types.stack.application;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/******************************************************************************
 *  Compilation:  javac Evaluate.java
 *  Execution:    java Evaluate
 *  Dependencies: Stack.java
 *
 *  Evaluates (fully parenthesized) arithmetic expressions using
 *  Dijkstra's two-stack algorithm.
 *
 *  % java Evaluate
 *  ( 1 + ( ( 2 + 3 ) * ( 4 * 5 ) ) )
 *  101.0
 *
 *  % java Evaulate
 *  ( ( 1 + sqrt ( 5 ) ) / 2.0 )
 *  1.618033988749895
 *
 *
 *  Note: the operators, operands, and parentheses must be
 *  separated by whitespace. Also, each operation must
 *  be enclosed in parentheses. For example, you must write
 *  ( 1 + ( 2 + 3 ) ) instead of ( 1 + 2 + 3 ).
 *  See EvaluateDeluxe.java for a fancier version.
 *
 *
 *  Remarkably, Dijkstra's algorithm computes the same
 *  answer if we put each operator *after* its two operands
 *  instead of *between* them.
 *
 *  % java Evaluate
 *  ( 1 ( ( 2 3 + ) ( 4 5 * ) * ) + )
 *  101.0
 *
 *  Moreover, in such expressions, all parentheses are redundant!
 *  Removing them yields an expression known as a postfix expression.
 *  1 2 3 + 4 5 * * +
 *
 *
 ******************************************************************************/

public class Evaluate {
    public static void main(String[] args) {
        Stack<String> operatorStack = new Stack<String>();
        Stack<Double> operandStack = new Stack<Double>();

        while (!StdIn.isEmpty()) {
            // 从标准输入中读取 当前字符
            String currentCharacter = StdIn.readString();

            if (currentCharacter.equals("(")) ; // 如果当前字符是 左括号，则：什么都不做
            else if (currentCharacter.equals("+")) operatorStack.push(currentCharacter);
            else if (currentCharacter.equals("-")) operatorStack.push(currentCharacter);
            else if (currentCharacter.equals("*")) operatorStack.push(currentCharacter);
            else if (currentCharacter.equals("/")) operatorStack.push(currentCharacter);
            else if (currentCharacter.equals("sqrt")) operatorStack.push(currentCharacter); // 如果当前字符是操作符，则：将其压入操作符栈
            else if (currentCharacter.equals(")")) { // 如果当前字符是 右括号，则...
                String operator = operatorStack.pop();
                double calculateResult = 0.0;

                if (operator.equals("+")) {
                    double added = operandStack.pop();
                    Double addend = operandStack.pop();
                    calculateResult = addend + added;
                } else if (operator.equals("-")) {
                    double subtractor = operandStack.pop();
                    Double minuend = operandStack.pop();
                    calculateResult = minuend - subtractor;
                } else if (operator.equals("*")) {
                    double multiplier = operandStack.pop();
                    Double multiplicand = operandStack.pop();
                    calculateResult = multiplicand * multiplier;
                } else if (operator.equals("/")) {
                    double divider = operandStack.pop();
                    Double dividend = operandStack.pop();
                    calculateResult = dividend / divider;
                } else if (operator.equals("sqrt")) {
                    double operand = operandStack.pop();
                    calculateResult = Math.sqrt(operand);
                }
                operandStack.push(calculateResult);
            } else operandStack.push(Double.parseDouble(currentCharacter));
        }
        StdOut.println(operandStack.pop());
    }
}
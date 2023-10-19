package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ArithmeticSolver {
    public double solveByLibrary(String expression){
        try{
            Expression exp = new ExpressionBuilder(expression).build();
            return exp.evaluate();
        }catch(Exception e){
            e.printStackTrace();
            return Double.NaN;
        }
    }
    public double solve(String expression) {
        Stack<Double> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == ' ') {
                continue;
            }
            if (Character.isDigit(ch)) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                i--;
                operands.push(Double.parseDouble(sb.toString()));
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    double rightOperand = operands.pop();
                    double leftOperand = operands.pop();
                    char operator = operators.pop();
                    double result = applyOperator(leftOperand, rightOperand, operator);
                    operands.push(result);
                }
                operators.pop();
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operators.isEmpty() && hasHigherPriority(ch, operators.peek())) {
                    double rightOperand = operands.pop();
                    double leftOperand = operands.pop();
                    char operator = operators.pop();
                    double result = applyOperator(leftOperand, rightOperand, operator);
                    operands.push(result);
                }
                operators.push(ch);
            } else {
                throw new IllegalArgumentException("Неверный символ: " + ch);
            }
        }

        while (!operators.isEmpty()) {
            double rightOperand = operands.pop();
            double leftOperand = operands.pop();
            char operator = operators.pop();
            double result = applyOperator(leftOperand, rightOperand, operator);
            operands.push(result);
        }

        return operands.pop();
    }

    public static double regexSolve(String expression){
        Stack<Double> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        Pattern numberPattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = numberPattern.matcher(expression);

        while (matcher.find()) {
            double operand = Double.parseDouble(matcher.group());
            operands.push(operand);
        }

        Pattern operatorPattern = Pattern.compile("[+\\-*/()]");
        matcher = operatorPattern.matcher(expression);

        while (matcher.find()) {
            char operator = matcher.group().charAt(0);
            if (operator == '(') {
                operators.push(operator);
            } else if (operator == ')') {
                while (operators.peek() != '(') {
                    double rightOperand = operands.pop();
                    double leftOperand = operands.pop();
                    char op = operators.pop();
                    double result = applyOperator(leftOperand, rightOperand, op);
                    operands.push(result);
                }
                operators.pop();
            } else if (operator == '+' || operator == '-' || operator == '*' || operator == '/') {
                while (!operators.isEmpty() && hasHigherPriority(operator, operators.peek())) {
                    double rightOperand = operands.pop();
                    double leftOperand = operands.pop();
                    char op = operators.pop();
                    double result = applyOperator(leftOperand, rightOperand, op);
                    operands.push(result);
                }
                operators.push(operator);
            }
        }

        while (!operators.isEmpty()) {
            double rightOperand = operands.pop();
            double leftOperand = operands.pop();
            char operator = operators.pop();
            double result = applyOperator(leftOperand, rightOperand, operator);
            operands.push(result);
        }

        return operands.pop();
    }

    private static boolean hasHigherPriority(char op1, char op2) {
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return true;
        }
        if ((op1 == '+' || op1 == '-') && (op2 == '*' || op2 == '/')) {
            return false;
        }
        return false;
    }

    private static double applyOperator(double leftOperand, double rightOperand, char operator) {
        switch (operator) {
            case '+':
                return leftOperand + rightOperand;
            case '-':
                return leftOperand - rightOperand;
            case '*':
                return leftOperand * rightOperand;
            case '/':
                return leftOperand / rightOperand;
            default:
                throw new IllegalArgumentException("Неверный оператор: " + operator);
        }
    }
}
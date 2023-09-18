import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class ArithmeticSolver {
    public double solve(String expression) {
        // Решаем арифметическую задачу или уравнение
        // В данном примере решается только арифметическая задача
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

    private boolean hasHigherPriority(char op1, char op2) {
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return true;
        }
        if ((op1 == '+' || op1 == '-') && (op2 == '*' || op2 == '/')) {
            return false;
        }
        return false;
    }

    private double applyOperator(double leftOperand, double rightOperand, char operator) {
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


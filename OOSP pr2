import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nВведите математическое выражение (или 'exit' для выхода):");
            System.out.println("Поддерживаемые операции: +, -, *, /, ^, **, //, !, exp(), log(), скобки");
            System.out.println("Пример: -3234+((exp(2)*843/log(3234)-4232123)/(34+123+32+5))*3234");
            
            String input = scanner.nextLine();
            
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            
            try {
                double result = evaluateExpression(input);
                System.out.println("Результат: " + result);
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
        
        scanner.close();
    }

    public static double evaluateExpression(String expression) {
        // Проверка на пустое выражение
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Выражение не может быть пустым");
        }

        // Проверка баланса скобок
        if (!checkParenthesesBalance(expression)) {
            throw new IllegalArgumentException("Несбалансированное количество скобок");
        }

        // Проверка, что выражение начинается и заканчивается допустимым символом
        if (!expression.matches("^[-+0-9(].*[0-9)]$")) {
            throw new IllegalArgumentException("Выражение должно начинаться и заканчиваться числом или скобкой");
        }

        // Удаляем все пробелы
        expression = expression.replaceAll("\\s+", "");

        // Конвертируем в постфиксную нотацию и вычисляем
        String postfix = infixToPostfix(expression);
        return evaluatePostfix(postfix);
    }

    private static boolean checkParenthesesBalance(String expression) {
        int balance = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }

    private static String infixToPostfix(String expression) {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                // Собираем все число
                while (i < expression.length() && 
                      (Character.isDigit(expression.charAt(i)) || 
                       expression.charAt(i) == '.' || 
                       (expression.charAt(i) == '-' && (i == 0 || isOperator(expression.charAt(i-1)) || expression.charAt(i-1) == '(')))) {
                    output.append(expression.charAt(i));
                    i++;
                }
                output.append(" ");
                i--;
            } 
            // Обработка функций exp() и log()
            else if (i + 3 < expression.length() && expression.substring(i, i+4).equalsIgnoreCase("exp(")) {
                stack.push('e');
                i += 3;
            }
            else if (i + 3 < expression.length() && expression.substring(i, i+4).equalsIgnoreCase("log(")) {
                stack.push('l');
                i += 3;
            }
            else if (c == '(') {
                stack.push(c);
            } 
            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop()).append(" ");
                }
                stack.pop(); // Удаляем '('
            } 
            else if (isOperator(c) || c == '!' || 
                    (c == '*' && i+1 < expression.length() && expression.charAt(i+1) == '*') || 
                    (c == '/' && i+1 < expression.length() && expression.charAt(i+1) == '/')) {
                // Обработка **
                if (c == '*' && i+1 < expression.length() && expression.charAt(i+1) == '*') {
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence('^')) {
                        output.append(stack.pop()).append(" ");
                    }
                    stack.push('^');
                    i++; // Пропускаем следующий '*'
                } 
                // Обработка //
                else if (c == '/' && i+1 < expression.length() && expression.charAt(i+1) == '/') {
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence('\\')) {
                        output.append(stack.pop()).append(" ");
                    }
                    stack.push('\\');
                    i++; // Пропускаем следующий '/'
                } 
                // Обработка факториала
                else if (c == '!') {
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                        output.append(stack.pop()).append(" ");
                    }
                    output.append("! ");
                }
                else {
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                        output.append(stack.pop()).append(" ");
                    }
                    stack.push(c);
                }
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" ");
        }

        return output.toString();
    }

    private static double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfix.split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (token.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(token));
            } else {
                switch (token.charAt(0)) {
                    case '+': 
                        double bAdd = stack.pop();
                        double aAdd = stack.pop();
                        stack.push(aAdd + bAdd); 
                        break;
                    case '-': 
                        double bSub = stack.pop();
                        double aSub = stack.pop();
                        stack.push(aSub - bSub); 
                        break;
                    case '*': 
                        double bMul = stack.pop();
                        double aMul = stack.pop();
                        stack.push(aMul * bMul); 
                        break;
                    case '/': 
                        double bDiv = stack.pop();
                        double aDiv = stack.pop();
                        if (bDiv == 0) throw new ArithmeticException("Деление на ноль");
                        stack.push(aDiv / bDiv); 
                        break;
                    case '^': 
                        double bPow = stack.pop();
                        double aPow = stack.pop();
                        stack.push(Math.pow(aPow, bPow)); 
                        break;
                    case '\\': // Целочисленное деление
                        double bIntDiv = stack.pop();
                        double aIntDiv = stack.pop();
                        if (bIntDiv == 0) throw new ArithmeticException("Деление на ноль");
                        stack.push((double)((int)(aIntDiv / bIntDiv))); 
                        break;
                    case '!': // Факториал
                        double num = stack.pop();
                        if (num < 0) throw new ArithmeticException("Факториал отрицательного числа");
                        if (num > 20) throw new ArithmeticException("Факториал слишком большого числа");
                        stack.push(factorial((int)num));
                        break;
                    case 'e': // exp()
                        double expArg = stack.pop();
                        stack.push(Math.exp(expArg));
                        break;
                    case 'l': // log()
                        double logArg = stack.pop();
                        if (logArg <= 0) throw new ArithmeticException("Логарифм неположительного числа");
                        stack.push(Math.log(logArg) / Math.log(2));
                        break;
                }
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Некорректное выражение");
        }

        return stack.pop();
    }

    private static double factorial(int n) {
        if (n == 0) return 1;
        double result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static int precedence(char c) {
        switch (c) {
            case 'e':
            case 'l':
                return 5;
            case '^': 
                return 4;
            case '*':
            case '/':
            case '\\': 
                return 3;
            case '+':
            case '-': 
                return 2;
            case '!':
                return 1;
            default: 
                return 0;
        }
    }
}

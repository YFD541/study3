import java.util.Scanner;
import java.util.Stack;

public class SimpleMathCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nВведите математическое выражение (или 'exit' для выхода):");
            System.out.println("Поддерживаемые операции: +, -, *, /, ^, // (целочисленное деление)");
            System.out.println("Пример: -3234+843/3234-4232123/(34+123+32+5)*3234");
            
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

        // Проверка, что выражение начинается и заканчивается числом
        if (!expression.matches("^-?\\d+.*\\d+$")) {
            throw new IllegalArgumentException("Выражение должно начинаться и заканчиваться числом");
        }

        // Удаляем все пробелы
        expression = expression.replaceAll("\\s+", "");

        // Конвертируем в постфиксную нотацию и вычисляем
        String postfix = infixToPostfix(expression);
        return evaluatePostfix(postfix);
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
                       (expression.charAt(i) == '-' && (i == 0 || isOperator(expression.charAt(i-1)))))) {
                    output.append(expression.charAt(i));
                    i++;
                }
                output.append(" ");
                i--;
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    output.append(stack.pop()).append(" ");
                }
                stack.pop(); // Удаляем '('
            } else if (isOperator(c) || (c == '/' && i+1 < expression.length() && expression.charAt(i+1) == '/')) {
                // Обработка //
                if (c == '/' && i+1 < expression.length() && expression.charAt(i+1) == '/') {
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence('\\')) {
                        output.append(stack.pop()).append(" ");
                    }
                    stack.push('\\');
                    i++; // Пропускаем следующий '/'
                } else {
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
                double val2 = stack.pop();
                double val1 = stack.pop();
                switch (token.charAt(0)) {
                    case '+': stack.push(val1 + val2); break;
                    case '-': stack.push(val1 - val2); break;
                    case '*': stack.push(val1 * val2); break;
                    case '/': 
                        if (val2 == 0) throw new ArithmeticException("Деление на ноль");
                        stack.push(val1 / val2); 
                        break;
                    case '^': stack.push(Math.pow(val1, val2)); break;
                    case '\\': // Обработка //
                        if (val2 == 0) throw new ArithmeticException("Деление на ноль");
                        stack.push((double)((int)(val1 / val2))); 
                        break;
                }
            }
        }

        return stack.pop();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static int precedence(char c) {
        switch (c) {
            case '^': return 4;
            case '*':
            case '/':
            case '\\': return 3;
            case '+':
            case '-': return 2;
            default: return 0;
        }
    }
}

import java.util.*;
import java.io.*;

public class Calculator {
    private static List<String> history = new ArrayList<>();
    private static final String HISTORY_FILE = "calculator_history.log";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nМеню калькулятора:");
            System.out.println("1. Вычислить выражение");
            System.out.println("2. Показать историю");
            System.out.println("3. Сохранить историю в файл");
            System.out.println("4. Выход");
            System.out.print("Выберите действие: ");
            
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 1 до 4");
                continue;
            }
            
            switch (choice) {
                case 1:
                    calculateExpression(scanner);
                    break;
                case 2:
                    showHistory();
                    break;
                case 3:
                    saveHistoryToFile(scanner);
                    break;
                case 4:
                    System.out.println("Выход из программы");
                    return;
                default:
                    System.out.println("Ошибка: неверный выбор");
            }
        }
    }

    private static void calculateExpression(Scanner scanner) {
        System.out.print("Введите математическое выражение: ");
        String expression = scanner.nextLine();
        
        try {
            double result = evaluate(expression);
            System.out.println("Результат: " + result);
            history.add(expression + " = " + result);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static double evaluate(String expression) {
        // Упрощенная реализация вычисления (без учета приоритетов операций)
        // В реальном приложении нужно использовать более сложный алгоритм
        return new Object() {
            int pos = -1, ch;
            
            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }
            
            boolean isDigit() {
                return Character.isDigit(ch) || ch == '.';
            }
            
            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Неправильное выражение");
                return x;
            }
            
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }
            
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }
            
            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();
                
                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Отсутствует закрывающая скобка");
                } else if (isDigit()) {
                    while (isDigit()) nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Неправильное выражение");
                }
                
                if (eat('^')) x = Math.pow(x, parseFactor());
                
                return x;
            }
            
            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }
        }.parse();
    }

    private static void showHistory() {
        if (history.isEmpty()) {
            System.out.println("История пуста");
            return;
        }
        
        System.out.println("\nИстория вычислений:");
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
    }

    private static void saveHistoryToFile(Scanner scanner) {
        if (history.isEmpty()) {
            System.out.println("Нет данных для сохранения");
            return;
        }
        
        System.out.print("Введите путь для сохранения (или оставьте пустым): ");
        String filePath = scanner.nextLine();
        
        String finalPath = getFinalPath(filePath);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(finalPath))) {
            for (String entry : history) {
                writer.println(entry);
            }
            System.out.println("История сохранена в файл: " + new File(finalPath).getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    private static String getFinalPath(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return HISTORY_FILE;
        }
        
        filePath = filePath.trim();
        
        if (filePath.endsWith("/") || filePath.endsWith("\\")) {
            return filePath + "log.log";
        }
        
        if (!filePath.contains(".")) {
            return filePath + ".log";
        }
        
        return filePath;
    }
}

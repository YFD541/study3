import java.util.*;
import java.util.concurrent.*;

public class Main {
    // Имитация базы данных
    private static Map<Integer, DataRecord> database = new HashMap<>();
    // Кэш для read-only данных
    private static Map<Integer, DataRecord> cache = new ConcurrentHashMap<>();
    // Таймер для обновления данных
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    public static void main(String[] args) {
        // Инициализация тестовых данных
        initializeTestData();
        
        // Запуск периодического обновления данных
        scheduler.scheduleAtFixedRate(() -> refreshNonCachedData(), 0, 5, TimeUnit.SECONDS);
        
        // Пример работы с API
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== API Interface ===");
            System.out.println("1. Получить данные по ID");
            System.out.println("2. Обновить данные");
            System.out.println("3. Выгрузить результаты");
            System.out.println("4. Выход");
            System.out.print("Выберите действие: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    getData(scanner);
                    break;
                case 2:
                    updateData(scanner);
                    break;
                case 3:
                    exportResults(scanner);
                    break;
                case 4:
                    scheduler.shutdown();
                    System.exit(0);
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }
    
    // Инициализация тестовых данных
    private static void initializeTestData() {
        database.put(1, new DataRecord(1, "Read-only данные 1", true));
        database.put(2, new DataRecord(2, "Read-only данные 2", true));
        database.put(3, new DataRecord(3, "Изменяемые данные 1", false));
        database.put(4, new DataRecord(4, "Изменяемые данные 2", false));
        
        // Кэшируем read-only данные
        for (DataRecord record : database.values()) {
            if (record.isReadOnly()) {
                cache.put(record.getId(), record);
            }
        }
    }
    
    // Получение данных
    private static void getData(Scanner scanner) {
        System.out.print("Введите ID записи: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        DataRecord record;
        if (cache.containsKey(id)) {
            record = cache.get(id);
            System.out.println("(Данные из кэша)");
        } else {
            record = database.get(id);
            if (record == null) {
                System.out.println("Запись не найдена!");
                return;
            }
            System.out.println("(Данные из БД)");
        }
        
        System.out.println("ID: " + record.getId());
        System.out.println("Содержимое: " + record.getContent());
        System.out.println("Read-only: " + record.isReadOnly());
    }
    
    // Обновление данных
    private static void updateData(Scanner scanner) {
        System.out.print("Введите ID записи для обновления: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        DataRecord record = database.get(id);
        if (record == null) {
            System.out.println("Запись не найдена!");
            return;
        }
        
        if (record.isReadOnly()) {
            System.out.println("Ошибка: запись read-only не может быть изменена!");
            return;
        }
        
        System.out.print("Введите новое содержимое: ");
        String newContent = scanner.nextLine();
        
        record.setContent(newContent);
        database.put(id, record);
        System.out.println("Данные обновлены!");
    }
    
    // Выгрузка результатов
    private static void exportResults(Scanner scanner) {
        System.out.println("\n=== Выгрузка результатов ===");
        System.out.println("1. Выгрузить все данные");
        System.out.println("2. Выгрузить только read-only данные");
        System.out.println("3. Выгрузить только изменяемые данные");
        System.out.print("Выберите вариант: ");
        
        int option = scanner.nextInt();
        scanner.nextLine();
        
        List<DataRecord> results = new ArrayList<>();
        switch (option) {
            case 1:
                results.addAll(database.values());
                break;
            case 2:
                for (DataRecord record : database.values()) {
                    if (record.isReadOnly()) {
                        results.add(record);
                    }
                }
                break;
            case 3:
                for (DataRecord record : database.values()) {
                    if (!record.isReadOnly()) {
                        results.add(record);
                    }
                }
                break;
            default:
                System.out.println("Неверный выбор!");
                return;
        }
        
        System.out.println("\n=== Результаты выгрузки ===");
        for (DataRecord record : results) {
            System.out.println("ID: " + record.getId() + 
                             ", Содержимое: " + record.getContent() + 
                             ", Read-only: " + record.isReadOnly());
        }
    }
    
    // Периодическое обновление неизменяемых данных
    private static void refreshNonCachedData() {
        System.out.println("\n[Система] Обновление данных из БД...");
        for (DataRecord record : database.values()) {
            if (!record.isReadOnly()) {
                // В реальной системе здесь был бы запрос к БД
                System.out.println("Обновлена запись ID: " + record.getId());
            }
        }
    }
    
    // Класс для хранения данных
    static class DataRecord {
        private int id;
        private String content;
        private boolean readOnly;
        
        public DataRecord(int id, String content, boolean readOnly) {
            this.id = id;
            this.content = content;
            this.readOnly = readOnly;
        }
        
        public int getId() { return id; }
        public String getContent() { return content; }
        public boolean isReadOnly() { return readOnly; }
        public void setContent(String content) { this.content = content; }
    }
}

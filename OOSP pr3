import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, User> users = new HashMap<>();
    private static Map<String, Cik> ciks = new HashMap<>();
    private static List<Candidate> candidates = new ArrayList<>();
    private static List<Voting> votings = new ArrayList<>();
    private static User currentUser = null;

    public static void main(String[] args) {
        // Добавляем тестовые данные
        initializeTestData();
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showRoleMenu();
            }
        }
    }

    private static void initializeTestData() {
        // Администратор
        User admin = new User("admin", "admin123", "Админ Системы", "01.01.1980", "123-456-789 00");
        admin.setRole("ADMIN");
        users.put("admin", admin);
        
        // ЦИК
        Cik cik1 = new Cik("cik1", "cik123");
        ciks.put("cik1", cik1);
        
        // Кандидаты
        candidates.add(new Candidate("cand1", "cand123", "Иванов Иван Иванович"));
        candidates.add(new Candidate("cand2", "cand123", "Петров Петр Петрович"));
        
        // Голосования
        votings.add(new Voting("Выборы президента", "31.12.2023"));
    }

    private static void showLoginMenu() {
        System.out.println("\n=== Система голосования ===");
        System.out.println("1. Вход");
        System.out.println("2. Регистрация");
        System.out.println("3. Выход");
        System.out.print("Выберите: ");
        
        int choice = getIntInput(1, 3);
        
        switch (choice) {
            case 1: login(); break;
            case 2: registerUser(); break;
            case 3: System.exit(0);
        }
    }

    private static void login() {
        System.out.print("\nЛогин: ");
        String login = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        
        if (users.containsKey(login) {
            if (users.get(login).getPassword().equals(password)) {
                currentUser = users.get(login);
                System.out.println("Добро пожаловать, " + currentUser.getFullName() + "!");
                return;
            }
        } else if (ciks.containsKey(login)) {
            if (ciks.get(login).getPassword().equals(password)) {
                currentUser = ciks.get(login);
                System.out.println("Добро пожаловать, ЦИК!");
                return;
            }
        }
        
        System.out.println("Неверный логин или пароль!");
    }

    private static void registerUser() {
        System.out.println("\n=== Регистрация ===");
        System.out.print("ФИО: ");
        String fullName = scanner.nextLine();
        System.out.print("Дата рождения (дд.мм.гггг): ");
        String birthDate = scanner.nextLine();
        System.out.print("СНИЛС: ");
        String snils = scanner.nextLine();
        System.out.print("Логин: ");
        String login = scanner.nextLine();
        
        if (users.containsKey(login) || ciks.containsKey(login)) {
            System.out.println("Логин занят!");
            return;
        }
        
        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        
        users.put(login, new User(login, password, fullName, birthDate, snils));
        System.out.println("Регистрация успешна!");
    }

    private static void showRoleMenu() {
        switch (currentUser.getRole()) {
            case "ADMIN": showAdminMenu(); break;
            case "CIK": showCikMenu(); break;
            case "CANDIDATE": showCandidateMenu(); break;
            default: showUserMenu();
        }
    }

    private static void showAdminMenu() {
        System.out.println("\n=== Администратор ===");
        System.out.println("1. Управление пользователями");
        System.out.println("2. Управление ЦИК");
        System.out.println("3. Управление кандидатами");
        System.out.println("4. Выйти");
        System.out.print("Выберите: ");
        
        int choice = getIntInput(1, 4);
        
        switch (choice) {
            case 1: manageUsers(); break;
            case 2: manageCiks(); break;
            case 3: manageCandidates(); break;
            case 4: currentUser = null;
        }
    }

    private static void manageUsers() {
        System.out.println("\n=== Пользователи ===");
        if (users.isEmpty()) {
            System.out.println("Нет пользователей");
            return;
        }
        
        int i = 1;
        for (User user : users.values()) {
            System.out.println(i++ + ". " + user.getLogin() + " - " + user.getFullName());
        }
        
        System.out.print("\nУдалить (номер) или 0 для отмены: ");
        int choice = getIntInput(0, users.size());
        
        if (choice > 0) {
            User userToRemove = (User) users.values().toArray()[choice-1];
            users.remove(userToRemove.getLogin());
            System.out.println("Удалено!");
        }
    }

    private static void showCikMenu() {
        System.out.println("\n=== ЦИК ===");
        System.out.println("1. Создать голосование");
        System.out.println("2. Добавить кандидата");
        System.out.println("3. Просмотр результатов");
        System.out.println("4. Выйти");
        System.out.print("Выберите: ");
        
        int choice = getIntInput(1, 4);
        
        switch (choice) {
            case 1: createVoting(); break;
            case 2: addCandidate(); break;
            case 3: showResults(); break;
            case 4: currentUser = null;
        }
    }

    private static void createVoting() {
        System.out.println("\n=== Новое голосование ===");
        System.out.print("Название: ");
        String name = scanner.nextLine();
        System.out.print("Дата окончания (дд.мм.гггг): ");
        String endDate = scanner.nextLine();
        
        votings.add(new Voting(name, endDate));
        System.out.println("Голосование создано!");
    }

    private static void showResults() {
        System.out.println("\n=== Результаты ===");
        for (Voting voting : votings) {
            System.out.println("\n" + voting.getName() + " (до " + voting.getEndDate() + ")");
            if (voting.getResults().isEmpty()) {
                System.out.println("Нет результатов");
            } else {
                for (Map.Entry<String, Integer> entry : voting.getResults().entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue() + " голосов");
                }
            }
        }
    }

    private static void showUserMenu() {
        System.out.println("\n=== Пользователь ===");
        System.out.println("1. Проголосовать");
        System.out.println("2. Список кандидатов");
        System.out.println("3. Выйти");
        System.out.print("Выберите: ");
        
        int choice = getIntInput(1, 3);
        
        switch (choice) {
            case 1: vote(); break;
            case 2: showCandidates(); break;
            case 3: currentUser = null;
        }
    }

    private static void vote() {
        System.out.println("\n=== Голосование ===");
        if (votings.isEmpty()) {
            System.out.println("Нет активных голосований");
            return;
        }
        
        System.out.println("Выберите голосование:");
        for (int i = 0; i < votings.size(); i++) {
            System.out.println((i+1) + ". " + votings.get(i).getName());
        }
        
        System.out.print("Ваш выбор: ");
        int votingChoice = getIntInput(1, votings.size());
        Voting voting = votings.get(votingChoice-1);
        
        System.out.println("\nКандидаты:");
        for (int i = 0; i < candidates.size(); i++) {
            System.out.println((i+1) + ". " + candidates.get(i).getFullName());
        }
        
        System.out.print("Ваш выбор: ");
        int candidateChoice = getIntInput(1, candidates.size());
        String candidateName = candidates.get(candidateChoice-1).getFullName();
        
        voting.addVote(candidateName);
        System.out.println("Спасибо за ваш голос!");
    }

    private static int getIntInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) return input;
                System.out.print("Введите число от " + min + " до " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Введите число: ");
            }
        }
    }

    // Остальные методы (addCandidate, manageCiks, manageCandidates, showCandidateMenu) аналогично
}

class User {
    private String login;
    private String password;
    private String fullName;
    private String birthDate;
    private String snils;
    private String role = "USER";
    
    public User(String login, String password, String fullName, String birthDate, String snils) {
        this.login = login;
        this.password = password;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.snils = snils;
    }
    
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

class Cik extends User {
    public Cik(String login, String password) {
        super(login, password, "ЦИК " + login, "01.01.1970", "000-000-000 00");
        setRole("CIK");
    }
}

class Candidate extends User {
    public Candidate(String login, String password, String fullName) {
        super(login, password, fullName, "01.01.1970", "000-000-000 00");
        setRole("CANDIDATE");
    }
}

class Voting {
    private String name;
    private String endDate;
    private Map<String, Integer> results = new HashMap<>();
    
    public Voting(String name, String endDate) {
        this.name = name;
        this.endDate = endDate;
    }
    
    public String getName() { return name; }
    public String getEndDate() { return endDate; }
    public Map<String, Integer> getResults() { return results; }
    
    public void addVote(String candidateName) {
        results.put(candidateName, results.getOrDefault(candidateName, 0) + 1);
    }
}

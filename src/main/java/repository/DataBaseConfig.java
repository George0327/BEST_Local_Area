package repository;

public class DataBaseConfig {
    public static final String JDBC_URL = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/best_la");
    public static final String JDBC_USER = System.getenv().getOrDefault("DB_USER", "postgres");
    public static final String JDBC_PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "");
}

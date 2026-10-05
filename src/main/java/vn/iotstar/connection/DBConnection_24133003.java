package vn.iotstar.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection_24133003 {

    public Connection getConnection() throws Exception {
        String url = setting("DB_URL", "jdbc:sqlserver://localhost:1433;databaseName=WebExamDB;encrypt=true;trustServerCertificate=true");
        String password = setting("DB_PASSWORD", null);
        if (password == null || password.isBlank())
            throw new IllegalStateException("Cần cấu hình DB_PASSWORD trước khi chạy ứng dụng.");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, setting("DB_USER", "sa"), password);
    }

    public static String setting(String key, String fallback) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}

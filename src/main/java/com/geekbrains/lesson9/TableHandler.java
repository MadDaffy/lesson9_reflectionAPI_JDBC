package com.geekbrains.lesson9;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TableHandler {
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;

    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new SQLException("Unable to connect");
        }
    }

    public static void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void buildTable(Class classTable) {
        try {
            connect();
            StringBuilder stringBuilder = new StringBuilder();
            Map<Class, String> map = new HashMap<>();
            map.put(int.class, "INTEGER");
            map.put(String.class, "TEXT");
            try {
                classTable = Class.forName("com.geekbrains.lesson9.Student");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            if (!classTable.isAnnotationPresent(TableAnnotation.class)) {
                System.out.println("No annotation");
            } else {
                stringBuilder =
                        new StringBuilder("CREATE TABLE ")
                                .append(((TableAnnotation) classTable.getAnnotation(TableAnnotation.class)).title())
                                .append(" (");
            }
            for (Field field : classTable.getDeclaredFields()) {
                if (field.isAnnotationPresent(ColumnAnnotation.class)) {
                    stringBuilder.append(field.getName()).append(" ")
                            .append(map.get(field.getType()))
                            .append(", ");
                }
            }
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(");");
            try {
                statement.executeUpdate(stringBuilder.toString());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

    }

    public static void addObjectInTable(Student student) {
        try {
            connect();
            StringBuilder stringBuilder = new StringBuilder();
            int fieldCount = 0;
            Class classTable;
            try {
                classTable = Class.forName("com.geekbrains.lesson9.Student");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            if (!classTable.isAnnotationPresent(TableAnnotation.class)) {
                System.out.println("No annotation");
            } else {
                stringBuilder =
                        new StringBuilder("INSERT INTO ")
                                .append(((TableAnnotation) classTable.getAnnotation(TableAnnotation.class)).title())
                                .append(" (");
            }
            for (Field field : classTable.getDeclaredFields()) {
                if (field.isAnnotationPresent(ColumnAnnotation.class)) {
                    stringBuilder.append(field.getName())
                            .append(", ");
                    fieldCount++;
                }
            }
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(") Values (");
            for (int i = 0; i < fieldCount; i++) {
                stringBuilder.append("?, ");
            }
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(") ");
            preparedStatement = connection.prepareStatement(stringBuilder.toString());
            //Как тут сделать через setObject в цикле, предварительно кастанув к типу поля?
            preparedStatement.setInt(1, student.id);
            preparedStatement.setString(2, student.name);
            try {
                int rows = preparedStatement.executeUpdate();
                System.out.println(rows);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            disconnect();
        }

    }
}

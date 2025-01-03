package com.university.exam;

import com.university.exam.model.SimpleUser;
import java.util.*;

public class UserManager {
    private static UserManager instance;
    private Map<String, SimpleUser> users;

    private UserManager() {
        users = new HashMap<>();
        // Add default admin account
        users.put("admin", new SimpleUser("admin", "admin123", "admin", "System Administrator"));
        // Add default teacher account
        users.put("teacher", new SimpleUser("teacher", "teacher123", "teacher", "Default Teacher"));
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean registerUser(String username, String password, String role, String fullName) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new SimpleUser(username, password, role, fullName));
        return true;
    }

    public SimpleUser getUser(String username) {
        return users.get(username);
    }

    public boolean authenticateUser(String username, String password) {
        SimpleUser user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public List<SimpleUser> getAllStudents() {
        List<SimpleUser> students = new ArrayList<>();
        for (SimpleUser user : users.values()) {
            if (user.getRole().equals("student")) {
                students.add(user);
            }
        }
        return students;
    }

    public List<SimpleUser> getAllTeachers() {
        List<SimpleUser> teachers = new ArrayList<>();
        for (SimpleUser user : users.values()) {
            if (user.getRole().equals("teacher")) {
                teachers.add(user);
            }
        }
        return teachers;
    }
}

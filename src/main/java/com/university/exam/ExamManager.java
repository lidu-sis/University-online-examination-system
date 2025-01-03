package com.university.exam;

import com.university.exam.model.SimpleExam;
import java.util.*;

public class ExamManager {
    private static ExamManager instance;
    private Map<String, SimpleExam> exams;
    private Map<String, Map<String, Integer>> results; // username -> (examTitle -> score)

    private ExamManager() {
        exams = new HashMap<>();
        results = new HashMap<>();
    }

    public static ExamManager getInstance() {
        if (instance == null) {
            instance = new ExamManager();
        }
        return instance;
    }

    public void addExam(SimpleExam exam) {
        exams.put(exam.getTitle(), exam);
    }

    public SimpleExam getExam(String title) {
        return exams.get(title);
    }

    public Map<String, Integer> getAvailableExams() {
        Map<String, Integer> availableExams = new HashMap<>();
        for (Map.Entry<String, SimpleExam> exam : exams.entrySet()) {
            availableExams.put(exam.getKey(), exam.getValue().getDuration());
        }
        return availableExams;
    }

    public void submitExamResult(String username, String examTitle, int score) {
        results.computeIfAbsent(username, k -> new HashMap<>()).put(examTitle, score);
    }

    public Map<String, Integer> getUserResults(String username) {
        return results.getOrDefault(username, new HashMap<>());
    }

    public Map<String, Map<String, Integer>> getAllResults() {
        return results;
    }
}

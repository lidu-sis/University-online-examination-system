package com.university.exam.model;

import java.util.ArrayList;
import java.util.List;

public class SimpleExam {
    private String title;
    private List<SimpleQuestion> questions;
    private int duration; // in minutes

    public SimpleExam(String title, int duration) {
        this.title = title;
        this.duration = duration;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(SimpleQuestion question) {
        questions.add(question);
    }

    public String getTitle() { return title; }
    public List<SimpleQuestion> getQuestions() { return questions; }
    public int getDuration() { return duration; }
}

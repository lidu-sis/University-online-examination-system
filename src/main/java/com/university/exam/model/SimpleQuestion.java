package com.university.exam.model;

import java.util.List;
import java.util.ArrayList;

public class SimpleQuestion {
    private String question;
    private List<String> options;
    private int correctOption;

    public SimpleQuestion(String question, List<String> options, int correctOption) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
    }

    public String getQuestion() { return question; }
    public List<String> getOptions() { return options; }
    public int getCorrectOption() { return correctOption; }
}

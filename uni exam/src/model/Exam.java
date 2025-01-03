package model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Exam {
    private int id;
    private String title;
    private String subject;
    private Date startTime;
    private int duration; // in minutes
    private List<Question> questions;
    private int totalMarks;
    private boolean isActive;

    public Exam(int id, String title, String subject, Date startTime, int duration) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.startTime = startTime;
        this.duration = duration;
        this.questions = new ArrayList<>();
        this.isActive = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { 
        this.questions = questions;
        calculateTotalMarks();
    }
    
    public int getTotalMarks() { return totalMarks; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    private void calculateTotalMarks() {
        this.totalMarks = questions.stream()
                .mapToInt(Question::getMarks)
                .sum();
    }

    public void addQuestion(Question question) {
        questions.add(question);
        calculateTotalMarks();
    }
}

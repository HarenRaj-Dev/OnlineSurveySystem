package com.example.survey.model;

public class SurveyResponse {
    private final String timestampIso;
    private final String name;
    private final String email;
    private final String rating;   // 1..5 as string for simplicity
    private final String comments;

    public SurveyResponse(String timestampIso, String name, String email, String rating, String comments) {
        this.timestampIso = timestampIso;
        this.name = name;
        this.email = email;
        this.rating = rating;
        this.comments = comments;
    }

    public String getTimestampIso() { return timestampIso; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRating() { return rating; }
    public String getComments() { return comments; }
}
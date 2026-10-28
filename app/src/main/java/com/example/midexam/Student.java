package com.example.midexam;

public class Student {
    private int id;
    private String name;
    private String studentId;
    private boolean enrolled;

    public Student(int id, String name, String studentId, boolean enrolled) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
        this.enrolled = enrolled;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getStudentId() { return studentId; }
    public boolean isEnrolled() { return enrolled; }

    @Override
    public String toString() {
        return name + " (" + studentId + ") - " + (enrolled ? "Enrolled" : "Not Enrolled");
    }
}
package org.wgu.termtracker.models;

import java.io.Serializable;

public class CourseMentorModel implements Serializable {
    private long courseMentorId;

    private String name;

    private String phoneNumber;

    private String email;

    public long getCourseMentorId() {
        return courseMentorId;
    }

    public void setCourseMentorId(long courseMentorId) {
        this.courseMentorId = courseMentorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("%s%n%s%n%s", name, phoneNumber, email);
    }
}

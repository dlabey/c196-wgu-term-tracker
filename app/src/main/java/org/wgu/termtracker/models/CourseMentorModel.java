package org.wgu.termtracker.models;

import android.icu.text.SimpleDateFormat;

import org.wgu.termtracker.Constants;

import java.io.Serializable;

public class CourseMentorModel implements Serializable {
    private String name;

    private String phoneNumber;

    private String email;

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

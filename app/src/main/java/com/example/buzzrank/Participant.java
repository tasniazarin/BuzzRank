package com.example.buzzrank;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Participant {

    private String name;
    @ServerTimestamp
    private Date timestamp;

    public Participant() {}

    public Participant(String name, Date timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
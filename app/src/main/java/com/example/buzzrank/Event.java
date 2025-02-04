package com.example.buzzrank;

import java.util.List;
import java.util.Map;

public class Event {


    private String name;
    private String id;


    // Default constructor
    public Event() {}

    // Constructor with parameters
    public Event(String name, String id) {
        this.name = name;
        this.id = id;
    }

    // Getter for event name
    public String getName() {
        return name;
    }

    // Getter for event ID
    public String getId() {
        return id;
    }




}


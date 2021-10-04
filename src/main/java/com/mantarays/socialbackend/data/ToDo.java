package com.mantarays.socialbackend.data;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private boolean completed;

    public ToDo() {
        this.title = "";
        this.completed = false;
    }

    public ToDo(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public boolean getCompleted() {
        return completed;
    }
}

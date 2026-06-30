package com.aegis.security;
import java.sql.Timestamp;
public class SecurityLog {
    private int id;
    private String username;
    private String action;
    private Timestamp timestamp;
    public SecurityLog(int id, String username, String action, Timestamp timestamp) {
        this.id = id;
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
    }
    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getAction() {
        return action;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
}

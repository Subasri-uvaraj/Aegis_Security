package com.aegis.security;

import java.sql.Timestamp;

public class Message {
    private int id;
    private String sender;
    private String receiver;
    private String encryptedMessage;
    private String decryptedMessage;
    private Timestamp timestamp;
    private String status;
    public Message() {}
    public Message(int id, String sender, String receiver, String encryptedMessage, String decryptedMessage, Timestamp timestamp, String status) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.encryptedMessage = encryptedMessage;
        this.decryptedMessage = decryptedMessage;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getEncryptedMessage() { return encryptedMessage; }
    public String getDecryptedMessage() { return decryptedMessage; }
    public Timestamp getTimestamp() { return timestamp; }
    public String getStatus() { return status; }

    //Setters 
    public void setId(int id) { this.id = id; }
    public void setSender(String sender) { this.sender = sender; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public void setEncryptedMessage(String encryptedMessage) { this.encryptedMessage = encryptedMessage; }
    public void setDecryptedMessage(String decryptedMessage) { this.decryptedMessage = decryptedMessage; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
    public void setStatus(String status) { this.status = status; }
}

package com.aegis.security;

import java.util.List;

public class MessageController {

    private final MessageService messageService;

    public MessageController() {
        this.messageService = new MessageService();
    }

    // Send a personal message
    public void sendMessage(String sender, String receiver, String message) {
        try {
            messageService.sendMessage(sender, receiver, message);
        } catch (Exception e) {
            System.out.println("Error while sending message: " + e.getMessage());
        }
    }

    // Send a group message
    public void sendGroupMessage(String sender, String message) {
        try {
            messageService.sendGroupMessage(sender, message);
        } catch (Exception e) {
            System.out.println("Error while sending group message: " + e.getMessage());
        }
    }

    // Fetch all messages (personal + group)
    public List<Message> fetchMessages(String username) {
        try {
            return messageService.fetchMessages(username);
        } catch (Exception e) {
            System.out.println("Error while fetching messages: " + e.getMessage());
            return null;
        }
    }
}

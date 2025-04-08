package com.email.writer.app;

import lombok.Data;

import java.util.Collection;

@Data
public class EmailRequest {
     private String emailContent;
     private String tone;

    // Getter
    public String getEmailContent() {
        return emailContent;
    }

    // Setter
    public void setEmailContent(String newName) {
        this.emailContent = newName;
    }

    // Getter
    public String getTone() {
        return tone;
    }

    // Setter
    public void setTone(String newName) {
        this.tone = newName;
    }
}

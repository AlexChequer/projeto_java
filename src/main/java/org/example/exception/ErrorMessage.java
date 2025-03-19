package org.example.exception;


import java.time.LocalDateTime;

public class ErrorMessage {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;

    public ErrorMessage(int status, String message, String path)
    {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() { return timestamp; };
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public String getPath() { return path;}
}

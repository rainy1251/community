package com.service.community.ui.view;

public class MessageEvent{
    private String message;
    public  MessageEvent(String message){
        this.message=message;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
}
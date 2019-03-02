package com.service.community.ui.view;

public class ChangeHomeEvent {
    private String message;
    public ChangeHomeEvent(String message){
        this.message=message;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
}
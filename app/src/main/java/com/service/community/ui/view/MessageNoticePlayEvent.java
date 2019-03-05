package com.service.community.ui.view;

public class MessageNoticePlayEvent {
    private String message;
    public MessageNoticePlayEvent(String message){
        this.message=message;
    }
 
    public String getMessage() {
        return message;
    }
 
    public void setMessage(String message) {
        this.message = message;
    }
}
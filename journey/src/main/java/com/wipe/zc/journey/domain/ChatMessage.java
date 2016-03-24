package com.wipe.zc.journey.domain;

import java.util.Calendar;

/**
 * Created by hp on 2016/3/23.
 */
public class ChatMessage {

    String receiveAvatar;
    String receiveContent;
    Calendar chatTime;
    String sendAvatar;
    String sendContent;

    public String getReceiveAvatar() {
        return receiveAvatar;
    }

    public void setReceiveAvatar(String receiveAvatar) {
        this.receiveAvatar = receiveAvatar;
    }

    public String getReceiveContent() {
        return receiveContent;
    }

    public void setReceiveContent(String receiveContent) {
        this.receiveContent = receiveContent;
    }

    public Calendar getChatTime() {
        return chatTime;
    }

    public void setChatTime(Calendar chatTime) {
        this.chatTime = chatTime;
    }

    public String getSendAvatar() {
        return sendAvatar;
    }

    public void setSendAvatar(String sendAvatar) {
        this.sendAvatar = sendAvatar;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }
}

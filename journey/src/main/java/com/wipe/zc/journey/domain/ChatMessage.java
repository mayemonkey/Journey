package com.wipe.zc.journey.domain;

import java.util.Calendar;

/**
 * Created by hp on 2016/3/23.
 */
public class ChatMessage {

    String receiveAvatar;
    String content;
    Calendar chatTime;
    String sendAvatar;
    String messageId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReceiveAvatar() {
        return receiveAvatar;
    }

    public void setReceiveAvatar(String receiveAvatar) {
        this.receiveAvatar = receiveAvatar;
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

}

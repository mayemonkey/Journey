package com.wipe.zc.journey.domain;

import com.easemob.chat.EMMessage;

import java.util.Calendar;

/**
 * Chat信息
 */
public class ChatMessage {

    private String receiveAvatar;
    private String content;
    private Calendar chatTime;
    private String sendAvatar;
    private String messageId;
    private EMMessage.Type type;

    public EMMessage.Type getType() {
        return type;
    }

    public void setType(EMMessage.Type type) {
        this.type = type;
    }

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

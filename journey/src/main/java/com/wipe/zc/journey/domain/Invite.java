package com.wipe.zc.journey.domain;

/**
 * 好友邀请对象
 */
public class Invite {
    private String inviter;
    private String reason;

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

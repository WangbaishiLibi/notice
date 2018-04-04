package com.model;

import java.io.Serializable;

/**
 * Created by lb on 2018/2/27.
 */
public class MsgNotice implements Serializable{

    private Integer userId;
    private String userName;
    private Integer caseId;
    private String msgType;
    private String msgContent;

    public MsgNotice() {
    }

    public MsgNotice(Integer userId, String userName, Integer caseId, String msgType, String msgContent) {
        this.userId = userId;
        this.userName = userName;
        this.caseId = caseId;
        this.msgType = msgType;
        this.msgContent = msgContent;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}

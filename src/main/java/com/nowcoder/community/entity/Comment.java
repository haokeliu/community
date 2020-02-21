package com.nowcoder.community.entity;

import java.util.Date;

public class Comment {

    private int id;
    private int UserId;
    private int entityType;
    private int targetId;
    private String content;

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    private int entityId;
    private int status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getTargetId() {
        return targetId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", UserId=" + UserId +
                ", entityType=" + entityType +
                ", targetId=" + targetId +
                ", content='" + content + '\'' +
                ", entityId=" + entityId +
                ", status=" + status +
                ", createTime=" + createTime +
                '}';
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private Date createTime;

}

package com.webber.retorfitdemo;

public class LikesItem {
    public String id;
    public String targetId;
    public String targetType = "PRODUCT";

    public LikesItem() {

    }

    public LikesItem(String targetId) {
        this.targetId = targetId;
    }
}

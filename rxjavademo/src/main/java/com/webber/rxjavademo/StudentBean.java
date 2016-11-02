package com.webber.rxjavademo;

import java.util.List;

/**
 * Created by mxh on 2016/11/2.
 * Describe：
 */

public class StudentBean {
    private String name;
    private List<Cause> causeList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Cause> getCauseList() {
        return causeList;
    }

    public void setCauseList(List<Cause> causeList) {
        this.causeList = causeList;
    }
}

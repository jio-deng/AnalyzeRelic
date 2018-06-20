package com.opengl.deng.testnewrelic.analyzesdk.bean;

/**
 * @Description bean for user event
 * Created by deng on 2018/6/20.
 */
public class UserEventBean {
    private String category;
    private String name;
    private String message;
    private long startTime;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

}

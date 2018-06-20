package com.opengl.deng.testnewrelic.analyzesdk.bean;

/**
 * @Description bean for user performance
 * Created by deng on 2018/6/20.
 */
public class UserPerformBean {

    private String surface;
    private long startTime;
    private long endTime;
    private long duration;

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}

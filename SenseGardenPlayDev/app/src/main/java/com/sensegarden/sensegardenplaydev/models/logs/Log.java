package com.sensegarden.sensegardenplaydev.models.logs;

public class Log {

    private final String activityType;
    private final String caregiverId;
    private final String caregiverReg;
    private final String date;
    private final String duration;
    private final int feedbackValue;
    private final String flowId;
    private final String id;
    private final String liveSessionId;
    private final String mediaElementEndTime;
    private final String mediaElementId;
    private final String mediaElementStartTime;
    private final String mediaElementType;
    private final String mood;
    private final String primaryId;
    private final String primaryReg;
    private final String startTime;
    private final String textFeedback;

    public Log(String activityType, String caregiverId, String caregiverReg, String date, String duration, int feedbackValue, String flowId, String id, String liveSessionId, String mediaElementEndTime, String mediaElementId, String mediaElementStartTime, String mediaElementType, String mood, String primaryId, String primaryReg, String startTime, String textFeedback) {
        this.activityType = activityType;
        this.caregiverId = caregiverId;
        this.caregiverReg = caregiverReg;
        this.date = date;
        this.duration = duration;
        this.feedbackValue = feedbackValue;
        this.flowId = flowId;
        this.id = id;
        this.liveSessionId = liveSessionId;
        this.mediaElementEndTime = mediaElementEndTime;
        this.mediaElementId = mediaElementId;
        this.mediaElementStartTime = mediaElementStartTime;
        this.mediaElementType = mediaElementType;
        this.mood = mood;
        this.primaryId = primaryId;
        this.primaryReg = primaryReg;
        this.startTime = startTime;
        this.textFeedback = textFeedback;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getCaregiverId() {
        return caregiverId;
    }

    public String getCaregiverReg() {
        return caregiverReg;
    }

    public String getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }

    public int getFeedbackValue() {
        return feedbackValue;
    }

    public String getFlowId() {
        return flowId;
    }

    public String getId() {
        return id;
    }

    public String getLiveSessionId() {
        return liveSessionId;
    }

    public String getMediaElementEndTime() {
        return mediaElementEndTime;
    }

    public String getMediaElementId() {
        return mediaElementId;
    }

    public String getMediaElementStartTime() {
        return mediaElementStartTime;
    }

    public String getMediaElementType() {
        return mediaElementType;
    }

    public String getMood() {
        return mood;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public String getPrimaryReg() {
        return primaryReg;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTextFeedback() {
        return textFeedback;
    }
}

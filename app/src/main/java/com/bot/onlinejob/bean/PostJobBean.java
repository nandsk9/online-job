package com.bot.onlinejob.bean;

import java.io.Serializable;

public class PostJobBean implements Serializable {
    String jobId;
    String jobType;
    String jobCategory;
    String jobName;
    String jobLocation;
    String jobContactNumber;
    String jobWage;
    String jobQualification;
    String jobDate;
    String jobTime;
    String jobImgUrl;
    String status;
    String postedOnDate;
    String providerUserId;
    String seekerUserId;
    //default constructor
    public PostJobBean() {

    }
    //parameterized constructor


    public PostJobBean(String jobId, String jobType, String jobCategory, String jobName, String jobLocation, String jobContactNumber, String jobWage, String jobQualification, String jobDate, String jobTime, String jobImgUrl, String status, String postedOnDate, String providerUserId, String seekerUserId) {
        this.jobId = jobId;
        this.jobType = jobType;
        this.jobCategory = jobCategory;
        this.jobName = jobName;
        this.jobLocation = jobLocation;
        this.jobContactNumber = jobContactNumber;
        this.jobWage = jobWage;
        this.jobQualification = jobQualification;
        this.jobDate = jobDate;
        this.jobTime = jobTime;
        this.jobImgUrl = jobImgUrl;
        this.status = status;
        this.postedOnDate = postedOnDate;
        this.providerUserId = providerUserId;
        this.seekerUserId = seekerUserId;
    }

    //getters and setters


    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getSeekerUserId() {
        return seekerUserId;
    }

    public void setSeekerUserId(String seekerUserId) {
        this.seekerUserId = seekerUserId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobContactNumber() {
        return jobContactNumber;
    }

    public void setJobContactNumber(String jobContactNumber) {
        this.jobContactNumber = jobContactNumber;
    }

    public String getJobWage() {
        return jobWage;
    }

    public void setJobWage(String jobWage) {
        this.jobWage = jobWage;
    }

    public String getJobQualification() {
        return jobQualification;
    }

    public void setJobQualification(String jobQualification) {
        this.jobQualification = jobQualification;
    }

    public String getJobDate() {
        return jobDate;
    }

    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }

    public String getJobTime() {
        return jobTime;
    }

    public void setJobTime(String jobTime) {
        this.jobTime = jobTime;
    }

    public String getJobImgUrl() {
        return jobImgUrl;
    }

    public void setJobImgUrl(String jobImgUrl) {
        this.jobImgUrl = jobImgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = status;

    }

    public String getPostedOnDate() {
        return postedOnDate;
    }

    public void setPostedOnDate(String postedOnDate) {
        this.postedOnDate = postedOnDate;
    }

}

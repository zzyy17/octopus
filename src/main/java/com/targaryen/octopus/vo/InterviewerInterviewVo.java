package com.targaryen.octopus.vo;

import lombok.Getter;

import java.util.Date;

/**
 * @author Liu Mengyang
 */
@Getter
public class InterviewerInterviewVo {
    private final int interviewId;
    private final Date interviewStartTime;
    private final String interviewPlace;
    private final int interviewResultStatus;
    private final int applicantStatus;
    private final int reservationStatus;
    private final String interviewResultComment;
    private final int applicationId;
    private final int interviewerId;
    private final String interviewerName;
    private final int interviewerStatus;
    private final int applicantId;
    private final String applicantName;
    private final int rounds;
    private final String postName;
    private final int departmentId;
    private final String departmentName;
    private final String postType;
    private final String postLocale;
    private final int interviewNum;

    public static class Builder {
        private int interviewId;
        private Date interviewStartTime;
        private String interviewPlace;
        private int interviewResultStatus;
        private int applicantStatus;
        private int reservationStatus;
        private String interviewResultComment;
        private int applicationId;
        private int interviewerId;
        private String interviewerName;
        private int interviewerStatus;
        private int applicantId;
        private String applicantName;
        private int rounds;
        private String postName;
        private int departmentId;
        private String departmentName;
        private String postType;
        private String postLocale;
        private int interviewNum;


        public Builder interviewId(int interviewId) {
            this.interviewId = interviewId;
            return this;
        }

        public Builder interviewStartTime(Date interviewStartTime) {
            this.interviewStartTime = interviewStartTime;
            return this;
        }

        public Builder interviewPlace(String interviewPlace) {
            this.interviewPlace = interviewPlace;
            return this;
        }

        public Builder interviewResultStatus(int interviewResultStatus) {
            this.interviewResultStatus = interviewResultStatus;
            return this;
        }

        public Builder applicantStatus(int applicantStatus) {
            this.applicantStatus = applicantStatus;
            return this;
        }

        public Builder reservationStatus(int interviewStatus) {
            this.reservationStatus = interviewStatus;
            return this;
        }

        public Builder interviewResultComment(String interviewResultComment) {
            this.interviewResultComment = interviewResultComment;
            return this;
        }

        public Builder applicationId(int applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder interviewerId(int interviewerId) {
            this.interviewerId = interviewerId;
            return this;
        }

        public Builder interviewerName(String interviewerName) {
            this.interviewerName = interviewerName;
            return this;
        }

        public Builder interviewerStatus(int interviewerStatus) {
            this.interviewerStatus = interviewerStatus;
            return this;
        }

        public Builder applicantId(int applicantId) {
            this.applicantId = applicantId;
            return this;
        }

        public Builder applicantName(String applicantName) {
            this.applicantName = applicantName;
            return this;
        }

        public Builder rounds(int rounds) {
            this.rounds = rounds;
            return this;
        }

        public Builder postName(String postName) {
            this.postName = postName;
            return this;
        }

        public Builder departmentId(int departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder postType(String postType) {
            this.postType = postType;
            return this;
        }

        public Builder postLocale(String postLocale) {
            this.postLocale = postLocale;
            return this;
        }

        public Builder interviewNum(int interviewNum) {
            this.interviewNum = interviewNum;
            return this;
        }

        public InterviewerInterviewVo build() {
            return new InterviewerInterviewVo(this);
        }

    }

    private InterviewerInterviewVo(Builder builder) {
        this.interviewId = builder.interviewId;
        this.interviewStartTime = builder.interviewStartTime;
        this.interviewPlace = builder.interviewPlace;
        this.interviewResultStatus = builder.interviewResultStatus;
        this.applicantStatus = builder.applicantStatus;
        this.reservationStatus = builder.reservationStatus;
        this.interviewResultComment = builder.interviewResultComment;
        this.applicationId = builder.applicationId;
        this.interviewerId = builder.interviewerId;
        this.interviewerName = builder.interviewerName;
        this.interviewerStatus = builder.interviewerStatus;
        this.applicantId = builder.applicantId;
        this.applicantName = builder.applicantName;
        this.rounds = builder.rounds;
        this.postName = builder.postName;
        this.departmentId = builder.departmentId;
        this.departmentName = builder.departmentName;
        this.postType = builder.postType;
        this.postLocale = builder.postLocale;
        this.interviewNum = builder.interviewNum;
    }
}

package com.targaryen.octopus.dto;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 *  @author He Junfeng
 * */

@Data
@Entity
@Table(name = "t_resume")
@EntityListeners(AuditingEntityListener.class)
public class ResumeDto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_resume_seq")
    @SequenceGenerator(name = "t_resume_seq", sequenceName = "t_resume_seq", allocationSize = 1)
    private int resumeId;

    @NotBlank
    private String applicantName;
    private int applicantSex = -1;
    private int applicantAge = -1;
    private String applicantSchool;
    private int applicantDegree = -1;
    private String applicantMajor;
    private String applicantCity;
    private String applicantEmail;
    private String applicantPhone;
    private String applicantCV;
    private String applicantHometown;
    private String applicantNation;
    private String applicantPoliticalStatus;
    private String applicantMaritalStatus;
    @Temporal(TemporalType.TIMESTAMP)
    private Date applicantDateOfBirth;
    @Temporal(TemporalType.TIMESTAMP)
    private Date applicantTimeToWork;
    private int applicantCurrentSalary = -1;
    private int applicantExpectSalary = -1;
    @Temporal(TemporalType.TIMESTAMP)
    private Date applicantDutyTime;
    private String recommenderName;
    private String applicantAddress;
    private String applicantSelfIntro;
    private String applicantPhoto;
    private String applicantDegreePhoto;
    private String familyContactRelation;
    private String familyContactName;
    private String familyContactCompany;
    private String familyContactPhoneNum;

    @JoinColumn(name = "applicant_id")
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private ApplicantDto applicant;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "resume", cascade = CascadeType.REMOVE)
    private List<WorkExperienceDto> workExperiences;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "resume", cascade = CascadeType.REMOVE)
    private List<EducationExperienceDto> educationExperiences;


}

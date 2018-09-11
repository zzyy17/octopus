package com.targaryen.octopus.service;

import com.targaryen.octopus.dao.*;
import com.targaryen.octopus.dto.*;
import com.targaryen.octopus.util.DataTransferUtil;
import com.targaryen.octopus.util.StatusCode;
import com.targaryen.octopus.util.status.ApplicantStatus;
import com.targaryen.octopus.util.status.ApplicationStatus;
import com.targaryen.octopus.util.status.InterviewerStatus;
import com.targaryen.octopus.util.status.ReservationStatus;
import com.targaryen.octopus.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Created by Liu Mengyang on 2018/09/05
 */
@Service
public class ApplicantServiceImpl implements ApplicantService {

    private ApplicantDtoRepository applicantDtoRepository;
    private UserDtoRepository userDtoRepository;
    private ResumeDtoRepository resumeDtoRepository;
    private PostDtoRepository postDtoRepository;
    private ApplicationDtoRepository applicationDtoRepository;
    private InterviewDtoRepository interviewDtoRepository;

    @Autowired
    public ApplicantServiceImpl(DaoFactory daoFactory) {
        this.applicantDtoRepository = daoFactory.getApplicantDtoRepository();
        this.userDtoRepository = daoFactory.getUserDtoRepository();
        this.resumeDtoRepository = daoFactory.getResumeDtoRepository();
        this.postDtoRepository = daoFactory.getPostDtoRepository();
        this.applicantDtoRepository = daoFactory.getApplicantDtoRepository();
        this.interviewDtoRepository = daoFactory.getInterviewDtoRepository();
        this.applicationDtoRepository = daoFactory.getApplicationDtoRepository();
    }


    public int SaveResume(int userId, ResumeVo resumeVo) {
        ResumeDto resumeDto;
        ApplicantDto applicantDto;

        applicantDto = applicantDtoRepository.findApplicantDtoByApplicantId(resumeVo.getApplicantId());

        if(findResumeByUserId(userId) == null) {
            resumeDto = new ResumeDto();
            resumeDto.setApplicant(applicantDto);
        }
        else {
            resumeDto = applicantDto.getResume();
        }

        try {
            applicantDto.setResume(resumeDto);
            resumeDto.setApplicantAge(resumeVo.getApplicantAge());
            resumeDto.setApplicantCity(resumeVo.getApplicantCity());
            resumeDto.setApplicantCV(resumeVo.getApplicantCV());
            resumeDto.setApplicantDegree(resumeVo.getApplicantDegree());
            resumeDto.setApplicantEmail(resumeVo.getApplicantEmail());
            resumeDto.setApplicantMajor(resumeVo.getApplicantMajor());
            resumeDto.setApplicantName(resumeVo.getApplicantName());
            resumeDto.setApplicantPhone(resumeVo.getApplicantPhone());
            resumeDto.setApplicantSchool(resumeVo.getApplicantSchool());
            resumeDto.setApplicantSex(resumeVo.getApplicantSex());
            resumeDtoRepository.save(resumeDto);
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }

        return StatusCode.SUCCESS;
    }

    public ResumeVo findResumeByUserId(int userId) {
        ApplicantDto applicantDto;
        UserDto userDto;
        ResumeDto resumeDto;

        try {
            userDto = userDtoRepository.findUserDtoByUserId(userId);
            applicantDto = userDto.getApplicant();
            if(applicantDto == null)
                return null;
            resumeDto = applicantDto.getResume();
            if(resumeDto == null)
                return null;
        } catch (DataAccessException e) {
            return null;
        }

        return new ResumeVo.Builder()
                .applicantAge(resumeDto.getApplicantAge())
                .applicantCity(resumeDto.getApplicantCity())
                .applicantCV(resumeDto.getApplicantCV())
                .applicantDegree(resumeDto.getApplicantDegree())
                .applicantEmail(resumeDto.getApplicantEmail())
                .applicantMajor(resumeDto.getApplicantMajor())
                .applicantName(resumeDto.getApplicantName())
                .applicantPhone(resumeDto.getApplicantPhone())
                .applicantSchool(resumeDto.getApplicantSchool())
                .applicantSex(resumeDto.getApplicantSex())
                .resumeId(resumeDto.getResumeId()).build();
    }


    public int CreateNewApplication(ApplicationVo applicationVo) {
        ApplicationDto applicationDto = new ApplicationDto();
        PostDto postDto;
        ApplicantDto applicantDto;
        List<ApplicationDto> existed = applicationDtoRepository.findByApplicantAndPost(
                applicantDtoRepository.findApplicantDtoByApplicantId(applicationVo.getApplicantId()),
                postDtoRepository.findPostDtoByPostId(applicationVo.getPostId())
        );


        for(ApplicationDto a: existed) {
            if(a.getStatus() >= 0)
                return StatusCode.FAILURE;
        }


        applicationDto.setStatus(ApplicationStatus.INIT);

        try {
            postDto = postDtoRepository.findPostDtoByPostId(applicationVo.getPostId());
            applicantDto = applicantDtoRepository.findApplicantDtoByApplicantId(applicationVo.getApplicantId());
            applicationDto.setApplicant(applicantDto);
            applicationDto.setPost(postDto);
            applicationDto.setCreateTime(Calendar.getInstance().getTime());
            applicationDtoRepository.save(applicationDto);
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }

        return StatusCode.SUCCESS;
    }

    public List<ApplicantApplicationVo> findApplicationsByUserId(int userId) {
        UserDto userDto;
        ApplicantDto applicantDto;
        List<ApplicationDto> applicationDtos;
        List<ApplicantApplicationVo> applicationVos = new ArrayList<>();

        try {
            userDto = userDtoRepository.findUserDtoByUserId(userId);
            if(userDto == null)
                return new ArrayList<>();
            applicantDto = userDto.getApplicant();
            if(applicantDto == null)
                return new ArrayList<>();
            applicationDtos = applicantDto.getApplications();
            for(ApplicationDto a: applicationDtos) {
                applicationVos.add(
                        DataTransferUtil.ApplicationDtoToApplicantApplicationVo(a)
                );
            }
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }

        return applicationVos;
    }

    private List<InterviewDto> findInterviewDtosByUserIdAndApplicantStatus(int userId, int applicantStatus) {
        UserDto userDto;
        ApplicantDto applicantDto;
        List<ApplicationDto> applicationDtos;
        List<InterviewDto> interviewDtos = new ArrayList<>();
        List<InterviewDto> filtered;

        try {
            userDto = userDtoRepository.findUserDtoByUserId(userId);
            if(userDto == null)
                return new ArrayList<>();
            applicantDto = userDto.getApplicant();
            if(applicantDto == null)
                return new ArrayList<>();
            applicationDtos = applicantDto.getApplications();

        } catch (DataAccessException e) {
            return new ArrayList<>();
        }

        for(ApplicationDto a: applicationDtos) {
            interviewDtos.addAll(a.getInterviews());
        }

        filtered = interviewDtos.stream()
                .filter(x -> (x.getApplicantStatus() == applicantStatus)
                        && (x.getInterviewerStatus() == InterviewerStatus.ACCEPTED))
                .collect(Collectors.toList());

        return filtered;
    }

    private List<InterviewVo> findInterviewsByUserIdAndApplicantStatus(int userId, int applicantStatus) {
        List<InterviewDto> filtered = findInterviewDtosByUserIdAndApplicantStatus(userId, applicantStatus);
        List<InterviewVo> interviewVos = new ArrayList<>();

        for(InterviewDto i: filtered) {
            interviewVos.add(DataTransferUtil.InterviewDtoToVo(i));
        }

        return interviewVos;
    }

    public List<InterviewVo> findUnreplyedInterviewsByUserId(int userId) {
        return findInterviewsByUserIdAndApplicantStatus(userId, ApplicantStatus.INIT);
    }

    public List<InterviewVo> findAcceptedInterviewsByUserId(int userId) {
        return findInterviewsByUserIdAndApplicantStatus(userId, ApplicantStatus.ACCEPTED);
    }

    private List<ApplicantInterviewVo> findInterviewDetailsByUserIdAndApplicantStatus(int userId, int applicantStatus) {
        List<InterviewDto> filtered = findInterviewDtosByUserIdAndApplicantStatus(userId, applicantStatus);
        List<ApplicantInterviewVo> interviewVos = new ArrayList<>();

        for(InterviewDto i: filtered) {
            interviewVos.add(DataTransferUtil.InterviewDtoToApplicantInterviewVo(i));
        }

        return interviewVos;
    }

    public List<ApplicantInterviewVo> findUnreplyedInterviewDetailsByUserId(int userId) {
        return findInterviewDetailsByUserIdAndApplicantStatus(userId, ApplicantStatus.INIT);
    }

    public List<ApplicantInterviewVo> findAcceptedInterviewDetailsByUserId(int userId) {
        return findInterviewDetailsByUserIdAndApplicantStatus(userId, ApplicantStatus.ACCEPTED);
    }



    public int updateApplicantStatusOfInterview(int interviewId, int applicantStatus, String comment) {
        InterviewDto interviewDto;

        try {
            interviewDto = interviewDtoRepository
                    .findInterviewDtoByInterviewId(interviewId);
            interviewDto.setApplicantStatus(applicantStatus);
            if(ApplicantStatus.REJECTED.equals(applicantStatus)) {
                interviewDto.setReservationResultTime(Calendar.getInstance().getTime());
                interviewDto.setReservationStatus(ReservationStatus.FAIL);
                interviewDto.setApplicantComment(comment);
            } else if (ApplicantStatus.ACCEPTED.equals(applicantStatus)){
                interviewDto.setReservationResultTime(Calendar.getInstance().getTime());
                interviewDto.setReservationStatus(ReservationStatus.SUCCESS);
            }
            interviewDtoRepository.save(interviewDto);
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }

        return StatusCode.SUCCESS;
    }

    public List<ApplicantInterviewVo> findApplicantInterviewsByApplicationId(int applicationId) {
        ApplicationDto applicationDto;
        List<InterviewDto> interviewDtos;
        List<ApplicantInterviewVo> applicantInterviewVos = new ArrayList<>();

        try {
            applicationDto = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
            interviewDtos = applicationDto.getInterviews();
            interviewDtos = interviewDtos.stream().filter(x -> (x.getReservationStatus() == ReservationStatus.SUCCESS))
                    .sorted(Comparator.comparing(x -> x.getCreateTime())).collect(Collectors.toList());
            for(InterviewDto i: interviewDtos) {
                applicantInterviewVos.add(DataTransferUtil.InterviewDtoToApplicantInterviewVo(i));
            }
        } catch (DataAccessException e) {
            return new ArrayList<>();
        }

        return applicantInterviewVos;
    }

    public ApplicationVo findApplicationByApplicationId(int applicationId) {
        ApplicationDto applicationDto;

        try {
            applicationDto = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
            return DataTransferUtil.ApplicationDtoToVo(applicationDto);
        } catch (DataAccessException e) {
            return null;
        }
    }



    private int updateApplicationStatus(int applicationId, int status) {
        ApplicationDto applicationDto;

        try {
            applicationDto = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
            applicationDto.setStatus(status);
            applicationDto.setApplicantFeedbackTime(Calendar.getInstance().getTime());
            applicationDtoRepository.save(applicationDto);
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }

        return  StatusCode.SUCCESS;
    }

    public int acceptOfferByApplicationId(int applicationId) {
        return updateApplicationStatus(applicationId, ApplicationStatus.APPLICANT_ACCEPT);
    }

    public int rejectOfferByApplicationId(int applicationId) {
        return updateApplicationStatus(applicationId, ApplicationStatus.APPLICANT_REJECT);
    }

}

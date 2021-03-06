package com.targaryen.octopus.service;

import com.targaryen.octopus.dao.*;
import com.targaryen.octopus.dto.*;
import com.targaryen.octopus.util.DataTransferUtil;
import com.targaryen.octopus.util.StatusCode;
import com.targaryen.octopus.util.status.*;
import com.targaryen.octopus.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author He Junfeng
 */
@Service
public class HRServiceImpl implements HRService {
    private PostDtoRepository postDtoRepository;
    private ApplicationDtoRepository applicationDtoRepository;
    private InterviewerDtoRepository interviewerDtoRepository;
    private InterviewDtoRepository interviewDtoRepository;
    private ResumeModelDtoRepository resumeModelDtoRepository;
    private BatchRepository batchRepository;

    @Autowired
    public HRServiceImpl(DaoFactory daoFactory) {
        this.postDtoRepository = daoFactory.getPostDtoRepository();
        this.applicationDtoRepository = daoFactory.getApplicationDtoRepository();
        this.interviewerDtoRepository = daoFactory.getInterviewerDtoRepository();
        this.interviewDtoRepository = daoFactory.getInterviewDtoRepository();
        this.resumeModelDtoRepository = daoFactory.getResumeModelDtoRepository();
        this.batchRepository = daoFactory.getBatchRepository();
    }

    @Override
    public List<PostVo> listPosts() {
        return postDtoRepository.findAllByBatchAndStatusNotOrderByPostIdDesc(batchRepository.findByBatchId(1), PostStatus.REVOKED).stream()
                .map(n -> DataTransferUtil.PostDtoToVo(n))
                .collect(Collectors.toList());
    }

    @Override
    public PostVo findPostById(int postId) {
        PostDto postDto = postDtoRepository.findPostDtoByPostId(postId);
        if(postDto == null) {
            return new PostVo.Builder().build();
        } else {
            return DataTransferUtil.PostDtoToVo(postDto);
        }
    }

    @Transactional
    @Override
    public int publishPostById(int postId) {
        try {
            PostDto postDto = postDtoRepository.findPostDtoByPostId(postId);
            postDto.setStatus(PostStatus.PUBLISHED);
            postDto.setPublishTime(Calendar.getInstance().getTime());
            postDtoRepository.save(postDto);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }

    }

    @Transactional
    @Override
    public int closePostById(int postId) {
        try {
            PostDto postDto = postDtoRepository.findPostDtoByPostId(postId);
            postDto.setStatus(PostStatus.INIT);
            postDtoRepository.save(postDto);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Transactional
    @Override
    public int finishPostById(int postId) {
        try {
            PostDto postDto = postDtoRepository.findPostDtoByPostId(postId);
            postDto.setStatus(PostStatus.FINISHED);
            postDtoRepository.save(postDto);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Transactional
    @Override
    public int updatePost(PostVo updatePost) {
        try {
            PostDto post = postDtoRepository.findPostDtoByPostId(updatePost.getPostId());
            postDtoRepository.save(DataTransferUtil.updatePostDtoByVo(post, updatePost));
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Override
    public ResumeModelVo findResumeModelVoByPostId(int postId) {
        PostDto postDto = postDtoRepository.findPostDtoByPostId(postId);
        return DataTransferUtil.ResumeModeDtoToVo(postDto.getResumeModel());
    }

    @Transactional
    @Override
    public int updateResumeModelById(ResumeModelVo resumeModelVo) {
        try {
            ResumeModelDto resumeModelDto = resumeModelDtoRepository.findResumeModelDtoByResumeModelId(resumeModelVo.getResumeModelId());
            resumeModelDtoRepository.save(DataTransferUtil.updateResumeModelDtoByVo(resumeModelDto, resumeModelVo));
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Override
    public List<ApplicationResumeVo> findApplicationsByPostId(int postId) {
        PostDto post = postDtoRepository.findPostDtoByPostId(postId);
        if(post != null) {
            return post.getApplications().stream()
                    .map(n -> DataTransferUtil.ApplicationDtoToApplicationResumeVo(n))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<ApplicationResumeVo>();
        }
    }

    @Override
    public ApplicationResumeVo findApplicationResumeVoByApplicationId(int applicationId) {
        ApplicationDto applicationDto = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
        ApplicationResumeVo applicationResumeVo;
        if(applicationDto != null) {
            applicationResumeVo = DataTransferUtil.ApplicationDtoToApplicationResumeVo(applicationDto);
        } else {
            applicationResumeVo = null;
        }
        return applicationResumeVo;
    }

    @Override
    public List<ApplicationResumeVo> findFilterPassAppByPostId(int postId) {
        PostDto post = postDtoRepository.findPostDtoByPostId(postId);
        if(post != null) {
            return post.getApplications().stream()
                    .filter(n -> ApplicationStatus.FILTER_PASS.equals(n.getStatus()))
                    .map(n -> DataTransferUtil.ApplicationDtoToApplicationResumeVo(n))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<ApplicationResumeVo>();
        }
    }

    @Override
    public List<EducationExperienceVo> findEducationExperienceVoByApplicationId(int applicationId) {
        ApplicationDto application = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
        ResumeDto resume = application.getApplicant().getResume();
        List<EducationExperienceVo> educationExperienceVos;
        if(resume.getEducationExperiences() == null) {
            educationExperienceVos = new ArrayList<>();
        } else {
            educationExperienceVos = resume.getEducationExperiences().stream()
                    .map(n -> DataTransferUtil.EducationExperienceDtoToVo(n))
                    .collect(Collectors.toList());
        }
        return educationExperienceVos;
    }

    @Override
    public List<WorkExperienceVo> findWorkExperienceVoByApplicationId(int applicationId) {
        ApplicationDto application = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
        ResumeDto resume = application.getApplicant().getResume();
        List<WorkExperienceVo> workExperienceVos;
        if(resume.getEducationExperiences() == null) {
            workExperienceVos = new ArrayList<>();
        } else {
            workExperienceVos = resume.getWorkExperiences().stream()
                    .map(n -> DataTransferUtil.WorkExperienceDtoToVo(n))
                    .collect(Collectors.toList());
        }
        return workExperienceVos;
    }

    @Transactional
    @Override
    public int filterPassApplicationById(int applicationId) {
        try {
            ApplicationDto application = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
            application.setStatus(ApplicationStatus.FILTER_PASS);
            application.setFilterEndTime(Calendar.getInstance().getTime());
            applicationDtoRepository.save(application);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Transactional
    @Override
    public int filterFailApplicationById(int applicationId) {
        try {
            ApplicationDto application = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
            application.setStatus(ApplicationStatus.FILTER_FAIL);
            application.setFilterEndTime(Calendar.getInstance().getTime());
            applicationDtoRepository.save(application);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Transactional
    @Override
    public int revokeFilterApplicationById(int applicationId) {
        try {
            ApplicationDto application = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
            application.setStatus(ApplicationStatus.INIT);
            applicationDtoRepository.save(application);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Override
    public List<ApplicationResumeVo> findApplicationsByPostIdAndStatus(int postId, Integer status) {
        PostDto post = postDtoRepository.findPostDtoByPostId(postId);
        if(post != null) {
            return post.getApplications().stream()
                    .filter(n -> status.equals(n.getStatus()))
                    .map(n -> DataTransferUtil.ApplicationDtoToApplicationResumeVo(n))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<ApplicationResumeVo>();
        }
    }

    @Override
    public List<InterviewerVo> listInterviewersByPostId(int postId) {
        return postDtoRepository.findPostDtoByPostId(postId).getDepartment().getInterviewers().stream()
                .map(n -> DataTransferUtil.InterviewerDtoToVo(n))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public int createInterview(InterviewVo interviewVo) {
        try {
            interviewDtoRepository.save(createInterviewProcess(interviewVo));
            return StatusCode.SUCCESS;
        } catch (Exception e) {
            return StatusCode.FAILURE;
        }
    }

    @Transactional
    @Override
    public int createListOfInterviews(List<InterviewVo> interviewVos) {
        try {
            List<InterviewDto> interviewDtos = new ArrayList<>();
            for (InterviewVo interviewVo: interviewVos) {
                interviewDtos.add(createInterviewProcess(interviewVo));
            }
            interviewDtoRepository.saveAll(interviewDtos);
            return StatusCode.SUCCESS;
        } catch (Exception e) {
            return StatusCode.FAILURE;
        }
    }

    @Override
    public InterviewVo findInterviewById(int interviewId) {
        try {
            InterviewDto interviewDto = interviewDtoRepository.findInterviewDtoByInterviewId(interviewId);
            return DataTransferUtil.InterviewDtoToVo(interviewDto);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Transactional
    @Override
    public int updateInterviewerOfInterview(int interviewId, int interviewerId) {
        try {
            InterviewDto interviewDto = interviewDtoRepository.findInterviewDtoByInterviewId(interviewId);
            interviewDto.setInterviewer(interviewerDtoRepository.findInterviewerDtoByInterviewerId(interviewerId));
            interviewDto.setInterviewerStatus(InterviewerStatus.ACCEPTED);
            if(ApplicantStatus.ACCEPTED.equals(interviewDto.getApplicantStatus())) {
                interviewDto.setReservationStatus(ReservationStatus.SUCCESS);
                interviewDto.setReservationResultTime(Calendar.getInstance().getTime());
            }
            interviewDtoRepository.save(interviewDto);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Transactional
    @Override
    public int updateApplicationOfInterview(int interviewId, int applicationId) {
        try {
            InterviewDto interviewDto = interviewDtoRepository.findInterviewDtoByInterviewId(interviewId);
            interviewDto.setApplication(applicationDtoRepository.findApplicationDtoByApplicationId(applicationId));
            interviewDto.setApplicantStatus(ApplicantStatus.ACCEPTED);
            if(InterviewerStatus.ACCEPTED.equals(interviewDto.getInterviewerStatus())) {
                interviewDto.setReservationStatus(ReservationStatus.SUCCESS);
                interviewDto.setReservationResultTime(Calendar.getInstance().getTime());
            }
            interviewDtoRepository.save(interviewDto);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Override
    @Transactional
    public int deleteInterviewById(int interviewId) {
        try {
            interviewDtoRepository.deleteInterviewDtoByInterviewId(interviewId);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Override
    public List<InterviewVo> findInterviewByApplicationId(int applicationId) {
        ApplicationDto application = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
        if(application != null) {
            return application.getInterviews().stream()
                    .sorted(Comparator.comparing(n -> n.getInterviewStartTime()))
                    .map(n -> DataTransferUtil.InterviewDtoToVo(n))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<InterviewVo> findInterviewByPostIdAndRound(int postId, int interviewRound) {
        return interviewDtoRepository.findAllByPostAndInterviewRound(postDtoRepository.findPostDtoByPostId(postId),
                interviewRound).stream()
                .map(n -> DataTransferUtil.InterviewDtoToVo(n))
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewVo> findLatestAppInterviewByPostId(int postId) {
        PostDto post = postDtoRepository.findPostDtoByPostId(postId);
        List<ApplicationDto> applicationDtos = post.getApplications();
        List<InterviewVo> interviewVos = new ArrayList<>();
        if(applicationDtos != null) {
            for(ApplicationDto application : applicationDtos) {
                List<InterviewDto> interviews = application.getInterviews();
                if(interviews != null) {
                    InterviewDto latest = null;
                    int maxRound = 0;
                    for(InterviewDto interview : interviews) {
                        int thisRound = interview.getInterviewRound();
                        if(thisRound > maxRound &&
                                ReservationStatus.SUCCESS.equals(interview.getReservationStatus())) {
                            maxRound = thisRound;
                            latest = interview;
                        }
                    }
                    if(latest != null) {
                        interviewVos.add(DataTransferUtil.InterviewDtoToVo(latest));
                    }
                }
            }
        }
        return interviewVos;
    }

    @Override
    public List<InterviewVo> findInterviewByPostIdAndRoundAndTime(int postId, int interviewRound, Date beginTime, Date endTime) {
        return interviewDtoRepository.findAllByPostAndRoundAndTime(postDtoRepository.findPostDtoByPostId(postId),
                interviewRound, beginTime, endTime).stream()
                .map(n -> DataTransferUtil.InterviewDtoToVo(n))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public int addNewInterviewRoundByPostId(int postId) {
        try {
            PostDto post = postDtoRepository.findPostDtoByPostId(postId);
            int interviewRound = post.getInterviewRound();
            post.setInterviewRound(++interviewRound);
            postDtoRepository.save(post);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Transactional
    @Override
    public int interviewPassApplicationById(int applicationId) {
        try {
            ApplicationDto application = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
            application.setStatus(ApplicationStatus.INTERVIEW_PASS);
            application.setInterviewEndTime(Calendar.getInstance().getTime());
            applicationDtoRepository.save(application);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Transactional
    @Override
    public int interviewFailApplicationById(int applicationId) {
        try {
            ApplicationDto application = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
            application.setStatus(ApplicationStatus.INTERVIEW_FAIL);
            application.setInterviewEndTime(Calendar.getInstance().getTime());
            applicationDtoRepository.save(application);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Transactional
    @Override
    public int sendOfferByApplicationId(int applicationId) {
        try {
            ApplicationDto application = applicationDtoRepository.findApplicationDtoByApplicationId(applicationId);
            application.setStatus(ApplicationStatus.OFFER);
            application.setOfferTime(Calendar.getInstance().getTime());
            applicationDtoRepository.save(application);
            return StatusCode.SUCCESS;
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }
    }

    @Override
    public List<PostRecruitProgressVo> listPostRecruitProgress() {
        List<PostDto> posts = postDtoRepository.findAllByBatchAndStatusNotOrderByPostIdDesc(
                batchRepository.findByBatchId(1), PostStatus.REVOKED);
        List<PostRecruitProgressVo> postRecruitProgressVos = new ArrayList<>();
        for(PostDto post : posts) {
            List<ApplicationDto> applications = post.getApplications();
            if(applications != null) {
                int applicationNum = applications.size();
                int filterPassNum = 0;
                int interviewPassNum = 0;
                int dptPassNum = 0;
                int offerNum = 0;
                for(ApplicationDto application : applications) {
                    int applicationStatus = application.getStatus();
                    if(ApplicationStatus.FILTER_PASS.equals(applicationStatus)) {
                        ++filterPassNum;
                    } else if(ApplicationStatus.INTERVIEW_PASS.equals(applicationStatus)) {
                        ++filterPassNum;
                        ++interviewPassNum;
                    } else if(ApplicationStatus.DPT_PASS.equals(applicationStatus)) {
                        ++filterPassNum;
                        ++interviewPassNum;
                        ++dptPassNum;
                    } else if(ApplicationStatus.OFFER.equals(applicationStatus)) {
                        ++filterPassNum;
                        ++interviewPassNum;
                        ++dptPassNum;
                        ++offerNum;
                    }
                }
                postRecruitProgressVos.add(new PostRecruitProgressVo.Builder()
                        .postId(post.getPostId())
                        .postName(post.getPostName())
                        .publishTime(post.getPublishTime())
                        .applicationNum(applicationNum)
                        .dptPassNum(dptPassNum)
                        .filterPassNum(filterPassNum)
                        .interviewPassNum(interviewPassNum)
                        .offerNum(offerNum)
                        .recruitNum(post.getRecruitNum())
                        .postStatus(post.getStatus())
                        .build());
            }
        }
        return postRecruitProgressVos;
    }

    @Override
    public List<SourceRecruitProgressVo> listSourceRecruitProgress() {
        Map<String, SourceRecruitProgressDto> srpDtoMap = new HashMap<>();
        List<PostDto> posts = postDtoRepository.findAllByBatchAndStatusNotOrderByPostIdDesc(
                batchRepository.findByBatchId(1), PostStatus.REVOKED);
        for(PostDto post : posts) {
            List<ApplicationDto> applications = post.getApplications();
            if(applications != null) {
                for(ApplicationDto application : applications) {
                    int applicationStatus = application.getStatus();
                    ResumeDto resume = application.getApplicant().getResume();
                    String school = resume.getApplicantSchool();
                    String city = resume.getApplicantCity();
                    if(srpDtoMap.containsKey(school)) {
                        SourceRecruitProgressDto srpDto = srpDtoMap.get(school);

                        updateSRP(srpDto, applicationStatus);
                    } else {
                        SourceRecruitProgressDto srpDto = new SourceRecruitProgressDto();
                        srpDto.setSourceName(school);
                        srpDto.setSourceType(SourceType.SCHOOL);
                        updateSRP(srpDto, applicationStatus);
                        srpDtoMap.put(school, srpDto);
                    }
                    if(srpDtoMap.containsKey(city)) {
                        SourceRecruitProgressDto srpDto = srpDtoMap.get(city);
                        updateSRP(srpDto, applicationStatus);
                    } else {
                        SourceRecruitProgressDto srpDto = new SourceRecruitProgressDto();
                        srpDto.setSourceName(city);
                        srpDto.setSourceType(SourceType.CITY);
                        updateSRP(srpDto, applicationStatus);
                        srpDtoMap.put(city, srpDto);
                    }
                }
            }
        }
        List<SourceRecruitProgressVo> srpVos = new ArrayList<>();
        for(String key : srpDtoMap.keySet()) {
            SourceRecruitProgressDto srpDto = srpDtoMap.get(key);
            srpVos.add(new SourceRecruitProgressVo.Builder()
                    .sourceName(srpDto.getSourceName())
                    .sourceType(srpDto.getSourceType())
                    .applicationNum(srpDto.getApplicationNum())
                    .dptPassNum(srpDto.getDptPassNum())
                    .filterPassNum(srpDto.getFilterPassNum())
                    .interviewPassNum(srpDto.getInterviewPassNum())
                    .offerNum(srpDto.getOfferNum())
                    .build());
        }
        return srpVos;
    }

    private SourceRecruitProgressDto updateSRP(SourceRecruitProgressDto srpDto, int applicationStatus) {
        if(ApplicationStatus.FILTER_PASS.equals(applicationStatus)) {
            int applicationNum = srpDto.getApplicationNum();
            srpDto.setApplicationNum(++applicationNum);
        } else if(ApplicationStatus.INTERVIEW_PASS.equals(applicationStatus)) {
            int applicationNum = srpDto.getApplicationNum();
            int filterPassNum = srpDto.getFilterPassNum();
            int interviewPassNum = srpDto.getInterviewPassNum();
            srpDto.setApplicationNum(++applicationNum);
            srpDto.setFilterPassNum(++filterPassNum);
            srpDto.setInterviewPassNum(++interviewPassNum);
        } else if(ApplicationStatus.DPT_PASS.equals(applicationStatus)) {
            int applicationNum = srpDto.getApplicationNum();
            int filterPassNum = srpDto.getFilterPassNum();
            int interviewPassNum = srpDto.getInterviewPassNum();
            int dptPassNum = srpDto.getDptPassNum();
            srpDto.setApplicationNum(++applicationNum);
            srpDto.setFilterPassNum(++filterPassNum);
            srpDto.setInterviewPassNum(++interviewPassNum);
            srpDto.setDptPassNum(dptPassNum);
        } else if(ApplicationStatus.OFFER.equals(applicationStatus)) {
            int applicationNum = srpDto.getApplicationNum();
            int filterPassNum = srpDto.getFilterPassNum();
            int interviewPassNum = srpDto.getInterviewPassNum();
            int dptPassNum = srpDto.getDptPassNum();
            int offerNum = srpDto.getOfferNum();
            srpDto.setApplicationNum(++applicationNum);
            srpDto.setFilterPassNum(++filterPassNum);
            srpDto.setInterviewPassNum(++interviewPassNum);
            srpDto.setDptPassNum(dptPassNum);
            srpDto.setOfferNum(offerNum);
        }
        return srpDto;
    }

    private InterviewDto createInterviewProcess(InterviewVo interviewVo) {
        InterviewDto newInterview = new InterviewDto();

        ApplicationDto application;
        if(interviewVo.getApplicationId() <= 0) {
            application = null;
        } else {
            application = applicationDtoRepository.findApplicationDtoByApplicationId(interviewVo.getApplicationId());
        }
        newInterview.setApplication(application);

        InterviewerDto interviewer;
        if(interviewVo.getInterviewerId() <= 0) {
            interviewer = null;
        } else {
            interviewer = interviewerDtoRepository.findInterviewerDtoByInterviewerId(interviewVo.getInterviewerId());
        }
        newInterview.setInterviewer(interviewer);

        PostDto post = postDtoRepository.findPostDtoByPostId(interviewVo.getPostId());
        newInterview.setPost(post);
        if(interviewer != null && RecruitTypeStatus.CAMPUS.equals(post.getRecruitType())) {
            newInterview.setInterviewerStatus(InterviewerStatus.ACCEPTED);
        } else {
            newInterview.setInterviewerStatus(InterviewerStatus.INIT);
        }

        newInterview.setInterviewStartTime(interviewVo.getInterviewStartTime());
        newInterview.setInterviewPlace(interviewVo.getInterviewPlace());
        newInterview.setApplicantStatus(ApplicantStatus.INIT);
        newInterview.setReservationStatus(ReservationStatus.INIT);
        newInterview.setInterviewResultStatus(InterviewResultStatus.INIT);
        newInterview.setCreateTime(Calendar.getInstance().getTime());

        if(RecruitTypeStatus.CAMPUS.equals(post.getRecruitType())) {
            newInterview.setInterviewRound(interviewVo.getInterviewRound());
        } else {
            if(application != null) {
                int interviewRound = 0;
                List<InterviewDto> interviewDtos = application.getInterviews();
                if(interviewDtos != null) {
                    for (InterviewDto interview: interviewDtos) {
                        if(ReservationStatus.SUCCESS.equals(interview.getReservationStatus())) {
                            int r = interview.getInterviewRound();
                            interviewRound = r > interviewRound? r : interviewRound;
                        }
                    }
                    newInterview.setInterviewRound(++interviewRound);
                }
            }
        }
        return newInterview;
    }

}

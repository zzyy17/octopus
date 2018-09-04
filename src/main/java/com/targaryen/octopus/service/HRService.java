package com.targaryen.octopus.service;

import com.targaryen.octopus.dto.PostDto;
import com.targaryen.octopus.vo.ApplicationVo;
import com.targaryen.octopus.vo.PostVo;
import com.targaryen.octopus.vo.ResumeVo;

import java.util.List;

/**
 * @author He Junfeng
 */
public interface HRService {
    /**
     * List posts
     *
     */
    List<PostVo> listPosts();

    /**
     * find post by postId
     *
     */
    PostVo findPostById(int postId);

    /**
     * Publish post by postId
     *
     */
    void publishPostById(int postId);

    /**
     * Close post by postId
     *
     */
    void closePostById(int postId);

    /**
     * update post
     *
     */
    void updatePost(PostDto updatePost);

    /**
     * find applications by postId
     *
     */
    List<ApplicationVo> findApplicationsByPostId(int postId);

    /**
     * find resume by applicantId
     *
     */
    ResumeVo findResumeByApplicantId(int applicantId);
}

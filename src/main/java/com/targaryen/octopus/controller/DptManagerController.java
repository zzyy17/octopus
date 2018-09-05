package com.targaryen.octopus.controller;

import com.targaryen.octopus.dto.PostDto;
import com.targaryen.octopus.entity.PostEntity;
import com.targaryen.octopus.security.AuthInfo;
import com.targaryen.octopus.service.DptManagerService;
import com.targaryen.octopus.service.ServiceFactory;
import com.targaryen.octopus.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/octopus", produces= MediaType.TEXT_HTML_VALUE)
public class DptManagerController {
    private DptManagerService dptManagerService;

    @Autowired
    public DptManagerController(ServiceFactory serviceFactory) {
        this.dptManagerService = serviceFactory.getDptManagerService();
    }

    @RequestMapping(value = "/dpt/post/add", method = RequestMethod.GET)
    public String postAddGet(ModelMap map) {
        /* UI Settings */
        map.addAttribute("title", "Add new post need");
        map.addAttribute("action", "add");
        map.addAttribute("returnUrl", "list");
        map.addAttribute("swalTextSuccess", "You have successfully added a new post need!");
        map.addAttribute("swalTextFailure", "You have not successfully added a new post need.");

        map.addAttribute("post", new PostDto());
        return "dpt-post-detail";
    }

    @RequestMapping(value = "/dpt/post/add", method = RequestMethod.POST)
    @ResponseBody
    public String postAddPost(PostEntity postEntity) {
        PostVo postVo = new PostVo.Builder()
                .postId(postEntity.getPostId())
                .postName(postEntity.getPostName())
                .postType(postEntity.getPostType())
                .postLocale(postEntity.getPostLocale())
                .postDescription(postEntity.getPostDescription())
                .postRequirement(postEntity.getPostRequirement())
                .recruitNum(postEntity.getRecruitNum())
                .recruitDpt(postEntity.getRecruitDpt())
                .publishTime(postEntity.getPublishTime())
                .status(postEntity.getStatus())
                .build();

        dptManagerService.createNewPost(postVo, AuthInfo.getUserId());
        return "OK";
    }

    @RequestMapping("/dpt/post/list")
    public String postList(ModelMap map) {
        map.addAttribute("postList", dptManagerService.findPostsByUserId(AuthInfo.getUserId()));
        return "dpt-post-list";
    }

    @RequestMapping(value = "/dpt/post/detail/{postId}", method = RequestMethod.GET)
    public String postDetail(@PathVariable("postId") int postId, ModelMap map) {
        /* UI Settings */
        map.addAttribute("title", "Check/Edit post need detail");
        map.addAttribute("action", "../edit");
        map.addAttribute("returnUrl", "../list");
        map.addAttribute("swalTextSuccess", "You have successfully edited this post need!");
        map.addAttribute("swalTextFailure", "You have not successfully edited this post need.");

        map.addAttribute("post", dptManagerService.findPostById(postId));
        return "dpt-post-detail";
    }

    @RequestMapping(value = "/dpt/post/edit", method = RequestMethod.POST)
    @ResponseBody
    public String postEditPost(PostEntity postEntity) {
        PostVo postVo = new PostVo.Builder()
                .postId(postEntity.getPostId())
                .postName(postEntity.getPostName())
                .postType(postEntity.getPostType())
                .postLocale(postEntity.getPostLocale())
                .postDescription(postEntity.getPostDescription())
                .postRequirement(postEntity.getPostRequirement())
                .recruitNum(postEntity.getRecruitNum())
                .recruitDpt(postEntity.getRecruitDpt())
                .publishTime(postEntity.getPublishTime())
                .status(postEntity.getStatus())
                .build();
        
        int result = dptManagerService.updatePost(postVo);
        return "OK";
    }
}

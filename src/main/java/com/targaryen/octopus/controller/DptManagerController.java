package com.targaryen.octopus.controller;

import com.targaryen.octopus.dto.DptManagerDto;
import com.targaryen.octopus.dto.PostDto;
import com.targaryen.octopus.service.DptManagerService;
import com.targaryen.octopus.service.ServiceFactory;
import com.targaryen.octopus.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/octopus", produces= MediaType.TEXT_HTML_VALUE)
public class DptManagerController {
    private DptManagerService dptManagerService;

    @Autowired
    public DptManagerController(ServiceFactory serviceFactory) {
        this.dptManagerService = serviceFactory.getDptManagerService();
    }

    @RequestMapping("/dpt/post/add")
    public String postAdd(ModelMap map) {
        map.addAttribute("title", "Add new post need");
        return "dptPostDetail";
    }

    @RequestMapping("/dpt/post/list")
    public String postList(ModelMap map) {
        map.addAttribute("postList", dptManagerService.findPostsByDptManagerId(1));
        return "dptPostList";
    }

    @RequestMapping("/dpt/post/detail")
    public String postDetail(ModelMap map) {
        map.addAttribute("title", "Post need detail");
        // map.addAttribute("postDetail", dptManagerService.)
        return "dptPostDetail";
    }
}

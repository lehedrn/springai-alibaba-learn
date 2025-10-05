package com.coderlee.ai.text2image.controller;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/text2image")
public class Text2ImageController {

    private static final String IMAGE_MODEL = "wanx2.1-t2i-turbo";

    @Resource
    private ImageModel imageModel;

    @GetMapping("image")
    public String image(@RequestParam(name = "prompt", defaultValue = "冰天雪地") String prompt) {
        return this.imageModel.call(
            new ImagePrompt(prompt, DashScopeImageOptions.builder().withModel(IMAGE_MODEL).build())
        ).getResult()
        .getOutput()
        .getUrl();
    }
    


}

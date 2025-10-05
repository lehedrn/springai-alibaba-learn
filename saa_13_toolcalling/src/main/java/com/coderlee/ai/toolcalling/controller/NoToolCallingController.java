package com.coderlee.ai.toolcalling.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("no-tool-calling")
public class NoToolCallingController {

    @Resource
    private ChatModel chatModel;

    @GetMapping("chat")
    public Flux<String> chat(@RequestParam(name = "msg", defaultValue = "你是谁，现在几点了") String msg) {
        return this.chatModel.stream(msg);
    }

}

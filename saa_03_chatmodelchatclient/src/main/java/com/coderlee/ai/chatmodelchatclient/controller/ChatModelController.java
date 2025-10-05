package com.coderlee.ai.chatmodelchatclient.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("chatmodel")
public class ChatModelController {

    @Resource
    private ChatModel dashScopeChatModel;

    @GetMapping("simple/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "你是谁") String query) {
        return this.dashScopeChatModel.call(query);
    }

    @GetMapping("stream/chat")
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你是谁") String query) {
        return this.dashScopeChatModel.stream(query);
    }
    
    
}

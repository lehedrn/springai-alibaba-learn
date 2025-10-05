package com.coderlee.ai.callbaidumap.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("call")
public class CallMcpServerController {

    @Resource
    private ChatClient chatClient;
    @Resource
    private ChatModel chatModel;

    @GetMapping("client/chat")
    public Flux<String> chat(String msg) {
        return this.chatClient.prompt(msg).stream().content();
    }

    @GetMapping("model/chat")
    public Flux<String> modelChat(String msg) {
        return this.chatModel.stream(msg);
    }
}

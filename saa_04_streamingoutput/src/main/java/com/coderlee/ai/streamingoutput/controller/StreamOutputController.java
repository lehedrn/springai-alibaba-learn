package com.coderlee.ai.streamingoutput.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("stream")
public class StreamOutputController {

    private static final String DEFAULT_QUERY = "你是谁";

    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;

    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @GetMapping("chatmodel/deepseek")
    public Flux<String> deepseekChat(@RequestParam(name = "question", defaultValue = DEFAULT_QUERY) String question) {
        return this.deepseekChatModel.stream(question);
    }
    
    @GetMapping("chatmodel/qwen")
    public Flux<String> qwenChat(@RequestParam(name = "question", defaultValue = DEFAULT_QUERY) String question) {
        return this.qwenChatModel.stream(question);
    }

    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;

    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping("chatclient/deepseek")
    public Flux<String> deepseekClientChat(@RequestParam(name = "question", defaultValue = DEFAULT_QUERY) String question) {
        return this.deepseekChatClient.prompt(question).stream().content();
    }
    
    @GetMapping("chatclient/qwen")
    public Flux<String> qwenClientChat(@RequestParam(name = "question", defaultValue = DEFAULT_QUERY) String question) {
        return this.qwenChatClient.prompt(question).stream().content();
    }    
}

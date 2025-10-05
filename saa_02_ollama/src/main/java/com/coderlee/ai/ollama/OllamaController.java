package com.coderlee.ai.ollama;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("ollama")
public class OllamaController {

    private static final String DEFAULT_QUERY = "请简要介绍一下你自己";

    // 方式一
    /* @Resource(name = "ollamaChatModel")
    private ChatModel chatModel; */

    // 方式二
    @Resource
    @Qualifier("ollamaChatModel")
    private ChatModel chatModel;

    @GetMapping("simple/chat")
    public String simpleChat(@RequestParam(value="query", defaultValue = DEFAULT_QUERY) String query) {
        return chatModel.call(query);
    }

    @GetMapping("stream/chat")
    public Flux<String> streamChat(@RequestParam(value="query", defaultValue = DEFAULT_QUERY) String query) {
        return chatModel.stream(query);
    }
    
}

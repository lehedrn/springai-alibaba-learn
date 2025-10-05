package com.coderlee.ai.helloworld;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("helloworld_04")
public class HelloworldController_04 {

    @Resource
    private ChatModel chatModel;

    @GetMapping("simple/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗?") String query) {
        return chatModel.call(query);
    }

    @GetMapping("stream/chat")
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗?") String query,
            HttpServletResponse response) {
        return chatModel.stream(query);
    }

}

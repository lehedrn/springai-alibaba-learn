package com.coderlee.ai.todaymenu.controller;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("today-menu")
public class TodayMenuController {

    @Resource
    private ChatModel chatModel;

    @GetMapping("chat")
    public Flux<String> chat(@RequestParam(name = "msg", defaultValue = "今天吃什么") String msg) {
        String info = """
                你是一个AI厨师助手,每次随机生成三个家常菜，并且提供这些家常菜的详细做法步骤，以HTML格式返回
                字数控制在1500字以内。
                """;
        SystemMessage systemMessage = new SystemMessage(info);
        UserMessage userMessage = new UserMessage(msg);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return this.chatModel.stream(prompt).map(resp -> resp.getResults().get(0).getOutput().getText());
    }

}

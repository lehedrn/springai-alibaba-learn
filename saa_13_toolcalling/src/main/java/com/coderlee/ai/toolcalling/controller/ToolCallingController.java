package com.coderlee.ai.toolcalling.controller;

import java.sql.Date;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coderlee.ai.toolcalling.utils.DateTimeTools;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("tool-calling")
public class ToolCallingController {

    @Resource
    private ChatModel chatModel;

    @Resource
    private ChatClient chatClient;

    @GetMapping("model/chat")
    public Flux<String> modelChat(@RequestParam(name = "msg",defaultValue = "你是谁现在几点") String msg) {
        ToolCallback[] tools = ToolCallbacks.from(new DateTimeTools());
        ChatOptions chatOptions = ToolCallingChatOptions.builder().toolCallbacks(tools).build();
        Prompt prompt = new Prompt(msg, chatOptions);
        return this.chatModel.stream(prompt).map(chatResponse -> chatResponse.getResult().getOutput().getText());
    }
    
    @GetMapping("client/chat")
    public Flux<String> clientChat(@RequestParam(name = "msg",defaultValue = "你是谁现在几点") String msg) {
        return this.chatClient.prompt(msg).tools(new DateTimeTools()).stream().content();
    }

}

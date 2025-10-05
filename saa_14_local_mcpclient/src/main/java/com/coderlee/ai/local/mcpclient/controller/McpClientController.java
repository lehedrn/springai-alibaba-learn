package com.coderlee.ai.local.mcpclient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("mcp-client")
@Slf4j
public class McpClientController {

    @Resource
    private ChatClient chatClient;

    @Resource
    private ChatModel chatModel;

    @GetMapping("client/chat")
    public Flux<String> clientChat(@RequestParam(name = "msg",defaultValue = "北京") String msg) {
        log.info("used mcp ,invoke mcp server, city: {}", msg);
        return this.chatClient.prompt(msg).stream().content();
    }
    

    @GetMapping("model/chat")
    public Flux<String> modelChat(@RequestParam(name = "msg",defaultValue = "北京") String msg) {
        log.info("no used mcp ,city: {}", msg);
        return this.chatModel.stream(msg);
    }
}

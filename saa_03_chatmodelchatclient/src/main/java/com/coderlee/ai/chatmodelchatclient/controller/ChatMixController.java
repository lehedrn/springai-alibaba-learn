package com.coderlee.ai.chatmodelchatclient.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("chatmix")
public class ChatMixController {

    @Resource
    private ChatModel dashScopeChatModel;

    @Resource
    private ChatClient dashScopeChatClient;

    @GetMapping("model/simple/chat")
    public String simpleChatModel(@RequestParam(value = "query", defaultValue = "你是谁") String query) {
        return this.dashScopeChatModel.call(query);
    }

    @GetMapping("model/stream/chat")
    public Flux<String> streamChatModel(@RequestParam(value = "query", defaultValue = "你是谁") String query) {
        return this.dashScopeChatModel.stream(query);
    }

    @GetMapping("client/simple/chat")
    public String simpleChatClient(@RequestParam(value = "query", defaultValue = "你是谁") String query) {
        return this.dashScopeChatClient.prompt().user(query).call().content();
    }

    @GetMapping("client/stream/chat")
    public Flux<String> streamChatClient(@RequestParam(value = "query", defaultValue = "你是谁") String query) {
        return this.dashScopeChatClient.prompt().user(query).stream().content();
    }
}

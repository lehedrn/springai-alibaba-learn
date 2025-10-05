package com.coderlee.ai.chatmodelchatclient.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("chatclient")
public class ChatClientController {

    private final ChatClient dashScopeChatClient;

    public ChatClientController(ChatModel dashScopeModel) {
        this.dashScopeChatClient = ChatClient.builder(dashScopeModel).build();
    }

    @GetMapping("simple/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "你是谁") String query) {
        return this.dashScopeChatClient.prompt().user(query).call().content();
    }

    @GetMapping("stream/chat")
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你是谁") String query) {
        return this.dashScopeChatClient.prompt().user(query).stream().content();
    }
    
    
}

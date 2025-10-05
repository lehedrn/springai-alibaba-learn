package com.coderlee.ai.helloworld;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;

import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("helloworld_01")
public class HelloworldController_01 {

    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    // 使用自动配置的ChatClient.Builder构建ChatClient
    private final ChatClient chatClient;

    public HelloworldController_01(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.defaultSystem(DEFAULT_PROMPT)
                .defaultOptions(DashScopeChatOptions.builder().withTopP(0.7).build()).build();
    }

    @GetMapping("simple/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗?") String query) {
        return this.chatClient.prompt().user(query).call().content();
    }

    @GetMapping("stream/chat")
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗?") String query,
            HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return this.chatClient.prompt().user(query).stream().content();
    }
}

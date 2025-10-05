package com.coderlee.ai.helloworld;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;

import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("helloworld_02")
public class HelloworldController_02 {

    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    // 也可以使用如下的方式注入 ChatClient
    private final ChatClient dashScopeChatClient;

    public HelloworldController_02(ChatClient.Builder chatClientBuilder) {
        this.dashScopeChatClient = chatClientBuilder.defaultSystem(DEFAULT_PROMPT)
                .defaultAdvisors(
                    // 实现Logger的Advisor
                    new SimpleLoggerAdvisor()
                )
                .defaultOptions(DashScopeChatOptions.builder().withTopP(0.7).build()).build();
    }

    @GetMapping("simple/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗?") String query) {
        return dashScopeChatClient.prompt(query).call().content();
    }

    @GetMapping("stream/chat")
    public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗?") String query,
            HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        return dashScopeChatClient.prompt(query).stream().content();
    }

    @GetMapping("advisor/chat/{id}")
    public Flux<String> advisorChat(HttpServletResponse response, @PathVariable String id, @RequestParam String query) {
        return this.dashScopeChatClient.prompt(query)
                        .stream().content();
    }
    

}

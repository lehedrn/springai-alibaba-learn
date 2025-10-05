package com.coderlee.ai.helloworld;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;

import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("helloworld_03")
public class HelloworldController_03 {

    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    // 以编程方式创建 ChatClient
    private final ChatClient chatClient;

    public HelloworldController_03(@Value("${spring.ai.dashscope.api-key}") String apikey) {
        ChatModel myChatModel = DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder().apiKey(apikey).build())
                .defaultOptions(DashScopeChatOptions.builder().withModel(DashScopeApi.DEFAULT_CHAT_MODEL).withTopP(0.7).build())
                .build();
        ChatClient.Builder chatClientBuilder = ChatClient.builder(myChatModel);
        this.chatClient = chatClientBuilder.defaultSystem(DEFAULT_PROMPT).build();
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

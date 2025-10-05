package com.coderlee.ai.prompttemplate.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;

@Configuration
public class SaaConfig {
    // 模型名称常量定义，一套系统多模型共存
    private final String DEEPSEEK_MODEL = "deepseek-v3";
    private final String QWEN_MODEL = "qwen-max";

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Bean(name = "deepseek")
    public ChatModel deepseek() {
        return DashScopeChatModel.builder()
                    .dashScopeApi(DashScopeApi.builder().apiKey(this.apiKey).build())
                    .defaultOptions(DashScopeChatOptions.builder().withModel(DEEPSEEK_MODEL).build())
                    .build();
    }

    @Bean(name = "qwen")
    public ChatModel qwen() {
        return DashScopeChatModel.builder()
                    .dashScopeApi(DashScopeApi.builder().apiKey(this.apiKey).build())
                    .defaultOptions(DashScopeChatOptions.builder().withModel(QWEN_MODEL).build())
                    .build();
    }

    @Bean(name = "deepseekChatClient")
    public ChatClient deepseekChatClient(@Qualifier("deepseek") ChatModel deepseek) {
        return ChatClient.builder(deepseek)
                    .defaultOptions(ChatOptions.builder().model(DEEPSEEK_MODEL).build())
                    .build();
    }

    @Bean(name = "qwenChatClient")
    public ChatClient qwenChatClient(@Qualifier("qwen") ChatModel qwen) {
        return ChatClient.builder(qwen)
                    .defaultOptions(ChatOptions.builder().model(QWEN_MODEL).build())
                    .build();
    }
}

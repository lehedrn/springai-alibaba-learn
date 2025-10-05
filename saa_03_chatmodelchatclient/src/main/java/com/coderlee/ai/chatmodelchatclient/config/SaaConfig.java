package com.coderlee.ai.chatmodelchatclient.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;

@Configuration
public class SaaConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String apikey;

    @Bean
    public DashScopeApi dashScopeApi() {
        return DashScopeApi.builder().apiKey(apikey).build();
    }

    @Bean
    public ChatClient chatClient(ChatModel dashScopeModel) {
        return ChatClient.builder(dashScopeModel).build();
    }
}

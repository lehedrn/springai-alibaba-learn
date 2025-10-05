package com.coderlee.ai.todaymenu.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;

@Configuration
public class SaaConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;
    @Value("${spring.ai.dashscope.workspace-id}")
    private String workspaceId;
    
    @Bean
    public DashScopeApi dashScopeApi() {
        return DashScopeApi.builder().apiKey(this.apiKey).workSpaceId(this.workspaceId).build();
    }

    @Bean
    public ChatClient chatClient(ChatModel dashscopeChatModel) {
        return ChatClient.builder(dashscopeChatModel).build();
    }
}

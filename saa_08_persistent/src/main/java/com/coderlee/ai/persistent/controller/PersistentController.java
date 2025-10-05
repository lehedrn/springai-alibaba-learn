package com.coderlee.ai.persistent.controller;

import java.util.function.Consumer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.AdvisorSpec;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("persistent")
public class PersistentController {

    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping("qwen/client/chat")
    public String qwenClientChat(String msg, String userId) {
        return this.qwenChatClient
                .prompt(msg)
                /* .advisors(new Consumer<ChatClient.AdvisorSpec>() {
                    @Override
                    public void accept(AdvisorSpec advisorSpec) {
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, userId);
                    }
                }) */
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userId))
                .call()
                .content();
                
    }

}

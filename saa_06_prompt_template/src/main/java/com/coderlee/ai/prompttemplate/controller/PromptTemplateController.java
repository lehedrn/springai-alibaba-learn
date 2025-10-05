package com.coderlee.ai.prompttemplate.controller;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/prompt-template")
public class PromptTemplateController {

    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;

    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;
    
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @Value("classpath:/prompt_template/template_01.txt")
    private org.springframework.core.io.Resource userTemplate;

    @GetMapping("deepseek/client/chat")
    public Flux<String> chat(String topic, String output_format, String wordCount) {
        PromptTemplate promptTemplate = new PromptTemplate(
            "讲一个关于{topic}的故事，"
             + "并以{output_format}格式输出, "
             + "字数在{wordCount}左右。"
        );
        Prompt prompt = promptTemplate.create(Map.of(
            "topic", topic,
            "output_format", output_format,
            "wordCount", wordCount
        ));
        return this.deepseekChatClient.prompt(prompt).stream().content();
    }

    @GetMapping("deepseek/client/chat/template")
    public Flux<String> chatTemplate(String topic, String output_format, String wordCount) {
        PromptTemplate promptTemplate = new PromptTemplate(this.userTemplate);
        Prompt prompt = promptTemplate.create(Map.of(
            "topic", topic,
            "output_format", output_format,
            "wordCount", wordCount
        ));
        return this.deepseekChatClient.prompt(prompt).stream().content();
    }

    @GetMapping("deepseek/client/chat/template2")
    public String chatTemplate02(String sysTopic, String userTopic) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("你是{systemTopic}助手，只回答{systemTopic}其它无可奉告，以HTML格式的结果。");
        Message sysMessage = systemPromptTemplate.createMessage(Map.of("systemTopic", sysTopic));
        PromptTemplate userPromptTemplate = new PromptTemplate("解释一下{userTopic}");
        Message userMessage = userPromptTemplate.createMessage(Map.of("userTopic", userTopic));
        Prompt prompt = new Prompt(List.of(sysMessage, userMessage));
        return this.deepseekChatClient.prompt(prompt).call().content();
    }

    @GetMapping("deepseek/client/chat/template3")
    public Flux<String> chat(String question) {
        return this.deepseekChatClient.prompt()
                        .system("你是一个Java编程助手，拒绝回答非技术问题。")
                        .user(question)
                        .stream()
                        .content();
    }

    @GetMapping("deepseek/model/chat/template")
    public String modelChatTemplate(String question) { 
        SystemMessage systemMessage = new SystemMessage("你是一个Java编程助手，拒绝回答非技术问题。");
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        return this.deepseekChatModel.call(prompt).getResult().getOutput().getText();
    }


}

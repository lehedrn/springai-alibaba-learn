package com.coderlee.ai.prompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("prompt")
public class PromptController {
@Resource(name = "deepseek")
    private ChatModel deepseekChatModel;

    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;
    
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping("deepseek/client/chat")
    public Flux<String> deepseekClientChat(String question) {
        return this.deepseekChatClient.prompt()
                        // AI能力边界
                        .system("你是一个法律助手，只回答法律问题，其它问题回复，我只能回答法律相关问题，其它无可奉告")
                        .user(question)
                        .stream()
                        .content();
    }

    @GetMapping("deepseek/client/chatstring")
    public String deepseekClientChatString(@RequestParam String question) {
        AssistantMessage assistantMessage = deepseekChatClient.prompt()
                                                .user(question)
                                                .call()
                                                .chatResponse()
                                                .getResult()
                                                .getOutput();
        return assistantMessage.getText();                                                
    }
    

    @GetMapping("deepseek/model/chatresponse")
    public Flux<ChatResponse> deepseekModelChatResponse(String question) {
        SystemMessage systemMessage = new SystemMessage("你是一个讲故事的助手，每个故事控制在300字以内");
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return this.deepseekChatModel.stream(prompt);
    }

    @GetMapping("deepseek/model/chatstring")
    public Flux<String> deepseekModelChatString(String question) {
        SystemMessage systemMessage = new SystemMessage("你是一个讲故事的助手,每个故事控制在600字以内且以HTML格式返回");
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return this.deepseekChatModel.stream(prompt)
                        .map(chatResponse -> chatResponse.getResults().get(0).getOutput().getText());
    }


    @GetMapping("qwen/client/chat")
    public Flux<String> qwenClientChat(String question) {
        return this.qwenChatClient.prompt()
                        // AI能力边界
                        .system("你是一个法律助手，只回答法律问题，其它问题回复，我只能回答法律相关问题，其它无可奉告")
                        .user(question)
                        .stream()
                        .content();
    }

    @GetMapping("qwen/client/chatstring")
    public String qwenClientChatString(@RequestParam String question) {
        AssistantMessage assistantMessage = this.qwenChatClient.prompt()
                                                .user(question)
                                                .call()
                                                .chatResponse()
                                                .getResult()
                                                .getOutput();
        return assistantMessage.getText();                                                
    }
    

    @GetMapping("qwen/model/chatresponse")
    public Flux<ChatResponse> qwenkModelChatResponse(String question) {
        SystemMessage systemMessage = new SystemMessage("你是一个讲故事的助手，每个故事控制在300字以内");
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return this.qwenChatModel.stream(prompt);
    }

    @GetMapping("qwen/model/chatstring")
    public Flux<String> qwenModelChatString(String question) {
        SystemMessage systemMessage = new SystemMessage("你是一个讲故事的助手,每个故事控制在600字以内且以HTML格式返回");
        UserMessage userMessage = new UserMessage(question);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return this.qwenChatModel.stream(prompt)
                        .map(chatResponse -> chatResponse.getResults().get(0).getOutput().getText());
    }

    //关于tool调用,后续讨论
}

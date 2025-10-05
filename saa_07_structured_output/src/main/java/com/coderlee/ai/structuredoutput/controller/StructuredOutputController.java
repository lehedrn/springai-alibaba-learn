package com.coderlee.ai.structuredoutput.controller;

import java.util.Map;
import java.util.function.Consumer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coderlee.ai.structuredoutput.entity.Book;
import com.coderlee.ai.structuredoutput.records.StudentRecord;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("structured-output")
public class StructuredOutputController {

    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;
    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @GetMapping("qwen/client/chat/student")
    public StudentRecord qwenClientChatStudent(
        @RequestParam(name = "name") String name,
        @RequestParam(name = "email") String email
    ) {
        return this.qwenChatClient.prompt().user(new Consumer<ChatClient.PromptUserSpec>() {
            @Override
            public void accept(ChatClient.PromptUserSpec t) {
                t.text("学号1001，我叫{name},大学专业计算机科学与技术,邮箱{email}")
                    .param("name", name)
                    .param("email", email);
            }
        }).call().entity(StudentRecord.class);
    }

    @GetMapping("qwen/model/chat/student")
    public StudentRecord qwenModelChatStudent(
        @RequestParam(name = "name") String name,
        @RequestParam(name = "email") String email
    ) {
        BeanOutputConverter<StudentRecord> beanOutputConverter = new BeanOutputConverter<>(StudentRecord.class);
        String formater = beanOutputConverter.getFormat();
        System.out.println(formater);
        String template = "将 学号1001，我叫{name},大学专业计算机科学与技术,邮箱{email} 转换成如下格式：{formater}";
        Prompt prompt = new PromptTemplate(template).create(Map.of("name", name, "email", email, "formater", formater));
        System.out.println(prompt);
        Generation generation = this.qwenChatModel.call(prompt).getResult();
        String output = generation.getOutput().getText();
        System.out.println(output);
        return beanOutputConverter.convert(output);
    }

    @GetMapping("qwen/client/chat/book")
    public Book qwenClientChatBook(@RequestParam(name = "name") String name) {
        String template = "书号1，书名{name}";
        return this.qwenChatClient.prompt().user(promptUserSpec -> promptUserSpec.text(template)
            .param("name", name)).call().entity(Book.class);
    }
}

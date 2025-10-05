package com.coderlee.ai.bailianrag.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.ai.advisor.DocumentRetrievalAdvisor;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("bailian-rag")
public class BailianRagController {

    @Resource
    private ChatClient chatClient;
    @Resource
    private DashScopeApi dashScopeApi;

    private static final String INDEX_NAME = "ops";

    @GetMapping("chat")
    public Flux<String> chat(@RequestParam(name = "msg", defaultValue = "00000错误信息") String msg) {
        DashScopeDocumentRetriever retriever = new DashScopeDocumentRetriever(this.dashScopeApi, DashScopeDocumentRetrieverOptions.builder().withIndexName(INDEX_NAME).build());
        return this.chatClient.prompt().user(msg).advisors(new DocumentRetrievalAdvisor(retriever)).stream().content();
    }
}

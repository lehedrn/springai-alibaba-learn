package com.coderlee.ai.rag4aiops.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("rag")
public class Rag4AiOpsController {

    @Resource(name = "qwenChatClient")
    private ChatClient chatClient;
    @Resource
    private VectorStore vectorStore;

    @GetMapping("chat")
    public Flux<String> getMethodName(String msg) {
        String systemInfo = """
                你是一个运维工程师,按照给出的编码给出对应故障解释,否则回复找不到信息。
                """;
        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                                                                        .documentRetriever(
                                                                            VectorStoreDocumentRetriever.builder().vectorStore(this.vectorStore).build()
                                                                        )
                                                                        .build();
        return this.chatClient.prompt().system(systemInfo).user(msg).advisors(advisor).stream().content();
    }
    
}

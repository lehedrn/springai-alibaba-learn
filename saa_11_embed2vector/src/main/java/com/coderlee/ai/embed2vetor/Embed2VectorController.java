package com.coderlee.ai.embed2vetor;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("embed2vector")
@Slf4j
public class Embed2VectorController {

    @Resource
    private EmbeddingModel embeddingModel;

    @Resource
    private VectorStore vectorStore;

    @GetMapping("text2embed")
    public EmbeddingResponse text2Embed(String msg) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.call(
            new EmbeddingRequest(List.of(msg), 
                DashScopeEmbeddingOptions.builder().withModel("text-embedding-v4").build())
        );
        log.info("vetors: {}", embeddingResponse.getResult().getOutput());
        return embeddingResponse;
    }

    @GetMapping("add2vs")
    public String add2vs(String msg) {
        this.vectorStore.add(List.of(new Document(msg)));
        return "success";
    }

    @GetMapping("search")
    public List<Document> search(String msg) {
        SearchRequest searchRequest = SearchRequest.builder()
                                                .query(msg)
                                                .topK(5)
                                                .build();
        List<Document> list = this.vectorStore.similaritySearch(searchRequest);
        log.info("search list: {}", list);
        return list;
    }

}

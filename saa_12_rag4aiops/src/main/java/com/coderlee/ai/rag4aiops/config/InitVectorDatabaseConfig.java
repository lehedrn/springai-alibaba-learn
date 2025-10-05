package com.coderlee.ai.rag4aiops.config;

import java.nio.charset.Charset;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class InitVectorDatabaseConfig {

    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("classpath:ops.txt")
    private Resource opsFile;
    @Value("${spring.ai.vectorstore.redis.prefix}")
    private String redisPrefix;

    @PostConstruct
    public void init() throws Exception {
        // 读取资源文件
        TextReader textReader = new TextReader(opsFile);
        textReader.setCharset(Charset.defaultCharset());

        // 转换成为向量(开启分词)
        List<Document> list = new TokenTextSplitter().transform(textReader.read());
        // 这里不应该直接写入向量数据库，因为重启服务会导致数据重复写入
        // this.vectorStore.add(list);
        String sourceMetadata = (String) textReader.getCustomMetadata().get("source");
        log.info("资源文件: {}", sourceMetadata);
        String textHash = SecureUtil.md5(sourceMetadata);
        String redisKey = "vector-resources:" + textHash;
        String fileValue = DigestUtil.md5Hex(opsFile.getFile());
        Boolean exists = this.redisTemplate.opsForValue().setIfAbsent(redisKey, fileValue);
        if (Boolean.TRUE.equals(exists)) {
            log.info("开始加载 {} 到向量数据库", sourceMetadata);
            this.vectorStore.add(list);
        } else {
            String redisValue = this.redisTemplate.opsForValue().get(redisKey);
            if (fileValue.equals(redisValue)) {
                log.info("向量数据库已初始化，数据来自 {}", sourceMetadata);
            } else {
                log.info("资源文件 {} 已发生变化，重新加载向量数据库中", sourceMetadata);
                // 先删除旧的向量数据
                SearchRequest searchRequest = SearchRequest.builder().query(sourceMetadata).topK(5).build();
                List<Document> searchList = this.vectorStore.similaritySearch(searchRequest);
                if (CollectionUtil.isNotEmpty(searchList) && null != searchList) {
                    // 从searchList获取id的List集合
                    List<String> docIds = searchList.stream().map(document -> document.getId()).toList();
                    this.vectorStore.delete(docIds);    
                }
                
                // "source": "ops.txt",
                // this.vectorStore.delete("source == 'ops.txt'");
                // 再添加新的向量数据
                this.vectorStore.add(list);
                // 更新文件hash
                this.redisTemplate.opsForValue().set(redisKey, fileValue);
            }
        }
    }

}

package com.coderlee.ai.todaymenu.controller;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgent;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("today-menu-call-agent")
public class TodayMenuCallAgentController {

    @Value("${spring.ai.dashscope.agent.options.app-id}")
    private String appId;

    private DashScopeAgent dashScopeAgent;

    public TodayMenuCallAgentController(DashScopeAgentApi dashScopeAgentApi) {
        this.dashScopeAgent = new DashScopeAgent(dashScopeAgentApi);
    }

    @GetMapping("chat")
    public Flux<String> chat(@RequestParam(name = "msg", defaultValue = "今天吃什么") String msg) {
        DashScopeAgentOptions options = DashScopeAgentOptions.builder().withAppId(this.appId).build();
        Prompt prompt = new Prompt(msg, options);
        return this.dashScopeAgent.stream(prompt).map(resp -> resp.getResults().get(0).getOutput().getText());
    }

}

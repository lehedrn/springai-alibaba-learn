package com.coderlee.ai.toolcalling.utils;

import java.time.LocalDateTime;

import org.springframework.ai.tool.annotation.Tool;

public class DateTimeTools {

    @Tool(description = "获取当前时间", returnDirect = false)
    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }

}

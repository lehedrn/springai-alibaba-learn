package com.coderlee.ai.text2voice;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;

import jakarta.annotation.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("text2voice")
public class Text2VoiceController {

    @Resource
    private SpeechSynthesisModel speechSynthesisModel;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final String BAILIAN_VOICE_MODEL = "cosyvoice-v2";
    // voice timber 音色列表：https://help.aliyun.com/zh/model-studio/cosyvoice-java-sdk#722dd7ca66a6x
    public static final String BAILIAN_VOICE_TIMBER = "longyingcui";//龙应催

    @GetMapping("voice")
    public String voice(@RequestParam(name = "msg",defaultValue = "温馨提醒，支付宝到账100元请注意查收") String msg) {
        // 定义音频文件路径，再resources目录下
        DashScopeSpeechSynthesisOptions options = DashScopeSpeechSynthesisOptions.builder()
                                                                                .model(BAILIAN_VOICE_MODEL)
                                                                                .voice(BAILIAN_VOICE_TIMBER)
                                                                                .build();
        SpeechSynthesisResponse response = speechSynthesisModel.call(new SpeechSynthesisPrompt(msg, options));

        ByteBuffer byteBuffer = response.getResult().getOutput().getAudio();
        String fileName = UUID.randomUUID() + ".mp3";
        try {
            org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:static");
            File staticDir = resource.getFile();
            String filePath = Paths.get(staticDir.getAbsolutePath(), fileName).toString();
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(byteBuffer.array());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}

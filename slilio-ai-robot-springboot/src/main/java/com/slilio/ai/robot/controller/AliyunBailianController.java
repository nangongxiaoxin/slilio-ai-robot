package com.slilio.ai.robot.controller;

import com.slilio.ai.robot.model.AIResponse;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;

/**
 * @Author: slilio
 * @CreateTime: 2025-07-03
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/v6/ai")
public class AliyunBailianController {
    @Resource private OpenAiChatModel openAiChatModel;

    /**
     * 普通对话
     * @param message
     * @return
     */
    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message",defaultValue = "你是谁？")String message){
        //一次性返回结果
        return openAiChatModel.call(message);

    }

    /**
     * 流式输出
     *
     * @param message
     * @return
     */
    @GetMapping(value = "generateStream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AIResponse> generateStream(@RequestParam(value = "message",defaultValue = "你是谁")String message){
        Prompt prompt = new Prompt(message);

        //流式输出
        return openAiChatModel.stream(prompt)
                .mapNotNull(chatResponse -> {
                    Generation generation = chatResponse.getResult();
                    String text = generation.getOutput().getText();

                    return StringUtils.isNotBlank(text)?
                            AIResponse.builder().v(text).build() : AIResponse.builder().v("").build();

                });
    }
}

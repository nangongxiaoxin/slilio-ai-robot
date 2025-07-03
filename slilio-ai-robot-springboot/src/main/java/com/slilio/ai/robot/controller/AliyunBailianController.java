package com.slilio.ai.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
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
    @GetMapping(value = "generateStream",produces = "text/html;charset=utf-8")
    public Flux<String> generateStream(@RequestParam(value = "message",defaultValue = "你是谁")String message){
        Prompt prompt = new Prompt(message);

        //流式输出
        return openAiChatModel.stream(prompt)
                .mapNotNull(chatResponse -> {
                    Generation generation = chatResponse.getResult();
                    return Objects.nonNull(generation) ? generation.getOutput().getText() : null;
                });
    }
}

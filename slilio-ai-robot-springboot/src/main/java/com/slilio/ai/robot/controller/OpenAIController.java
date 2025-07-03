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
@RequestMapping("/v5/ai")
public class OpenAIController {
    @Resource private OpenAiChatModel openAiChatModel;

    /**
     * 普通对话
     * @param message
     * @return
     */
    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message",defaultValue = "你是谁？") String message){
        return openAiChatModel.call(message);
    }

    /**
     * 流式对话
     * @param message
     * @return
     */
    @GetMapping(value = "/generateStream", produces = "text/html;charset=utf-8")
    public Flux<String> generateStream(@RequestParam(value = "message",defaultValue = "你是谁")String message){
        //构建提示词
        Prompt prompt = new Prompt(message);

        return openAiChatModel.stream(prompt)
                .mapNotNull(chatResponse -> {
                    Generation generation = chatResponse.getResult();
                    return Objects.nonNull(generation) ? generation.getOutput().getText(): null;
                });
    }
}

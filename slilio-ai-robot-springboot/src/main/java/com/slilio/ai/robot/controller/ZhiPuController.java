package com.slilio.ai.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * @Author: slilio
 * @CreateTime: 2025-07-03
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/v4/ai")
public class ZhiPuController {
    @Resource private ZhiPuAiChatModel  zhiPuAiChatModel;


    /**
     * 普通对话
     * @param message
     * @return
     */
    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message",defaultValue = "你是谁")String message){
        //一次性返回结果
        return zhiPuAiChatModel.call(message);
    }

    /**
     * 流式输出
     * @param message
     * @return
     */
    @GetMapping(value = "/generateStream", produces = "text/html;charset=utf-8")
    public Flux<String> generateStream(@RequestParam(value = "message",defaultValue = "你是谁")String message){
        //构建提示词
        Prompt prompt = new Prompt(message);

        //流式输出
        return zhiPuAiChatModel.stream(prompt)
                .mapNotNull(chatResponse -> chatResponse.getResult().getOutput().getText());
    }
}

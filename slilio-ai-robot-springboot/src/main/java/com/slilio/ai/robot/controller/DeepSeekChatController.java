package com.slilio.ai.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Author: slilio
 * @CreateTime: 2025-07-02
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping("/ai")
public class DeepSeekChatController {

    @Resource private DeepSeekChatModel  deepSeekChatModel;

    /**
     * 普通对话
     * @param message
     * @return
     */
    @GetMapping("/generate")
    public String generate(@RequestParam(value = "message",defaultValue = "你是谁？") String message){
        //一次返回结果
        return deepSeekChatModel.call(message);
    }


    /**
     * 流式对话
     * @param message
     * @return
     */
    @GetMapping(value = "generateStream",produces = "text/html;charset=utf-8")
    public Flux<String> generateStream(@RequestParam(value = "message",defaultValue = "你是谁？") String message){
        //构建提示词语
        Prompt prompt = new Prompt(new UserMessage(message));

        //流式输出
        return deepSeekChatModel.stream(prompt).mapNotNull(chatResponse -> chatResponse.getResult().getOutput().getText());
    }
}

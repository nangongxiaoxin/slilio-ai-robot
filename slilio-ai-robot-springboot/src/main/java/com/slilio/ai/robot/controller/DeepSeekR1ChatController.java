package com.slilio.ai.robot.controller;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: slilio
 * @CreateTime: 2025-07-02
 * @Description: DeepSeek 聊天（R1 推理大模型）
 * @Version: 1.0
 */
@RestController
@RequestMapping("/v1/ai")
public class DeepSeekR1ChatController {
    @Resource private DeepSeekChatModel  deepSeekChatModel;

    /**
     * 流式对话
     * @param message
     * @return
     */
    @GetMapping(value = "generateStream",produces = "text/html;charset=utf-8")
    public Flux<String> generateStream(@RequestParam(value = "message",defaultValue = "你是谁？") String message){
        //提示词
        Prompt prompt = new Prompt(new UserMessage(message));

        //使用原子布尔值跟踪分割线状态(每个请求独立)
        AtomicBoolean needSeparator = new AtomicBoolean(true);

        //流式输出
        return deepSeekChatModel.stream(prompt)
                .mapNotNull(chatResponse -> {
                    //获取响应内容
                    DeepSeekAssistantMessage deepSeekAssistantMessage = (DeepSeekAssistantMessage) chatResponse.getResult().getOutput();
                    //推理内容
                    String reasoningContent = deepSeekAssistantMessage.getReasoningContent();
                    //推理结束后的内容
                    String text = deepSeekAssistantMessage.getText();

                    //是否是正式回答
                    boolean isTextResponse = false;

                    //若推理内容有值，则返回内容值，否则就返回正式回答
//                    String rawContent =  StringUtils.isNotBlank(reasoningContent)?reasoningContent:text;
                    String rawContent;
                    if(Objects.isNull(text)){
                        rawContent = reasoningContent;
                    }else {
                        rawContent = text;
                        isTextResponse = true; //标记为正式回答
                    }


                    //处理换行 将\n替换为html标签的换行符<br>
                    String processed =  StringUtils.isNotBlank(rawContent)?rawContent.replace("\n","<br>"):rawContent;

                    //在正式回答前添加分隔号
                    if (isTextResponse && needSeparator.compareAndSet(true,false)){
                        //添加HTML的<hr>标签
                        processed = "<hr>" + processed;
                    }

                    return processed;
                });
    }
}

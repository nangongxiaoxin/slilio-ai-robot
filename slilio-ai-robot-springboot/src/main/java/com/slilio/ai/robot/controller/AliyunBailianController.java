package com.slilio.ai.robot.controller;

import com.slilio.ai.robot.model.AIResponse;
import jakarta.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Author: slilio @CreateTime: 2025-07-03 @Description: @Version: 1.0
 */
@RestController
@RequestMapping("/v6/ai")
public class AliyunBailianController {
  @Resource private OpenAiChatModel openAiChatModel;

  // 存储聊天对话
  private Map<String, List<Message>> chatMemoryStore = new ConcurrentHashMap<>();

  /**
   * 普通对话
   *
   * @param message
   * @return
   */
  @GetMapping("/generate")
  public String generate(
      @RequestParam(value = "message", defaultValue = "你是谁？") String message,
      @RequestParam(value = "chatId") String chatId) {
    // 根据chatID获取对话记录
    List<Message> messages = chatMemoryStore.get(chatId);
    // 如果不存在，则初始化一份
    if (CollectionUtils.isEmpty(messages)) {
      messages = new ArrayList<>();
      chatMemoryStore.put(chatId, messages);
    }

    // 用户角色(用户角度)
    messages.add(new UserMessage(message));

    // 提示词
    Prompt prompt = new Prompt(messages);
    // 结果
    String responseText = openAiChatModel.call(prompt).getResult().getOutput().getText();

    // 添加助手(ai角度)
    if (StringUtils.isNotBlank(responseText)) {
      messages.add(new AssistantMessage(responseText));
    }

    // 一次性返回结果
    return responseText;
  }

  /**
   * 流式输出
   *
   * @param message
   * @return
   */
  @GetMapping(value = "generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<AIResponse> generateStream(
      @RequestParam(value = "message", defaultValue = "你是谁") String message,
      @RequestParam(value = "chatId") String chatId) {

    // 系统角色信息
    //    SystemMessage systemMessage = new SystemMessage("请你扮演一名聊天助手");

    // 用户角色信息
    UserMessage userMessage = new UserMessage(message);

    // 根据chatID获取对话记录
    List<Message> messages = chatMemoryStore.get(chatId);
    // 如果不存在，则初始化一份
    if (CollectionUtils.isEmpty(messages)) {
      messages = new ArrayList<>();
      chatMemoryStore.put(chatId, messages);
    }
    messages.add(userMessage);
    //    messages.add(systemMessage);

    // 构建提示词
    Prompt prompt = new Prompt(messages);

    // 流式输出
    return openAiChatModel.stream(prompt)
        .mapNotNull(
            chatResponse -> {
              Generation generation = chatResponse.getResult();
              String text = generation.getOutput().getText();

              return StringUtils.isNotBlank(text)
                  ? AIResponse.builder().v(text).build()
                  : AIResponse.builder().v("").build();
            });
  }
}

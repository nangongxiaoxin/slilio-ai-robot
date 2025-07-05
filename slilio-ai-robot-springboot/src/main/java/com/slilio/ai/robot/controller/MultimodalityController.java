package com.slilio.ai.robot.controller;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Author: slilio @CreateTime: 2025-07-05 @Description: @Version: 1.0
 */
@RestController
@RequestMapping("/v9/ai")
public class MultimodalityController {
  //  @Resource private OpenAiChatModel openAiChatModel;
  @Resource private OllamaChatModel ollamaChatModel;

  /**
   * 流式对话
   *
   * @param message
   * @return
   */
  @GetMapping(value = "/generateStream", produces = "text/html;charset=utf-8")
  public Flux<String> generateStream(@RequestParam(value = "message") String message) {
    // 1.创建媒体资源
    //    Media image = new Media(MimeTypeUtils.IMAGE_JPEG, new
    // ClassPathResource("/images/multimodal-test.jpg"));
    Media image = new Media(MimeTypeUtils.IMAGE_JPEG, new ClassPathResource("/images/demo.jpg"));

    // 2.附件选项(可选) ，如温度值等等
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("temperature", 0.7);

    // 3.构建多模态消息
    UserMessage userMessage =
        UserMessage.builder().text(message).media(image).metadata(metadata).build();

    // 4.构建提示词
    Prompt prompt = new Prompt(List.of(userMessage));
    // 5.流式调用
    //    return openAiChatModel.stream(prompt)
    //        .mapNotNull(
    //            chatResponse -> {
    //              Generation generation = chatResponse.getResult();
    //              return generation.getOutput().getText();
    //            });
    return ollamaChatModel.stream(prompt)
        .mapNotNull(
            chatResponse -> {
              Generation generation = chatResponse.getResult();
              return generation.getOutput().getText();
            });
  }
}

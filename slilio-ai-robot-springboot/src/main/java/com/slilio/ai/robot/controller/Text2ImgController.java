package com.slilio.ai.robot.controller;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: slilio @CreateTime: 2025-07-06 @Description: @Version: 1.0
 */
@RestController
@RequestMapping("/v10/ai")
@Slf4j
public class Text2ImgController {

  @Value("${spring.ai.openai.api-key}")
  private String apiKey;

  /**
   * 调用阿里百练图生文大模型
   *
   * @param prompt
   * @return
   */
  @GetMapping("/text2img")
  public String text2Image(@RequestParam(value = "prompt") String prompt) {
    // 构建文生图参数
    ImageSynthesisParam param =
        ImageSynthesisParam.builder()
            .apiKey(apiKey)
            .model("wanx2.1-t2i-plus") // 模型
            .prompt(prompt) // 提示词
            .n(1) // 图片数量
            .size("1024*1024") // 图片分辨率
            .build();

    // 同步调用AI大模型，生成图片
    ImageSynthesis imageSynthesis = new ImageSynthesis();
    ImageSynthesisResult result = null;
    try {
      log.info("## 同步调用，请稍等一会...");
      result = imageSynthesis.call(param);
    } catch (ApiException | NoApiKeyException e) {
      log.error("", e);
    }

    // 返回生成的结果（包含图片的 URL 链接）
    return JsonUtils.toJson(result);
  }
}

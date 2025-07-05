package com.slilio.ai.robot.controller;

import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesis;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisParam;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisResult;
import com.alibaba.dashscope.utils.JsonUtils;
import java.util.HashMap;
import java.util.Map;
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
@RequestMapping("/v12/ai")
@Slf4j
public class Text2VideoController {

  @Value("${spring.ai.openai.api-key}")
  private String apiKey;

  /**
   * 调用阿里百炼图生视频大模型
   *
   * @param prompt
   * @return
   */
  @GetMapping("/text2video")
  public String text2video(@RequestParam(value = "prompt") String prompt) {
    // 设置视频处理参数，如指定输出视频的分辨率、视频时长等。
    Map<String, Object> parameters = new HashMap<>();
    // 是否开启prompt智能改写。开启后使用大模型对输入 prompt 进行智能改写。对于较短的 prompt 生成效果提升明显，但会增加耗时。
    parameters.put("prompt_extend", true);

    // 静态图片路径，将它转换为动态视频
    String imgUrl = "file:///" + "F:/GitHub/JAVA/slilio-ai-robot/demo.jpg";

    // 构建调用大模型所需要的参数
    VideoSynthesisParam param =
        VideoSynthesisParam.builder()
            .apiKey(apiKey)
            .model("wanx2.1-i2v-plus") // 模型名称
            .prompt(prompt) // 提示词
            .imgUrl(imgUrl) // 静态图片路径
            .parameters(parameters) // 视频处理参数
            .build();

    log.info("## 正在生成中，请稍后...");

    // 调用AI大模型生成视频
    VideoSynthesis vs = new VideoSynthesis();
    VideoSynthesisResult result = null;
    try {
      result = vs.call(param);
    } catch (Exception e) {
      log.error("", e);
    }

    // 返参
    return JsonUtils.toJson(result);

    // 结果：
    // https://dashscope-result-wlcb-acdr-1.oss-cn-wulanchabu-acdr-1.aliyuncs.com/1d/31/20250706/2b1a5779/58a76b31-a3fd-4cd7-bd62-007ccc0a6b32.mp4?Expires=1751830359&OSSAccessKeyId=LTAI5tKPD3TMqf2Lna1fASuh&Signature=H%2F1JoqGRobk1WQoLJIqJC9WN38Y%3D
  }
}

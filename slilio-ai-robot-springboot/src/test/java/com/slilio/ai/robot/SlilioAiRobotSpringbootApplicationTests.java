package com.slilio.ai.robot;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileCopyUtils;

@SpringBootTest
class SlilioAiRobotSpringbootApplicationTests {

  @Value("classpath:/prompts/code-assistant.st")
  private org.springframework.core.io.Resource templateResource;

  @Test
  void contextLoads() throws IOException {
    String txt =
        FileCopyUtils.copyToString(
            new InputStreamReader(templateResource.getInputStream(), StandardCharsets.UTF_8));
    System.out.println(txt);
  }
}

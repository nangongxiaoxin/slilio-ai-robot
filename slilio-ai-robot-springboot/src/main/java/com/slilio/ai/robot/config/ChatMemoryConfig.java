package com.slilio.ai.robot.config;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.cassandra.CassandraChatMemoryRepository;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: slilio @CreateTime: 2025-07-03 @Description: @Version: 1.0
 */
@Configuration
public class ChatMemoryConfig {
  // 记忆存储 本地存储
  //    @Resource private ChatMemoryRepository chatMemoryRepository; //本地存储
  @Resource CassandraChatMemoryRepository chatMemoryRepository;

  // 初始化一个chatMemory实例
  public ChatMemory chatMemory() {
    return MessageWindowChatMemory.builder()
        .maxMessages(50) // 最大窗口消息
        .chatMemoryRepository(chatMemoryRepository) // 记忆存储
        .build();
  }
}

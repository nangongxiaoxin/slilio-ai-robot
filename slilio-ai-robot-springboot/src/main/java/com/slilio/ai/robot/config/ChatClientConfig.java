package com.slilio.ai.robot.config;


import com.slilio.ai.robot.advisor.MyLoggerAdvisor;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: slilio
 * @CreateTime: 2025-07-02
 * @Description:
 * @Version: 1.0
 */
@Configuration
public class ChatClientConfig {

    @Resource private ChatMemory  chatMemory;

    /**
     * 初始化ChatClient客户端
     * @param chatModel
     * @return
     */
    @Bean
    public ChatClient chatClient(DeepSeekChatModel chatModel){ //（依赖注入 starter 自动装配的 DeepSeekChatModel 实例，它是 ChatModel 接口的具体实现类）底层对接Deepseek大模型
        //构建者 将deepseek作为底层绑定到chatClient客户端
        return ChatClient.builder(chatModel)
                .defaultSystem("请你扮演一名 Java 项目实战专栏的客服人员")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), //添加日志记录功能
//                        new MyLoggerAdvisor() // 添加自定义的日志打印 Advisor
                        MessageChatMemoryAdvisor.builder(chatMemory).build() //记忆存储
                ).build();
    }
}

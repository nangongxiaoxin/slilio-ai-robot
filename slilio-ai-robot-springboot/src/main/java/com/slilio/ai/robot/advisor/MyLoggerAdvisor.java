package com.slilio.ai.robot.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;

/**
 * @Author: slilio
 * @CreateTime: 2025-07-02
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class MyLoggerAdvisor implements CallAdvisor {
    //注意：CallAdvisor 和 StreamAdvisor是两种不同类型的 Advisor，分别用于处理 同步调用 和 流式调用 场景。此处实现的是CallAdvisor接口

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        //类似AOP (拿到请求->逻辑处理->输出信息)
        log.info("## 请求入参: {}", chatClientRequest);
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        log.info("## 请求出参: {}", chatClientResponse);
        return chatClientResponse;
    }

    @Override
    public String getName() {
        //获取类名称
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        // order 值越小，越先执行
        return 1;
    }
}

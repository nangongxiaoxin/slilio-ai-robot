package com.slilio.ai.robot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: slilio
 * @CreateTime: 2025-07-04
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIResponse {
    //流式响应内容
    private String v;
}

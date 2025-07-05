package com.slilio.ai.robot.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: slilio @CreateTime: 2025-07-05 @Description: @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
  private String title; // 书名
  private String author; // 作者
  private Integer year; // 发布年份
  private List<String> genres; // 类型
  private String description; // 简介
}

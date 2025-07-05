package com.slilio.ai.robot.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

/**
 * @Author: slilio @CreateTime: 2025-07-05 @Description: 演员 - 电影集合 @Version: 1.0
 */
@JsonPropertyOrder({"actor", "movies"})
public record ActorFilmography(String actor, List<String> movies) {}

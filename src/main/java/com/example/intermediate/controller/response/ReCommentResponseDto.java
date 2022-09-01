package com.example.intermediate.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReCommentResponseDto {
    private Long id;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCount;
}
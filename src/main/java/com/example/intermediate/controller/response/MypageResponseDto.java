package com.example.intermediate.controller.response;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
public class MypageResponseDto {
    private List<PostResponseDto> postList;
    private List<CommentResponseDto> commentList;
    private List<Comment> recommentList;
    private List<PostResponseDto> likePostList;
    private List<CommentResponseDto> likeCommentList;
}

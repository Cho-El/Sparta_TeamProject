package com.example.intermediate.controller.response;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Builder
@Getter
public class MypageResponseDto {
    private List<Post> postList;
    private List<Comment> commentList;
    private List<Comment> recommentList;
    private List<Post> likePostList;
    private List<Comment> likeCommentList;
}

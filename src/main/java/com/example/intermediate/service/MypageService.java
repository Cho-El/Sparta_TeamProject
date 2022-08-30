package com.example.intermediate.service;


import com.example.intermediate.controller.response.MypageResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Post;
import com.example.intermediate.domain.UserDetailsImpl;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.LikeRepository;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MypageService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public ResponseDto<?> mypage (@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long member_id = userDetails.getMember().getId();
        List<Post> post = postRepository.findAllByMemberId(member_id);
        List<Comment> comment = commentRepository.findAllByMemberIdAndParent(member_id,null);
        List<Comment> recomment = commentRepository.findAllByMemberIdAndParentNotNull(member_id);

        List<Post> like_post = likeRepository.findAllByUserIdAndPostId(member_id,null).stream()
                .map(like -> postRepository.findById(like.getPost().getId()).orElseThrow(
                        () -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.")))
                .collect(Collectors.toList());

        List<Comment> like_comment = likeRepository.findAllByUserIdAndCommentId(member_id,null).stream()
                .map(like -> commentRepository.findById(like.getComment().getId()).orElseThrow(
                        () -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.")))
                .collect(Collectors.toList());



        return ResponseDto.success(
                MypageResponseDto.builder()
                        .postList(post)
                        .commentList(comment)
                        .recommentList(recomment)
                        .likePostList(like_post)
                        .likeCommentList(like_comment)
                        .build()
        );
    }
}

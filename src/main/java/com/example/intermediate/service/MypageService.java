package com.example.intermediate.service;


import com.example.intermediate.controller.response.CommentResponseDto;
import com.example.intermediate.controller.response.MypageResponseDto;
import com.example.intermediate.controller.response.PostResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Likes;
import com.example.intermediate.domain.Post;
import com.example.intermediate.domain.UserDetailsImpl;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.LikeRepository;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@RequiredArgsConstructor
@Service
public class MypageService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;


    public int comment_like(long id) { return likeRepository.countByCommentId(id); }
    public int post_like(long id) { return likeRepository.countByPostId(id); }
    public ResponseDto<?> mypage (@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long member_id = userDetails.getMember().getId();
        List<Post> postList = postRepository.findAllByMemberId(member_id);
        List<Comment> commentList = commentRepository.findAllByMemberIdAndParent(member_id,null);
        List<Comment> recommentList = commentRepository.findAllByMemberIdAndParentIsNotNull(member_id);

        List<Post> like_post = likeRepository.findAllByUserIdAndPostId(member_id, null).stream()
                .map(postId -> postRepository.findById(postId.getPost().getId()).orElseThrow(
                        () -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다.")))
                .collect(Collectors.toList());
        log.info("member_postList : {} " , postList.size());
        log.info("like_post : {}",like_post.size());

        List<Comment> like_comment = likeRepository.findAllByUserIdAndCommentId(member_id,null).stream()
                .map(commentId -> commentRepository.findById(commentId.getComment().getId()).orElseThrow(
                        () -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다.")))
                .collect(Collectors.toList());
        log.info("like_comment : {}",like_comment.size());
        List<PostResponseDto> member_postList = new ArrayList<>();

        List<PostResponseDto> member_like_postList = new ArrayList<>();
        List<CommentResponseDto> member_commentList = new ArrayList<>();

        for(Post post : postList){
            member_postList.add(
                    PostResponseDto.builder()
                            .id(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .author(post.getMember().getNickname())
                            .like_count(post_like(post.getId()))
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }

//        for(Post postLike : like_post)
//            member_like_postList.add(
//                    PostResponseDto.builder()
//                            .id(postLike.getId())
//                            .title(postLike.getTitle())
//                            .content(postLike.getContent())
//                            .author(postLike.getMember().getNickname())
//                            .like_count(post_like(postLike.getId()))
//                            .createdAt(postLike.getCreatedAt())
//                            .modifiedAt(postLike.getModifiedAt())
//                            .build()
//            );
        for(Comment comment : commentList){
            member_commentList.add(
                    CommentResponseDto.builder()
                    .id(comment.getId())
                    .author(comment.getMember().getNickname())
                    .content(comment.getContent())
                    .likeCount(comment_like(comment.getId()))
                    .createdAt(comment.getCreatedAt())
                    .modifiedAt(comment.getModifiedAt())
                    .build()
            );
        }



        return ResponseDto.success(
                MypageResponseDto.builder()
                        .postList(member_postList)
                        .commentList(member_commentList)
                        .recommentList(recommentList)
                        .likePostList(member_like_postList)
//                        .likeCommentList(like_comment)
                        .build()
        );
    }
}

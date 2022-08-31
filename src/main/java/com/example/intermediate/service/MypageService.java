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
        List<Comment> commentList = commentRepository.findAllByMemberId(member_id);
        List<Comment> recommentList = commentRepository.findAllByMemberIdAndParentIsNotNull(member_id);


        // 좋아요한 게시물 가져오기
        List<Post> like_postList = postRepository.findAll();
        List<Post> like_post = new ArrayList<>();
        for(int i=0; i<like_postList.size();i++) {
            Post post = like_postList.get(i);
            List<Likes> likes = likeRepository.findAllByUserIdAndPostId(member_id,post.getId());
            if(likes.size() != 0){
                like_post.add(post);
            }
        }

        // 좋아요한 댓글 가져오기
        List<Comment> like_commentList = commentRepository.findAll();
        List<Comment> like_comment = new ArrayList<>();
        for(int i=0; i<like_commentList.size();i++) {
            Comment comment = like_commentList.get(i);;
            List<Likes> likes = likeRepository.findAllByUserIdAndCommentId(member_id,comment.getId());
            if(likes.size() != 0){
                like_comment.add(comment);
            }
        }

        // 게시물 , 댓글, 좋아요한 게시물, 좋아요한 댓글 넣어둘 ResponseDto
        List<PostResponseDto> member_postList = new ArrayList<>();
        List<CommentResponseDto> member_commentList = new ArrayList<>();
        List<PostResponseDto> member_like_postList = new ArrayList<>();
        List<CommentResponseDto> member_like_commentList = new ArrayList<>();

        // 게시물 빌드
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

        // 좋아요한 게시물 빌드
        for(Post postLike : like_post)
            member_like_postList.add(
                    PostResponseDto.builder()
                            .id(postLike.getId())
                            .title(postLike.getTitle())
                            .content(postLike.getContent())
                            .author(postLike.getMember().getNickname())
                            .like_count(post_like(postLike.getId()))
                            .createdAt(postLike.getCreatedAt())
                            .modifiedAt(postLike.getModifiedAt())
                            .build()
            );

        // 댓글 빌드
        for(Comment comment : commentList) {
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

        // 좋아요한 댓글 빌드
        for(Comment commentLike : like_comment){
            member_like_commentList.add(
                    CommentResponseDto.builder()
                            .id(commentLike.getId())
                            .author(commentLike.getMember().getNickname())
                            .content(commentLike.getContent())
                            .likeCount(comment_like(commentLike.getId()))
                            .createdAt(commentLike.getCreatedAt())
                            .modifiedAt(commentLike.getModifiedAt())
                            .build()
            );

        }



        return ResponseDto.success(
                MypageResponseDto.builder()
                        .postList(member_postList)
                        .commentList(member_commentList)
                        .recommentList(recommentList)
                        .likePostList(member_like_postList)
                        .likeCommentList(member_like_commentList)
                        .build()
        );
    }
}
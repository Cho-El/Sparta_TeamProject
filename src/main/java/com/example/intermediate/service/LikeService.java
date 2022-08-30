package com.example.intermediate.service;

import com.example.intermediate.controller.request.LikeRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Likes;
import com.example.intermediate.domain.Post;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.LikeRepository;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentService commentService;
    private final TokenProvider tokenProvider;

    // 게시글 좋아요 등록 -> 게시글, 댓글, 대댓글 response에 좋아요 개수 함께 나마타내기
    public ResponseDto<?> enrollPostLike(LikeRequestDto requestDto, HttpServletRequest request){
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        Post post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        // like repo에 유저의 게시글 좋아요 기록이 없는 경우
        if (likeRepository.findByUserIdAndPost(requestDto.getUserId(),post).isEmpty()){
            likeRepository.save(new Likes(requestDto.getUserId(),post));
            return ResponseDto.success("Enroll like success");
        }
        return ResponseDto.success("Already Exists");
    }
    // 게시글 좋아요 취소
    public ResponseDto<?> cancelPostLike(LikeRequestDto requestDto, HttpServletRequest request){
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        Post post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        // like repo에 유저의 게시글 좋아요 기록이 있는 경우
        if (!likeRepository.findByUserIdAndPost(requestDto.getUserId(),post).isEmpty()){
            likeRepository.delete(new Likes(requestDto.getUserId(),post));
            return ResponseDto.success("Cancel likes success");
        }
        return ResponseDto.success("Not enroll");
    }

    // 댓글 좋아요 등록
    public ResponseDto<?> enrollCommentLike(LikeRequestDto requestDto, HttpServletRequest request){
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        Comment comment = commentService.isPresentComment(requestDto.getCommentId());
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        // like repo에 유저의 게시글 좋아요 기록이 없는 경우
        if (likeRepository.findByUserIdAndComment(requestDto.getUserId(),comment).isEmpty()){
            likeRepository.save(new Likes(requestDto.getUserId(),comment));
            return ResponseDto.success("Enroll comment like success");
        }
        return ResponseDto.success("Already Exists");
    }
    // 댓글 좋아요 취소
    public ResponseDto<?> cancelCommentLike(LikeRequestDto requestDto, HttpServletRequest request){
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        Comment comment = commentService.isPresentComment(requestDto.getCommentId());
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }
        // like repo에 유저의 게시글 좋아요 기록이 있는 경우
        if (!likeRepository.findByUserIdAndComment(requestDto.getUserId(),comment).isEmpty()){
            likeRepository.delete(new Likes(requestDto.getUserId(),comment));
            return ResponseDto.success("Cancel likes success");
        }
        return ResponseDto.success("Not enroll");
    }
}

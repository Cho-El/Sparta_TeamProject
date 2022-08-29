package com.example.intermediate.controller;

import com.example.intermediate.controller.request.LikeRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/likes/")
public class LikeController {
    private final LikeService likeService;

    // 게시판 좋아요 등록
    @PostMapping("/post")
    public ResponseDto<?> enrollPostLike(@RequestBody @Valid LikeRequestDto requestDto, HttpServletRequest request) {
        return likeService.enrollPostLike(requestDto, request);
    }
    // 게시판 좋아요 취소
    @DeleteMapping("/post")
    public ResponseDto<?> cancelPostLike(@RequestBody @Valid LikeRequestDto requestDto, HttpServletRequest request) {
        return likeService.cancelPostLike(requestDto,request);
    }

    // 댓글 좋아요 등록
    @PostMapping("/comment")
    public ResponseDto<?> enrollCommentLike(@RequestBody @Valid LikeRequestDto requestDto, HttpServletRequest request) {
        return likeService.enrollCommentLike(requestDto, request);
    }
    // 댓글 좋아요 취소
    @DeleteMapping("/comment")
    public ResponseDto<?> cancelCommentLike(@RequestBody @Valid LikeRequestDto requestDto, HttpServletRequest request) {
        return likeService.cancelCommentLike(requestDto,request);
    }
}

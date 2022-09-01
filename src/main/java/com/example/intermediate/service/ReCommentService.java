package com.example.intermediate.service;

import com.example.intermediate.controller.request.CommentRequestDto;
import com.example.intermediate.controller.request.ReCommentRequestDto;
import com.example.intermediate.controller.response.CommentResponseDto;
import com.example.intermediate.controller.response.ReCommentResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Post;
import com.example.intermediate.domain.ReComment;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.LikeRepository;
import com.example.intermediate.repository.ReCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReCommentService {

    private final ReCommentRepository reCommentRepository;
    private final TokenProvider tokenProvider;
    private final CommentService commentService;
    private final LikeRepository likeRepository;
    @Transactional
    public int comment_likes(long id) {
        return likeRepository.countByCommentId(id);
    }

    @Transactional
    public ResponseDto<?> createReComment(ReCommentRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment = commentService.isPresentComment(requestDto.getCommentId());
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }
        ReComment recomment = ReComment.builder()
                .member(member)
                .comment(comment)
                .content(requestDto.getContent())
                .build();

//        Comment comment = Comment.builder()
//                .member(member)
//                .post(post)
//                .content(requestDto.getContent())
//                .build();

        reCommentRepository.save(recomment);
        return ResponseDto.success(
                ReCommentResponseDto.builder()
                        .id(recomment.getId())
                        .author(recomment.getMember().getNickname())
                        .content(recomment.getContent())
                        .likeCount(comment_likes(comment.getId()))
                        .createdAt(recomment.getCreatedAt())
                        .modifiedAt(recomment.getModifiedAt())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllReCommentsByComment(Long commentId) {
        Comment comment = commentService.isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        List<ReComment> reCommentList = reCommentRepository.findAllByComment(comment);
        List<ReCommentResponseDto>reCommentResponseDtoList = new ArrayList<>();

        for (ReComment reComment : reCommentList) {
            reCommentResponseDtoList.add(
                    ReCommentResponseDto.builder()
                            .id(reComment.getId())
                            .author(reComment.getMember().getNickname())
                            .content(reComment.getContent())
                            .likeCount(comment_likes(reComment.getId()))
                            .createdAt(reComment.getCreatedAt())
                            .modifiedAt(reComment.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(reCommentResponseDtoList);
    }

    @Transactional
    public ResponseDto<?> updateReComment(Long id, ReCommentRequestDto requestDto, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Comment comment = commentService.isPresentComment(requestDto.getCommentId());
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        ReComment reComment = isPresentReComment(id);
        if (null == reComment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (reComment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        reComment.update(requestDto);
        return ResponseDto.success(
                ReCommentResponseDto.builder()
                        .id(reComment.getId())
                        .author(reComment.getMember().getNickname())
                        .content(reComment.getContent())
                        .likeCount(comment_likes(reComment.getId()))
                        .createdAt(reComment.getCreatedAt())
                        .modifiedAt(reComment.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> deleteReComment(Long id, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        ReComment reComment = isPresentReComment(id);
        if (null == reComment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        if (reComment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        reCommentRepository.delete(reComment);
        return ResponseDto.success("success");
    }

    @Transactional(readOnly = true)
    public ReComment isPresentReComment(Long id) {
        Optional<ReComment> optionalReComment = reCommentRepository.findById(id);
        return optionalReComment.orElse(null);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}

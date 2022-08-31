package com.example.intermediate.repository;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Likes;
import com.example.intermediate.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByUserIdAndPost(Long userId, Post post);
    Optional<Likes> findByUserIdAndComment(Long userId, Comment comment);

    List<Likes> findAllByUserIdAndCommentId(Long userId, Long commentId);
    List<Likes> findAllByUserIdAndPostId(Long userId, Long postId);

    //    int countByPostIdAndCommentId(Long post_id, Long comment_id);
    int countByPostId(Long post_id);
    int countByCommentId(Long comment_id);

}

package com.example.intermediate.repository;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Likes;
import com.example.intermediate.domain.Post;
import com.example.intermediate.domain.ReComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReCommentRepository extends JpaRepository<ReComment, Long> {
    List<ReComment> findAllByComment(Comment comment);
    List<ReComment> findAllByMemberId(Long member_id);

}

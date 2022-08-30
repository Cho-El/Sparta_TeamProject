package com.example.intermediate.repository;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByPost(Post post);

  List<Comment> findAllByMemberIdAndParent(Long member_id ,Comment parent);
  List<Comment> findAllByMemberIdAndParentNotNull(Long member_id);
}

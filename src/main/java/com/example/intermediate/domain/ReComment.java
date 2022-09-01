package com.example.intermediate.domain;

import com.example.intermediate.controller.request.CommentRequestDto;
import com.example.intermediate.controller.request.ReCommentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public void update(ReCommentRequestDto reCommentRequestDto) {
        this.content = reCommentRequestDto.getContent();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }
}

package com.example.intermediate.scheduler;

import com.example.intermediate.domain.Comment;
import com.example.intermediate.domain.Post;
import com.example.intermediate.repository.CommentRepository;
import com.example.intermediate.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @Scheduled(cron = "0 0 1 * * *")
    public void deletePost() {
        log.info("댓글이 없는 게시물 삭제 스케줄러 실행.");
        List<Post> postList = postRepository.findAll();
        for(int i=0; i<postList.size();i++){
            Post post = postList.get(i);

            if(post.getComments().size() == 0) {
                postRepository.deleteById(post.getId());
                log.info("게시물 <{}>이 삭제되었습니다.", post.getTitle());
            }
        }
        log.info("댓글이 없는 게시물 삭제 스케줄러 종료.");
    }
}

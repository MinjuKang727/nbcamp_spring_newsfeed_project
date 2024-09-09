package com.sparta.newsfeed_project.domain.comment.service;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.comment.dto.*;
import com.sparta.newsfeed_project.domain.comment.entity.Comment;
import com.sparta.newsfeed_project.domain.comment.repository.CommentRepository;
import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.post.repository.PostRepository;
import com.sparta.newsfeed_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;



    //댓글 등록
    @Transactional
    public CommentSaveResponseDto saveComment(User me, Long post_id, CommentSaveRequestDto commentSaveRequestDto) {

        //게시물이 있는지 확인
        Post post = postRepository.findById(post_id).
                orElseThrow(()-> new NullPointerException("게시물을 찾을 수 없습니다"));

        Comment comment = new Comment(commentSaveRequestDto.getContent(),post, me);
        Comment savedComment = commentRepository.save(comment);

        PostDto postDto = new PostDto(post.getPostId(),post.getTitle(),post.getContent(),post.getCreatedAt(),post.getModifiedAt());
        return new CommentSaveResponseDto(
                postDto,
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getModifiedAt()

        );
    }

    //댓글 조회
    public List<CommentSimpleResponseDto> getCommentList(Long post_id) {

        //게시물이 있는지 확인
        Post post = postRepository.findById(post_id).
                orElseThrow(()-> new NullPointerException("게시물을 찾을 수 없습니다"));

        List<Comment> comments = post.getComments();

        List<CommentSimpleResponseDto> simpleDtoList = new ArrayList<>();
        for(Comment comment : comments){
            CommentSimpleResponseDto dto = new CommentSimpleResponseDto(
                    comment.getId(),
                    comment.getContent(),
                    comment.getUser().getId(),
                    comment.getUser().getUsername(),
                    comment.getCreatedAt(),
                    comment.getModifiedAt()
                    );
            simpleDtoList.add(dto);
        }
        return simpleDtoList;
    }

    //댓글 수정
    @Transactional
    public CommentUpdateResponseDto updateComment(UserDetailsImpl userDetails,
                                                  Long post_id,
                                                  Long comment_Id,
                                                  CommentUpdateRequestDto commentUpdateRequestDto) {

        //게시물이 있는지 확인
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new NullPointerException("게시글을 찾을 수 없습니다."));
        //댓글이 있는지 확인
        Comment comment = commentRepository.findById(comment_Id)
                .orElseThrow(() -> new NullPointerException("댓글을 찾을 수 없습니다."));

        log.info((!comment.getUser().getId().equals(userDetails.getMyId()) && !post.getUser().getId().equals(userDetails.getMyId()))+"");
        //게시물의 작성자 or 댓글의 작성자인지 확인

        if (!comment.getUser().getId().equals(userDetails.getMyId()) && !post.getUser().getId().equals(userDetails.getMyId())){
            throw new RuntimeException("댓글 수정은 댓글의 작성자 혹은 게시글의 작성자만 가능합니다.");
        }

        comment.update(commentUpdateRequestDto.getContent());
        return new CommentUpdateResponseDto(
                comment.getUser().getId(),
                post.getPostId(),
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt());
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(UserDetailsImpl userDetails,Long post_id, Long comment_id) {
        //게시글이 있는지 확인
        Post post = this.postRepository.findById(post_id).orElseThrow(() -> new NullPointerException("게시글을 찾을 수 없습니다."));
        //댓글이 있는지 확인
        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() -> new NullPointerException("댓글을 찾을 수 없습니다."));

        //게시물의 작성자 or 댓글의 작성자인지 확인
        if (!comment.getUser().getId().equals(userDetails.getMyId()) && !post.getUser().getId().equals(userDetails.getMyId())){
            throw new RuntimeException("댓글 삭제는 댓글의 작성자 혹은 게시글의 작성자만 가능합니다.");
        }


        commentRepository.delete(comment);

    }
}

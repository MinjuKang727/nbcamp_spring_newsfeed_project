package com.sparta.newsfeed_project.domain.comment.service;

import com.sparta.newsfeed_project.auth.security.UserDetailsImpl;
import com.sparta.newsfeed_project.domain.comment.dto.*;
import com.sparta.newsfeed_project.domain.comment.entity.Comment;
import com.sparta.newsfeed_project.domain.comment.repository.CommentRepository;
import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.post.repository.PostRepository;
import com.sparta.newsfeed_project.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
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

    @Transactional
    public CommentSaveResponseDto saveComment(User me, Long post_id, CommentSaveRequestDto commentSaveRequestDto) {

        Post post = postRepository.findById(post_id).
                orElseThrow(()-> new EntityNotFoundException("게시물을 찾을 수 없습니다"));

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

    public List<CommentSimpleResponseDto> getCommentList(Long post_id) {

        Post post = postRepository.findById(post_id).
                orElseThrow(()-> new EntityNotFoundException("게시물을 찾을 수 없습니다"));

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

    @Transactional
    public CommentUpdateResponseDto updateComment(UserDetailsImpl userDetails,
                                                  Long post_id,
                                                  Long comment_Id,
                                                  CommentUpdateRequestDto commentUpdateRequestDto) {

        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        Comment comment = commentRepository.findById(comment_Id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getId().equals(userDetails.getUser().getId()) && !post.getUser().getId().equals(userDetails.getUser().getId())){
                throw new AuthorizationServiceException("댓글 수정은 댓글의 작성자 혹은 게시글의 작성자만 가능합니다.");
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

    @Transactional
    public void deleteComment(UserDetailsImpl userDetails,Long post_id, Long comment_id) {
        Post post =postRepository.findById(post_id).orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getId().equals(userDetails.getMyId()) && !post.getUser().getId().equals(userDetails.getMyId())){
            throw new AuthorizationServiceException("댓글 삭제는 댓글의 작성자 혹은 게시글의 작성자만 가능합니다.");
        }

        commentRepository.delete(comment);
    }
}

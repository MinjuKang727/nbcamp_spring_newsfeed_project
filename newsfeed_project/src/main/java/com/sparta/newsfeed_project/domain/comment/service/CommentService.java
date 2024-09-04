package com.sparta.newsfeed_project.domain.comment.service;

import com.sparta.newsfeed_project.domain.comment.dto.*;
import com.sparta.newsfeed_project.domain.comment.entity.Comment;
import com.sparta.newsfeed_project.domain.comment.repository.CommentRepository;
import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;



    //댓글 등록
    @Transactional
    public CommentSaveResponseDto saveComment(Long post_id, CommentSaveRequestDto commentSaveRequestDto) {

        //게시물이 있는지 확인
        Post post = postRepository.findById(post_id).
                orElseThrow(()-> new NullPointerException("게시물을 찾을 수 없습니다"));

        Comment comment = new Comment(commentSaveRequestDto.getContent(),commentSaveRequestDto.getPost(),commentSaveRequestDto.getUser());
        Comment savedComment = commentRepository.save(comment);

        PostDto postDto = new PostDto(post_id,post.getTitle(),post.getContent(),post.getCreatedAt(),post.getModifiedAt());
        return new CommentSaveResponseDto(
                savedComment.getContent(),
                postDto,
                savedComment.getCreatedAt(),
                savedComment.getModifiedAt()

        );
    }

    //댓글 조회
    public List<CommentSimpleResponseDto> getCommentList(Long post_id) {

        //게시물이 있는지 확인
        Post post = postRepository.findById(post_id).
                orElseThrow(()-> new NullPointerException("게시물을 찾을 수 없습니다"));

        List<Comment> comments = commentRepository.findAll();

        List<CommentSimpleResponseDto> simpleDtoList = new ArrayList<>();
        PostDto postDto =new PostDto(post.getPostId(),post.getTitle(),post.getContent(),post.getCreatedAt(),post.getModifiedAt());
        for(Comment comment : comments){
            CommentSimpleResponseDto dto = new CommentSimpleResponseDto(
                    comment.getContent(),
                    comment.getUser().getUsername(),
                    comment.getCreatedAt(),
                    comment.getModifiedAt(),
                    postDto);
            simpleDtoList.add(dto);
        }
        return simpleDtoList;
    }

    //댓글 수정
    @Transactional
    public CommentUpdateResponseDto updateComment(Long post_id,
                                                  Long comment_Id,
                                                  CommentUpdateRequestDto commentUpdateRequestDto) {

        //게시물이 있는지 확인
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new NullPointerException("게시글을 찾을 수 없습니다."));
        //댓글이 있는지 확인
        Comment comment = commentRepository.findById(comment_Id)
                .orElseThrow(() -> new NullPointerException("댓글을 찾을 수 없습니다."));

        //게시물의 작성자 or 댓글의 작성자인지 확인
//        if (!comment.getUser().getId().equals() || post.getUser().getId().equals())(
//                일치하지 않을 경우 예외처리!! 댓글 수정 권한이 없는 유저입니다.
//                )

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
    public void deleteComment(Long post_id, Long comment_id) {
        //게시글이 있는지 확인
        postRepository.findById(post_id).orElseThrow(() -> new NullPointerException("게시글을 찾을 수 없습니다."));
        //댓글이 있는지 확인
        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() -> new NullPointerException("댓글을 찾을 수 없습니다."));
        //게시물의 작성자 or 댓글의 작성자인지 확인
//        if (!comment.getUser().getId().equals() || post.getUser().getId().equals())(
//                일치하지 않을 경우 예외처리!!! 댓글 삭제 권한이 없는 유저입니다.
//                )

        commentRepository.delete(comment);

    }
}

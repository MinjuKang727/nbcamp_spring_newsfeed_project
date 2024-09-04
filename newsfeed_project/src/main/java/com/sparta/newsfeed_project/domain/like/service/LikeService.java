package com.sparta.newsfeed_project.domain.like.service;

import com.sparta.newsfeed_project.domain.comment.entity.Comment;
import com.sparta.newsfeed_project.domain.comment.repository.CommentRepository;
import com.sparta.newsfeed_project.domain.like.entity.Like;
import com.sparta.newsfeed_project.domain.like.repository.LikeRepository;
import com.sparta.newsfeed_project.domain.post.entity.Post;
import com.sparta.newsfeed_project.domain.post.repository.PostRepository;
import com.sparta.newsfeed_project.domain.user.entity.User;
import com.sparta.newsfeed_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
        );
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );
    }

    private <T> Like findLike(User user, T entity, String contentType) {
        if (entity instanceof Post) {
            return likeRepository.findByUserAndPost(user, (Post) entity);
        } else if (entity instanceof Comment) {
            return likeRepository.findByUserAndComment(user, (Comment) entity);
        }
        throw new IllegalArgumentException("잘못된 타입입니다: " + contentType);
    }

    private void validateLikeAction(User user, Long ownerId, String contentType) {
        if(user.getId().equals(ownerId)) {
            throw new IllegalArgumentException("본인이 작성한 " + contentType + "에 좋아요를 남길 수 없습니다.");
        }
    }

    private <T> void performLike(User user, T entity, String contentType) {
        if (findLike(user, entity, contentType) != null) {
            throw new IllegalArgumentException("이미 이 " + contentType + "에 좋아요를 남겼습니다.");
        }
        likeRepository.save(new Like(user, entity instanceof Post ? (Post) entity : null, entity instanceof Comment ? (Comment) entity : null));
    }

    private <T> void performUnlike(User user, T entity, String contentType) {
        Like like = findLike(user, entity, contentType);
        if (like == null) {
            throw new IllegalArgumentException(contentType + "에 좋아요를 누르지 않았습니다.");
        }
        likeRepository.delete(like);
    }

    public void likePost(User user, Long postId) {
        Post post = findPost(postId);
        validateLikeAction(user, post.getUser().getId(), "게시물");
        performLike(user, post, "게시물");
    }

    public void likeComment(User user, Long commentId) {
        Comment comment = findComment(commentId);
        validateLikeAction(user, comment.getUser().getId(), "댓글");
        performLike(user, comment, "댓글");
    }

    public void unlikePost(User user, Long postId) {
        performUnlike(user, findPost(postId), "게시물");
    }

    public void unlikeComment(User user, Long commentId) {
        performUnlike(user, findComment(commentId), "댓글");
    }
}

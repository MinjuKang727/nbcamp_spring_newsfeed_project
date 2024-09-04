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
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
    }

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

    private <T> Like findLike(Long myId, T entity, String contentType) {
        if (entity instanceof Post) {
            return likeRepository.findByUserIdAndPost(myId, (Post) entity);
        } else if (entity instanceof Comment) {
            return likeRepository.findByUserIdAndComment(myId, (Comment) entity);
        }
        throw new IllegalArgumentException("잘못된 타입입니다: " + contentType);
    }

    private void validateLikeAction(Long myId, Long ownerId, String contentType) {
        if(myId.equals(ownerId)) {
            throw new IllegalArgumentException("본인이 작성한 " + contentType + "에 좋아요를 남길 수 없습니다.");
        }
    }

    private <T> void performLike(Long myId, T entity, String contentType) {
        if (findLike(myId, entity, contentType) != null) {
            throw new IllegalArgumentException("이미 이 " + contentType + "에 좋아요를 남겼습니다.");
        }
        likeRepository.save(new Like(findUser(myId), entity instanceof Post ? (Post) entity : null, entity instanceof Comment ? (Comment) entity : null));
    }

    private <T> void performUnlike(Long myId, T entity, String contentType) {
        Like like = findLike(myId, entity, contentType);
        if (like == null) {
            throw new IllegalArgumentException(contentType + "에 좋아요를 누르지 않았습니다.");
        }
        likeRepository.delete(like);
    }

    public void likePost(Long myId, Long postId) {
        Post post = findPost(postId);
        validateLikeAction(myId, post.getUser().getId(), "게시물");
        performLike(myId, post, "게시물");
    }

    public void likeComment(Long myId, Long commentId) {
        Comment comment = findComment(commentId);
        validateLikeAction(myId, comment.getUser().getId(), "댓글");
        performLike(myId, comment, "댓글");
    }

    public void unlikePost(Long myId, Long postId) {
        performUnlike(myId, findPost(postId), "게시물");
    }

    public void unlikeComment(Long myId, Long commentId) {
        performUnlike(myId, findComment(commentId), "댓글");
    }
}

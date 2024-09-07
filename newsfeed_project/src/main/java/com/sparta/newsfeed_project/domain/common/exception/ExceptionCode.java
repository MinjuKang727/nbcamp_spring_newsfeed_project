package com.sparta.newsfeed_project.domain.common.exception;

public enum ExceptionCode {

    FAILED_SIGNUP("회원 가입 실패", 500),
    FAILED_LOGIN("로그인 실패", 500),
    FAILED_LOGOUT("로그아웃 실패", 500),
    FAILED_VIEW_USER("회원 조회 실패", 500),
    FAILED_UPDATE_USER("회원 정보 수정 실패", 500),
    FAILED_DELETE_USER("회원 탈퇴 실패", 500),

    FAILED_SAVE_COMMENT("댓글 등록 실패", 500),
    FAILED_UPDATE_COMMENT("댓글 수정 실패", 500),
    FAILED_DELETE_COMMENT("댓글 삭제 실패", 500),

    FAILED_FOLLOW_USER("팔로잉 실패", 500),
    FAILED_GET_FOLLOWLIST("팔로워 목록 조회 실패", 500),
    FAILED_ALLOW_FOLLOWING("팔로우 수락 실패", 500),
    FAILED_UNFOLLOW("팔로우 취소 실패", 500),

    FAILED_LIKE_POST("게시물 좋아요 실패", 500),
    FAILED_GET_LIKE_POSTLIST("좋아요 표시한 게시물을 찾을 수 없습니다.", 404),
    FAILED_LIKE_COMMENT("댓글 좋아요 실패", 500),
    FAILED_GET_LIKE_COMMENTLIST("좋아요 표시한 댓글을 찾을 수 없습니다.", 404),
    FAILED_UNLIKE_POST("게시물 좋아요 취소 실패", 500),
    FAILED_UNLIKE_COMMENT("댓글 좋아요 취소 실패", 500),

    FAILED_SAVE_POST("게시물 등록 실패", 500),
    FAILED_GET_NEWSFEEDLIST("뉴스피드 목록 조회 실패", 500),
    FAILED_UPDATE_POST("게시물 수정 실패", 500),
    FAILED_DELETE_POST("게시물 삭제 실패", 500),

    ALREADY_EXIST_EMAIL("해당 이메일의 사용자가 이미 존재합니다.", 409),
    ALREADY_EXIST_PHONE_NUMBER("해당 휴대폰 번호의 사용자가 이미 존재합니다.", 409),
    USER_NOT_FOUND("해당 사용자를 찾을 수 없습니다.", 409),
    INCORRECT_PASSWORD("비밀번호를 잘못입력하셨습니다.", 400),
    NOT_MY_ACCOUNT("회원 탈퇴는 본인 계정만 탈퇴 가능합니다.", 403),
    INVALID_ADMIN_TOKEN("관리자 암호가 틀려 등록이 불가능합니다.", 400),
    VIOLATE_VALIDATION("Validation Exception", 400),

    FAILED_CREATE_TOKEN("토큰 생성 실패", 500),
    FAILED_ENCODING_TOKEN("토큰 인코딩 실패", 500),
    FAILED_JWT_VALIDATION("JWT 토큰 검증 실패", 500),
    FAILED_GET_TOKEN("토큰값 가져오기 실패", 500),
    INVALID_JWT_SIGNATURE("유효하지 않은 JWT 서명입니다.", 401),
    EXPIRED_JWT_TOKEN("만료된 JWT 토큰입니다.", 401),
    UNSUPPORTED_JWT_TOKEN("지원되지 않는 JWT 토큰입니다.", 401),
    EMPTY_CLAIMS_JWT_TOKEN("잘못된 JWT 토큰입니다.", 401),
    NOT_FOUND_JWT_TOKEN("JWT 토큰이 존재하지 않습니다.", 400);


    private String message;
    private Integer status;

    ExceptionCode(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getStatus() {
        return this.status;
    }
}

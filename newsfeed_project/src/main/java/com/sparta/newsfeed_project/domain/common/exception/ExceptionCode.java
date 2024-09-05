package com.sparta.newsfeed_project.domain.common.exception;

public enum ExceptionCode {

    FAILED_SIGNUP("회원 가입 실패"),
    FAILED_LOGIN("로그인 실패"),
    FAILED_VIEW_USER("회원 조회 실패"),
    FAILED_UPDATE_USER("회원 정보 수정 실패"),
    FAILED_DELETE_USER("회원 탈퇴 실패"),
    ALREADY_EXIST_EMAIL("해당 이메일의 사용자가 이미 존재합니다."),
    ALREADY_EXIST_PHONE_NUMBER("해당 휴대폰 번호의 사용자가 이미 존재합니다."),
    USER_NOT_FOUND("해당 사용자를 찾을 수 없습니다."),
    INCORRECT_PASSWORD("비밀번호를 잘못입력하셨습니다."),
    NOT_MY_ACCOUNT("회원 탈퇴는 본인 계정만 탈퇴 가능합니다."),
    INVALID_ADMIN_TOKEN("관리자 암호가 틀려 등록이 불가능합니다."),
    VIOLATE_VALIDATION("Validation Exception"),

    FAILED_CREATE_TOKEN("토큰 생성 실패"),
    FAILED_JWT_VALIDATION("JWT 토큰 검증 실패"),
    FAILED_GET_TOKEN("토큰값 가져오기 실패"),
    INVALID_JWT_SIGNATURE("유효하지 않은 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN("만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN("지원되지 않는 JWT 토큰입니다."),
    EMPTY_CLAIMS_JWT_TOKEN("잘못된 JWT 토큰입니다."),
    NOT_FOUND_JWT_TOKEN("JWT 토큰이 존재하지 않습니다.");


    private String code;

    ExceptionCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}

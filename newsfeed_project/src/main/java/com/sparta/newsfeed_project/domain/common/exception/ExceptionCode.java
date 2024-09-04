package com.sparta.newsfeed_project.domain.common.exception;

public enum ExceptionCode {
    FAILED_CREATE_TOKEN("토큰 생성 실패"),
    FAILED_SIGNUP("회원 가입 실패"),
    FAILED_VIEW_USER("회원 조회 실패"),
    FAILED_UPDATE_USER("회원 정보 수정 실패"),
    FAILED_DELETE_USER("회원 탈퇴 실패"),
    ALREADY_EXIST_EMAIL("해당 이메일의 사용자가 이미 존재합니다."),
    ALREADY_EXIST_PHONE_NUMBER("해당 휴대폰 번호의 사용자가 이미 존재합니다."),
    USER_NOT_FOUND("해당 사용자를 찾을 수 없습니다."),
    INCORRECT_PASSWORD("비밀번호를 잘못입력하셨습니다."),
    NOT_MY_ACCOUNT("회원 탈퇴는 본인 계정만 탈퇴 가능합니다."),
    VIOLATE_VALIDATION("Validation Exception");


    private String code;

    ExceptionCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}

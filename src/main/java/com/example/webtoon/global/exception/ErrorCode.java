package com.example.webtoon.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    WRONG_VERIFICATION(HttpStatus.BAD_REQUEST, "잘못된 인증 시도입니다."),
    EXPIRE_CODE(HttpStatus.BAD_REQUEST, "인증 시간이 만료되었습니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "일치하는 회원이 없습니다."),
    ALREADY_VERIFY(HttpStatus.BAD_REQUEST, "이미 인증이 완료되었습니다."),
    DELETED_ACCOUNT(HttpStatus.BAD_REQUEST, "삭제 절차중인 계정입니다."),
    NO_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "입력된 계정정보가 일치하지 않습니다."),
    NOT_FOUND_WEBTOON(HttpStatus.BAD_REQUEST,"일치하는 웹툰이 없습니다."),
    FAIL_TO_UPLOAD_FILE(HttpStatus.BAD_REQUEST,"파일 업로드에 실패했습니다."),
    DENIED_ACCESS_PERMISSION(HttpStatus.BAD_REQUEST,"접근이 거부되었습니다."),
    EXISTS_WEBTOONNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 웹툰 이름입니다."),
    NO_EXISTS_WEBTOONCHAPTER(HttpStatus.BAD_REQUEST,"존재하지 않는 웹툰 챕터입니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}

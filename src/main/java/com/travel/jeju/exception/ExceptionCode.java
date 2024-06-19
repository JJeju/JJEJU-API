package com.travel.jeju.exception;


public enum ExceptionCode {
    SUCCESS("1", 200, "성공"),
	FAIL("-1" , 500, "실패"),

	NON_VALID_PARAMETER("VP001", 400, "파라미터가 유효하지 않습니다."),
	OTHER_USER_DATA("VP002", 400, "다른 사용자의 데이터입니다."),
	INTERNAL_SERVER_ERROR("ER500", 500, "내부 시스템 오류"),

	NOT_REGIST_ERROR_CODE("ER501", 500, "등록되지 않은 오류 코드"),

	DATA_NOT_FIND("DT001", 204,"데이터 를 찾을수 없습니다."),
	DATA_DUPLICATE("DT002", 500, "중복 데이터가 존재합니다."),
	DATA_NO("DT003", 500, "데이터 미존재"),

	ALREADY_CERT("CT001", 200, "이미 인증 되어있는 값 입니다."),
	ALREADY_LOGOUT("CT002", 200, "이미 로그아웃이 되어있습니다."),

	DB_ERROR("DB001", 500, "디비 처리중 오류"),

	TIME_OUT("T001", 400, "인증 시간 초과"),

	FILE_ERROR("F001", 500, "파일 읽어오기 오류"),
	FILE_PARSING_ERROR("F002", 500, "파일 파싱중 오류"),
	FILE_NOT_FOUND("F003", 500, "파일 및 경로를 찾을수가 없음"),

	NOT_AUTHENTICATION_USER("P001", 400, "인증실패"),
	NOT_AUTHORIZED_USER("P002", 401, "인가되지 않은 사용자"),
	NOT_PERMISSION("P003", 403, "권한이 없는 사용자"),
	SING_IN_FROM_ANOTHER_DEVICE("P004", 400, "다른기기에서 로그인"),

	REFRESH_JWT_EXPIRED("J001", 400, "리프레쉬 만료"),

	EXTERNAL_API_ERROR("EA500", 500, "외부서버 호출에 실패하였습니다.")
	;

	

	private final String code;

	private final int status;

	private final String message;
	
	private ExceptionCode(String code, int status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	public String code() {
		return this.code;
	}

	public String message() {
		return this.message;
	}

	public int status(){
		return this.status;
	}
}

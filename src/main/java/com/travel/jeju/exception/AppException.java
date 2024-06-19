package com.travel.jeju.exception;

import java.util.Locale;

import org.springframework.context.MessageSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

	private final Level level;

	private final Source source;

	private final ExceptionCode errorCode;
	
	private final Object[] paramArray;
	
	private String sourceErrorCode;
	
	private String sourceErrorMessage;

	public AppException(Throwable th, Level level, Source source, ExceptionCode errorCode, Object... paramArray) {
		super(th != null ? th.getMessage() : errorCode.message(), th);
		this.level = level;
		this.errorCode = errorCode;
		this.source = source;
		this.paramArray = paramArray;
		this.sourceErrorCode = errorCode.code();
		this.sourceErrorMessage = errorCode.message();
	}

	public AppException(Throwable th, Level level, ExceptionCode errorCode, Object... paramArray) {
		this(th, level, Source.INTERNAL, errorCode, paramArray);
	}

	public AppException(Throwable th, Source source, ExceptionCode errorCode, Object... paramArray) {
		this(th, Level.WARN, source, errorCode, paramArray);
	}

	public AppException(Throwable th, ExceptionCode errorCode, Object... paramArray) {
		this(th, Level.WARN, Source.INTERNAL, errorCode, paramArray);
	}

	public AppException(Level level, Source source, String errorCode, Object... paramArray) {
		this(null, level, source, errorCode, paramArray);
	}

	public AppException(Level level, String errorCode, Object... paramArray) {
		this(null, level, Source.INTERNAL, errorCode, paramArray);
	}

	public AppException(Source source, String errorCode, Object... paramArray) {
		this(null, Level.WARN, source, errorCode, paramArray);
	}

	public AppException(ExceptionCode errorCode, Object... paramArray) {
		this(null, Level.WARN, Source.INTERNAL, errorCode, paramArray);
	}
//	public BaseException(String errorCode) {
//		this(errorCode, new Object());
//	}

	public AppException(Throwable th, ExceptionCode erorCode, String sourceErrorMessage) {
		this(th, Level.WARN, ExceptionCode.INTERNAL_SERVER_ERROR, (Object[])null);
		this.sourceErrorCode = erorCode.code();
		this.sourceErrorMessage = errorCode.message();
	}

	public AppException(ExceptionCode sourceErrorCode, String sourceErrorMessage) {
		this(null, sourceErrorCode, sourceErrorMessage);
	}
	
	@Deprecated
	public AppException(Throwable th, Level level, ExceptionCode errorCode, Source source, Object... paramArray) {
		this(th, level, source, errorCode, paramArray);
	}

	@Deprecated
	public AppException(Level level, ExceptionCode errorCode, Source source, Object... paramArray) {
		this(null, level, source, errorCode, paramArray);
	}

	public String getCode() {
		return errorCode.code();
	}

    public ExceptionCode getErrorCode(){
        return errorCode;
    }

	public Object[] getParamArray() {
		return paramArray;
	}

	public Level getLevel() {
		return level;
	}

	public Source getSource() {
		return source;
	}

	public String getErrorMessage(MessageSource ms) {
		if (ms == null) {
			return ExceptionCode.INTERNAL_SERVER_ERROR.message();
		}
		log.debug("############ getErrorMessage     : {}",ms);
		log.debug("############ getErrorCode        : {}",getCode());
		log.debug("############ getParamArray       : {}",getParamArray());
		log.debug("############ ErrorCode.NOT_REGIST_ERROR_CODE.message() : {}",ExceptionCode.NOT_REGIST_ERROR_CODE.message());
		log.debug("############ Locale.getDefault() : {}",Locale.getDefault());
		return ms.getMessage(getCode(), getParamArray(), ExceptionCode.NOT_REGIST_ERROR_CODE.message(), Locale.getDefault());
	}

	public String getSourceErrorCode() {
		return sourceErrorCode;
	}

	public void setSourceErrorCode(String sourceErrorCode) {
		this.sourceErrorCode = sourceErrorCode;
	}

	public String getSourceErrorMessage() {
		return sourceErrorMessage;
	}

	public void setSourceErrorMessage(String sourceErrorMessage) {
		this.sourceErrorMessage = sourceErrorMessage;
	}

}

package com.travel.jeju.exception;

public enum Level {
    INFO(0), 
	WARN(1),
	RETRY(2),
	DROP(3),
	RECYCLE(4),
	CRITICAL(5);
	
	private int code;

	Level(int code) {
		setLevel(code);
	}

	public int getLevel() {
		return code;
	}

	public void setLevel(int code) {
		this.code = code;
	}   
}

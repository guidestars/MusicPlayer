package com.xu.musicplayer.bean;

public class PlayerBean {
	
	private int     returnIndex;
	private String  returnTime;
	private int     returnLong;
	private boolean isFinished;
	
	public PlayerBean(int returnIndex, String returnTime, int returnLong, boolean isFinished) {
		this.returnIndex = returnIndex;
		this.returnTime = returnTime;
		this.returnLong = returnLong;
		this.isFinished = isFinished;
	}

	public int getReturnIndex() {
		return returnIndex;
	}
	
	public void setReturnIndex(int returnIndex) {
		this.returnIndex = returnIndex;
	}
	
	public String getReturnTime() {
		return returnTime;
	}
	
	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}
	
	public int getReturnLong() {
		return returnLong;
	}
	
	public void setReturnLong(int returnLong) {
		this.returnLong = returnLong;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
}
package com.xu.musicplayer.download;

public interface MyNotify {
	/**
	 * 子线程跟主线程通讯
	 * @param object
	 */
	public void notifyResult(Object object);
	
}

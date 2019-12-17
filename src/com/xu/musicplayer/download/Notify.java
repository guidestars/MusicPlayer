package com.xu.musicplayer.download;

public interface Notify {
	/**
	 * 子线程跟主线程通讯
	 * @param object
	 */
	public void notifyResult(Object object);
	
}

package com.xu.musicplayer.bean;

public interface PlayerNotify {
	/**
	 * 子线程跟主线程通讯
	 * @param object
	 */
	public void PlayerResult(Object object);
	
}

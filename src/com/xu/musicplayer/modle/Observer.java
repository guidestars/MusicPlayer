package com.xu.musicplayer.modle;

import com.xu.musicplayer.entity.PlayerEntity;

/**
 * 抽象 观察者 接口
 * @author hyacinth
 * @date 2019年10月10日12:00:09
 *
 */
public interface Observer {

	public void start(PlayerEntity entity);
	
	public void stop();
	
	public void end();
	
}


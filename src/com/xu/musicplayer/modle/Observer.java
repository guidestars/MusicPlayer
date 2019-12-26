package com.xu.musicplayer.modle;

import com.xu.musicplayer.entity.PlayerEntity;

/**
 * Java MusocPlayer 抽象 观察者 接口
 * @Author: hyacinth
 * @ClassName: Observer   
 * @Description: TODO    
 * @Date: 2019年12月26日 下午8:02:58   
 * @Copyright: hyacinth
 */
public interface Observer {

	public void start(PlayerEntity entity);
	
	public void stop();
	
	public void end();
	
}

